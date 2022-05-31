package com.dongzz.quick.tools.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.config.bean.FileProperties;
import com.dongzz.quick.common.domain.UploadVo;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.DateUtil;
import com.dongzz.quick.common.utils.ExcelUtil;
import com.dongzz.quick.common.utils.FileUtil;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.tools.dao.ToolCosConfigMapper;
import com.dongzz.quick.tools.dao.ToolCosFileMapper;
import com.dongzz.quick.tools.domain.ToolCosConfig;
import com.dongzz.quick.tools.domain.ToolCosFile;
import com.dongzz.quick.tools.service.FileCosService;
import com.dongzz.quick.tools.utils.CosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "cos")
public class FileCosServiceImpl extends BaseMybatisServiceImpl<ToolCosFile> implements FileCosService {

    @Autowired
    private ToolCosConfigMapper cosConfigMapper;
    @Autowired
    private ToolCosFileMapper fileMapper;
    @Autowired
    private FileProperties fileProperties;

    @Override
    @Transactional
    @CachePut(key = "'config'")
    public ToolCosConfig config(ToolCosConfig config) throws Exception {
        cosConfigMapper.updateByPrimaryKeySelective(config);
        return config;
    }

    @Override
    @Cacheable(key = "'config'")
    public ToolCosConfig find() throws Exception {
        ToolCosConfig config = cosConfigMapper.selectByPrimaryKey(1);
        return config;
    }

    @Override
    @Transactional
    public ToolCosFile addFile(MultipartFile file, ToolCosConfig config) throws Exception {
        FileUtil.checkSize(fileProperties.getMaxSize(), file.getSize()); // 大小限制
        UploadVo vo = CosUtil.upload(file, config);
        if (ObjectUtil.isNull(vo)) {
            throw new ServiceException("腾讯云上传失败！");
        }
        try {
            ToolCosFile tcf = new ToolCosFile();
            tcf.setId(vo.getFileMd5());
            tcf.setName(vo.getFileName());
            tcf.setCacheName(vo.getCacheName());
            tcf.setCachePath(vo.getCachePath());
            tcf.setPath(vo.getFilePath());
            tcf.setBucket(config.bucket());
            tcf.setUrl(vo.getFileUrl());
            tcf.setContentType(vo.getContentType());
            tcf.setType(vo.getFileType());
            tcf.setSize(vo.getFileSize());
            tcf.setCreateTime(new Date());
            tcf.setUpdateTime(new Date());
            fileMapper.insertSelective(tcf);
            return tcf;
        } catch (Exception e) {
            CosUtil.delete(config, vo.getCachePath());
            throw new ServiceException("添加上传记录失败！");
        }
    }

    @Override
    @Transactional
    public void deleteFile(String id, ToolCosConfig config) throws Exception {
        if (StringUtil.isNotBlank(id)) {
            if (id.contains(",")) {
                String[] ids = StringUtil.split(id, ",");
                for (String fid : ids) {
                    ToolCosFile cf = fileMapper.selectByPrimaryKey(fid);
                    fileMapper.deleteByPrimaryKey(fid);
                    CosUtil.delete(config, cf.getCachePath());
                }
            } else {
                ToolCosFile cf = fileMapper.selectByPrimaryKey(id);
                fileMapper.deleteByPrimaryKey(id);
                CosUtil.delete(config, cf.getCachePath());
            }
        }
    }

    @Override
    @Transactional
    public void updateFile(ToolCosFile file) throws Exception {
        fileMapper.updateByPrimaryKeySelective(file);
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return fileMapper.count(request.getParams());
                } catch (Exception e) {
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {

                    return fileMapper.selectAllFiles(request.getParams(), request.getOffset(), request.getLimit());
                } catch (Exception e) {
                    return new ArrayList<>();
                }
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

    @Override
    public void download(List<ToolCosFile> files, HttpServletResponse response) throws Exception {
        String[] headers = new String[]{"ID", "名称", "内容类型", "大小", "类型", "存储地址", "相对路径", "创建时间"};
        List<Object[]> datas = new ArrayList<>();
        for (ToolCosFile file : files) {
            Object[] data = new Object[headers.length];
            data[0] = file.getId();
            data[1] = file.getName();
            data[2] = file.getContentType();
            data[3] = file.getSize();
            data[4] = file.getType();
            data[5] = file.getCachePath();
            data[6] = file.getPath();
            data[7] = DateUtil.getDate("yyyy-MM-dd HH:mm:ss", file.getCreateTime());
            datas.add(data);
        }
        ExcelUtil.writeExcel("上传文件表.xlsx", headers, datas, response);
    }
}
