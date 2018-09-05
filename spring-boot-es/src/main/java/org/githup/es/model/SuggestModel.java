package org.githup.es.model;

public class SuggestModel {

	/***
	 * id
	 */
	private Long id;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 数据
	 */
	private Integer data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getData() {
		return data;
	}

	public void setData(Integer data) {
		this.data = data;
	}

}
