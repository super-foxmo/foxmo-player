package com.foxmo.bilibili.mapper;

import com.foxmo.bilibili.domain.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    File getFileByMD5(String fileMd5);

    void addFile(File dbFile);
}
