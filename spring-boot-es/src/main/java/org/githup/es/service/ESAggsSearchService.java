package org.githup.es.service;

import java.util.List;

import org.githup.es.model.EsBasicSearchParam;
import org.githup.es.page.BootstrapTablePaginationVo;

/**
 * ES服务端
 * 
 * @author sdc
 *
 */
public interface ESAggsSearchService {
	
	/**
	 * 搜索
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public BootstrapTablePaginationVo<String> searchMsgByParam(EsBasicSearchParam param) throws Exception;
    
}
