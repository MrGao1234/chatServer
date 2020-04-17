> 1,消息类型 socket 长连接信息交互统一使用 json 字符串
```
C-S：
    新建连接：{"type":"OPEN","clientName":"WUJI"}    
    关闭连接：{"type":"CLOSE","clientName":"WUJI"}
    消息内容：{"type":"MESSAGE","message":"测试消息" ,"to":"ZHAO","clientName":"WUJI"}
    
    添加好友：{"type":"ADDREQUEST","message":"验证信息","clientName":"WUJI","to":"ZHAO"}     //有效期 七天
    统一添加：{"type":"AGREE","clientName":"ZHAO","to":"WUJI"}
    拒绝添加：{"type":"REFUSE","clientName":"ZHAO","to":"WUJI","message":"理由"}
    
S-C:
    发送消息：{"id":"1","message":"测试消息","time":"时间","sender":"ZHANG","accepter":"ZHAO"，"status":"0"}        //MessageInformation
    好友请求：{"sender":"ZHANG","accepter":"ZHAO","time":"2019-11-12"}                                              //FriendsInformation
    请求回复：{"sender":"ZHANG","accepter":"ZHAO","time":"2019-11-12","componts":"备注","passFlag":"1"}
```