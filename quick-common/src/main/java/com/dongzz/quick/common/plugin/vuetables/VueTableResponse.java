package com.dongzz.quick.common.plugin.vuetables;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询 响应结果
 */
public class VueTableResponse implements Serializable {

    private Integer recordsTotal; // 总记录数
    private Integer recordsFiltered; // 符合条件的记录数
    private List<?> data; // 数据集合

    public VueTableResponse(Integer recordsTotal, Integer recordsFiltered, List<?> data) {
        super();
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

}
