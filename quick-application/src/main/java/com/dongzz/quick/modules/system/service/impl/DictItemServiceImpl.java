package com.dongzz.quick.modules.system.service.impl;

import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.Util;
import com.dongzz.quick.modules.system.domain.SysDictItem;
import com.dongzz.quick.modules.system.dao.SysDictItemMapper;
import com.dongzz.quick.modules.system.service.DictItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DictItemServiceImpl extends BaseMybatisServiceImpl<SysDictItem> implements DictItemService {

    @Autowired
    private SysDictItemMapper dictItemMapper;

    @Override
    public void addDictItem(SysDictItem dictItem) throws Exception {
        SysDictItem dt = dictItemMapper.selectDictItemByDname(dictItem.getItemValue(), dictItem.getCode());
        if (null != dt) {
            throw new ServiceException("字典项名称已存在");
        } else {
            SysDictItem dtt = dictItemMapper.selectDictItemByDcode(dictItem.getItemCode(), dictItem.getCode());
            if (null != dtt) {
                throw new ServiceException("字典项编码已存在");
            }
        }

        dictItem.setCreateTime(new Date());
        dictItem.setUpdateTime(new Date());
        dictItem.setIsDel("0");
        dictItemMapper.insertSelective(dictItem);
    }

    @Override
    public void deleteDictItem(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String did : ids) {
                    dictItemMapper.deleteDictItem(Integer.parseInt(did));
                }
            } else {
                dictItemMapper.deleteDictItem(Integer.parseInt(id));
            }
        }
    }

    @Override
    public void updateDictItem(SysDictItem dictItem) throws Exception {
        SysDictItem dt = dictItemMapper.selectDictItemByDname(dictItem.getItemValue(), dictItem.getCode());
        if (null != dt && (dt.getId() != dictItem.getId())) {
            throw new ServiceException("字典项名称已存在");
        } else {
            SysDictItem dtt = dictItemMapper.selectDictItemByDcode(dictItem.getItemCode(), dictItem.getCode());
            if (null != dtt && (dtt.getId() != dictItem.getId())) {
                throw new ServiceException("字典项编码已存在");
            }
        }

        dictItem.setUpdateTime(new Date());
        dictItemMapper.updateByPrimaryKeySelective(dictItem);
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return dictItemMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    return dictItemMapper.selectAllDictItems(request.getParams(), request.getOffset(),
                            request.getLimit());
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
