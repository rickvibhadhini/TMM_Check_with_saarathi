package com.bff.demo.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum SendbackStatus {
	INITIATED,
	ACCEPTED,
	REJECTED;

	public static SendbackStatus resolve(String val) {
		try {
			return SendbackStatus.valueOf(val.toUpperCase());
		} catch (Exception e) {
			log.error("error resolving SendbackStatus: {}, invalid status", val);
		}
		return null;
	}
}
