package jp.co.kizuna_plus.unittestdesigner.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * テスト実施メソッドに渡す引数定義アノテーション
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestParameterAnnotation {

	/**
	 * パラメータ値
	 */
	String value() default "";
	
	/**
	 * 
	 * パラメータのクラス
	 */
	Class<?> parameterClass() default String.class;

	/**
	 * リストデリミター<br>
	 * 値がリストの場合は、文字列の区切り文字をしています。
	 */
	String listDelimiter() default "";
	
	/**
	 * 値タイプ
	 */
	TestParameterTypeEnum type() default TestParameterTypeEnum.NULL;

}