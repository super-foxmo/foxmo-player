package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    File getFileByMD5(String fileMd5);

    void insertFile(File file);

    void deleteFile(String fileMD5);

    File selectFileByMD5(String fileMD5);
}
