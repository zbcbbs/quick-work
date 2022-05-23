package com.dongzz.quick.common.plugin.vuetables;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询 处理器
 */
public class VueTableHandler {

    private CountHandler countHandler;
    private ListHandler listHandler;
    private OrderHandler orderHandler;

    public VueTableHandler(CountHandler countHandler, ListHandler listHandler) {
        super();
        this.countHandler = countHandler;
        this.listHandler = listHandler;
    }

    public VueTableHandler(CountHandler countHandler, ListHandler listHandler, OrderHandler orderHandler) {
        this(countHandler, listHandler);
        this.orderHandler = orderHandler;
    }

    /**
     * 分页查询核心处理方法
     *
     * @param dtRequest 分页请求
     * @return
     */
    public VueTableResponse handle(VueTableRequest dtRequest) {
        int count = 0;
        List<?> list = null;
        count = this.countHandler.count(dtRequest);
        if (count > 0) {
            if (null != orderHandler) {
                dtRequest = this.orderHandler.order(dtRequest);
            }
            list = this.listHandler.list(dtRequest);
        }
        if (null == list) {
            list = new ArrayList<>();
        }

        return new VueTableResponse(count, count, list);
    }

    /**
     * 分页查询处理器
     */
    public interface ListHandler {
        List<?> list(VueTableRequest request);
    }

    /**
     * 统计数量处理器
     */
    public interface CountHandler {
        int count(VueTableRequest request);
    }

    /**
     * 排序处理器
     */
    public interface OrderHandler {
        VueTableRequest order(VueTableRequest request);
    }
}
