package com.dongzz.quick.modules.system.controller;

import com.dongzz.quick.common.annotation.Limit;
import com.dongzz.quick.common.annotation.rest.AnonymousGetMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试接口限流
 *
 * @author zwk
 * @date 2022/5/17 16:31
 * @email zbcbbs@163.com
 */
@RestController
@RequestMapping("/api/limit")
@Api(tags = "系统：测试接口限流")
public class LimitController {

    public static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 分布式限流 redis+lua
     */
    @AnonymousGetMapping
    @ApiOperation(value = "分布式限流", notes = "Lua接口限流")
    @Limit(name = "Lua接口限流", key = "test", prefix = "limit", period = 60, count = 10)
    public int luaLimt() throws Exception {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
