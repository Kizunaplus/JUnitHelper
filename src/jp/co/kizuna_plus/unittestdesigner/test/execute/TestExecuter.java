package jp.co.kizuna_plus.unittestdesigner.test.execute;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;

import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestAssertAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParameterAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParameterTypeEnum;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParametersAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestSetupAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestTargetAnnotation;

/**
 * テスト実行クラス
 * 
 */
public class TestExecuter {

	/**
	 * テスト実行<br>
	 * アノテーションにて<br>
	 * テストの条件<br>
	 * テストメソッドのパラメータ<br>
	 * 期待値<br>
	 * などを設定が可能です。
	 * 
	 * @param targetInstance
	 *            テスト対象のインスタンス
	 * @param callInstance
	 *            テストメソッドのインスタンス
	 * @return テストメソッド実行結果
	 */
	public static Object execute(Object targetInstance, Object callInstance) {

		Object retVal = null;

		String thisClassName = TestExecuter.class.getName();
		int stackIndex = 1;
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		while (stackIndex < stacks.length && thisClassName.equals(stacks[stackIndex].getClassName())) {
			// テストクラスのトレースは無視をする。
			stackIndex++;
		}

		if (stacks.length <= stackIndex) {
			// スタックトレースに呼び出しクラスが存在しない場合
			Assert.fail();
		}

		Class<?> callClass = null;
		Method callMethod = null;
		try {
			callClass = Class.forName(stacks[stackIndex].getClassName());
			String methodName = stacks[stackIndex].getMethodName();

			callMethod = callClass.getDeclaredMethod(methodName, new Class<?>[0]);
			callMethod.setAccessible(true);
		} catch (Exception ex) {
			// 呼び出し元のクラスが取得できない場合
			ex.printStackTrace();
			Assert.fail();
		}

		TestParametersAnnotation parametersAnnotation = (TestParametersAnnotation) callMethod
				.getAnnotation(TestParametersAnnotation.class);
		List<Object> args = convert2ParamList(parametersAnnotation);

		TestAssertAnnotation assertAnnotation = (TestAssertAnnotation) callMethod
				.getAnnotation(TestAssertAnnotation.class);

		TestSetupAnnotation setupAnnotation = (TestSetupAnnotation) callMethod.getAnnotation(TestSetupAnnotation.class);
		if (setupAnnotation != null) {
			try {
				Method setupMethod = callClass.getDeclaredMethod(setupAnnotation.value(), List.class);
				setupMethod.setAccessible(true);
				setupMethod.invoke(callInstance, args);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail();
			}
		}

		TestTargetAnnotation targetAnnotation = (TestTargetAnnotation) callMethod
				.getAnnotation(TestTargetAnnotation.class);

		Class<?> targetClass = targetAnnotation.targetClass();
		String targetMethodName = targetAnnotation.targetMethod();
		Class<?>[] targetMethodParams = targetAnnotation.methodParameters();

		try {
			Method targetMethod = targetClass.getDeclaredMethod(targetMethodName, targetMethodParams);
			targetMethod.setAccessible(true);

			retVal = targetMethod.invoke(targetInstance, args.toArray());

			performAnnotationTypeAssert(assertAnnotation, retVal, args);
		} catch (Exception e) {
			try {
				if (e.getCause() != null) {
					performAnnotationTypeAssert(assertAnnotation, e.getCause(), args);
				} else {
					performAnnotationTypeAssert(assertAnnotation, e, args);					
				}
			} catch (Throwable e1) {
				e.printStackTrace();
				throw e1;
			}
		}

		return retVal;
	}

	/**
	 * パラメータアノテーションをパラメータに変換
	 * 
	 * @param parametersAnnotation
	 * @return
	 */
	private static List<Object> convert2ParamList(TestParametersAnnotation parametersAnnotation) {
		List<Object> paramList = new ArrayList<Object>();

		if (parametersAnnotation != null) {
			for (TestParameterAnnotation paramAnotation : parametersAnnotation.value()) {
				String value = paramAnotation.value();
				Class<?> clazz = paramAnotation.parameterClass();
				TestParameterTypeEnum type = paramAnotation.type();
				String listDelimiter = paramAnotation.listDelimiter();

				Object obj = convert2Param(value, clazz, type, listDelimiter);
				if (!"".equals(listDelimiter) && paramAnotation.isList()) {
					obj = Arrays.asList((Object[])obj);
				}
				
				paramList.add(obj);
			}
		}

		return paramList;
	}

