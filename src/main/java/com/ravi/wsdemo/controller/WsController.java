package com.ravi.wsdemo.controller;

import com.ravi.wsdemo.entity.Patient;
import com.ravi.wsdemo.websocket.WsEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WsController {

	@GetMapping("/finish")
	public void sendQueue() throws EncodeException, IOException {
		// TODO: 解析医生token，获得对应诊室id
		// TODO: 由诊室id找到对应诊室的缓存与队列，队首出队，缓存换人，缓存加入到导检池
		List<Patient> ret = new ArrayList<>();
		ret.add(new Patient("王蒙", "男"));
		ret.add(new Patient("王2", "男"));
		ret.add(new Patient("王3", "男"));
		ret.add(new Patient("王4", "男"));
		//WsEndpoint.sendMessage2All(ret, "WsMessage");
		WsEndpoint.SendMessage2Sb(ret, "lzl");
	}
}
