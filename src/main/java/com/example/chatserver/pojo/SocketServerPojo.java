package com.example.chatserver.pojo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器模型构建
 *
 * 1，创建一个ServerSocket 对象，进行端口监听
 * 2，while() 重复监听
 * 3，通过端口监听对象，获取 socket 实例，并且获取网络流
 * 4，输出网络流数据，并且关闭流
 * */
public class SocketServerPojo {

    /**
     * 解析接收到的消息
     * */
    public void acceptMessageFromClient(Socket socket){
        System.out.println( socket.getInetAddress()  );
        System.out.println( socket.getPort() );
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int len;
            byte[] bytes = new byte[1024];
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过socket向指定客户端发送消息
     * */
    public void sendMessageToClient(Socket socket ){
       BufferedOutputStream bufferedOutputStream;
        try {
            bufferedOutputStream = new BufferedOutputStream( socket.getOutputStream() );

            bufferedOutputStream.write("hello".getBytes());
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ServerSocket server = null;
    private Socket socket = null;

    public void server(){
        try {
            server = new ServerSocket(8088);
            System.out.println("等待链接...");

            while(true){
                //等待连接
                socket = server.accept();
                //接收到一个连接，就启动一个监听线程
                new SocketThreadPojo(server,socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

