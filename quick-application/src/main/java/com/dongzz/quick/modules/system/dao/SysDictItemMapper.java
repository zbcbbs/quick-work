package com.dongzz.quick.modules.system.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.modules.system.domain.SysDictItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典项 数据访问接口
 */
public interface SysDictItemMapper extends BaseMybatisMapper<SysDictItem> {

    /**
     * 逻辑删除
     *
     * @param dictId 字典ID
     * @throws Exception
     */
    void deleteDictItem(Integer dictId) throws Exception;

    /**
     * 根据字典项名称 查询
     *
     * @param itemName 字典项名称
     * @param code     字典编码
     * @return
     * @throws Exception
     */
    SysDictItem selectDictItemByDname(@Param("itemName") String itemName, @Param("code") String code)
            throws Exception;

    /**
     * 根据字典项编码 查询
     *
     * @param itemCode 字典项编码
     * @param code     字典编码
     * @return
     * @throws Exception
     */
    SysDictItem selectDictItemByDcode(@Param("itemCode") String itemCode, @Param("code") String code)
            throws Exception;

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
    List<SysDictItem> selectAllDictItems(@Param("params") Map<String, Object> params,
                                         @Param("offset") Integer offset, @Param("limit") Integer limit) throws Exception;
}