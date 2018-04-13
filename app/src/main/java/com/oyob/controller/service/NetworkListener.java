package com.oyob.controller.service;

public interface NetworkListener {
	void onRequestCompleted(String response, String errorString, int eventType);

}
