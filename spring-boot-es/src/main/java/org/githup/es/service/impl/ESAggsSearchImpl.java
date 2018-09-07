package org.githup.es.service.impl;

import org.githup.es.dao.ESAggsRepository;
import org.githup.es.dao.ESRepository;
import org.githup.es.page.BootstrapTablePaginationVo;
import org.githup.es.param.BasicSearchParam;
import org.githup.es.service.ESAggsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ES分类，Agg统计信息
 * 
 * 备注：抽出ES的分类信息
 * 
 * @author sdc
 *
 */
@Service
public class ESAggsSearchImpl implements ESAggsSearchService {

	@Autowired
	private ESRepository eSRepository;
	
	@Autowired
	private ESAggsRepository eSAggsRepository;

	@Override
	public BootstrapTablePaginationVo<String> searchMsgByParam(BasicSearchParam param) throws Exception {
		return eSAggsRepository.searchMsgByParam(param);
	}

}
