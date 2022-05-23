package com.dongzz.quick.security.service.impl;

import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.DateUtil;
import com.dongzz.quick.common.utils.ExcelUtil;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.common.utils.Util;
import com.dongzz.quick.security.dao.SysDeptMapper;
import com.dongzz.quick.security.domain.SysDept;
import com.dongzz.quick.security.service.DeptService;
import com.dongzz.quick.security.service.dto.DeptDto;
import com.dongzz.quick.security.service.mapstruct.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeptServiceImpl extends BaseMybatisServiceImpl<SysDept> implements DeptService {

    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private DeptMapper deptMapMapper; // 实体映射

    @Override
    public void addDept(SysDept dept) throws Exception {
        SysDept d = deptMapper.selectDeptByDname(dept.getName());
        if (null != d) {
            throw new ServiceException("部门名称已存在");
        }
        dept.setCreateTime(new Date());
        dept.setUpdateTime(new Date());
        deptMapper.insertSelective(dept);

        // 判断 存在父节点
        if (null != dept.getPid() && !dept.getPid().equals(0)) {
            // 刷新父节点
            Integer subCount = deptMapper.countByPid(dept.getPid());
            deptMapper.updateSubCount(dept.getPid(), subCount);
        }
    }

    @Override
    public void deleteDept(String id) throws Exception {
        if (StringUtil.isNotBlank(id)) {
            // 判断 是否批量删除
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String did : ids) {
                    deptMapper.deleteByPrimaryKey(Integer.valueOf(did));
                }
            } else {
                deptMapper.deleteByPrimaryKey(Integer.valueOf(id));
            }
        }
    }

    @Override
    public void updateDept(SysDept dept) throws Exception {
        SysDept d = deptMapper.selectDeptByDname(dept.getName());
        if ((null != d) && (!d.getId().equals(dept.getId()))) {
            throw new ServiceException("部门名称已存在");
        }
        dept.setUpdateTime(new Date());
        deptMapper.updateByPrimaryKeySelective(dept);

        // 判断 存在父节点
        if (null != dept.getPid() && !dept.getPid().equals(0)) {
            // 刷新父节点
            Integer subCount = deptMapper.countByPid(dept.getPid());
            deptMapper.updateSubCount(dept.getPid(), subCount);
        }
    }

    @Override
    public DeptDto findById(Integer id) throws Exception {
        return deptMapMapper.toDto(deptMapper.selectByPrimaryKey(id));
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return deptMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    List<SysDept> list = deptMapper.selectAllDepts(request.getParams(), request.getOffset(), request.getLimit());
                    // => Dto
                    List<DeptDto> deptDtos = list.stream().map(deptMapMapper::toDto).collect(Collectors.toList());
                    List<DeptDto> trees = new ArrayList<>();
                    buildTree(deptDtos, trees); // 构造树形结构
                    return trees;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new VueTableHandler.OrderHandler() {

            @Override
            public VueTableRequest order(VueTableRequest request) {
                return request;
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

    @Override
    public List<DeptDto> getSuperior(DeptDto deptDto, List<SysDept> depts) throws Exception {
        if (deptDto.getPid() == 0) { // 顶级节点，不再具有上级节点
            depts.addAll(deptMapper.selectByPid(0)); // 查询所有同级根节点
            return deptMapMapper.toDto(depts);
        }
        depts.addAll(deptMapper.selectByPid(deptDto.getPid()));
        return getSuperior(findById(deptDto.getPid()), depts);
    }

    @Override
    public void buildTree(List<DeptDto> all, List<DeptDto> trees) throws Exception {
        // 过滤根节点
        for (DeptDto deptDto : all) {
            boolean hasPid = false;
            for (DeptDto d : all) {
                if (deptDto.getPid().equals(d.getId())) {
                    hasPid = true;
                    break;
                }
            }
            if (!hasPid) {
                trees.add(deptDto);
            }
        }

        // 遍历根节点 构造树结构
        for (DeptDto deptDto : trees) {
            // 获取子节点 递归调用
            List<DeptDto> children = getChildren(deptDto.getId(), all);
            deptDto.setChildren(children);
        }
    }

    /**
     * 获取子节点
     *
     * @param id  父节点ID
     * @param all 所有节点
     * @return
     */
    private List<DeptDto> getChildren(Integer id, List<DeptDto> all) {
        List<DeptDto> children = new ArrayList<>();
        for (DeptDto deptDto : all) {
            if (deptDto.getPid().equals(id)) {
                children.add(deptDto);
            }
        }

        // 递归调用
        for (DeptDto deptDto : children) {
            deptDto.setChildren(getChildren(deptDto.getId(), all));
        }

        // 若无子节点，添加空集合，递归退出
        if (children.size() == 0) {
            return new ArrayList<>();
        }
        return children;
    }

    @Override
    public void download(List<DeptDto> deptDtos, HttpServletResponse response) throws Exception {
        String[] headers = new String[]{"ID", "部门名称", "状态", "子部门", "排序", "创建时间"};
        List<Object[]> datas = new ArrayList<>();
        for (DeptDto d : deptDtos) {
            Object[] data = new Object[headers.length];
            data[0] = d.getId();
            data[1] = d.getName();
            data[2] = d.getEnabled() ? "正常" : "禁用";
            data[3] = d.getSubCount();
            data[4] = d.getSort();
            data[5] = DateUtil.getDate("yyyy-MM-dd HH:mm:ss", d.getCreateTime());
            datas.add(data);
        }
        // 导出
        ExcelUtil.writeExcel("部门表.xlsx", headers, datas, response);
    }
}
