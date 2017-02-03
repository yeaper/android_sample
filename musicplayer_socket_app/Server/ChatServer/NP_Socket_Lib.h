#ifndef NP_SOCKET_LIB_H_
#define NP_SOCKET_LIB_H_

#include <winsock2.h>
#pragma comment(lib, "ws2_32.lib")

class NP_Socket_Lib
{
public:
	NP_Socket_Lib() { 
		WSADATA wsaData;
		WSAStartup(MAKEWORD(2,2), &wsaData);
	}
	~NP_Socket_Lib(){
		WSACleanup();
	}
};

#endif //NP_SOCKET_LIB_H_
