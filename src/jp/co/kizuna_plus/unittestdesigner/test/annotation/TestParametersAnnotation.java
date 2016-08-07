package jp.co.kizuna_plus.unittestdesigner.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * テスト実施メソッドに渡す引数定義アノテーション
 * TestParameterAnnotationを複数定義が可能にするためのアノテーション
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestParametersAnnotation {

	/**
	 * パラメータ値
	 */
	TestParameterAnnotation[] value() default {};

}