package com.zshq.packagetool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author zou.sq
 * @version v1.0.0
 */
public class MainPanel extends JPanel {

	private static final long serialVersionUID = 4331532130365800311L;

	private JTextField mEdtDomain;
	private JCheckBox[] mCbStyle = new JCheckBox[2];
	private JButton mBtnPackage;
	private List<String> lists = new ArrayList<String>();
	private SshHelper mSshHelper;

	public MainPanel() {
		setBackground(Color.white);
		// 垂直布局
		setLayout(new BoxLayout(MainPanel.this, BoxLayout.Y_AXIS));
		initViews();
	}

	private void initViews() {
		JPanel panel1 = new JPanel();
		BoxLayout layout = new BoxLayout(panel1, BoxLayout.Y_AXIS);
		panel1.setLayout(layout);

		JLabel jLabel = new JLabel("[输入幼儿园域名，多个以分号（英文;）隔开]");
		panel1.add(jLabel, BorderLayout.SOUTH);

		mEdtDomain = new JTextField();
		mEdtDomain.setEditable(true);
		panel1.add(mEdtDomain, BorderLayout.NORTH);

		JPanel panel2 = new JPanel(new FlowLayout());
		String jcbTexts[] = new String[] { "家长版", "教师版" };
		for (int i = 0; i < mCbStyle.length; i++) {
			mCbStyle[i] = new JCheckBox(jcbTexts[i], true);
			mCbStyle[i].setBackground(Color.white);
			panel2.add(mCbStyle[i]);
		}

		mBtnPackage = new JButton("开始打包");
		mBtnPackage.addActionListener(clickListener);

		JPanel srcPanel = new JPanel(new GridLayout(1, 1));
		JPanel desPanel = new JPanel(new GridLayout(1, 1));
		JPanel btnPanel = new JPanel(new GridLayout(2, 1));

		srcPanel.add(panel1);
		desPanel.add(panel2);
		btnPanel.add(mBtnPackage);

		srcPanel.setBorder(BorderFactory.createTitledBorder("输入定制信息"));
		desPanel.setBorder(BorderFactory.createTitledBorder("选择打包版本"));
		btnPanel.setBorder(BorderFactory.createTitledBorder("打包"));

		add(srcPanel);
		add(desPanel);
		add(btnPanel);
	}

	private ActionListener clickListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			lists.clear();
			String domain = mEdtDomain.getText().trim();
			if (null == domain || "".equals(domain)) {
				showError("域名不能为空");
				return;
			}
			if (mCbStyle[0].isSelected()) {
				lists.add("jyt");
			}
			if (mCbStyle[1].isSelected()) {
				lists.add("xyt");
			}
			if (lists.isEmpty()) {
				showError("选择打包版本");
				return;
			}
			String patten = "^[a-z;]+$";
			if (!Pattern.matches(patten, domain)) {
				showError("包含非法字符");
				return;
			}
			final String command = ConfigUtil.mInitCommand + ConfigUtil.mGitCommand + ConfigUtil.mDeleteCommand
					+ ConfigUtil.mPackageCommand + buildPackageCommand(domain, lists);
			startPackage(command);
		}

		private String buildPackageCommand(String domain, List<String> lists) {
			StringBuilder builder = new StringBuilder();
			String[] domains = domain.split(";");
			for (int i = 0; i < domains.length; i++) {
				for (int j = 0; j < lists.size(); j++) {
					builder.append("source build.sh " + lists.get(j) + " " + domains[i] + "; ");
				}
			}
			return builder.toString();
		}

		private void startPackage(String command) {

			mSshHelper = SshHelper.getInstances();
			try {
				boolean isSuccess = mSshHelper.connect();
				if (isSuccess) {
					Integer status = mSshHelper.exeCmd(command);
					if (null != status && 0 == status) {
						JOptionPane.showMessageDialog(null, "打包成功", "提示", JOptionPane.INFORMATION_MESSAGE);
					} else {
						showError("打包失败");
					}
				} else {
					showError("连接服务器失败");
					mSshHelper.closeConnection();
				}
			} catch (IOException e) {
				showError("打包失败");
				e.printStackTrace();
			}
		}
	};

	private void showError(String errorInfo) {
		JOptionPane.showMessageDialog(null, errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
	}
}
