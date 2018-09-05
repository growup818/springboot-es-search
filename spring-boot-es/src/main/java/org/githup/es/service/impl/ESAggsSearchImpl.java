package org.githup.es.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest.Item;
import org.githup.es.dao.ESRepository;
import org.githup.es.model.EsBasicSearchParam;
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
public class ESAggsSearchImpl implements ESSearchService{

	@Autowired
	private ESRepository eSRepository;
	
	@Override
	public String addJSONDataDoc(String index, String type, Object obj) throws Exception {
		// TODO Auto-generated method stub
		return eSRepository.addJSONDataDoc(index, type, obj);
	}

	@Override
	public int addPostDataDoc(String postId, String postContent) throws Exception {
		// TODO Auto-generated method stub
		return eSRepository.addPostDataDoc(postId, postContent);
	}

	@Override
	public void matchQuery(String keyWord, String index, int limit, int offset) throws Exception {
		// TODO Auto-generated method stub
		eSRepository.matchQuery(keyWord, index, limit, offset);
	}

	@Override
	public boolean buildIndex(String index) {
		return eSRepository.buildIndex(index);
	}

	@Override
	public boolean delIndex(String index) {
		return eSRepository.deleteIndex(index);
	}

	@Override
	public Map<String, Object> searchDataByParam(String index, String type, String id) {
		return eSRepository.searchDataByParam(index, type, id);
	}

	@Override
	public void updateDataById(JSONObject data, String index, String type, String id) {
		eSRepository.updateDataById(data, index, type, id);
	}

	@Override
	public String addTargetDataALL(JSONObject data, String index, String type, String id) {
		return eSRepository.addTargetDataALL(data, index, type, id);
	}

	@Override
	public void delDataById(String index, String type, String id) {
		eSRepository.delDataById(index, type, id);
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
		return eSRepository.searchMessageByKeyWord(index,filed, keyWord, limit, offset);
	}

	@Override
	public List<String> searchMsgByParam(EsBasicSearchParam param) throws Exception {
		return eSRepository.searchMsgByParam(param);
	}

	@Override
	public Long searchMsgCountByParam(EsBasicSearchParam param) throws Exception {
		return eSRepository.searchMsgCountByParam(param);
	}
	
}
