/*
 * (C) 2023 vkozhemi
 *      All rights reserved
 *
 * 4-24-2023; Volodymyr Kozhemiakin
 */

#include "Client.h"

/**
 * @param hostname - IP address of remote server
 * @param port - port number to connect to on remote server
 * @return - returns a socket or -1 if an error
 */
int Client::connectToServer(string hostname, int port) {
    int sockets = 0;
    struct sockaddr_in addr;

    if ((sockets = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        return -1;
    }

    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);

    if (inet_pton(AF_INET, hostname.c_str(), &addr.sin_addr) <= 0) {
        return -1;
    }

    if (connect(sockets, (struct sockaddr *) &addr, sizeof(addr)) < 0) {
        return -1;
    }
    return sockets;
}

/**
 * @param sock - active socket to send data through
 * @param data - data to be sent to the remote server
 */
void Client::sendData(int sock, string data) {
    __android_log_print(ANDROID_LOG_DEBUG, "Client", "sendData: %s", data.c_str());
    send(sock, data.c_str(), sizeof(data), 0);
}

/**
 * @param sock - active socket to listen for incoming data on
 * @return - incoming data from remote server
 */
string Client::receiveData(int sock) {
    char buffer[1024] = {0};
    int valread = read(sock, buffer, 1024);
    if (valread != -1) {
        __android_log_print(ANDROID_LOG_DEBUG, "Client", "receiveData: %s", buffer);
        return buffer;
    } else return "Error";
}

/**
 * @param sock - active socket to disconnect from
 */
void Client::disconnectFromServer(int sock) {
    close(sock);
}