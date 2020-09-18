package cn.easy4j.oss.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件相关的工具类
 *
 * @author ChenYichen
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil {

    public static final String FILE_DEFAULT_NAME = "default";

    public static final String FILE_DEFAULT_SUFFIX = "txt";

    /**
     * 获取文件后缀名 不包含点
     * @param fileWholeName 完整文件名称
     * @return 文件后缀
     */
    public static String getFileSuffix(String fileWholeName) {
        if (StringUtils.isBlank(fileWholeName)) {
            return StringUtils.EMPTY;
        }
        int lastIndexOf = fileWholeName.lastIndexOf('.');
        return fileWholeName.substring(lastIndexOf + 1);
    }

    /**
     * 获取文件名称(考虑文件名和后缀为空的情况，返回默认的文件名和后缀)
     * @param fileName 文件名
     * @param suffix 后缀
     * @return 文件名称
     */
    public static String getFileName(String fileName, String suffix) {
        if (StringUtils.isBlank(fileName)) {
            if (StringUtils.isBlank(suffix)) {
                return FILE_DEFAULT_NAME + "." + FILE_DEFAULT_SUFFIX;
            } else {
                return FILE_DEFAULT_NAME + "." + suffix;
            }
        } else {
            return fileName;
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
