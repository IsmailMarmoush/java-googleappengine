package com.marmoush.birj.poc.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class FigureEncoder implements Encoder.Text<Figure> {
	@Override
	public String encode(Figure figure) throws EncodeException {
		System.out.println("Encoding figure to:" + figure.getJson().toString());
		return figure.getJson().toString();
	}

	@Override
	public void init(EndpointConfig ec) {
		System.out.println("init encoder");
	}

	@Override
	public void destroy() {
		System.out.println("destroy encoder");
	}

}