package org.githup.es.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest.Item;
import org.githup.es.param.BasicSearchParam;

import com.alibaba.fastjson.JSONObject;

/**
 * ES服务端
 * 
 * @author sdc
 *
 */
public interface ESSearchService {
	
	/**
	 * 构建索引
	 * @param index
	 * @return
	 */
	public boolean buildIndex(String index);
	
    /**
     * 增加内容
     * @param postId
     * @param postContent
     * @return
     * @throws Exception
     */
    public int addPostDataDoc(String postId, String postContent) throws Exception;
    
    /**
     * 加入json data进入到es里
     * @param index
     * @param type
     * @param obj
     * @return
     * @throws Exception
     */
    public String addJSONDataDoc(String index, String type, Object obj) throws Exception;
    
    /**
     * 查询
     * @param keyWord
     * @param index
     * @param limit
     * @param offset
     * @throws Exception
     */
    public void matchQuery(String keyWord, String index, int limit, int offset) throws Exception;
    
    /**
     * 查询数据
     * @param index 索引<----->关系型数据库
     * @param type  类型<----->关系型数据表
     * @param id    数据ID<----->id
     * @return
     */
    public Map<String, Object> searchDataByParam(String index, String type, String id);
    
    /**
     * 添加数据
     *
     * @param data  添加的数据类型 json格式的
     * @param index 索引<----->关系型数据库
     * @param type  类型<----->关系型数据表
     * @param id    数据ID<----->id
     * @return
     */
    public String addTargetDataALL(JSONObject data, String index, String type, String id);
    
    /**
	 * 判断索引是否存在
	 *
	 * @param index
	 * @return
	 */
	public boolean isIndexExist(String index);
	
	/**
	 * @return
	 */
    public Iterator<MultiGetItemResponse> multiGetData(List<Item> itemList);
    
    /**
     * 关键词搜索
     * @param index
     * @param filed
     * @param keyWord
     * @param limit
     * @param offset
     * @return
     * @throws Exception
     */
    public List<String> searchMessageByKeyWord(String index, String filed, String keyWord, int limit, int offset) throws Exception;
	
    /**
     * 查询参数
     * @param param
     * @return
     * @throws Exception
     */
    public List<String> searchMsgByParam(BasicSearchParam param) throws Exception;
    
    /**
     * 查询总数
     * @param param
     * @return
     * @throws Exception
     */
    public Long searchMsgCountByParam(BasicSearchParam param) throws Exception;
    
}
