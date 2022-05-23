package com.dongzz.quick.modules.mnt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.*;
import com.dongzz.quick.modules.mnt.domain.MntDatabase;
import com.dongzz.quick.modules.mnt.service.dto.DatabaseDto;
import com.dongzz.quick.modules.mnt.service.dto.DatabaseQueryCriteria;
import com.dongzz.quick.modules.mnt.dao.MntDatabaseMapper;
import com.dongzz.quick.modules.mnt.service.DatabaseService;
import com.dongzz.quick.modules.mnt.utils.SqlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Transactional
public class DatabaseServiceImpl extends BaseMybatisServiceImpl<MntDatabase> implements DatabaseService {

    public static final Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    @Autowired
    private MntDatabaseMapper databaseMapper;

    @Override
    public void addDatabase(DatabaseDto dto) throws Exception {
        MntDatabase database = new MntDatabase();
        BeanUtil.copyProperties(dto, database);
        // 生成主键
        database.setId(IdUtil.simpleUUID());
        database.setCreateBy(SecurityUtil.getCurrentUser().getUsername());
        database.setCreateTime(new Date());
        databaseMapper.insertSelective(database);
    }

    @Override
    public void deleteDatabase(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String did : ids) {
                    databaseMapper.deleteByPrimaryKey(did);
                }
            } else {
                databaseMapper.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public void updateDatabase(DatabaseDto dto) throws Exception {
        MntDatabase database = new MntDatabase();
        BeanUtil.copyProperties(dto, database);
        database.setUpdateBy(SecurityUtil.getCurrentUser().getUsername());
        database.setUpdateTime(new Date());
        databaseMapper.updateByPrimaryKeySelective(database);
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return databaseMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    return databaseMapper.selectPage(request.getParams(), request.getOffset(), request.getLimit());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

    @Override
    public Page<MntDatabase> findAll(Page<MntDatabase> page, DatabaseQueryCriteria criteria) throws Exception {
        Page<MntDatabase> result = selectPage(page, criteria);
        return result;
    }

    @Override
    public boolean checkConnection(DatabaseDto dto) throws Exception {
        try {
            return SqlUtil.testConnection(dto.getJdbcUrl(), dto.getUsername(), dto.getPassword());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void download(VueTableRequest request, HttpServletResponse response) throws Exception {
        request.setLimit(null); // 消除分页
        VueTableResponse vtr = this.findAll(request);
        List<MntDatabase> data = (List<MntDatabase>) vtr.getData();
        List<Map<String, Object>> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(data)) {
            for (MntDatabase database : data) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("ID", database.getId());
                map.put("名称", database.getName());
                map.put("连接地址", database.getJdbcUrl());
                map.put("账号", database.getUsername());
                map.put("密码", database.getPassword());
                map.put("创建者", database.getCreateBy());
                map.put("创建时间", DateUtil.format(database.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                list.add(map);
            }
            ExcelUtil.writeExcel(list, response);
        }
    }
}