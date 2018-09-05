package org.githup.es.utils;

import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 描述: Elasticsearch 工具类
 *
 * 备注：还没做系统的工具类，预留出口子
 */
@Component
public class ESUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESUtils.class);

    @Autowired
    private TransportClient transportClient;

    //测试类

}
