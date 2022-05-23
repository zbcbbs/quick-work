package com.dongzz.quick.security.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.security.domain.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门 数据访问接口
 */
public interface SysDeptMapper extends BaseMybatisMapper<SysDept> {

    /**
     * 根据部门名称查询
     *
     * @param deptName
     * @return
     * @throws Exception
     */
    SysDept selectDeptByDname(String deptName) throws Exception;

    /**
     * 根据PID查询
     *
     * @param pid
     * @return
     * @throws Exception
     */
    List<SysDept> selectByPid(Integer pid) throws Exception;

    /**
     * 统计父节点的子节点数
     *
     * @param pid
     * @return
     * @throws Exception
     */
    Integer countByPid(Integer pid) throws Exception;

    /**
     * 修改指定节点的子节点数量
     *
     * @param id       节点ID
     * @param subCount 数量
     * @throws Exception
     */
    void updateSubCount(@Param("id") Integer id, @Param("subCount") Integer subCount) throws Exception;

    /**
     * 条件查询 统计数量
     *
     * @param params
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
    List<SysDept> selectAllDepts(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                                 @Param("limit") Integer limit) throws Exception;
}
