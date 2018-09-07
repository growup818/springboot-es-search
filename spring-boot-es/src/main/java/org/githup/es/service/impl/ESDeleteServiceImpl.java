package org.githup.es.service.impl;

import org.githup.es.dao.ESDeleteRepository;
import org.githup.es.param.DeleteParam;
import org.githup.es.service.EsDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ES分类：删除信息
 * 
 * 备注：抽出ES的分类信息
 * 
 * @author sdc
 *
 */
@Service
public class ESDeleteServiceImpl implements EsDeleteService{

	@Autowired
	private ESDeleteRepository esDeleteRepository;

	@Override
	public boolean delDataById(DeleteParam esDeleteParam) {
		return esDeleteRepository.delDataById(esDeleteParam);
	}

	
}
