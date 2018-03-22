package org.githup.es.service.impl;

import java.util.Map;

import org.githup.es.dao.ESRepository;
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
		// TODO Auto-generated method stub
		return eSRepository.searchDataByParam(index, type, id);
	}

	@Override
	public void updateDataById(JSONObject data, String index, String type, String id) {
		// TODO Auto-generated method stub
		eSRepository.updateDataById(data, index, type, id);
	}

	@Override
	public String addTargetDataALL(JSONObject data, String index, String type, String id) {
		// TODO Auto-generated method stub
		return eSRepository.addTargetDataALL(data, index, type, id);
	}

	@Override
	public void delDataById(String index, String type, String id) {
		// TODO Auto-generated method stub
		eSRepository.delDataById(index, type, id);
	}

	@Override
	public boolean isIndexExist(String index) {
		// TODO Auto-generated method stub
		return eSRepository.isIndexExist(index);
	}

}
