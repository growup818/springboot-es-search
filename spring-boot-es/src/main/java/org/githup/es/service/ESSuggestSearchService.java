package org.githup.es.service;

import org.githup.es.model.SuggestModel;

/**
 * ESSuggest
 * 
 * @author sdc
 *
 */
public interface ESSuggestSearchService {

	/**
	 * 添加 建议服务端
	 * @param index
	 * @param type
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public String addDataDocForSuggest(String index, String type, SuggestModel suggestModel) throws Exception;
	
	/**
	 * 构建索引，根据一些参数，比如分词器
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public boolean buildIndexByParam(String index) throws Exception;

}
