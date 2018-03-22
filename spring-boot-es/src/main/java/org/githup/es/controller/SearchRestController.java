package org.githup.es.controller;

import java.util.Map;

import org.githup.es.constants.ESWebStatusEnum;
import org.githup.es.constants.ResponseVo;
import org.githup.es.service.ESSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索服务
 * 
 * 备注：具体的服务在ESSearchService这个service里，请关注这个service
 * 
 * @author sdc
 *
 */
@RestController
@RequestMapping("/search")
public class SearchRestController extends BaseController{

	@Autowired
	private ESSearchService esSearchService;

	/**
	 * 构建索引
	 * @param index
	 * @return
	 */
	@RequestMapping(value = "/buildIndex")
	@ResponseBody
	public ResponseVo<?> buildIndex(
			@RequestParam(value = "index", required = false) String index
			) {
		//判空
		if(index == null) {
			return generateResponseVo(ESWebStatusEnum.FAILED, null);
		}
		esSearchService.buildIndex(index);
		return generateResponseVo(ESWebStatusEnum.SUCCESS, null);
	}
	
	/**
	 *  查询数据
	 *  
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/data")
	@ResponseBody
	public ResponseVo<?> search(
			@RequestParam(value = "index", required = false) String index,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "id", required = false) String id
			) {
		//判空
		if(index == null || type == null || id == null) {
			return generateResponseVo(ESWebStatusEnum.FAILED, null);
		}
		//搜索具体的数据来源
		Map<String, Object> returnMap = esSearchService.searchDataByParam("bond2018-03-15", "bond", "AWIoxzdzUfSIA3djz-ZK");
		return generateResponseVo(ESWebStatusEnum.SUCCESS, returnMap);
	}
	
	
}
