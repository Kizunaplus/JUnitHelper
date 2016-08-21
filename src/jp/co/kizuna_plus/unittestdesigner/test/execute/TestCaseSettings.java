package jp.co.kizuna_plus.unittestdesigner.test.execute;

import java.io.Serializable;
import java.util.Map;

public class TestCaseSettings implements Serializable {
	
	/**
	 * Serial Version id
	 */
	private static final long serialVersionUID = -1306461101587401357L;

	/**
	 * 対象とするパッケージ
	 */
	private String[] packages;
	
	/**
	 * 変換するメッセージマップ
	 */
	private Map<String, String> messages;

	/**
	 * 対象とするパッケージの取得
	 * @return 対象とするパッケージ
	 */
	public String[] getPackages() {
		return packages;
	}

	/**
	 * 対象とするパッケージの設定
	 * 
	 * @param packages 対象とするパッケージ
	 */
	public void setPackages(String[] packages) {
		this.packages = packages;
	}

	/**
	 * 変換するメッセージマップを取得
	 * 
	 * @return 変換するメッセージマップ
	 */
	public Map<String, String> getMessages() {
		return messages;
	}

	/**
	 * 変換するメッセージマップを設定
	 * 
	 * @param messages 変換するメッセージマップ
	 */
	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}
	
	
}
