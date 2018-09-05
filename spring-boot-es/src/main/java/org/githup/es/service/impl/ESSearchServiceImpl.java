package org.githup.es.service.impl;

import java.util.List;

import org.githup.es.dao.ESAggsRepository;
import org.githup.es.model.EsBasicSearchParam;
import org.githup.es.page.BootstrapTablePaginationVo;
import org.githup.es.service.ESAggsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ES具体实现类
 * 
 * 备注：抽出ES的分类信息
 * 
 * @author sdc
 *
 */
@Service
public class ESSearchServiceImpl implements ESAggsSearchService{

	@Autowired
	private ESAggsRepository esAggsRepository;

	@Override
	public BootstrapTablePaginationVo<String> searchMsgByParam(EsBasicSearchParam param) throws Exception {
		return esAggsRepository.searchMsgByParam(param);
	}
	
	
}
