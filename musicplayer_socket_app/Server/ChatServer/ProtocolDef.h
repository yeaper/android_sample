#ifndef __PROTOCOL_DEF__H
#define __PROTOCOL_DEF__H

#define PKT_LOGIN					0
#define PKT_LOGOUT					1
#define PKT_PLAY_START_STOP			2
#define PKT_PLAY_START_STOP_REPLY	3
#define PKT_LOGIN_REPLY				4
#define PKT_GET_MUSIC_LIST			5
#define PKT_GET_MUSIC_LIST_REPLY	6

#define LOGIN_FAIL					0
#define LOGIN_SUCCESS				1

#define PLAY_START					0
#define PLAY_STOP					1

#define PLAY_START_FAIL				0
#define PLAY_START_SUCCESS			1
#define PLAY_STOP_FAIL				2
#define PLAY_STOP_SUCCESS			3

#define UNLOGIN_STATE				0
#define LOGIN_STATE					1

typedef struct User{
	char name[20];
	char password[20];
}User;


typedef struct Session{
	char name[20];
	SOCKET connection;
	int state;

	char payload[1024];
	int pktLen;
	int recvPktLenSize;
	int recvPayloadSize;
}Session;

typedef struct PktHeader{
	int pktLen;
	int pktType;
}PktHeader;

typedef struct LoginPkt{
	PktHeader header;
	char	  name[20];
	char      password[20];
}LoginPkt;

typedef struct LogoutPkt{
	PktHeader header;
	char	  name[20];
}LogoutPkt;

typedef struct LoginReplyPkt{
	PktHeader header;
	int		  ret;
}LoginReplyPkt;

typedef struct PlayStartStopPkt{
	PktHeader header;
	int		  type;
	char	  musicName[30];
}PlayStartStopPkt;

typedef struct PlayStartStopReplyPkt {
	PktHeader header;
	int		  ret;
}PlayStartStopReplyPkt;

typedef struct GetMusicListPkt {
	PktHeader header;
}GetMusicListPkt;

typedef struct GetMusicListReplyPkt{
	PktHeader header;
	char	  musicName[30];
}GetMusicListReplyPkt;

#endif //__PROTOCOL_DEF__H