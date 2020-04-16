package com.example.chatserver.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/*文件工具*/
public class FileUtils {

    /** 上传单个文件
     *  path ：相对路径
     *
     */
    public static boolean uploadFiles(String path,MultipartFile file){
        //输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;

        //获取文件名称
        String fileName = file.getOriginalFilename();
        if(file.isEmpty()){
            return false;
        }

        String time = String.valueOf(new Date().getTime());
        System.out.println( path + time + fileName );

        File serverFile = new File(path + time + fileName);

        if( !(serverFile.getParentFile().exists()) ){
            serverFile.getParentFile().mkdirs();
        }

        //判断文件是否存在
        if(serverFile.exists()){
            return false;
        }
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(serverFile);
            //通过字节读文件
            int b;
            byte[] buffer = new byte[2048];
            while( (b = inputStream.read(buffer)) != -1 ){
                outputStream.write(buffer,0,b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }



    /**
     * 文件下载
     * String url：文件下载路径,到具体的文件,服务器硬盘上绝对路径
     * HttpServletResponse response：响应对象
     * */
    public static void downloadFile(String url, HttpServletResponse response){
        String fileName = url.substring(url.lastIndexOf("/")+1);
        //设置响应头，fileName 为下载后的文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName.substring(13));
        //封装要下载的文件
        File file = new File(url);
        ServletOutputStream out = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            out = response.getOutputStream();
            //通过字节数组读取数据
            int b = 0;
            byte[] buffer = new byte[2048];
            while ((b = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
