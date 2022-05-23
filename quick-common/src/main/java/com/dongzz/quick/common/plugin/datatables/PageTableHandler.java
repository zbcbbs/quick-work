package com.dongzz.quick.common.plugin.datatables;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询 处理器
 */
public class PageTableHandler {

    private CountHandler countHandler;
    private ListHandler listHandler;
    private OrderHandler orderHandler;

    public PageTableHandler(CountHandler countHandler, ListHandler listHandler) {
        super();
        this.countHandler = countHandler;
        this.listHandler = listHandler;
    }

    public PageTableHandler(CountHandler countHandler, ListHandler listHandler, OrderHandler orderHandler) {
        this(countHandler, listHandler);
        this.orderHandler = orderHandler;
    }

    /**
     * 分页查询核心处理方法
     *
     * @param dtRequest 分页请求
     * @return
     */
    public PageTableResponse handle(PageTableRequest dtRequest) {
        int count = 0;
        List<?> list = null;

        // 总记录数
        count = this.countHandler.count(dtRequest);
        // 排序+数据列表
        if (count > 0) {
            // 排序
            if (null != orderHandler) {
                dtRequest = this.orderHandler.order(dtRequest);
            }
            list = this.listHandler.list(dtRequest);
        }
        if (null == list) {
            list = new ArrayList<>();
        }

        return new PageTableResponse(count, count, list);
    }

    /**
     * 分页查询处理器
     */
    public interface ListHandler {
        List<?> list(PageTableRequest request);
    }

    /**
     * 统计数量处理器
     */
    public interface CountHandler {
        int count(PageTableRequest request);
    }

    /**
     * 排序处理器
     */
    public interface OrderHandler {
        PageTableRequest order(PageTableRequest request);
    }
}
