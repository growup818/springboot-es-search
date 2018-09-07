package org.githup.es.service;

import org.githup.es.param.DeleteParam;

/**
 * ES服务端
 * 
 * @author sdc
 *
 */
public interface EsDeleteService {
	
	/**
	 * 删除数据
	 * @param esDeleteParam
	 * @return
	 */
	public boolean delDataById(DeleteParam esDeleteParam);
	
}
