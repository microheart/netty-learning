# Learning Netty

## Echo
客户端不断随机发送数字和接收服务端的信息

## Telnet
类似Echo，输入'bye’结束会话。
核心处理逻辑为组合 `LineBasedFrameDecoder`/`DelimiterBasedFrameDecoder`，`StringDecoder`和`StringEncoder` 等Handler。

可进一步扩展，如提供天气等公共服务。


## Heart Beat
心跳检测