# websocket SpringBoot Demo
在controller的方法中调用WsEndpoint静态方法即可

```
src/main
├── java
│   └── com
│       └── ravi
│           └── wsdemo
│               ├── WsdemoApplication.java
│               ├── controller
│               │   └── WsController.java    主要调用静态方法
│               ├── entity
│               │   └── Patient.java         List对象元素
│               ├── resp
│               │   └── R.java               RestApi Response模板类 返回code，msg以及自定义字段
│               └── websocket                
│                   ├── ServerEncoder.java      编码器
│                   ├── WebSocketConfig.java    配置类，固定写法
│                   ├── WsEndpoint.java         处理ws一系列逻辑，可以封装一些静态工具方法
│                   └── WsMessage.java          消息类，需要实现编码器
└── resources
    ├── application.properties
    ├── static
    │   ├── index.html                        登陆页 启动项目后访问 http://localhost:8080/index.html
    │   └── ws.html                           ws展示页，会自动跳转
    └── templates
```