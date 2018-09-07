package org.githup.es.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest.Item;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.githup.es.param.BasicSearchParam;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
public class ESRepository extends BaseRepository{

	private static final Logger LOG = LoggerFactory.getLogger(ESRepository.class);

	@Autowired
	private TransportClient client;
	
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
	 * 搜索
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<String> searchMsgByParam(BasicSearchParam param) throws Exception {
		String keyWord = param.getKeyWord();
		String filed = param.getField();
		String index = param.getIndex();
		
		Assert.assertNotNull(client);
		Assert.assertNotNull(filed);
		Assert.assertNotNull(index);
		Assert.assertNotNull(keyWord);
		
		//校验索引是否成功
		if (!isIndexExist(index)) {
			return null;
		}
		
		//响应信息
    	List<String> responseStrList = new ArrayList<String>();
    	//去重的信息
    	CollapseBuilder cb = new CollapseBuilder(param.getDistictField());
    	
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(filed, keyWord);
		//查询
		SearchResponse response = client.prepareSearch(index)
										.setQuery(matchQueryBuilder)
										.setCollapse(cb)
										.setFrom(param.getOffset())
										.setSize(param.getLimit())
										.get();
		
		SearchHits shList = response.getHits();
		for (SearchHit searchHit : shList) {  
        	responseStrList.add(searchHit.getSourceAsString());
        }  
		return responseStrList;
	}
	
	/**
	 * 搜索
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Long searchMsgCountByParam(BasicSearchParam param) throws Exception {
		String keyWord = param.getKeyWord();
		String filed = param.getField();
		String index = param.getIndex();
		
		Assert.assertNotNull(client);
		Assert.assertNotNull(filed);
		Assert.assertNotNull(index);
		Assert.assertNotNull(keyWord);
		
		//校验索引是否成功
		if (!isIndexExist(index)) {
			return null;
		}
		
    	//去重的信息
    	CollapseBuilder cb = new CollapseBuilder(param.getDistictField());
    	
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(filed, keyWord);
		SearchResponse response = client.prepareSearch(index)
								.setQuery(matchQueryBuilder)
								.setCollapse(cb)
								.get();
		
		SearchHits shList = response.getHits();
		return shList.totalHits;
	}

	/**
	 * 查询
	 * 
	 * @param keyWord
	 * @param index
	 * @param limit
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public void matchQuery(String keyWord, String index, int limit, int offset) throws Exception {
		TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery("content", keyWord);
		SearchResponse response = client.prepareSearch(index).setQuery(queryBuilder).setFrom(offset).setSize(limit).get();
		for (SearchHit searchHit : response.getHits()) {
			String sourceStr = searchHit.getSourceAsString();
			LOG.info("matchQuery-->>>" + sourceStr);
		}
	}

	/**
	 * 批量查询
	 * 
	 * 备注： 1、批量查询是再你知道下面的属性的时候，才去批量查询，如果都不知道Index，type就 直接查询，那个是ES搜索，不是批量查询
	 * 2、批量查询能提高程序查询效率，根据需求自我添加
	 * 
	 * Item 类结构里有属性，index<==>_index，type<==>_type,id<==>_id
	 * 
	 * 下面是es文档结构 { "_index": "bond2018-03-15", "_type": "bond", "_id":
	 * "AWIoxzdzUfSIA3djz-ZK", "_score": 1, "_source": { "code": "130523",
	 * "@timestamp": "2018-03-15T16:29:27.214Z", "name": "15福建09", "@version":
	 * "1", "id": 13293, "type": "bond", "tags": [ ], "timestamp":
	 * "2018-03-15T16:29:27.214Z" } }
	 * 
	 * @param itemList
	 * @return
	 */
	public Iterator<MultiGetItemResponse> multiGetData(List<Item> itemList) {
		if (!CollectionUtils.isEmpty(itemList)) {
			MultiGetRequestBuilder mgrb = client.prepareMultiGet();
			itemList.forEach(item -> {
				mgrb.add(item);
			});
			MultiGetResponse response = mgrb.get();
			// 查询
			Iterator<MultiGetItemResponse> itMultigetItem = response.iterator();
			return itMultigetItem;
		}
		return null;
	}

	/**
	 * 用户添加索引数据文档
	 * 
	 * @param index
	 *            对应的数据库
	 * @param type
	 *            类型 对应mysql的数据表
	 * @param obj
	 *            可以添加目标类
	 * @return
	 * @throws Exception
	 */
	public int addTargetObjectDataDoc(String index, String type, Object obj) throws Exception {
		// 构建参数和需要属性
		Assert.assertNotNull(client);
		Assert.assertNotNull(index);
		Assert.assertNotNull(type);
		XContentBuilder xb = XContentFactory.jsonBuilder().startObject();

		// 下面是反射处理传来的Object类，对应每个字段映射到对应的索引里，如果不需要这么做的，就可以注释掉下面的代码
		// 得到类对象
		Class<?> userCla = (Class<?>) obj.getClass();
		// 得到类中的所有属性集合
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {// 遍历obj文档的字段字段，添加到数据里
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			Object val = new Object();
			val = f.get(obj);
			// 得到此属性的值
			xb.field(f.getName(), val);
		}

		// 返回数据来源

		IndexResponse indexResponse = client.prepareIndex().setIndex(index).setType(type)
		// .setId(id) // 如果没有设置id，则ES会自动生成一个id
				.setSource(xb.endObject()).get();
		LOG.info("添加document，index:" + index + ",type:" + type + ",目标类obj:" + JSONObject.toJSONString(obj));
		return indexResponse.hashCode();
	}

