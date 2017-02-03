#pragma comment(lib, "ws2_32.lib")
#include <stdio.h>
#include <winsock2.h>
#include <process.h>

#include <mmsystem.h>
#pragma comment(lib, "Winmm.lib")

#include "NP_Socket_Lib.h"
#include "ProtocolDef.h"
NP_Socket_Lib g_theSocketLib;

#define MAX_USER_NUM 64-1
User	g_userList[MAX_USER_NUM];
int		g_userNum;

#define MAX_CONNECTION_NUM MAX_USER_NUM

Session	g_sessionList[MAX_CONNECTION_NUM];
int		g_sessionNum;

int		LoadUserDB(const char* fileName);
bool	IsValidUser(const char* name, const char* password);

void	AddSession(const Session* session);
void	RemoveSession(const Session* session);
void	RemoveSession(const char* name);
void	RemoveSession(SOCKET connection);

void	ProcessPkt(const char* pkt, Session* session);
void	LoginProcess(const LoginPkt* pkt, Session* session);
void	LogoutProcess(const LogoutPkt* pkt);

void	SendMusicListProcess(char* cpath, Session* session);
void	SendMusic(char *musicname, Session* session);

void	PlayStartStopProcess(const PlayStartStopPkt* pkt, Session* session);

int main(int argc, char* argv[])
{
	//step 1: 读入用户数据库
	const char* userFileName = "users.txt";
	if(LoadUserDB(userFileName) != 1)
	{
		printf("打开用户名文件失败\n");
		return -1;
	}

	//step 2: 创建服务器本地套接字
	SOCKET sListen = socket(AF_INET, SOCK_STREAM, 0);
	if(sListen == INVALID_SOCKET)
	{
		printf("socket() error: %d\n", WSAGetLastError());
		return -1;
	}

	//step 3: 创建本地实体
	sockaddr_in local;
	memset(&local, 0, sizeof(local));
	local.sin_family = AF_INET; //Address family
	local.sin_addr.s_addr = INADDR_ANY; //Wild card IP address
	local.sin_port = htons((u_short)8888); //port to use

	//step 4: 将服务器本地套接字和本地实体绑定
	if(bind(sListen, (struct sockaddr *)&local, sizeof(local))!=0)
	{
		printf("bind() error: %d\n", WSAGetLastError());
		closesocket(sListen);
		return -1;
	}

	listen(sListen, 5);

	//step 5: 数据通信
	fd_set fd_read;
	while(true)
	{		
		//接受数据
		FD_ZERO(&fd_read);
		FD_SET(sListen, &fd_read);
		int i;

		for(i=0; i<g_sessionNum; i++)
		{
			FD_SET(g_sessionList[i].connection, &fd_read);
		}

		int ret = select(0, &fd_read, NULL, NULL, NULL);
		if(SOCKET_ERROR == ret)
		{
			printf("Select failed\n!");
			return -2;
		}
		
		if(ret > 0)
		{	
			// check whether sockListen have read event
			if(FD_ISSET(sListen, &fd_read))
			{
				SOCKET connection = accept(sListen, NULL, NULL);
				if (INVALID_SOCKET == connection)
				{
					printf("Accept socket failed!\n");
					return -3;
				}
				g_sessionList[g_sessionNum].connection = connection;
				g_sessionList[g_sessionNum].recvPayloadSize = 0;
				g_sessionList[g_sessionNum].recvPktLenSize = 0;
				g_sessionList[g_sessionNum++].state = UNLOGIN_STATE;
			}

			for(i=0; i<g_sessionNum; i++)
			{
				Session* session = &g_sessionList[i];
				if (FD_ISSET(session->connection, &fd_read))
				{
					if (session->recvPktLenSize < 4)
					{
						int recvBytes = recv(session->connection,
							session->payload + session->recvPktLenSize, 4 - session->recvPktLenSize, 0);
						if (recvBytes < 0)
						{
							printf("recv error!\n");
							continue;
						}
						else if (recvBytes == 0)
						{
							printf("client has close connection!\n");
							continue;
						}
						session->recvPktLenSize += recvBytes;
						if (session->recvPktLenSize == 4)
						{
							session->pktLen = *((int*)(session->payload));
						}
					}
					else if (session->recvPayloadSize < session->pktLen)
					{
						int recvBytes = recv(session->connection,
							session->payload + 4 + session->recvPayloadSize, session->pktLen - session->recvPayloadSize, 0);
						if (recvBytes < 0)
						{
							printf("recv error!\n");
							continue;
						}
						else if (recvBytes == 0)
						{
							printf("client has close connection!\n");
							continue;
						}
						session->recvPayloadSize += recvBytes;
						if (session->recvPayloadSize == session->pktLen)
						{
							ProcessPkt(session->payload, session);
							session->recvPayloadSize = 0;
							session->recvPktLenSize = 0;
						}
					}
				}
			}
		}	
	}

	//step 6：关闭服务器套接字
	closesocket(sListen);
	return 0;
}

