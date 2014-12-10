package com.zshq.packagetool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SshHelper {
	private Connection mConnection;
	private static SshHelper MSSHHELPER;

	private SshHelper() {
	}

	public static SshHelper getInstances() {
		if (null == MSSHHELPER) {
			MSSHHELPER = new SshHelper();
		}
		return MSSHHELPER;
	}

	public boolean connect() {
		boolean isSuccess = false;
		try {
			mConnection = getOpenedConnection(ConfigUtil.mHostName, ConfigUtil.mUserName, ConfigUtil.mPassword);
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * 执行ssh命令
	 * 
	 * @param connection
	 *            Connection
	 * @param cmd
	 *            执行的命令
	 * @return exit status
	 * @throws IOException
	 */
	public int exeCmd(String cmd) throws IOException {
		if (null == cmd || "".equals(cmd)) {
			System.out.println("命令不能为空");
		}
		if (null == mConnection) {
			return -1;
		}
		System.out.println("运行ssh命令 [" + cmd + "]");

		Session sess = mConnection.openSession();
		sess.execCommand(cmd);

		InputStream stdout = new StreamGobbler(sess.getStdout());
		InputStream stderr = new StreamGobbler(sess.getStderr());
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));

		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
		}
		while (true) {
			String line = stderrReader.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
		}
		br.close();
		stderrReader.close();
		sess.close();
		Integer statu = sess.getExitStatus().intValue();
		System.out.println("statu:" + statu);
		return statu;
	}

	/**
	 * return a opened Connection
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	private static Connection getOpenedConnection(String host, String username, String password) throws IOException {
		Connection conn = new Connection(host);
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(username, password);
		if (isAuthenticated == false) {
			throw new IOException("连接服务器失败，请检查网络和登录信息");
		}
		return conn;
	}

	public void closeConnection() {
		if (null != mConnection) {
			mConnection.close();
		}
		mConnection = null;
	}
}
