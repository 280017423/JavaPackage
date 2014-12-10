/**
 * @Filename ConfigUtil.java
 * @Package com.zshq.packagetool
 * @Description TODO
 * @version 1.0
 * @author admin012 - 2014 Cindigo.All Rights Reserved.
 **/
package com.zshq.packagetool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

	static String mHostName;
	static String mUserName;
	static String mPassword;
	static String mInitCommand;
	static String mGitCommand;
	static String mDeleteCommand;
	static String mPackageCommand;

	static {
		gitConfigInfo();
	}

	private static void gitConfigInfo() {
		InputStream inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream(
				"com/zshq/packagetool/config.properties");
		Properties prop = new Properties();
		try {
			prop.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mHostName = prop.getProperty("host_name");
		mUserName = prop.getProperty("user_name");
		mPassword = prop.getProperty("password");
		mInitCommand = prop.getProperty("init_command");
		mGitCommand = prop.getProperty("git_command");
		mDeleteCommand = prop.getProperty("delete_command");
		mPackageCommand = prop.getProperty("package_command");
	}

}
