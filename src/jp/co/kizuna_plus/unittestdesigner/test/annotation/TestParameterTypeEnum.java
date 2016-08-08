package jp.co.kizuna_plus.unittestdesigner.test.annotation;

/**
 * パラメータ値の種類Enum
 *
 */
public enum TestParameterTypeEnum {
	/**
	 * 直値
	 */
	VALUE,
	
	/**
	 * ENUM値
	 */
	ENUM,
	
	/**
	 * Staticメンバー変数
	 */
	STATIC_MEMBER,
	
	/**
	 * 引数なしStaticメソッド
	 */
	NON_PARAM_STATIC_METHOD,
	
	/**
	 * デフォルトコンストラクタ
	 */
	DEFAULT_CONSTRUCTOR,
	
	/**
	 * null値
	 */
	NULL,
	
	/**
	 * class値
	 */
	TYPE,
}
