package com.ravi.wsdemo.controller;

import com.ravi.wsdemo.entity.CheckRoom;
import com.ravi.wsdemo.entity.Patient;
import com.ravi.wsdemo.websocket.WsEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

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
		WsEndpoint.SendMessage2Sb(ret, "admin01");
	}

	@GetMapping("/global")
	public String sendGlobal() {
		Patient p1 = new Patient("张佳", "女");
		Patient p2 = new Patient("王楠", "女");
		Patient p3 = new Patient("张妙", "女");
		Patient p4 = new Patient("张可", "女");

		CheckRoom room1 = new CheckRoom();
		CheckRoom room2 = new CheckRoom();
		room1.setRoomName("CaiChao1");
		room1.setCheckPatient(p1);
		LinkedBlockingDeque<Patient> waitingList1 = room1.getWaitingList();
		waitingList1.add(p2);
		waitingList1.add(p3);

		room2.setRoomName("CYS");
		room2.setCheckPatient(null);
		room2.getWaitingList().add(p4);

		List<CheckRoom> allRooms = new ArrayList<>();
		allRooms.add(room1);
		allRooms.add(room2);
		WsEndpoint.sendAdminGlobal(allRooms);
		WsEndpoint.sendSingleCheckMessage2sameRoom(room1, room1.getRoomName());
		return "ok";
	}
}
