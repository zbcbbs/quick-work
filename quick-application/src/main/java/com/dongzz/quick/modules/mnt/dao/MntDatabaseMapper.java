package com.dongzz.quick.modules.mnt.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.modules.mnt.domain.MntDatabase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据库管理
 *
 * @author zwk
 * @date 2022/05/19 15:55
 * @email zbcbbs@163.com
 */
public interface MntDatabaseMapper extends BaseMybatisMapper<MntDatabase> {

    /**
     * 条件查询 统计数量
     *
     * @param params 查询条件
     * @return
     * @throws Exception
     */
    Integer count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 条件查询 分页
     *
     * @param params 查询条件 包含排序
     * @param offset 每页起始索引
     * @param limit  每页显示条数
     * @return
     * @throws Exception
     */
    List<MntDatabase> selectPage(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                                 @Param("limit") Integer limit) throws Exception;
}