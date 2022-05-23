package com.dongzz.quick.modules.system.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.modules.system.domain.SysDictItem;

/**
 * 字典项 相关服务接口
 */
public interface DictItemService extends BaseMybatisService<SysDictItem> {

    /**
     * 新增字典项
     *
     * @param dictItem 字典项
     * @throws Exception
     */
    void addDictItem(SysDictItem dictItem) throws Exception;

    /**
     * 删除，逻辑，支持批量
     *
     * @param id 字典项ID 1 或 1,2
     * @throws Exception
     */
    void deleteDictItem(String id) throws Exception;

    /**
     * 修改字典项
     *
     * @param dictItem 字典项
     * @throws Exception
     */
    void updateDictItem(SysDictItem dictItem) throws Exception;

    /**
     * 分页 条件查询
     *
     * @param request
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;
}
