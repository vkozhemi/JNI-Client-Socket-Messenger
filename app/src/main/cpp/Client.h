/*
 * (C) 2023 vkozhemi
 *      All rights reserved
 *
 * 4-24-2023; Volodymyr Kozhemiakin
 */

#ifndef jniclient_CLIENT_H
#define jniclient_CLIENT_H

#pragma once

#include <string>
#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string>
#include <android/log.h>

using namespace std;

class Client {
public:
    int connectToServer(string hostname, int port);

    void sendData(int sock, string data);

    string receiveData(int sock);

    void disconnectFromServer(int sock);
};


#endif //jniclient_CLIENT_H
