package org.githup.es.dao;

import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * ES的操作数据类
 * 
 * 备注：对es的一些操作做了一些封装，抽出来一些操作，就是传统的dao层，数据服务
 * 
 * @author sdc
 *
 */
@Component
public class ESRepository {

	private static final Logger log = LoggerFactory.getLogger(ESRepository.class);

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
			log.info("Index is not exits!");
		}
		CreateIndexResponse buildIndexresponse = client.admin().indices().prepareCreate(index).execute().actionGet();
		log.info(" 创建索引的标志: " + buildIndexresponse.isAcknowledged());

		return buildIndexresponse.isAcknowledged();
	}
	
	 /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public boolean deleteIndex(String index) {
        if (!isIndexExist(index)) {
            log.info(" 索引不存在 ！！！！！!");
        }
        DeleteIndexResponse diResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
        if (diResponse.isAcknowledged()) {
        	log.info("删除索引**成功** index->>>>>>>" + index);
        } else {
        	log.info("删除索引**失败** index->>>>> " + index);
        }
        return diResponse.isAcknowledged();
    }
    
    /**
     * 查询数据
     * @param index 索引<----->关系型数据库
     * @param type  类型<----->关系型数据表
     * @param id    数据ID<----->id
     * @return
     */
    public Map<String, Object> searchDataByParam(String index, String type, String id) {
    	if(index == null || type == null || id == null) {
    		log.info(" 无法查询数据，缺唯一值!!!!!!! ");
    		return null;
    	}
    	//来获取查询数据信息
        GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);
        GetResponse getResponse = getRequestBuilder.execute().actionGet(); 
        //这里也有指定的时间获取返回值的信息，如有特殊需求可以
        
        return getResponse.getSource();
    }
    
    /**
     * 更新数据
     *
     * @param data  添加的数据类型 json格式的
     * @param index 索引<----->关系型数据库
     * @param type  类型<----->关系型数据表
     * @param id    数据ID<----->id
     * @return
     */
    public void updateDataById(JSONObject data, String index, String type, String id) {
    	if(index == null || type == null || id == null) {
    		log.info(" 无法更新数据，缺唯一值!!!!!!! ");
    		return;
    	}
    	
    	//更新步骤
        UpdateRequest up = new UpdateRequest();
        up.index(index).type(type).id(id).doc(data);

        //获取响应信息
        //.actionGet(timeoutMillis)，也可以用这个方法，当过了一定的时间还没得到返回值的时候，就自动返回。
        UpdateResponse response = client.update(up).actionGet();
        log.info("更新数据状态信息，status{}", response.status().getStatus());
    }
    
    /**
     * 添加数据
     *
     * @param data  添加的数据类型 json格式的
     * @param index 索引<----->关系型数据库
     * @param type  类型<----->关系型数据表
     * @param id    数据ID<----->id
     * @return
     */
    public String addTargetDataALL(JSONObject data, String index, String type, String id) {
    	//判断一下次id是否为空，为空的话就设置一个id
    	if(id == null) {
    		id = UUID.randomUUID().toString();
    	}
    	//正式添加数据进去
        IndexResponse response = client.prepareIndex(index, type, id).setSource(data).get();
        
        log.info("addTargetDataALL 添加数据的状态:{}", response.status().getStatus());
        
        return response.getId();
    }
    
    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     */
    public void delDataById(String index, String type, String id) {

    	if(index == null || type == null || id == null) {
    		log.info(" 无法删除数据，缺唯一值!!!!!!! ");
    		return;
    	}
    	//开始删除数据
        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();

        log.info("删除数据状态，status-->>>>{},", response.status().getStatus());
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
			log.info("此索引 [" + index + "] 已经在ES集群里存在");
		} else {
			log.info(" 没有此索引 [" + index + "] ");
		}
		return iep.isExists();
	}

}
