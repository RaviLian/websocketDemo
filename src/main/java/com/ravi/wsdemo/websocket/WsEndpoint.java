package com.ravi.wsdemo.websocket;

import com.ravi.wsdemo.resp.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@Slf4j
@Component
@ServerEndpoint(value = "/ws/{username}", encoders = {ServerEncoder.class})
public class WsEndpoint {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;

	private static final ConcurrentHashMap<String,WsEndpoint> websocketMap = new ConcurrentHashMap<String,WsEndpoint>();

	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	private String username = "";

	/**
	 * 此方法基本上仅在类内调用，就是给当前的WsEndpoint对象发送message
	 * 底层调用sendObject方法，需要设置一个传入的Object的编码器，即ServerEncoder
	 */
	public void sendMessage(Object message) throws IOException, EncodeException {
		this.session.getBasicRemote().sendObject(message);
	}

	/**
	 * 给导诊屏和医生端的信息推送
	 * 需要检测导诊屏Type和医生Type
	 * @param message 单独诊室的信息推送
	 * @param roomName 诊室id
	 */
	public static void sendSingleCheckMessage2sameRoom(Object message, String roomName) {
		Set<String> keys = websocketMap.keySet();
		Set<String> sameRoom = new CopyOnWriteArraySet<>();
		for (String key : keys) {
			if (StringUtils.contains(key, roomName)) {
				sameRoom.add(key);
			}
		}
		for (String client : sameRoom) {
			sendWsMessage(message, client);
		}
	}

	public static void sendAdminGlobal(Object message) {
		Set<String> keys = websocketMap.keySet();
		Set<String> adminClients = new CopyOnWriteArraySet<>();
		for (String key : keys) {
			if (StringUtils.contains(key, "admin")) {
				adminClients.add(key);
			}
		}
		for (String client : adminClients) {
			sendWsMessage(message, client);
		}
	}

	/**
	 * 发消息的进一步封装
	 * @param message 消息
	 * @param client 客户端
	 */
	private static void sendWsMessage(Object message, String client) {
		WsEndpoint item  = websocketMap.get(client);
		try {
			WsMessage msg = new WsMessage();
			R r = R.ok();
			r.put("data",message);
			msg.setMessage(r);
			item.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送给某一个客户端
	 * @param message 消息
	 * @param username 客户端唯一标识
	 */
	public static void SendMessage2Sb(Object message, String username) {
		Set<String> keys = websocketMap.keySet();
		log.info("当前在线者有: " + keys);
		log.info("需要发送给: " + username);
		for (String key : keys) {
			if (key.equals(username)) {
				sendWsMessage(message, key);
				break;
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WsEndpoint.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WsEndpoint.onlineCount--;
	}

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) {
		this.session = session;
		this.username = username;
		if(websocketMap.containsKey(username)){
			websocketMap.remove(username);
			websocketMap.put(username, this);
		}else {
			websocketMap.put(username, this);
			addOnlineCount();           //在线数加1
		}
		log.info("用户" + username + "连接, 当前在线人数为: " + getOnlineCount());
		try {
			WsMessage msg = new WsMessage();
			R r = R.ok();
			msg.setMessage(r);
			sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("IO异常");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(@PathParam("username") String username) {
		if (websocketMap.containsKey(username)) {
			websocketMap.remove(username);
			subOnlineCount();  //在线数减1
		}
		log.info("用户" + username + "退出！当前在线人数为: " + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("接收到窗口: " + username + "发来的消息: " + message);
		WsMessage msg = new WsMessage();
		R r = R.ok("收到回复!");
		msg.setMessage(r);
		try {
			sendMessage(msg);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 发生错误时调用
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误!");
		error.printStackTrace();
	}
}
