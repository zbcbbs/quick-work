package com.dongzz.quick.security.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.security.domain.SysDept;
import com.dongzz.quick.security.service.dto.DeptDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 部门 服务接口
 */
public interface DeptService extends BaseMybatisService<SysDept> {

    /**
     * 新增部门
     */
    void addDept(SysDept dept) throws Exception;

    /**
     * 删除部门 支持批量
     *
     * @param id 部门ID 1 或 1,2
     * @throws Exception
     */
    void deleteDept(String id) throws Exception;

    /**
     * 修改部门
     *
     * @param dept
     * @throws Exception
     */
    void updateDept(SysDept dept) throws Exception;

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     * @throws Exception
     */
    DeptDto findById(Integer id) throws Exception;

    /**
     * 分页 条件查询
     *
     * @param request
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 根据ID获取同级与上级数据
     *
     * @param deptDto
     * @param depts
     * @return
     */
    List<DeptDto> getSuperior(DeptDto deptDto, List<SysDept> depts) throws Exception;

    /**
     * 构建树形结构
     *
     * @param all   所有部门
     * @param trees 树形集合
     */
    void buildTree(List<DeptDto> all, List<DeptDto> trees) throws Exception;

    /**
     * 下载导出 Excel
     *
     * @param deptDtos 数据
     * @param response
     * @throws Exception
     */
    void download(List<DeptDto> deptDtos, HttpServletResponse response) throws Exception;
}
