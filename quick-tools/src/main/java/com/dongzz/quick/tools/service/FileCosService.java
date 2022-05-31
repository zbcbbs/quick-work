package com.dongzz.quick.tools.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.tools.domain.ToolCosConfig;
import com.dongzz.quick.tools.domain.ToolCosFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 腾讯云存储 相关服务接口
 */
public interface FileCosService extends BaseMybatisService<ToolCosFile> {

    /**
     * 保存配置
     *
     * @param config
     * @return
     * @throws Exception
     */
    ToolCosConfig config(ToolCosConfig config) throws Exception;

    /**
     * 查询配置
     *
     * @return
     * @throws Exception
     */
    ToolCosConfig find() throws Exception;

    /**
     * 文件上传
     *
     * @param file   文件
     * @param config 配置
     * @return
     * @throws Exception
     */
    ToolCosFile addFile(MultipartFile file, ToolCosConfig config) throws Exception;

    /**
     * 删除文件 支持批量
     *
     * @param id     文件ID 1 或 1,2
     * @param config 配置
     * @throws Exception
     */
    void deleteFile(String id, ToolCosConfig config) throws Exception;

    /**
     * 修改文件
     *
     * @param file 文件
     * @throws Exception
     */
    void updateFile(ToolCosFile file) throws Exception;

    /**
     * 条件和分页查询
     *
     * @param request
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 导出 Excel
     *
     * @param files    文件列表
     * @param response 响应
     * @throws Exception
     */
    void download(List<ToolCosFile> files, HttpServletResponse response) throws Exception;
}
