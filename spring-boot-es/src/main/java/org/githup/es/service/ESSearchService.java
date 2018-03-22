package org.githup.es.service;

import java.util.Map;

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
	 * 删除索引
	 * @param index
	 * @return
	 */
    public boolean delIndex(String index);
    
    /**
     * 查询数据
     * @param index 索引<----->关系型数据库
     * @param type  类型<----->关系型数据表
     * @param id    数据ID<----->id
     * @return
     */
    public Map<String, Object> searchDataByParam(String index, String type, String id);
    
    /**
     * 更新数据
     *
     * @param data  添加的数据类型 json格式的
     * @param index 索引<----->关系型数据库
     * @param type  类型<----->关系型数据表
     * @param id    数据ID<----->id
     * @return
     */
    public void updateDataById(JSONObject data, String index, String type, String id);
    
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
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     */
    public void delDataById(String index, String type, String id);
    
    /**
	 * 判断索引是否存在
	 *
	 * @param index
	 * @return
	 */
	public boolean isIndexExist(String index);
	
	
}
