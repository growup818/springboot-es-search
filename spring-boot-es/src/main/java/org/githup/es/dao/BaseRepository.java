package org.githup.es.dao;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
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
public class BaseRepository {

	private static final Logger LOG = LoggerFactory.getLogger(BaseRepository.class);

	@Autowired
	private TransportClient client;
	
	/**
	 * 创建索引
	 *
	 * @param index
	 * @return
	 */
	public boolean buildIndex(String index) {
		if (!isIndexExist(index)) {
			LOG.info("Index is not exits!");
		}
		CreateIndexResponse buildIndexresponse = client.admin().indices().prepareCreate(index).execute().actionGet();
		LOG.info(" 创建索引的标志: " + buildIndexresponse.isAcknowledged());

		return buildIndexresponse.isAcknowledged();
	}

	/**
	 * 增加文档，测试用的- 增加文档
	 * 
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public int addPostDataDoc(String postId, String postContent) throws Exception {
		IndexResponse response = client.prepareIndex("forum_index", "post").setSource(XContentFactory.jsonBuilder().startObject().field("id", postId).field("content", postContent).endObject()).get();
		return response.hashCode();
	}

	/**
	 * 删除索引
	 *
	 * @param index
	 * @return
	 */
	public boolean deleteIndex(String index) {
		if (!isIndexExist(index)) {
			LOG.info(" 索引不存在 ！！！！！!");
		}
		DeleteIndexResponse diResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
		if (diResponse.isAcknowledged()) {
			LOG.info("删除索引**成功** index->>>>>>>" + index);
		} else {
			LOG.info("删除索引**失败** index->>>>> " + index);
		}
		return diResponse.isAcknowledged();
	}


	/**
	 * 判断索引是否存在
	 *
	 * @param index
	 * @return
	 */
	public boolean isIndexExist(String index) {
		IndicesExistsResponse iep = client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
		if (iep.isExists()) {
			LOG.info("此索引 [" + index + "] 已经在ES集群里存在");
		} else {
			LOG.info(" 没有此索引 [" + index + "] ");
		}
		return iep.isExists();
	}

}
