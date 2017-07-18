package com.junranhuigu.simpleNote;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.LoggerFactory;

public class TeamProperty {
	private static TeamProperty instance;
	private Properties properties;
	
	private TeamProperty() {
		try {
			properties = new Properties();
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			properties.load(new FileInputStream(path + "config.properties"));
		} catch (Exception e) {
			LoggerFactory.getLogger(TeamProperty.class).error("", e);
		}
	}

	public static TeamProperty getInstance() {
		if(instance == null){
			instance = new TeamProperty();
		}
		return instance;
	}
	
	public String get(String key){
		return properties.getProperty(key, null);
	}
}
