package com.dongzz.quick.common.plugin.vuetables;

import java.io.Serializable;
import java.util.Map;

/**
 * 分页查询 封装请求参数
 */
public class VueTableRequest implements Serializable {

    private Integer offset; // 当前页起始索引
    private Integer limit; // 每页显示条数
    private Map<String, Object> params; // 查询条件集合

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
