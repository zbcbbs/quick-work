package com.dongzz.quick.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.DateUtil;
import com.dongzz.quick.common.utils.ExcelUtil;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.common.utils.Util;
import com.dongzz.quick.security.dao.SysRoleMapper;
import com.dongzz.quick.security.dao.SysUserMapper;
import com.dongzz.quick.security.domain.SysRole;
import com.dongzz.quick.security.domain.SysUser;
import com.dongzz.quick.security.service.UserService;
import com.dongzz.quick.security.service.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl extends BaseMybatisServiceImpl<SysUser> implements UserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 加密器

    @Override
    public void addUser(SysUser user) throws Exception {
        SysUser u = userMapper.selectUserByUsername(user.getUsername());
        if (null != u) {
            throw new ServiceException("用户名已存在");
        }
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setStatus("1");
        user.setIsDel("0");
        // 新增
        userMapper.insertSelective(user);
    }

    @Override
    public void addUser(UserDto userDto) throws Exception {
        SysUser user = userDto;
        // 校验用户
        SysUser u = userMapper.selectUserByUsername(userDto.getUsername());
        if (null != u) {
            throw new ServiceException("用户名已存在");
        }

        // 新增用户
        String password = StringUtil.isNotBlank(userDto.getPassword()) ? userDto.getPassword() : "123456";
        user.setPassword(passwordEncoder.encode(password)); // 密码加密
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDel("0");
        user.setStatus("1");
        // 新增 获取新增记录主键
        userMapper.insertOne(user);

        // 用户授予角色
        List<Integer> roleIds = userDto.getRoles();
        if (!CollectionUtils.isEmpty(roleIds)) {
            userMapper.insertUserRoles(user.getId(), roleIds);
        }

    }

    @Override
    public void grantRoles(Integer userId, List<Integer> roleIds) throws Exception {
        userMapper.insertUserRoles(userId, roleIds);
    }

    @Override
    public void deleteUser(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            // 判断 是否批量删除
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String userId : ids) {
                    // 逻辑删除
                    userMapper.deleteUser(Integer.parseInt(userId));
                }
            } else {
                userMapper.deleteUser(Integer.parseInt(id));
            }
        }
    }

    @Override
    public void updateUser(Integer id, String status) throws Exception {
        userMapper.updateUserStatus(id, status);
    }

    @Override
    public void updateUser(UserDto userDto) throws Exception {
        SysUser user = userDto;
        // 校验用户
        SysUser u = userMapper.selectUserByUsername(userDto.getUsername());
        if (null != u && (u.getId() != userDto.getId())) {
            throw new ServiceException("用户名已存在");
        }

        // 修改
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);

        // 重新授予角色
        List<Integer> roleIds = userDto.getRoles();
        if (!CollectionUtils.isEmpty(roleIds)) {
            // 删除原有角色
            userMapper.deleteUserRoles(user.getId());
            userMapper.insertUserRoles(user.getId(), roleIds);
        }
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return userMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    List<UserDto> userDtos = new ArrayList<>();
                    List<LinkedHashMap> list = userMapper.selectAllUsers(request.getParams(), request.getOffset(), request.getLimit()); // 原始结果集
                    for (LinkedHashMap map : list) {
                        UserDto userDto = BeanUtil.mapToBean(map, UserDto.class, true);
                        // 查询用户的角色
                        List<SysRole> roles = roleMapper.selectRolesByUserId(userDto.getId());
                        List<Integer> ids = roles.stream().map(SysRole::getId).collect(Collectors.toList());
                        userDto.setRoles(ids);
                        userDtos.add(userDto);
                    }
                    return userDtos;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new VueTableHandler.OrderHandler() {

            @Override
            public VueTableRequest order(VueTableRequest request) {
                // 分页查询 预处理排序
                Map<String, Object> params = request.getParams();
                if (null != params.get("orderBy")) {
                    String orderBy = params.get("orderBy").toString();
                    if (orderBy.contains("id")) {
                        orderBy = orderBy.replace("id", "u.id");
                    }
                    // 重置排序
                    params.put("orderBy", orderBy);
                }
                return request;
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

    @Override
    public SysUser findUserByUname(String username) throws Exception {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public SysUser findUserByUemail(String email) throws Exception {
        return userMapper.selectUserByUemail(email);
    }

    @Override
    public SysUser findUserByUphone(String phone) throws Exception {
        return userMapper.selectUserByUphone(phone);
    }

    @Override
    public void download(List<UserDto> userDtos, HttpServletResponse response) throws Exception {
//        List<UserExcel> data = new ArrayList<>();
//        ExcelUtil.writeExcel("用户表.xlsx", data, UserExcel.class, response);
        String[] headers = new String[]{"ID", "用户名", "昵称", "手机", "固话", "邮箱", "性别", "创建时间"};
        List<Object[]> datas = new ArrayList<>();
        for (UserDto u : userDtos) {
            Object[] data = new Object[headers.length];
            data[0] = u.getId();
            data[1] = u.getUsername();
            data[2] = u.getNickname();
            data[3] = u.getPhone();
            data[4] = u.getTelephone();
            data[5] = u.getEmail();
            data[6] = u.getSex().equals("1") ? "男" : "女";
            data[7] = DateUtil.getDate("yyyy-MM-dd HH:mm:ss", u.getCreateTime());
            datas.add(data);
        }
        // 导出
        ExcelUtil.writeExcel("用户表.xlsx", headers, datas, response);
    }
}
