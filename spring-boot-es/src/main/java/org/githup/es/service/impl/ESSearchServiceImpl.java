package org.githup.es.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest.Item;
import org.githup.es.dao.ESRepository;
import org.githup.es.dao.ESSuggestRepository;
import org.githup.es.param.BasicSearchParam;
import org.githup.es.service.ESSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * ES具体实现类
 * 
 * 备注：抽出ES的分类信息
 * 
 * @author sdc
 *
 */
@Service
public class ESSearchServiceImpl implements ESSearchService{

	@Autowired
	private ESRepository eSRepository;
	
	@Autowired
	private ESSuggestRepository eSSuggestRepository;

	@Override
	public boolean buildIndex(String index) {
		return eSRepository.buildIndex(index);
	}

	@Override
	public int addPostDataDoc(String postId, String postContent) throws Exception {
		return eSRepository.addPostDataDoc(postId, postContent);
	}

	@Override
	public String addJSONDataDoc(String index, String type, Object obj) throws Exception {
		return eSRepository.addJSONDataDoc(index, type, obj);
	}

	@Override
	public void matchQuery(String keyWord, String index, int limit, int offset) throws Exception {
		eSRepository.matchQuery(keyWord, index, limit, offset);
	}

	@Override
	public Map<String, Object> searchDataByParam(String index, String type, String id) {
		return eSRepository.searchDataByParam(index, type, id);
	}

	@Override
	public String addTargetDataALL(JSONObject data, String index, String type, String id) {
		return eSRepository.addTargetDataALL(data, index, type, id);
	}

	@Override
	public boolean isIndexExist(String index) {
		return eSRepository.isIndexExist(index);
	}

	@Override
	public Iterator<MultiGetItemResponse> multiGetData(List<Item> itemList) {
		return eSRepository.multiGetData(itemList);
	}

	@Override
	public List<String> searchMessageByKeyWord(String index, String filed, String keyWord, int limit, int offset) throws Exception {
		return eSRepository.searchMessageByKeyWord(index, keyWord, limit, offset);
	}

	@Override
	public List<String> searchMsgByParam(BasicSearchParam param) throws Exception {
		return eSRepository.searchMsgByParam(param);
	}

	@Override
	public Long searchMsgCountByParam(BasicSearchParam param) throws Exception {
		return eSRepository.searchMsgCountByParam(param);
	}

	
}
