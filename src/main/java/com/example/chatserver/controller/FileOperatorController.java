package com.example.chatserver.controller;

import com.example.chatserver.enums.ResponseCode;
import com.example.chatserver.enums.ResponseMessage;
import com.example.chatserver.pojo.ResponsePojo;
import com.example.chatserver.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/fileOperate")
public class FileOperatorController {

    @Value("${file.realdir}")
    private String fileUrl;

    @RequestMapping("/uploadFile")
    public String uploadFile(String path, MultipartFile file){

        if(FileUtils.uploadFiles(fileUrl + path,file)){
            return new ResponsePojo().response(ResponseCode.SuccessMessage.getCode(), ResponseMessage.SuccessUpload.toString());
        }else{
            return new ResponsePojo().response(ResponseCode.FailMessage.getCode(), ResponseMessage.FailUpload.toString());
        }
    }

    //path为相对路径
    @RequestMapping("downloadFile")
    public void downloadFile(String path, HttpServletResponse response){
        System.out.println(fileUrl + path);
        FileUtils.downloadFile(fileUrl + path,response);
    }
}
