package org.githup.es.service;

import java.util.List;

import org.githup.es.page.BootstrapTablePaginationVo;
import org.githup.es.param.BasicSearchParam;

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
	public BootstrapTablePaginationVo<String> searchMsgByParam(BasicSearchParam param) throws Exception;
    
}
