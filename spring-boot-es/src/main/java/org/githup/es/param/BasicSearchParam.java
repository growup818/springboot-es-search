package org.githup.es.param;

import java.io.Serializable;

/***
 * es搜索参数格式化
 * 
 * @author shangdc
 *
 */
public class BasicSearchParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 关键词
	 */
	private String keyWord;

	/**
	 * 集中在那个字段上
	 */
	private String field;

	/**
	 * 索引
	 */
	private String index;

	/**
	 * 去重字段
	 */
	private String distictField;

	/**
	 * 分页参数
	 */
	private Integer limit = 10;

	/**
	 * 分页参数
	 */
	private Integer offset = 0;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getDistictField() {
		return distictField;
	}

	public void setDistictField(String distictField) {
		this.distictField = distictField;
	}

}
