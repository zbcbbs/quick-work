package com.dongzz.quick.modules.system.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.modules.system.domain.SysDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典 数据访问接口
 */
public interface SysDictMapper extends BaseMybatisMapper<SysDict> {

    /**
     * 逻辑删除
     *
     * @param dictId 字典ID
     * @throws Exception
     */
    void deleteDict(Integer dictId) throws Exception;

    /**
     * 根据字典名称 查询字典
     *
     * @param dictName 字典名称
     * @return
     * @throws Exception
     */
    SysDict selectDictByDname(String dictName) throws Exception;

    /**
     * 根据字典编码 查询字典
     *
     * @param dictCode 字典编码
     * @return
     * @throws Exception
     */
    SysDict selectDictByDcode(String dictCode) throws Exception;

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
    List<SysDict> selectAllDicts(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                                 @Param("limit") Integer limit) throws Exception;
}