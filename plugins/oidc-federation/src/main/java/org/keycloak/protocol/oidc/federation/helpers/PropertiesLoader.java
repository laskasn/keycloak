package org.keycloak.protocol.oidc.federation.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

	
	public static Properties properties = new Properties();
	
	static { 
		try {
			properties = PropertiesLoader.fromFile("oidc-federation.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Properties fromFile(String fileName) throws IOException {
		Properties props = new Properties();
		InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
		if (inputStream != null) {
			props.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
		}
		return props;
	}
	
	public static Properties getProperties() {
		return properties;
	}
	
	
	
}
