package com.example.chatserver.socket;

import com.alibaba.fastjson.JSON;
import com.example.chatserver.bean.LoginInformation;
import com.example.chatserver.bean.MessageInformation;
import com.example.chatserver.dao.LoginInformationDao;
import com.example.chatserver.dao.MessageInformationDao;
import com.example.chatserver.enums.LoginInformationEnum;
import com.example.chatserver.enums.RedisEnum;
import com.example.chatserver.utils.ApplicationContextProvider;
import com.example.chatserver.utils.RedisUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SocketThread extends Thread{

    //jpa
    private LoginInformationDao loginInformationDao;
    private MessageInformationDao messageInformationDao;

    //redis工具类
    private RedisUtils redisUtils;
    //redis列表
   // private HashOperations hashOperations;
    private ListOperations listOperations;

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
        this.loginInformationDao = ApplicationContextProvider.getBean(LoginInformationDao.class);
        this.messageInformationDao = ApplicationContextProvider.getBean(MessageInformationDao.class);
        this.redisUtils = ApplicationContextProvider.getBean(RedisUtils.class);
        this.listOperations = ApplicationContextProvider.getBean(ListOperations.class);
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
                        loginInformationDao.save(new LoginInformation(clientName,new Date(),LoginInformationEnum.EXIT.getCode()));
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

                            //持久化数据
                            MessageInformation information = new MessageInformation(msg,new Date(),clientName,to,"1");
                            persisted(information);

                        }else{

                            //向对方回信
                            //MessagePojo messagePojo = new MessagePojo("MESSAGE",msg,clientName,to,new SimpleDateFormat("yyyy-MM-dd HH-mm_ss").format(new Date()));
                            MessageInformation information = new MessageInformation(msg,new Date(),clientName,to,"0");
                            serverThreadMap.get(to).os.write(JSON.toJSONString(information).getBytes());

                            //持久化数据

                            persisted(information);

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(clientName + "断开连接！");
        } finally{
            serverThreadMap.remove(clientName);
            loginInformationDao.save(new LoginInformation(clientName,new Date(),LoginInformationEnum.EXIT.getCode()));
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


    public void persisted(MessageInformation message){
        //将消息添加到redis中
        listOperations.rightPush(RedisEnum.ChatMessageList.getCode(),message);
        //消息够十条就保存数据库
        if( (listOperations.size(RedisEnum.ChatMessageList.getCode()) ) >= 10){
            List<MessageInformation> messageList = listOperations.range(RedisEnum.ChatMessageList.getCode(),0,listOperations.size(RedisEnum.ChatMessageList.getCode()));
            //持久化到数据库
            for(int i = 0;i < messageList.size();i++){
                MessageInformation messageInformation = messageList.get(i);
                messageInformationDao.save(messageInformation);
            }
            //messageInformationDao.saveAll(messageList);
            //清空redis
            //listOperations.p(RedisEnum.ChatMessageList.getCode());
            redisUtils.deleteKey(RedisEnum.ChatMessageList.getCode());
        }
    }
}
