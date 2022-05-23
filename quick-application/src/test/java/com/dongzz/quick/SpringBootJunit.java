package com.dongzz.quick;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * spring boot 单元测试 基础类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 采用随机端口启动服务器测试，避免集成了websocket时报错
public class SpringBootJunit {

}
