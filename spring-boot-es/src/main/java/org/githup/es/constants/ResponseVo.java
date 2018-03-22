package org.githup.es.constants;

import java.io.Serializable;

/**
 * 响应信息
 * @author sdc
 *
 * @param <T>
 */
public class ResponseVo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 状态码对应的信息
	 */
	private String message;

	/**
	 * 数据对象
	 */
	private T data;

	public ResponseVo(String code, String message, T data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
