package com.marmoush.birj.poc.websocket;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;

public class Figure {
	private JsonObject json;

	public JsonObject getJson() {
		return json;
	}

	public void setJson(JsonObject json) {
		this.json = json;
	}

	public Figure(JsonObject json) {
		this.json = json;
	}

	@Override
	public String toString() {
		return json.toString();
	}
}