package jp.co.kizuna_plus.unittestdesigner.demo;

import java.util.ArrayList;
import java.util.List;

import jp.co.kizuna_plus.unittestdesigner.test.annotation.TestParameterTypeEnum;

/**
 * テスト対象クラス
 *
 */
public class DemoTestTarget {

	private List<String> list;
	
	private int count;
	
	public DemoTestTarget() {
		list = new ArrayList<String>();
		count = 0;
	}
	
	public void add(String value) {
		this.list.add(value);
	}
	
	public String get(int i) {
		if (this.list.size() <= i || i < 0) {
			return null;
		}
		this.count++;
		
		return this.list.get(i);
	}
	
	public int getCount() {
		return this.count;
	}
	
	public TestParameterTypeEnum getEnum(TestParameterTypeEnum argEnum) {
		return argEnum;
	}
	
	/**
	 * Nullまたは、空欄かチェックします。
	 * 
	 * @param array 配列
	 * @return true NULLか空欄, false それ以外
	 */
	public boolean isNullOrEmpty(String[] array) {
		return array == null || array.length <= 0;
	}
}
