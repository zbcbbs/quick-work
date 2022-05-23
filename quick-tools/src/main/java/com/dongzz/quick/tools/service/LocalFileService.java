package com.dongzz.quick.tools.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.tools.domain.ToolLocalFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 本地存储 相关服务接口
 */
public interface LocalFileService extends BaseMybatisService<ToolLocalFile> {

    /**
     * 文件上传
     *
     * @param file 文件
     * @param name 文件名
     * @return
     * @throws Exception
     */
    ToolLocalFile addFile(MultipartFile file, String name) throws Exception;

    /**
     * 删除文件 支持批量
     *
     * @param id 文件ID 1 或 1,2
     * @throws Exception
     */
    void deleteFile(String id) throws Exception;

    /**
     * 修改文件
     *
     * @param file 文件
     * @throws Exception
     */
    void updateFile(ToolLocalFile file) throws Exception;

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
    void download(List<ToolLocalFile> files, HttpServletResponse response) throws Exception;

}
