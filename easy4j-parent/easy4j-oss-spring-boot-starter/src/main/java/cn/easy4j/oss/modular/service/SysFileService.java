package cn.easy4j.oss.modular.service;

import cn.easy4j.common.exception.BusinessException;
import cn.easy4j.oss.core.storage.FileStorageStrategy;
import cn.easy4j.oss.core.util.FileUtil;
import cn.easy4j.oss.modular.entity.SysFile;
import cn.easy4j.oss.modular.mapper.SysFileMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 文件表 服务实现类
 *
 * @author ChenYichen
 */
@Service
public class SysFileService extends ServiceImpl<SysFileMapper, SysFile> {

    @Resource
    private FileStorageStrategy fileStorageStrategy;

    @Transactional(rollbackFor = Exception.class)
    public SysFile saveAndUpload(@NonNull MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = FileUtil.getFileSuffix(originalFilename);
        String storageName = UUID.randomUUID() + "." + fileSuffix;
        try (InputStream inputStream = file.getInputStream()) {
            fileStorageStrategy.saveFile(storageName, inputStream);
        } catch (IOException e) {
            throw new BusinessException("读取文件失败");
        }

        SysFile sysFileForInsert = new SysFile()
                .setFileOriginName(originalFilename).setFileSuffix(fileSuffix)
                .setFileSize(file.getSize()).setFileStorageName(storageName);
        this.save(sysFileForInsert);
        return sysFileForInsert;
    }

}
