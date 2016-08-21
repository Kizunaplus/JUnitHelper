package jp.co.kizuna_plus.unittestdesigner.test.execute;

import java.io.Serializable;
import java.util.Map;

public class TestCaseInfo implements Serializable {

	/**
	 * 
	 * Serial Version id
	 */
	private static final long serialVersionUID = -113752288553848858L;

	/**
	 * テスト対象クラス名
	 */
	private String className;

	/**
	 * テスト対象メソッド名
	 */
	private String methodName;

	/**
	 * テスト対象パラメータ内容
	 */
	private String parameters;

	/**
	 * パラメータ値
	 */
	private Map<String, String> parameterValueMap;

	/**
	 * テスト事前設定
	 */
	private String setupMessage;

	/**
	 * 期待値
	 */
	private String expectedMessage;

	/**
	 * テスト対象クラス名を取得します。
	 * 
	 * @return テスト対象クラス名
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * テスト対象クラス名を設定します。
	 * 
	 * @param className
	 *            テスト対象クラス名
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getParameterValueMap() {
		return parameterValueMap;
	}

	public void setParameterValueMap(Map<String, String> parameterValueMap) {
		this.parameterValueMap = parameterValueMap;
	}

	public String getSetupMessage() {
		return setupMessage;
	}

	public void setSetupMessage(String setupMessage) {
		this.setupMessage = setupMessage;
	}

	public String getExpectedMessage() {
		return expectedMessage;
	}

	public void setExpectedMessage(String expectedMessage) {
		this.expectedMessage = expectedMessage;
	}

}
