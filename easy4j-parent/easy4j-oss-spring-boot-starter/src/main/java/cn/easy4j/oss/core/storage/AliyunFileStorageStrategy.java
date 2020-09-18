package cn.easy4j.oss.core.storage;

import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.oss.config.properties.Easy4jOssProperties;
import cn.easy4j.oss.core.util.FileUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 阿里云oss操作封装
 *
 * @author ChenYichen
 */
@Slf4j
public class AliyunFileStorageStrategy implements FileStorageStrategy {

    private final Easy4jOssProperties.Aliyun aliyun;

    private final OSSClient ossClient;

    public AliyunFileStorageStrategy(Easy4jOssProperties.Aliyun aliyun, OSSClient ossClient) {
        this.aliyun = aliyun;
        this.ossClient = ossClient;
    }

    @Override
    public boolean isExistingFile(String fileName) {
        try {
            ossClient.getObject(this.aliyun.getBucketName(), fileName);
        } catch (OSSException | ClientException e) {
            log.info("阿里云上找不到该文件！文件名：" + fileName);
            return false;
        }
        return true;
    }

    @Override
    public byte[] getFileBytes(String fileName) {
        OSSObject object;
        try {
            object = ossClient.getObject(this.aliyun.getBucketName(), fileName);
        } catch (OSSException | ClientException e) {
            log.info("阿里云上找不到该文件，文件名：{}", fileName);
            throw new BusinessException("文件不存在");
        }

        try (InputStream objectContent = object.getObjectContent()) {
            return FileUtil.toByteArray(objectContent);
        } catch (Exception e) {
            log.error("流读取错误：{}", e.getMessage());
            throw new BusinessException("文件不存在或已损坏");
        }
    }

    @Override
    public void saveFile(String fileName, InputStream inputStream) {
        ossClient.putObject(this.aliyun.getBucketName(), fileName, inputStream);
    }

    @Override
    public void saveFile(String fileName, byte[] bytes) {
        ossClient.putObject(this.aliyun.getBucketName(), fileName, new ByteArrayInputStream(bytes));
    }

    @Override
    public boolean copyFile(String sourceFileFinalName, String newFileFinalName) {
        String bucketName = this.aliyun.getBucketName();
        CopyObjectRequest copyObjectFile = new CopyObjectRequest(bucketName, sourceFileFinalName, bucketName, newFileFinalName);
        ObjectMetadata meta = new ObjectMetadata();
        copyObjectFile.setNewObjectMetadata(meta);
        try {
            ossClient.copyObject(copyObjectFile);
        } catch (Exception e) {
            log.error("oss拷贝文件错误：{}", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String getFileUrl(@NonNull String storageName) {
        String domain = this.aliyun.getBucketName() + "." + this.aliyun.getEndpoint();
        return "//" + domain + "/" + storageName;
    }

}
