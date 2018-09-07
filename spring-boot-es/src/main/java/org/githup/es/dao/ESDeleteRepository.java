package org.githup.es.dao;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.rest.RestStatus;
import org.githup.es.param.DeleteParam;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ES的操作数据类
 * 
 * 备注：对es的一些操作做了一些封装，抽出来一些操作，就是传统的dao层，数据服务
 * 
 * @author sdc
 *
 */
@Component
public class ESDeleteRepository extends BaseRepository{

	private static final Logger LOG = LoggerFactory.getLogger(ESDeleteRepository.class);

	@Autowired
	private TransportClient client;
	
	/**
	 * 通过ID删除数据
	 *
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @param id
	 *            数据ID
	 */
	public boolean delDataById(DeleteParam esDeleteParam) {
		String index = esDeleteParam.getIndex();
		String id = esDeleteParam.getId();
		String filed = esDeleteParam.getField();
		String keyWord = esDeleteParam.getKeyWord();
		String type = esDeleteParam.getType();
		Assert.assertNotNull(client);
    	Assert.assertNotNull(index);
    	Assert.assertNotNull(keyWord);
    	Assert.assertNotNull(filed);
    	Assert.assertNotNull(id);
    	Assert.assertNotNull(type);
    	
		// 开始删除数据
		DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();
		RestStatus restStatus = response.status();
		LOG.info("删除数据状态，status-->>>>{},", response.status().getStatus());
		
		if(restStatus.getStatus() == RestStatus.OK.getStatus()) {
			return true;
		}
		return false;
	}
	
	
	
}
