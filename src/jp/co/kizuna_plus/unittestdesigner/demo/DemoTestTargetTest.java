package jp.co.kizuna_plus.unittestdesigner.demo;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestAssertAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParameterAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParameterTypeEnum;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParametersAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestSetupAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestTargetAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.execute.TestExecuter;

/**
 * テスト対象クラス
 *
 */
public class DemoTestTargetTest {

	/**
	 * テスト対象
	 */
	private DemoTestTarget target;
	
	@Before
	public void setup() {
		this.target = new DemoTestTarget();
	}
	
	/**
	 * 値をひとつ設定
	 * @param args
	 */
	public void ValueOneSetup(List<Object> args) {
		this.target.add("001");
	}
	
	/**
	 * Countをひとつ設定
	 * @param args
	 */
	public void CountOneSetup(List<Object> args) {
		this.target.add("001");
		this.target.get(0);
	}
	
	/**
	 * Countをふたつ設定
	 * @param args
	 */
	public void CountTwoSetup(List<Object> args) {
		this.target.add("001");
		this.target.get(0);
		this.target.get(0);
	}
	
	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="get", methodParameters={int.class})
	@TestParametersAnnotation(value={@TestParameterAnnotation(value="0", type=TestParameterTypeEnum.VALUE, parameterClass=Integer.class)})
	@TestAssertAnnotation(value="001", type=TestParameterTypeEnum.VALUE)
	@TestSetupAnnotation(value="ValueOneSetup", description="値をひとつ設定")
	public void testGet_001() {
		TestExecuter.execute(this.target, this);		
	}

	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="get", methodParameters={int.class})
	@TestParametersAnnotation(value={@TestParameterAnnotation(value="-1", type=TestParameterTypeEnum.VALUE, parameterClass=Integer.class)})
	@TestAssertAnnotation()
	@TestSetupAnnotation(value="ValueOneSetup", description="値をひとつ設定")
	public void testGet_002() {
		TestExecuter.execute(this.target, this);		
	}

	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="get", methodParameters={int.class})
	@TestParametersAnnotation(value={@TestParameterAnnotation(value="2", type=TestParameterTypeEnum.VALUE, parameterClass=Integer.class)})
	@TestAssertAnnotation()
	@TestSetupAnnotation(value="ValueOneSetup", description="値をひとつ設定")
	public void testGet_003() {
		TestExecuter.execute(this.target, this);		
	}

	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="getCount", methodParameters={})
	@TestParametersAnnotation(value={})
	@TestAssertAnnotation(value="0", type=TestParameterTypeEnum.VALUE, valueClass=Integer.class)
	@TestSetupAnnotation(value="ValueOneSetup", description="値をひとつ設定")
	public void testCount_001() {
		TestExecuter.execute(this.target, this);
	}

	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="getCount", methodParameters={})
	@TestParametersAnnotation(value={})
	@TestAssertAnnotation(value="1", type=TestParameterTypeEnum.VALUE, valueClass=Integer.class)
	@TestSetupAnnotation(value="CountOneSetup", description="countをひとつ設定")
	public void testCount_002() {
		TestExecuter.execute(this.target, this);
	}


	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="getCount", methodParameters={})
	@TestParametersAnnotation(value={})
	@TestAssertAnnotation(value="2", type=TestParameterTypeEnum.VALUE, valueClass=Integer.class)
	@TestSetupAnnotation(value="CountTwoSetup", description="countをひとつ設定")
	public void testCount_003() {
		TestExecuter.execute(this.target, this);
	}


	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="getEnum", methodParameters={TestParameterTypeEnum.class})
	@TestParametersAnnotation(value={@TestParameterAnnotation(value="VALUE", type=TestParameterTypeEnum.VALUE, parameterClass=TestParameterTypeEnum.class)})
	@TestAssertAnnotation(value="VALUE", type=TestParameterTypeEnum.VALUE, valueClass=TestParameterTypeEnum.class)
	public void testGetEnum_001() {
		TestExecuter.execute(this.target, this);
	}


	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="getEnum", methodParameters={TestParameterTypeEnum.class})
	@TestParametersAnnotation(value={@TestParameterAnnotation(value="NULL", type=TestParameterTypeEnum.VALUE, parameterClass=TestParameterTypeEnum.class)})
	@TestAssertAnnotation(value="NULL", type=TestParameterTypeEnum.VALUE, valueClass=TestParameterTypeEnum.class)
	public void testGetEnum_002() {
		TestExecuter.execute(this.target, this);
	}


	@Test
	@TestTargetAnnotation(targetClass=DemoTestTarget.class, targetMethod="isNullOrEmpty", methodParameters={String[].class})
	@TestParametersAnnotation(value={@TestParameterAnnotation(value="1111,nnnn", isList=false, listDelimiter=",", type=TestParameterTypeEnum.VALUE, parameterClass=String[].class)})
	@TestAssertAnnotation(value="false", type=TestParameterTypeEnum.VALUE, valueClass=Boolean.class)
	public void testIsNullOrEmpty_001() {
		TestExecuter.execute(this.target, this);
	}

}
