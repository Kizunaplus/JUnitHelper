package jp.co.kizuna_plus.unittestdesigner.test.execute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestAssertAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParameterAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParametersAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestSetupAnnotation;
import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestTargetAnnotation;

public class TestCaseGenerater {

	private static final char PACKAGE_SEP = '.';
	private static final char PACKAGE_RESOURCE_SEP = '/';

	/**
	 * テストケース表のもととなるXMLファイルを作成します。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Required argument.");
			System.out.println("TestCaseGenerater <Setting xml file path>");
			System.out.println("ex. TestCaseGenerater settings.xml");
			return;
		}

		File xmlFile = new File(args[0]);
		if (!xmlFile.exists()) {
			System.out.println("File not exist : " + args[0]);
			return;
		}

		FileInputStream fis = null;
		ObjectInputStream ois = null;
		TestCaseSettings settings;
		try {
			fis = new FileInputStream(xmlFile);
			ois = new ObjectInputStream(fis);
			settings = (TestCaseSettings) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (settings == null) {
			System.out.println("Does not Setting file : " + args[0]);
			return;
		}

		List<TestCaseInfo> testCaseList = new ArrayList<>();
		for (String packageName : settings.getPackages()) {

			String packageResource = packageName.replace(PACKAGE_SEP, PACKAGE_RESOURCE_SEP);

			ClassLoader loader = ClassLoader.getSystemClassLoader();

			URL resource = loader.getResource(packageResource);

			File f = new File(resource.getPath());

			Class<?> foundClass = null;
			for (String file : f.list()) {
				if (file.matches(".*\\.class$")) {
					String className = packageName + PACKAGE_SEP + file.replaceAll("\\.class$", "");

					try {
						foundClass = Class.forName(className);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						continue;
					}
				}
			}

			if (foundClass == null) {
				// クラスが見つからない場合
				continue;
			}

			for (Method method : foundClass.getDeclaredMethods()) {
				TestTargetAnnotation targetAnnotation = method.getAnnotation(TestTargetAnnotation.class);
				if (targetAnnotation != null) {
					continue;
				}

				TestCaseInfo testCase = new TestCaseInfo();
				testCase.setClassName(targetAnnotation.getClass().getSimpleName());
				testCase.setMethodName(targetAnnotation.targetMethod());
				
				Method targetMethod = null;
				try {
					targetMethod = targetAnnotation.getClass().getDeclaredMethod(targetAnnotation.targetMethod(), targetAnnotation.methodParameters());
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
					return;
				}

				TestSetupAnnotation setupAnnotation = method.getAnnotation(TestSetupAnnotation.class);
				if (setupAnnotation != null) {
					testCase.setSetupMessage(setupAnnotation.description());
				}

				TestParametersAnnotation parametersAnnotation = method.getAnnotation(TestParametersAnnotation.class);
				if (parametersAnnotation != null) {
					StringBuffer parameters = new StringBuffer();
					
					Map<String, String> paramMap = new HashMap<>();
					for (int paramIndex = 0; paramIndex < parametersAnnotation.value().length; paramIndex++) {
						TestParameterAnnotation paramAnnotation = parametersAnnotation.value()[paramIndex];
						Parameter methodParam = targetMethod.getParameters()[paramIndex];
						//paramAnnotation.
					}
					testCase.setParameterValueMap(paramMap);
					
					testCase.setParameters(parameters.toString());
				}

				TestAssertAnnotation assertAnnotation = method.getAnnotation(TestAssertAnnotation.class);
				if (assertAnnotation != null) {
					StringBuffer expectedMessage = new StringBuffer();
					
					testCase.setExpectedMessage(expectedMessage.toString());
				}
				
				testCaseList.add(testCase);
			}
		}

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(xmlFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(testCaseList);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
