package com.shipfast;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class Services {
	private boolean isDocker;
	private Map<String, Integer> ports;

	public Services(Environment environment) {
		isDocker = Arrays.asList(environment.getActiveProfiles()).contains("docker");
		ports = new HashMap<>();
		ports.put("inventory", 10004);
		ports.put("orders", 10001);
		ports.put("shipments", 10002);
		ports.put("www", 10003);
	}

	public String getURI(String service) {
		if(isDocker) {
			return "http://" + service + "/api/" + service;
		}
		else {
			return "http://localhost:" + ports.get(service.toLowerCase()) + "/api/" + service.toLowerCase();
		}
	}
}
