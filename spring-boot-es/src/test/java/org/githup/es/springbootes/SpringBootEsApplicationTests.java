package org.githup.es.springbootes;

import org.githup.es.SpringBootEsApplication;
import org.githup.es.service.ESSearchService;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试
 * 
 * @author sdc
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootEsApplication.class)
public class SpringBootEsApplicationTests {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringBootEsApplicationTests.class);

	/**
	 * 搜索服务
	 */
	@Autowired
	private ESSearchService esSearchService;
	
	
	
}
