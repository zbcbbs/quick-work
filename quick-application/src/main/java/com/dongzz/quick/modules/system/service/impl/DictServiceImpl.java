package com.dongzz.quick.modules.system.service.impl;

import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.Util;
import com.dongzz.quick.modules.system.domain.SysDict;
import com.dongzz.quick.modules.system.dao.SysDictMapper;
import com.dongzz.quick.modules.system.service.DictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class DictServiceImpl extends BaseMybatisServiceImpl<SysDict> implements DictService {

    @Autowired
    private SysDictMapper dictMapper;

    @Override
    public void addDict(SysDict dict) throws Exception {
        SysDict d = dictMapper.selectDictByDname(dict.getName());
        if (null != d) {
            throw new ServiceException("字典名称已存在");
        } else {
            SysDict dd = dictMapper.selectDictByDcode(dict.getCode());
            if (null != dd) {
                throw new ServiceException("字典编码已存在");
            }
        }

        dict.setCreateTime(new Date());
        dict.setUpdateTime(new Date());
        dict.setIsDel("0");
        dictMapper.insertSelective(dict);
    }

    @Override
    public void deleteDict(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String did : ids) {
                    dictMapper.deleteDict(Integer.parseInt(did));
                }
            } else {
                dictMapper.deleteDict(Integer.parseInt(id));
            }
        }
    }

    @Override
    public void updateDict(SysDict dict) throws Exception {
        SysDict d = dictMapper.selectDictByDname(dict.getName());
        if (null != d && (d.getId() != dict.getId())) {
            throw new ServiceException("字典名称已存在");
        }
        dict.setUpdateTime(new Date());
        dictMapper.updateByPrimaryKeySelective(dict);
    }

    @Override
    public List<SysDict> findAll() throws Exception {
        VueTableRequest request = new VueTableRequest();
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", " order by id desc");
        request.setParams(params);
        List<SysDict> dicts = dictMapper.selectAllDicts(request.getParams(), request.getOffset(), request.getLimit());
        return dicts;
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return dictMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    return dictMapper.selectAllDicts(request.getParams(), request.getOffset(), request.getLimit());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

}
