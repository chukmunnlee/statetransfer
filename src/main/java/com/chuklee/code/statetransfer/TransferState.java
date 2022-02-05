package com.chuklee.code.statetransfer;

import java.util.HashMap;
import java.util.Map;

import jakarta.json.JsonValue;

public class TransferState {

	private final String appId;
	private final Map<String, JsonValue> states;

	public TransferState(String appId) {
		this.appId = appId;
		this.states = new HashMap<>();
	}

	public void add(String name, JsonValue value) {
		this.states.put(name, value);
	}

	public String render() {
		return StateTransferUtils.scriptTag(this.appId, this.states);
	}
}
