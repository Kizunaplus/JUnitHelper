package jp.co.kizuna_plus.unittestdesigner.test.execute;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

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
			fis = new FileInputStream(new File("C:\\tmp\\myObj.obj"));
			ois = new ObjectInputStream(fis);
			settings = (TestCaseSettings)ois.readObject();
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
		
		for (String packageName : settings.getPackages()) {
		    
		    String packageResource 
		        = packageName.replace(PACKAGE_SEP, PACKAGE_RESOURCE_SEP); 
		    
		    
		    ClassLoader loader = ClassLoader.getSystemClassLoader();
		    
		    URL resource = loader.getResource(packageResource);
		    
		    File f = new File(resource.getPath());
		    
		    Class<?> foundClass = null;
		    for (String file : f.list()) {
		        if (file.matches(".*\\.class$")) {
		            String className = packageName + PACKAGE_SEP + file.replaceAll("\\.class$","");
		            
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
		}

	}

}
