package org.githup.es.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation.SingleValue;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.githup.es.page.BootstrapTablePaginationVo;
import org.githup.es.param.BasicSearchParam;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ES 的统计 聚合 dao层
 * 
 * ElasticSearch Aggregations
 * 
 * @author sdc
 *
 */
@Component
public class ESAggsRepository extends BaseRepository {

	private static final Logger LOG = LoggerFactory.getLogger(ESAggsRepository.class);

	@Autowired
	private TransportClient client;

	/**
	 * 搜索参数
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public BootstrapTablePaginationVo<String> searchMsgByParam(BasicSearchParam param) throws Exception {
		String keyWord = param.getKeyWord();
		String filed = param.getField();
		String index = param.getIndex();

		Assert.assertNotNull(client);
		Assert.assertNotNull(filed);
		Assert.assertNotNull(index);
		Assert.assertNotNull(keyWord);

		// 校验索引是否成功
		if (!isIndexExist(index)) {
			return null;
		}

		BootstrapTablePaginationVo<String> vo = new BootstrapTablePaginationVo<String>();

		// 响应信息
		List<String> responseStrList = new ArrayList<String>();
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(filed, keyWord);
		SearchRequestBuilder sbr = client.prepareSearch(index).setQuery(matchQueryBuilder);
		// 去重的字段
		if (param.getDistictField() != null) {
			// 去重的信息
			CollapseBuilder cb = new CollapseBuilder(param.getDistictField());
			sbr.setCollapse(cb);
		}
		CardinalityAggregationBuilder acb = AggregationBuilders.cardinality("count_id").field(param.getDistictField());

		// 列表参数
		SearchResponse response = sbr.addAggregation(acb).setFrom(param.getOffset()).setSize(param.getLimit()).get();
		SearchHits shList = response.getHits();
		for (SearchHit searchHit : shList) {
			responseStrList.add(searchHit.getSourceAsString());
		}
		vo.setRows(responseStrList);

		// 统计模块
		SingleValue responseAgg = response.getAggregations().get("count_id");
		int count = 0;
		if (responseAgg != null) {
			double value = responseAgg.value();
			count = getInt(value);
		}
		vo.setTotal(count);

		return vo;
	}

	public static int getInt(double number) {
		BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
		return Integer.parseInt(bd.toString());
	}

	//不要删除，留着备用
		/*public static void getArticleInfoByTags(String tags, List<String> filterList)
	            throws Exception {
	        //
	        SearchRequestBuilder responsebuilder = client.prepareSearch("info")
	                .setTypes("articles")
	                .setSearchType(SearchType.QUERY_THEN_FETCH);

	        // 1. pics过滤
	        NestedQueryBuilder picsFilter = QueryBuilders.nestedQuery(
	                "pics",
	                QueryBuilders.boolQuery()
	                        .must(QueryBuilders.matchQuery("pics.is_down", "1"))
	                        .must(QueryBuilders.matchQuery("pics.is_qr", "0")),
	                ScoreMode.Avg);

	        // 2. 关键词权重计算过滤,过滤ID
	        // ---------keyword查询
	        NestedQueryBuilder kwIDsQuery = QueryBuilders.nestedQuery(
	                "keywords02",
	                QueryBuilders
	                        .boolQuery()
	                        .must(QueryBuilders.matchQuery("keywords02.keyword",
	                                tags).analyzer("xxxx_xxx_analyzer"))
	                        .mustNot(QueryBuilders.termsQuery("_id", filterList)),
	                ScoreMode.Avg);

	        // ---------权重分数函数
	        FilterFunctionBuilder[] keyWeithFunctionBuilders = { new FunctionScoreQueryBuilder.FilterFunctionBuilder(
	                // 这里把_score * doc['keywords02.weight'].value修改成了随机化            
	                ScoreFunctionBuilders.randomFunction(Math.round(Math.random() * 100))
	        ) };

	        // ---kw函数权重查询
	        FunctionScoreQueryBuilder query = QueryBuilders.functionScoreQuery(
	                kwIDsQuery, keyWeithFunctionBuilders);

	        // 综合查询
	        SearchResponse myresponse = responsebuilder.setQuery(query)
	                .setPostFilter(picsFilter)
	                .setCollapse(new CollapseBuilder("keywords01.raw")).setFrom(0)
	                .setSize(5).get();
	        System.out.println(myresponse.toString());
	        SearchHits hits = myresponse.getHits();
	        System.out.println(hits.totalHits);
	        for (int i = 0; i < hits.getHits().length; i++) {
	            String sourceAsString = hits.getHits()[i].getSourceAsString();
	            System.out.println(sourceAsString);
	        }
	    }*/
}
