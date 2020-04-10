package com.example.chatserver.socket;

import com.alibaba.fastjson.JSON;
import com.example.chatserver.bean.LoginInformation;
import com.example.chatserver.dao.LoginInformationDao;
import com.example.chatserver.enums.LoginInformationEnum;
import com.example.chatserver.pojo.MessagePojo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("Duplicates")
public class SocketThread extends Thread{

    @Autowired
    private LoginInformationDao loginInformationDao;

    /**
     * 客户端连接集合
     * */
    private static Map<String, SocketThread> serverThreadMap = new HashMap<>();

    ServerSocket server = null;
    Socket socket = null;
    InputStream is = null;
    OutputStream os = null;
    String clientName = null;
    boolean alive = true;

    public SocketThread(){}

    public SocketThread(ServerSocket server, Socket socket){
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

                        //添加登录信息到数据库表中
                        loginInformationDao.save(new LoginInformation(clientName,new Date(), LoginInformationEnum.LOGIN.getCode()));

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

                        //if("PRIVATE".equals(chat)){
                        String to = (String)hashMap.get("to");
                        //serverThreadMap.get(to).os.write(new String(b,0,length).getBytes());
                        //后台打印
                        System.out.println(clientName + "向" + to + "说：" + msg);

                        /**发送消息*/
                        //判断对方是否登录
                        if(serverThreadMap.get(to) == null){
                            System.out.println("to" + "尚未登录" );
                        }else{
                            MessagePojo messagePojo = new MessagePojo("MESSAGE",msg,clientName,to,new SimpleDateFormat("yyyy-MM-dd HH-mm_ss").format(new Date()));
                            serverThreadMap.get(to).os.write(JSON.toJSONString(messagePojo).getBytes());
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
