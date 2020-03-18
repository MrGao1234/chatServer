package com.example.chatserver.pojo;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class SocketThreadPojo extends Thread{

    /**
     * 客户端连接集合
     * */
    private static Map<String,SocketThreadPojo> serverThreadMap = new HashMap<>();

    ServerSocket server = null;
    Socket socket = null;
    InputStream is = null;
    OutputStream os = null;
    String clientName = null;
    boolean alive = true;

    public SocketThreadPojo(){}

    public SocketThreadPojo(ServerSocket server,Socket socket){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run(){

        try {
            //输入输出流
            is = socket.getInputStream();
            os = socket.getOutputStream();

            //缓存区
            byte[] b = new byte[1024];
            int length = 0;
            while(alive){

                //接收从客户端发送的消息
                length = is.read(b);
                if(length != -1){
                    String message = new String(b,0,length);

                    //json字符串转 HashMap
                    HashMap hashMap = null;

                    try {
                        hashMap = new ObjectMapper().readValue(message, HashMap.class);
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    }

                    //消息类型
                    String type = (String)hashMap.get("type");

                    //新连接
                    if("OPEN".equals(type)){
                        clientName = (String)hashMap.get("clientName");

                        //添加客户端到集合容器中
                        serverThreadMap.put(clientName,this);
                        System.out.println(clientName + "连接成功！");
                        System.out.println("当前连接客户端数量：" + serverThreadMap.size());
                    }

                    //关闭
                    if("CLOSE".equals(type)){
                        alive = false;
                        System.out.println(clientName + "退出连接，关闭监听线程！");
                    }

                    //文本消息
                    if("MESSAGE".equals(type)){
                        String msg = (String)hashMap.get("message");
                        String chat = (String)hashMap.get("chat");

                        //群聊(广播)
                        if("GROUP".equals(chat)){
                            //遍历容器，给容器每个对象转发消息
                            for(SocketThreadPojo st : serverThreadMap.values()){
                                if(st != this){
                                    st.os.write(new String(b,0,length).getBytes());
                                }
                            }

                            //后台打印
                            System.out.println(clientName + ":" + msg);
                        }

                        if("PRIVATE".equals(chat)){
                            String to = (String)hashMap.get("to");
                            serverThreadMap.get(to).os.write(new String(b,0,length).getBytes());
                            //后台打印
                            System.out.println(clientName + "向" + to + "说：" + msg);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(clientName + "断开连接！");
        } finally{
            serverThreadMap.remove(clientName);
            System.out.println( "当前客户端数量：" + serverThreadMap.size() );
            try {
                os.close();
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
