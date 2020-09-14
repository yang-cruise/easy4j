package cn.easy4j.oss.core.storage.impl;

import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.oss.config.properties.Easy4jOssProperties;
import cn.easy4j.oss.core.storage.FileStorage;
import cn.easy4j.oss.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * 本地文件存储
 * @author yangzongmin
 * @since 2020/9/8 10:48
 */
@Slf4j
public class FileStorageLocalImpl implements FileStorage {

    private final Easy4jOssProperties.Local local;

    public FileStorageLocalImpl(Easy4jOssProperties.Local local) {
        this.local = local;
    }

    @Override
    public boolean isExistingFile(String fileName) {
        return this.getFile(fileName).exists();
    }

    @Override
    public byte[] getFileBytes(String fileName) {
        File file = this.getFile(fileName);
        try (InputStream inputStream = new FileInputStream(file)) {
            return FileUtil.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("读取文件异常：{}", e.getMessage());
        }
        return new byte[0];
    }

    @Override
    public void saveFile(String fileName, InputStream inputStream) {
        try {
            this.saveFile(fileName, FileUtil.toByteArray(inputStream));
        } catch (IOException e) {
            log.error("上传文件失败：", e);
            throw new BusinessException("上传文件失败");
        }
    }

    @Override
    public void saveFile(String fileName, byte[] bytes) {
        File file = this.getFile(fileName);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            log.error("上传文件失败：", e);
            throw new BusinessException("上传文件失败");
        }
    }

    @Override
    public boolean copyFile(String sourceFileFinalName, String newFileFinalName) {
        try {
            this.saveFile(newFileFinalName, this.getFileBytes(sourceFileFinalName));
            return true;
        } catch (Exception e) {
            log.error("复制文件失败：", e);
        }
        return false;
    }

    @Override
    public String getFileUrl(@NonNull String storageName) {
        return "/sys_files/stream/" + storageName;
    }

    private File getFile(String fileName) {
        File upload;
        if (StringUtils.isBlank(local.getPath())) {
            File path;
            try {
                path = new File(ResourceUtils.getURL("classpath:").getPath());
            } catch (FileNotFoundException e) {
                log.error("获取文件存储路径失败：{}", e.getMessage());
                path = new File(StringUtils.EMPTY);
            }
            upload = new File(path.getAbsolutePath(), "static/upload/");
        } else {
            upload = new File(local.getPath());
        }

        if (!upload.exists()) {
            boolean mkdir = upload.mkdirs();
            log.info("创建文件夹 = [{}]，结果 = [{}]", upload.getAbsolutePath(), mkdir);
        }
        return new File(upload.getAbsolutePath() + File.separator + fileName);
    }

}
