package cn.easy4j.oss.modular.controller;

import cn.easy4j.oss.core.storage.FileStorageStrategy;
import cn.easy4j.oss.core.storage.LocalFileStorageStrategy;
import cn.easy4j.oss.modular.entity.SysFile;
import cn.easy4j.oss.modular.service.SysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Objects;

/**
 * 文件管理
 *
 * @author ChenYichen
 */
@Api(tags = "1000.文件管理")
@Slf4j
@RestController
@RequestMapping("/sys_files")
public class SysFileController {

    @Resource
    private SysFileService sysFileService;

    @Resource
    private FileStorageStrategy fileStorageStrategy;

    private static final Integer RETURN_TYPE_URL = 2;

    @ApiOperation("上传文件")
    @ApiImplicitParam(name = "returnType", value = "返回类型，1-文件名，2-访问链接，默认值：1")
    @PostMapping
    public String post(@RequestParam MultipartFile file, @RequestParam(required = false) Integer returnType) {
        SysFile sysFile = sysFileService.saveAndUpload(file);
        return Objects.equals(returnType, RETURN_TYPE_URL) ? fileStorageStrategy.getFileUrl(sysFile.getFileStorageName()) : sysFile.getFileStorageName();
    }

    @ApiOperation("获得文件流")
    @GetMapping("/stream/{storageName}")
    public void getStream(@PathVariable String storageName, HttpServletResponse response) {
        if (fileStorageStrategy instanceof LocalFileStorageStrategy) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + storageName);

            byte[] fileBytes = fileStorageStrategy.getFileBytes(storageName);
            try (OutputStream out = response.getOutputStream()) {
                out.write(fileBytes);
            } catch (IOException e) {
                log.error("下载文件异常：{}", e.getMessage());
            }
        } else {
            // 非本地存储时，直接重定向，避免从服务器再读取一次文件流
            try {
                response.sendRedirect(fileStorageStrategy.getFileUrl(storageName));
            } catch (IOException e) {
                log.error("重定向异常：{}", e.getMessage());
            }
        }
    }

    @ApiOperation("获得文件返回base64格式")
    @GetMapping("/base64/{storageName}")
    public String getBase64(@PathVariable String storageName) {
        try {
            byte[] fileBytes = fileStorageStrategy.getFileBytes(storageName);
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (Exception e) {
            log.error("读取文件异常：{}", e.getMessage());
        }
        return StringUtils.EMPTY;
    }

}