int LoadUserDB(const char* fileName)
{
	FILE* fp = fopen(fileName, "r");
	if(NULL == fp)
	{
		return 0;
	}
	while(!feof(fp))
	{
		fscanf(fp, "%s%s", g_userList[g_userNum].name, g_userList[g_userNum].password);
		g_userNum++;
	}
	return 1;
}

bool IsValidUser(const char* name, const char* password)
{
	for(int i=0; i<g_userNum; i++)
	{
		if(strcmp(g_userList[i].name, name) == 0 &&
			strcmp(g_userList[i].password, password) == 0)
		{
			return true;
		}
	}
	return false;
}

void AddSession(const Session* session)
{
	g_sessionList[g_sessionNum++] = *session;
}

void RemoveSession(const Session* session)
{
	RemoveSession(session->connection);
}

void RemoveSession(const char* name)
{
	int i;
	for(i=0; i<g_sessionNum; i++)
	{
		if(strcmp(g_sessionList[i].name, name) == 0)
		{
			g_sessionNum--;
			int j;
			for(j=i; j<g_sessionNum;j++)
			{
				g_sessionList[j] = g_sessionList[j+1];
			}
			break;
		}
	}
}

void RemoveSession(SOCKET connection)
{
	int i;
	for(i=0; i<g_sessionNum; i++)
	{
		if(g_sessionList[i].connection == connection)
		{
			g_sessionNum--;
			int j;
			for(j=i; j<g_sessionNum;j++)
			{
				g_sessionList[j] = g_sessionList[j+1];
			}
			break;
		}
	}
}

void ProcessPkt(const char* pkt, Session* session)
{
	PktHeader* header = (PktHeader*)pkt;
	int pktType = header->pktType;

	if(pktType == PKT_LOGIN && session->state != LOGIN_STATE)
	{
		LoginProcess((LoginPkt*)pkt, session);
	}
	else if(pktType == PKT_LOGOUT && session->state == LOGIN_STATE)
	{
		closesocket(session->connection);
		LogoutProcess((LogoutPkt*)pkt);
	}
	else if (pktType == PKT_GET_MUSIC_LIST)
	{
		SendMusicListProcess(".\\music\\", session);
	}
	else if(pktType == PKT_PLAY_START_STOP)
	{
		PlayStartStopProcess((PlayStartStopPkt*)pkt, session);
	}
	else
	{
		printf("unknow packet type\n");
	}
}

void LoginProcess(const LoginPkt* pkt, Session* session)
{
	if(IsValidUser(pkt->name, pkt->password))
	{
		LoginReplyPkt reply;
		reply.header.pktLen = 4 + 4;
		reply.header.pktType = PKT_LOGIN_REPLY;
		reply.ret = LOGIN_SUCCESS;
		send(session->connection, (char*)&reply, sizeof(reply), 0);

		session->state = LOGIN_STATE;
		strcpy(session->name, pkt->name);
	}
	else
	{
		LoginReplyPkt reply;
		reply.header.pktLen = 4 + 4;
		reply.header.pktType = PKT_LOGIN_REPLY;
		reply.ret = LOGIN_FAIL;
		send(session->connection, (char*)&reply, sizeof(reply), 0);

		session->state = UNLOGIN_STATE;
		strcpy(session->name, pkt->name);
	}
}

