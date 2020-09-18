package cn.easy4j.oss.core.storage;

import org.springframework.lang.NonNull;

import java.io.InputStream;

/**
 * 文件操纵者
 *
 * @author ChenYichen
 */
public interface FileStorageStrategy {

    /**
     * 判断是否存在文件
     * @param fileName 文件名称
     * @return 成功true、失败false
     */
    boolean isExistingFile(String fileName);

    /**
     * 获取文件字节
     * @param fileName  文件名称
     * @return  二进制数组
     */
    byte[] getFileBytes(String fileName);

    /**
     * 存储文件
     * @param fileName 文件名称
     * @param inputStream  文件流
     */
    void saveFile(String fileName, InputStream inputStream);

    /**
     * 存储文件
     * @param fileName 文件名称
     * @param bytes byte数组
     */
    void saveFile(String fileName, byte[] bytes);

    /**
     * 描述:拷贝一份新的原文件信息
     * @param newFileFinalName  新文件
     * @param sourceFileFinalName  源文件
     * @return 成功true、失败false
     */
    boolean copyFile(String sourceFileFinalName, String newFileFinalName);

    /**
     * 获取文件真实地址
     * @param storageName 文件名
     * @return 文件地址
     */
    String getFileUrl(@NonNull String storageName);

}
