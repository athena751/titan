package com.hp.app.dao;

import java.io.Serializable;
import java.util.UUID;

public class IdGenerator {

	public static Serializable createId() {
		return UUID.randomUUID().toString();
	}

	public static String createStringId() {
		return (String) createId();
	}
}
