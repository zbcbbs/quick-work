package com.dongzz.quick.modules.system.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.modules.system.domain.SysDict;

import java.util.List;

/**
 * 字典 服务接口
 */
public interface DictService extends BaseMybatisService<SysDict> {

    /**
     * 添加字典
     *
     * @param dict 字典
     * @throws Exception
     */
    void addDict(SysDict dict) throws Exception;

    /**
     * 删除，逻辑，支持批量
     *
     * @param id 删除字典 1 或 1,2
     * @throws Exception
     */
    void deleteDict(String id) throws Exception;

    /**
     * 修改字典
     *
     * @param dict 字典
     * @throws Exception
     */
    void updateDict(SysDict dict) throws Exception;

    /**
     * 查询所有
     *
     * @return
     * @throws Exception
     */
    List<SysDict> findAll() throws Exception;

    /**
     * 分页 条件查询
     *
     * @param request 分页请求
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

}
