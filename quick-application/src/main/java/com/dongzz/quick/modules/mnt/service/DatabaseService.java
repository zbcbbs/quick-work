package com.dongzz.quick.modules.mnt.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.modules.mnt.domain.MntDatabase;
import com.dongzz.quick.modules.mnt.service.dto.DatabaseDto;
import com.dongzz.quick.modules.mnt.service.dto.DatabaseQueryCriteria;
import com.dongzz.quick.common.utils.Page;

import javax.servlet.http.HttpServletResponse;

/**
 * 数据库管理
 *
 * @author zwk
 * @date 2022/05/19 15:55
 * @email zbcbbs@163.com
 */
public interface DatabaseService extends BaseMybatisService<MntDatabase> {

    /**
     * 添加
     *
     * @param dto /
     * @throws Exception
     */
    void addDatabase(DatabaseDto dto) throws Exception;

    /**
     * 删除，支持批量
     *
     * @param id 主键ID 1 或 1,2
     * @throws Exception
     */
    void deleteDatabase(String id) throws Exception;

    /**
     * 修改
     *
     * @param dto /
     * @throws Exception
     */
    void updateDatabase(DatabaseDto dto) throws Exception;

    /**
     * 分页条件查询
     *
     * @param request 分页请求
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 分页条件查询
     *
     * @param page     分页参数
     * @param criteria 条件
     * @return
     * @throws Exception
     */
    Page<MntDatabase> findAll(Page<MntDatabase> page, DatabaseQueryCriteria criteria) throws Exception;

    /**
     * 测试连接
     *
     * @param dto 连接参数
     * @return
     * @throws Exception
     */
    boolean checkConnection(DatabaseDto dto) throws Exception;

    /**
     * 导出数据
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void download(VueTableRequest request, HttpServletResponse response) throws Exception;
}