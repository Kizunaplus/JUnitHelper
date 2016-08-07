package jp.co.kizuna_plus.unittestdesigner.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * テスト結果検証データ設定アノテーション
 * 
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestSetupAnnotation {

	/**
	 * setupメソッド名
	 */
	String value() default "";
	
	/**
	 * setupの概要を保持
	 */
	String description() default "";
}
