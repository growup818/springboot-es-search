package org.githup.es.dao;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.githup.es.model.SuggestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * ES Suggest 数据层
 * 
 * ES中，Suggester主要分为四种类型：term, phrase,completion以及context。
 * 
 * @author sdc
 *
 */
@Component
public class ESSuggestRepository extends BaseRepository{

	private static final Logger LOG = LoggerFactory.getLogger(ESSuggestRepository.class);

	@Autowired
	private TransportClient client;

	/**
	 * 创建索引
	 *
	 * @param index
	 *            对应的数据库
	 * 
	 * @return
	 */
	public boolean buildIndexByParam(String index) throws Exception {
		if (!isIndexExist(index)) {
			LOG.info("Index is not exits!");
		}
		// 创建索引
		// this.buildIndex(index);
		// setting 设置
		XContentBuilder settingsXb = XContentFactory.jsonBuilder()
				.startObject()
					//设置一下分词器，目前采用的是ngram分词器，例如：中华崛起 ，可以分成中，中华，中华崛，中华崛起，华，华崛，华崛起，崛，崛起，起
					//具体拆分主要根据min_gram和max_gram来拆分
					.startObject("analysis")
						.startObject("filter")
							.startObject("gramFilter")
								.field("type", "ngram")
								.field("min_gram", "1")
								.field("max_gram", "20")
								.array("token_chars", "letter", "digit")
							.endObject()
						.endObject()
						.startObject("analyzer")
							.startObject("gramAnalyzer")
								.field("type", "custom")
								.field("tokenizer", "whitespace")
								.array("filter", "lowercase", "gramFilter")
							.endObject()
							.startObject("whitespaceAnalyzer")
								.field("type", "custom")
								.field("tokenizer", "whitespace")
								.array("filter", "lowercase")
							.endObject()
						.endObject()
					.endObject()
				.endObject();
		// mapping 设置
		/*XContentBuilder mappingXb = XContentFactory.jsonBuilder()
					.startObject() 
							.startObject("properties") 
								.startObject("id")
									.field("type", "long")
								.endObject()
								.startObject("data")
									.field("type", "long")
								.endObject()
								.startObject("content")
									.field("type", "completion")//目前suggest主要有四类,term，phrase，completion,text
									.field("analyzer", "gramAnalyzer")
								.endObject()
							.endObject()
					.endObject();*/
		
		XContentBuilder mappingXb = XContentFactory.jsonBuilder()
				.startObject()
					.startObject("properties")
						.startObject("content")
							.field("type", "text")////目前suggest主要有四类,term，phrase，completion,text
							.field("analyzer", "gramAnalyzer")
						.endObject()
					.endObject()
				.endObject();
	
		LOG.info(" settingsXb: " + settingsXb.string());
		LOG.info(" mappingXb: " + mappingXb.string());
		
		//创建索引
		CreateIndexResponse indexResponse = client.admin().indices().prepareCreate(index).setSettings(settingsXb).addMapping("tech", mappingXb).get();

		LOG.info("索引index创建成功标识--》：" + indexResponse.isAcknowledged());

		return indexResponse.isAcknowledged();
	}
	
	/**
	 * 用户添加索引数据文档
	 * 
	 * @param index
	 *            对应的数据库
	 * @param type
	 *            类型 对应mysql的数据表
	 * @param SuggestModel
	 *            目标类 ，这个可以是Object类，或者对应的类型，这里就写死了，后期谁用，谁自己再改一下
	 * @return
	 * @throws Exception
	 */
	public String addTargetObjectDataDoc(String index, String type, SuggestModel suggestModel) throws Exception {
		// 构建参数和需要属性
		XContentBuilder xb = XContentFactory.jsonBuilder();

		xb.startObject();
		//可以用反射处理传来的Object类，对应每个字段映射到对应的索引里
		xb.field("id", suggestModel.getId());
		xb.field("data", suggestModel.getData());
		xb.field("content", suggestModel.getContent());
		xb.endObject();

		// 返回数据来源
		IndexResponse indexResponse = client.prepareIndex()
				.setIndex(index)
				.setType(type)
				.setSource(xb)
				.get();

		LOG.info("添加document，index:" + index + ",type:" + type + ",目标类obj:" + JSONObject.toJSONString(suggestModel));

		return indexResponse.getId();
	}
	
	//测试信息，无关紧要
	/*public void suggestSearch(String index, String filed, String type) throws InterruptedException, ExecutionException {
		CompletionSuggestionBuilder csb =  SuggestBuilders.completionSuggestion(filed).text(type);
		SearchResponse searchResponse = client.prepareSearch()
				.suggest(new SuggestBuilder().addSuggestion(index, csb)).execute().get();
		
		List<? extends Entry<? extends Option>> entryList = searchResponse.getSuggest().getSuggestion(filed).getEntries();
		System.out.println("测试->>>" + JSONObject.toJSONString(entryList));
	}*/

	
}
