package jp.co.kizuna_plus.unittestdesigner.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.co.kizuna_plus.unittestdesigner.test.execute.TestExecuter;

/**
 * テスト結果検証データ設定アノテーション
 * 
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAssertAnnotation {

	/**
	 * 期待値<br>
	 * 値タイプにより指定値の値が決定します。
	 */
	String value() default "";
	
	/**
	 * 
	 * 期待値のクラス<br>
	 * 値タイプによりクラスの解釈が異なります。
	 */
	Class<?> valueClass() default String.class;
	
	
	/**
	 * 期待値タイプ<br>
	 * パラメータ値がどのような値かを指定します。
	 */
	TestParameterTypeEnum type() default TestParameterTypeEnum.NULL;
	
	/**
	 * リストデリミター<br>
	 * 値がリストの場合は、文字列の区切り文字をしています。
	 */
	String listDelimiter() default "";

	
	/**
	 * アサーションを行うモード<br>
	 * TestExecuterクラスに定義されているアサートメソッド名を設定します。
	 */
	Class<?> assertDefClass() default TestExecuter.class;
	
	/**
	 * アサーションを行うモード<br>
	 * TestExecuterクラスに定義されているアサートメソッド名を設定します。
	 */
	String assertMode() default "assertInstance";
}