	/**
	 * パラメータアノテーションをパラメータに変換
	 * 
	 * @param parameterAnnotation
	 * @return
	 */
	public static Object convert2Param(TestAssertAnnotation parameterAnnotation) {
		Object paramValue = null;

		if (parameterAnnotation != null) {
			String value = parameterAnnotation.value();
			Class<?> clazz = parameterAnnotation.valueClass();
			TestParameterTypeEnum type = parameterAnnotation.type();
			String listDelimiter = parameterAnnotation.listDelimiter();

			paramValue = convert2Param(value, clazz, type, listDelimiter);
		}

		return paramValue;
	}

	/**
	 * パラメータアノテーションをパラメータに変換
	 * 
	 * @param parametersAnnotation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object convert2Param(String value, Class<?> clazz, TestParameterTypeEnum type,
			String listDelimiter) {
		Object paramValue = null;

		if (type == TestParameterTypeEnum.VALUE) {
			// 直値
			if (!"".equals(listDelimiter)) {
				// デリミターが設定されている場合
				String[] values = value.split(listDelimiter);
				Object[] paramValues = createArrayOfType(clazz, values.length);
				for (int valIndex = 0; valIndex < values.length; valIndex++) {
					String val = values[valIndex];
					if ("null".equals(val)) {
						val = null;
					}
					paramValues[valIndex] = convert2ParamValue(val, clazz.getComponentType());
				}
				paramValue = paramValues;
			} else {
				paramValue = convert2ParamValue(value, clazz);
			}
		} else if (type == TestParameterTypeEnum.ENUM) {
			// Enum
			paramValue = Enum.valueOf((Class<Enum>) clazz, value);
		} else if (type == TestParameterTypeEnum.STATIC_MEMBER) {
			// Static メンバー変数
			try {
				Field field = clazz.getDeclaredField(value);
				field.setAccessible(true);
				paramValue = field.get(null);
			} catch (Exception ex) {
				ex.printStackTrace();
				Assert.fail();
			}
		} else if (type == TestParameterTypeEnum.NON_PARAM_STATIC_METHOD) {
			// Static メソッド
			try {
				Method method = clazz.getDeclaredMethod(value, new Class<?>[0]);
				method.setAccessible(true);
				paramValue = method.invoke(null, new Object[0]);
			} catch (Exception ex) {
				ex.printStackTrace();
				Assert.fail();
			}
		} else if (type == TestParameterTypeEnum.DEFAULT_CONSTRUCTOR) {
			// デフォルトコンストラクタ
			try {
				paramValue = clazz.newInstance();
			} catch (Exception ex) {
				ex.printStackTrace();
				Assert.fail();
			}
		} else if (type == TestParameterTypeEnum.TYPE) {
			// Class型値
			paramValue = clazz;
		} else if (type == TestParameterTypeEnum.NULL) {
			// NULL値
			paramValue = null;
		}

		return paramValue;
	}

	/**
	 * 変換が可能な形の場合に値として使用します。
	 * 
	 * @param paramAnotation
	 *            パラメータアノテーション
	 * @return 値
	 */
	@SuppressWarnings("unchecked")
	private static <T> T convert2ParamValue(String value, Class<T> clazz) {

		if (value == null) {
			// 値がnullの場合は常にnullを設定
			return null;
		}

		Object retValue = null;
		if (Integer.class.isAssignableFrom(clazz)) {
			// Integer
			retValue = Integer.parseInt(value);
		} else if (Long.class.isAssignableFrom(clazz)) {
			// Long
			retValue = Long.parseLong(value);
		} else if (Byte.class.isAssignableFrom(clazz)) {
			// Byte
			retValue = Byte.parseByte(value);
		} else if (Short.class.isAssignableFrom(clazz)) {
			// Short
			retValue = Short.parseShort(value);
		} else if (Float.class.isAssignableFrom(clazz)) {
			// Float
			retValue = Float.parseFloat(value);
		} else if (Double.class.isAssignableFrom(clazz)) {
			// Double
			retValue = Double.parseDouble(value);
		} else if (Boolean.class.isAssignableFrom(clazz)) {
			// Boolean
			retValue = Boolean.parseBoolean(value);
		} else if (Class.class.isAssignableFrom(clazz)) {
			// Class
			try {
				retValue = Class.forName(value);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if (clazz == null || String.class.isAssignableFrom(clazz)) {
			// 文字列または、形の指定がない場合
			retValue = value;
		} else {
			// コンストラクタに文字列のみを受け取る場合はインスタンスを生成する。
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
				constructor.setAccessible(true);
				retValue = constructor.newInstance(value);

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail();
			}

		}

		return (T)retValue;
	}
	
	/**
	 * 指定classのList型を取得します。
	 * @param type
	 * @return
	 */
	public static <T> List<T> createListOfType(T type) {
	    return new ArrayList<T>();
	}
	
	/**
	 * 指定classのList型を取得します。
	 * @param <T>
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] createArrayOfType(Class<T> type, int size) {
	    return (T[]) Array.newInstance(type.getComponentType(), size); 
	}

	/**
	 * アノテーションに設定されたタイプに対応したアサートを実施
	 * 
	 * @param annotation
	 *            アサート定義
	 * @param actual
	 *            実行結果
	 * @param args
	 *            メソッド引数
	 */
	private static void performAnnotationTypeAssert(TestAssertAnnotation annotation, Object actual, List<Object> args) {
		String mode = annotation.assertMode();

		Method method;
		try {
			method = annotation.assertDefClass().getDeclaredMethod(mode, TestAssertAnnotation.class, Object.class, List.class);
			method.setAccessible(true);
			method.invoke(null, annotation, actual, args);
		} catch (Exception e) {
			if (e.getCause() instanceof AssertionError) {
				throw (AssertionError) e.getCause();
			}
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * インスタンスの比較<br>
	 * 一致することを確認
	 * 
	 * @param annotation
	 * @param actual
	 * @param args
	 */
	public static void assertInstance(TestAssertAnnotation annotation, Object actual, List<Object> args) {
		Object obj = convert2Param(annotation);

		Assert.assertEquals(obj, actual);
	}

	/**
	 * インスタンスの比較<br>
	 * 一致しないことを確認
	 * 
	 * @param annotation
	 * @param actual
	 * @param args
	 */
	public static void assertNotInstance(TestAssertAnnotation annotation, Object actual, List<Object> args) {
		Object obj = convert2Param(annotation);

		Assert.assertNotSame(obj, actual);
	}

	/**
	 * 型の比較<br>
	 * 一致することを確認
	 * 
	 * @param annotation
	 * @param actual
	 * @param args
	 */
	public static void assertType(TestAssertAnnotation annotation, Object actual, List<Object> args) {
		Object obj = convert2Param(annotation);

		Assert.assertEquals(obj, actual.getClass());
	}

	/**
	 * 型の比較<br>
	 * 一致しないことを確認
	 * 
	 * @param annotation
	 * @param actual
	 * @param args
	 */
	public static void assertNotType(TestAssertAnnotation annotation, Object actual, List<Object> args) {
		Object obj = convert2Param(annotation);

		Assert.assertNotSame(obj, actual.getClass());
	}

	/**
	 * サイズの比較<br>
	 * 一致することを確認
	 * 
	 * @param annotation
	 * @param actual
	 * @param args
	 */
	public static void assertSize(TestAssertAnnotation annotation, Object actual, List<Object> args) {
		Object obj = convert2Param(annotation);

		Assert.assertEquals(obj, ((Collection<?>) actual).size());
	}

	/**
	 * サイズの比較<br>
	 * 一致しないことを確認
	 * 
	 * @param annotation
	 * @param actual
	 * @param args
	 */
	public static void assertNotSize(TestAssertAnnotation annotation, Object actual, List<Object> args) {
		Object obj = convert2Param(annotation);

		Assert.assertNotSame(obj, ((Collection<?>) actual).size());
	}
	

	/**
	 * インスタンスの比較<br>
	 * 一致することを確認
	 * 
	 * @param annotation
	 * @param actual
	 * @param args
	 */
	public static void assertExceptionMessage(TestAssertAnnotation annotation, Object actual, List<Object> args) {
		Object obj = convert2Param(annotation);
		
		if (!(actual  instanceof Throwable)) {
			Assert.fail("does not throwable object : " + actual.toString());
		}
		Throwable actualThrowable = (Throwable)actual;

		Assert.assertEquals(obj, actualThrowable.getMessage());
	}
}
