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
import com.dongzz.quick.tools.dao.ToolLocalFileMapper;
import com.dongzz.quick.tools.domain.ToolLocalFile;
import com.dongzz.quick.tools.service.LocalFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LocalFileServiceImpl extends BaseMybatisServiceImpl<ToolLocalFile> implements LocalFileService {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileServiceImpl.class);

    @Autowired
    private ToolLocalFileMapper fileMapper;
    @Autowired
    private FileProperties fileProperties;

    @Override
    @Transactional
    public ToolLocalFile addFile(MultipartFile file, String name) throws Exception {
        FileUtil.checkSize(fileProperties.getMaxSize(), file.getSize()); // 检查大小限制
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename()); // 扩展名
        String type = FileUtil.getFileType(suffix); // 类型
        String filePath = fileProperties.getPath().getPath() + type + File.separator;
        filePath = filePath.replace("\\", "/");
        UploadVo vo = FileUtil.upload(file, filePath);
        if (ObjectUtil.isNull(vo)) {
            throw new ServiceException("上传文件失败！");
        }
        try {
            String path = File.separator + vo.getFileType() + File.separator + vo.getCacheName(); // 相对路径
            path = path.replace("\\", "/");
            ToolLocalFile tlf = new ToolLocalFile();
            tlf.setId(vo.getFileMd5());
            name = StringUtil.isNotBlank(name) ? name : vo.getFileName(); // 源名称
            tlf.setName(name);
            tlf.setCacheName(vo.getCacheName());
            tlf.setContentType(vo.getContentType());
            tlf.setType(vo.getFileType());
            tlf.setSize(vo.getFileSize());
            tlf.setUrl(fileProperties.getDomain() + "/file" + path);
            tlf.setPath(path);
            tlf.setCachePath(vo.getCachePath());
            tlf.setCreateTime(new Date());
            tlf.setUpdateTime(new Date());
            fileMapper.insertSelective(tlf);
            return tlf;
        } catch (Exception e) {
            FileUtil.delete(vo.getCachePath());
            throw new ServiceException("添加上传记录失败！", e);
        }
    }

    @Override
    @Transactional
    public void deleteFile(String id) throws Exception {
        if (StringUtil.isNotBlank(id)) {
            if (id.contains(",")) {
                String[] ids = StringUtil.split(id, ",");
                for (String fid : ids) {
                    ToolLocalFile lf = fileMapper.selectByPrimaryKey(fid);
                    fileMapper.deleteByPrimaryKey(fid);
                    FileUtil.delete(lf.getCachePath());
                }
            } else {
                ToolLocalFile lf = fileMapper.selectByPrimaryKey(id);
                fileMapper.deleteByPrimaryKey(id);
                FileUtil.delete(lf.getCachePath()); // 清磁盘
            }
        }
    }

    @Override
    @Transactional
    public void updateFile(ToolLocalFile file) throws Exception {
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
    public void download(List<ToolLocalFile> files, HttpServletResponse response) throws Exception {
        String[] headers = new String[]{"ID", "名称", "内容类型", "大小", "类型", "存储地址", "相对路径", "创建时间"};
        List<Object[]> datas = new ArrayList<>();
        for (ToolLocalFile file : files) {
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
