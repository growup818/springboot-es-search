package org.githup.es.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest.Item;
import org.githup.es.dao.ESRepository;
import org.githup.es.dao.ESSuggestRepository;
import org.githup.es.model.SuggestModel;
import org.githup.es.service.ESSearchService;
import org.githup.es.service.ESSuggestSearchService;
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
public class ESSuggestSearchServiceImpl implements ESSuggestSearchService{

	@Autowired
	private ESRepository eSRepository;
	
	@Autowired
	private ESSuggestRepository eSSuggestRepository;

	@Override
	public String addDataDocForSuggest(String index, String type, SuggestModel suggestModel) throws Exception {
		return eSSuggestRepository.addTargetObjectDataDoc(index, type, suggestModel);
	}

	@Override
	public boolean buildIndexByParam(String index) throws Exception {
		return eSSuggestRepository.buildIndexByParam(index);
	}
	
}
