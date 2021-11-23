package com.ravi.wsdemo.websocket;

import com.ravi.wsdemo.resp.R;

public class WsMessage {
	private static final long serialVersionUID = 1L;

	private R message;

	public R getMessage() {
		return message;
	}

	public void setMessage(R message) {
		this.message = message;
	}
}
