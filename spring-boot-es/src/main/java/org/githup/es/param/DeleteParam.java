package org.githup.es.param;

import java.io.Serializable;

/***
 * delete查询参数化
 * 
 * @author shangdc
 *
 */
public class DeleteParam implements Serializable {

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
	 * es索引唯一标识id
	 */
	private String id;

	/**
	 * type类型
	 */
	private String type;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
