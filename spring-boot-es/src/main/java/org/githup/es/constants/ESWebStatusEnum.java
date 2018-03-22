package org.githup.es.constants;

/**
 * 常量类，枚举类
 * 
 * @author sdc
 *
 */
public enum ESWebStatusEnum {

	/**
	 * 定义接口返回状态码
	 */
	SUCCESS("5000", "成功"), 
	FAILED("7000", "失败");

	/**
	 * 系统码
	 */
	private String code;

	/**
	 * 描述
	 */
	private String desc;

	ESWebStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static ESWebStatusEnum getWebStatusEnumByKey(String key) {
		for (ESWebStatusEnum bt : values()) {
			if (bt.getCode().equals(key))
				return bt;
		}
		return null;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
