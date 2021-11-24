package com.ravi.wsdemo.entity;

import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

@Data
public class CheckRoom {

	private String roomName;

	private Patient checkPatient;

	private final LinkedBlockingDeque<Patient> waitingList = new LinkedBlockingDeque<Patient>();
}
