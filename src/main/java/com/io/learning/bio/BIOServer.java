/**
 * Author: Tang Yuqian
 * Date: 2020/5/31
 */
package com.io.learning.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * socket服务端
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {

        // server开了8080端口等着客户端来连接
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("BIOServer has started, listening the port:{}" + serverSocket.getLocalSocketAddress());

        // Listens for a connection to be made to this socket and accepts
        //     * it. The method blocks until a connection is made.
        // return the new Socket
        // Block One: 如果一直没有client请求会一直阻塞
        Socket clientSocket = serverSocket.accept();
        // Returns the address of the endpoint this socket is connected to 客户端地址
        System.out.println("connect from:" + clientSocket.getRemoteSocketAddress());

        // Block Two: an input stream for reading bytes from this socket. 从客户端读入的请求数据
        Scanner scanner = new Scanner(clientSocket.getInputStream());
        String request = scanner.nextLine();
        System.out.println("request:" + request);

        String response = "I got a message:" + request;
        // 响应被发送给客户端
        clientSocket.getOutputStream().write(response.getBytes());




    }
}
