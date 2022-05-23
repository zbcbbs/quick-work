package com.dongzz.quick.modules.mnt.service.dto;

import lombok.Data;
import com.dongzz.quick.common.annotation.query.Criteria;

import java.util.Date;
import java.util.List;

import com.dongzz.quick.common.annotation.query.Query;

/**
 * 数据库管理
 *
 * @author zwk
 * @date 2022/05/19 15:55
 * @email zbcbbs@163.com
 */
@Data
public class DatabaseQueryCriteria implements Criteria {

    @Query(type = Query.Type.LIKE)
    private String name;
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
}