void SendMusicListProcess(char* cpath, Session* session)
{
	WIN32_FIND_DATA wfd;
	HANDLE hfd;
	char cdir[MAX_PATH];
	char subdir[MAX_PATH];
	int r;
	GetCurrentDirectory(MAX_PATH, cdir);
	SetCurrentDirectory(cpath);
	hfd = FindFirstFile("*.*", &wfd);
	if (hfd != INVALID_HANDLE_VALUE)
	{
		do 
		{
			// 当前文件为文件夹属性
			if (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
			{
				if (wfd.cFileName[0] != '.') 
				{
					// 合成完整路径名   
					sprintf(subdir, "%s\\%s", cpath, wfd.cFileName);
					// 递归枚举子目录   
					SendMusicListProcess(subdir, session);
				}
			}
			else
			{
				SendMusic(wfd.cFileName, session);
			}
		} while (r = FindNextFile(hfd, &wfd), r != 0);
	}
	SetCurrentDirectory(cdir);
}

void SendMusic(char *musicname, Session* session) 
{
	GetMusicListReplyPkt reply;
	reply.header.pktLen = 4 + 30;
	reply.header.pktType = PKT_GET_MUSIC_LIST_REPLY;
	strcpy(reply.musicName, musicname);
	send(session->connection, (char*)&reply, 4+4+30, 0);
}

void PlayStartStopProcess(const PlayStartStopPkt* pkt, Session* session) {

	if (pkt->type == PLAY_START) 
	{
		// 播放音乐
		TCHAR fileName[MAX_PATH];
		strcpy(fileName, ".\\music\\");
		strcat(fileName, pkt->musicName);
		strcat(fileName, ".mp3");
		TCHAR shortName[MAX_PATH];
		GetShortPathName(fileName, shortName, sizeof(shortName) / sizeof(TCHAR));
		TCHAR cmd[MAX_PATH + 10];
		wsprintf(cmd, "play %s", shortName);
		int state = mciSendString(cmd, "", NULL, NULL);  //回调窗口的句柄，一般为NULL  

		if (state == 0)
		{
			PlayStartStopReplyPkt reply;
			reply.header.pktLen = 4 + 4;
			reply.header.pktType = PKT_PLAY_START_STOP_REPLY;
			reply.ret = PLAY_START_SUCCESS;
			send(session->connection, (char*)&reply, sizeof(reply), 0);
		}
		else
		{
			PlayStartStopReplyPkt reply;
			reply.header.pktLen = 4 + 4;
			reply.header.pktType = PKT_PLAY_START_STOP_REPLY;
			reply.ret = PLAY_START_FAIL;
			send(session->connection, (char*)&reply, sizeof(reply), 0);
		}
	}
	else
	{
		// 停止音乐
		TCHAR fileName[MAX_PATH];
		strcpy(fileName, ".\\music\\");
		strcat(fileName, pkt->musicName);
		strcat(fileName, ".mp3");
		TCHAR shortName[MAX_PATH];
		GetShortPathName(fileName, shortName, sizeof(shortName) / sizeof(TCHAR));
		TCHAR cmd[MAX_PATH + 10];
		wsprintf(cmd, "stop %s", shortName);
		int state = mciSendString(cmd, "", NULL, NULL);  //回调窗口的句柄，一般为NULL  

		if (state == 0)
		{
			PlayStartStopReplyPkt reply;
			reply.header.pktLen = 4 + 4;
			reply.header.pktType = PKT_PLAY_START_STOP_REPLY;
			reply.ret = PLAY_STOP_SUCCESS;
			send(session->connection, (char*)&reply, sizeof(reply), 0);
		}
		else
		{
			PlayStartStopReplyPkt reply;
			reply.header.pktLen = 4 + 4;
			reply.header.pktType = PKT_PLAY_START_STOP_REPLY;
			reply.ret = PLAY_STOP_FAIL;
			send(session->connection, (char*)&reply, sizeof(reply), 0);
		}
	}
}

void LogoutProcess(const LogoutPkt* pkt)
{
	RemoveSession(pkt->name);
}