	/**
	 * 查询数据
	 * 
	 * @param index
	 *            索引<----->关系型数据库
	 * @param type
	 *            类型<----->关系型数据表
	 * @param id
	 *            数据ID<----->id
	 * @return
	 */
	public Map<String, Object> searchDataByParam(String index, String type, String id) {
		if (index == null || type == null || id == null) {
			LOG.info(" 无法查询数据，缺唯一值!!!!!!! ");
			return null;
		}
		// 来获取查询数据信息
		GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);
		GetResponse getResponse = getRequestBuilder.execute().actionGet();
		// 这里也有指定的时间获取返回值的信息，如有特殊需求可以

		return getResponse.getSource();
	}

	/**
	 * 更新数据
	 *
	 * @param data
	 *            添加的数据类型 json格式的
	 * @param index
	 *            索引<----->关系型数据库
	 * @param type
	 *            类型<----->关系型数据表
	 * @param id
	 *            数据ID<----->id
	 * @return
	 */
	public void updateDataById(JSONObject data, String index, String type, String id) {
		if (index == null || type == null || id == null) {
			LOG.info(" 无法更新数据，缺唯一值!!!!!!! ");
			return;
		}

		// 更新步骤
		UpdateRequest up = new UpdateRequest();
		up.index(index).type(type).id(id).doc(data);

		// 获取响应信息
		// .actionGet(timeoutMillis)，也可以用这个方法，当过了一定的时间还没得到返回值的时候，就自动返回。
		UpdateResponse response = client.update(up).actionGet();
		LOG.info("更新数据状态信息，status{}", response.status().getStatus());
	}

	/**
	 * 添加数据
	 *
	 * @param data
	 *            添加的数据类型 json格式的
	 * @param index
	 *            索引<----->关系型数据库
	 * @param type
	 *            类型<----->关系型数据表
	 * @param id
	 *            数据ID<----->id
	 * @return
	 */
	public String addTargetDataALL(JSONObject data, String index, String type, String id) {
		// 判断一下次id是否为空，为空的话就设置一个id
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		// 正式添加数据进去
		IndexResponse response = client.prepareIndex(index, type, id).setSource(data).get();

		LOG.info("addTargetDataALL 添加数据的状态:{}", response.status().getStatus());

		return response.getId();
	}
	
	/**
	 * JSON字符串加入到es里
	 * @param index
	 * @param type
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public String addJSONDataDoc(String index, String type, Object obj) throws Exception{  
    	//构建参数和需要属性
    	Assert.assertNotNull(client);
    	Assert.assertNotNull(index);
    	Assert.assertNotNull(type);

        client.prepareIndex().setIndex(index).setType(type).setSource();
        
        //返回数据来源
        IndexResponse indexResponse = client.prepareIndex().setIndex(index).setType(type)
                .setSource(JSONObject.toJSONString(obj), XContentType.JSON).get();
        LOG.debug("添加document，index:" + index + ",type:" + type + ",目标类obj:" + JSONObject.toJSONString(obj));
        return indexResponse.getId();
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

	/**
	 * 根据关键词查询
	 * 
	 * @param keyWord
	 *            搜索词
	 * @param index
	 *            索引
	 * @param limit
	 *            分页参数
	 * @param offset
	 *            分页参数
	 * @return
	 * @throws Exception
	 */
	public List<String> searchMessageByKeyWord(String index, String keyWord, int limit, int offset) throws Exception {
		List<String> responseStrList = new ArrayList<String>();
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("content", keyWord);
		SearchResponse response = client.prepareSearch(index).setQuery(matchQueryBuilder).setFrom(offset).setSize(limit).get();
		for (SearchHit searchHit : response.getHits()) {
			responseStrList.add(searchHit.getSourceAsString());
		}
		return responseStrList;
	}
	
	 /**
	 * @param index
	 * @param filed
	 * @param keyWord
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<String> search_IdByKeyWord(String index, String filed, String keyWord, int limit, int offset) {  
		LOG.debug("es serarch index->" + index + ",filed->" + filed + ",keyWord->" + keyWord);
    	Assert.assertNotNull(client);
    	Assert.assertNotNull(index);
    	Assert.assertNotNull(keyWord);
    	List<String> responseStrList = new ArrayList<String>();
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(filed, keyWord);
		
		SearchResponse response = client.prepareSearch(index).setQuery(matchQueryBuilder).setFrom(offset).setSize(limit).get();
		
        for (SearchHit searchHit : response.getHits()) {  
        	responseStrList.add(searchHit.getId());
        }  
        return responseStrList;
    }
	
	/**
     * 根据关键词查询,使用的查询市term_query
	 * @param index
	 * @param filed
	 * @param keyWord
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<String> searchMessageTermQueryByKeyWord(String index, String filed, String keyWord, int limit,
			int offset) {  
		LOG.info("es serarch index->" + index + ",filed->" + filed + ",keyWord->" + keyWord);
    	Assert.assertNotNull(client);
    	Assert.assertNotNull(index);
    	Assert.assertNotNull(keyWord);
    	List<String> responseStrList = new ArrayList<String>();
    	TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(filed, keyWord);
		
    	//查询信息
		SearchResponse response = client.prepareSearch(index).setQuery(termsQueryBuilder).setFrom(offset).setSize(limit).get();
        for (SearchHit searchHit : response.getHits()) {  
        	responseStrList.add(searchHit.getSourceAsString());
        }  
        return responseStrList;
    }
	
	/**
     * 根据关键词查询,使用的查询是match_phrase
	 * @param index
	 * @param filed
	 * @param keyWord
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<String> searchMessageMatchPhraseQueryByKeyWord(String index, String filed, String keyWord, int limit,
			int offset) {  
		LOG.info("es serarch index->" + index + ",filed->" + filed + ",keyWord->" + keyWord);
    	Assert.assertNotNull(client);
    	Assert.assertNotNull(index);
    	Assert.assertNotNull(keyWord);
    	List<String> responseStrList = new ArrayList<String>();
    	MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(filed, keyWord);
		
		SearchResponse response = client.prepareSearch(index).setQuery(matchPhraseQueryBuilder).setFrom(offset).setSize(limit).get();
        for (SearchHit searchHit : response.getHits()) {  
        	responseStrList.add(searchHit.getSourceAsString());
        }  
        return responseStrList;
    }
    
    /**
     * 根据关键词查询  分页查询
     * @param filedMap 搜索关键词Map key 是要搜索的字段 value是关键词
     * @param index 索引，库
     * @param limit
     * @param offset
     * @param filed 字段
     * @return
     * @throws Exception
     */
    public List<String> searchMessageByMapKeyWord(String index, Map<String, String> filedMap, int limit, int offset) throws Exception{  
    	LOG.info("es serarch index->" + index + ",filedMap->" + JSONObject.toJSONString(filedMap));
    	Assert.assertNotNull(client);
    	Assert.assertNotNull(index);
    	List<String> responseStrList = new ArrayList<String>();
		
    	QueryBuilder finalQueryBuilder = null;
    	if(!CollectionUtils.isEmpty(filedMap)) {
    		for(Map.Entry<String, String> entry : filedMap.entrySet()) {
    			String key = entry.getKey(); //key 是要搜索的字段
    			String value = entry.getValue();//value是关键词
    			
    			TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery(key, value);
    			finalQueryBuilder = QueryBuilders.boolQuery().must(termQueryBuilder1);
    		}
    	}
    	//query
		SearchResponse response = client.prepareSearch(index).setQuery(finalQueryBuilder).setFrom(offset).setSize(limit).get();  
        for (SearchHit searchHit : response.getHits()) {  
        	responseStrList.add(searchHit.getSourceAsString());
        }  
        return responseStrList;
    } 
    
    /**
     * 根据关键词查询  获取总数
     * @param filedMap 搜索关键词Map key 是要搜索的字段 value是关键词
     * @param index 索引，库
     * @param limit
     * @param offset
     * @param filed 字段
     * @return
     * @throws Exception
     */
    public long searchMessageByMapKeyWordCount(String index, Map<String, String> filedMap) throws Exception{  
    	LOG.info("es serarch index->" + index + ",filedMap->" + filedMap);
    	Assert.assertNotNull(client);
    	Assert.assertNotNull(index);
    	QueryBuilder finalQueryBuilder = null;
    	if(!CollectionUtils.isEmpty(filedMap)) {
    		for(Map.Entry<String, String> entry : filedMap.entrySet()) {
    			String key = entry.getKey(); //key 是要搜索的字段
    			String value = entry.getValue();//value是关键词
    			
    			TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery(key, value);
    			finalQueryBuilder = QueryBuilders.boolQuery().must(termQueryBuilder1);
    		}
    	}
    	long count = client.prepareSearch(index).setQuery(finalQueryBuilder).get().getHits().totalHits;
    	return count;
    } 

	public List<String> searchMessageByKeyWord(String index, String filed, String keyWord, int limit, int offset) throws Exception {
		List<String> responseStrList = new ArrayList<String>();
		TermQueryBuilder matchQueryBuilder = QueryBuilders.termQuery(filed, keyWord);
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("userId", "251");
		QueryBuilder finalQueryBuilder = QueryBuilders.boolQuery().must(matchQueryBuilder).must(termQueryBuilder);

		SearchResponse response = client.prepareSearch(index).setQuery(finalQueryBuilder).setFrom(offset).setSize(limit).get();
		
		for (SearchHit searchHit : response.getHits()) {
			responseStrList.add(searchHit.getSourceAsString());
		}
		return responseStrList;
	}
	
}
