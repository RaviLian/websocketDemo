package com.ravi.wsdemo.websocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

@Slf4j
public class ServerEncoder implements Encoder.Text<WsMessage> {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String encode(WsMessage message) {
		try {
			String json = JSON.toJSONString(message.getMessage(), false);
			log.info("消息编码: " + json);
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
