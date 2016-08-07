package jp.co.kizuna_plus.unittestdesigner.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * テスト対象設定アノテーション
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestTargetAnnotation {
	
	/**
	 * テスト対象クラス
	 */
	Class<?> targetClass();
	
	/**
	 * テスト対象メソッド
	 */
	String targetMethod() default "";
	
	/**
	 * テスト対象メソッドのパラメータ形
	 */
	Class<?>[] methodParameters() default {};

}
