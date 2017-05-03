<%@ page language="java" contentType="text/html; charset=Utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="jquery-3.2.0.min.js"></script>
</head>
<body>

</body>
<script type="text/javascript">
$(function(){
	//创建一个Socket实例
	var socket = new WebSocket('ws://localhost:8080/ws/ws/app/yothin'); 

	// 打开Socket 
	socket.onopen = function(event) { 

	  // 发送一个初始化消息
	  socket.send('你好'); 

	  // 监听消息
	  socket.onmessage = function(event) { 
	    console.log('Client received a message',event.data); 
	  }; 

	  // 监听Socket的关闭
	  socket.onclose = function(event) { 
	    console.log('Client notified socket has closed',event); 
	  }; 

	  // 关闭Socket.... 
	  //socket.close() 
	}
});

	
</script>
</html>