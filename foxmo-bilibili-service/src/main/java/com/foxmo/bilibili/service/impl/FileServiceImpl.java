package com.foxmo.bilibili.service.impl;

import com.foxmo.bilibili.domain.File;
import com.foxmo.bilibili.domain.exception.ConditionException;
import com.foxmo.bilibili.mapper.FileMapper;
import com.foxmo.bilibili.service.FileService;
import com.foxmo.bilibili.util.MD5Util;
import com.foxmo.bilibili.util.MinioUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;


@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private MinioUtil minioUtil;

    /*
    @Autowired
    private FastDFSUtil fastDFSUtil;
    */


    /*     fastDFS 分片试上传文件
    @Override
    public String uploadFileBySlices(MultipartFile slice, String fileMd5, Integer sliceNo, Integer totalSliceNo) throws Exception{
        File dbFile = fileMapper.getFileByMD5(fileMd5);
        if (dbFile != null){
            return dbFile.getUrl();
        }

        String filePath = fastDFSUtil.uploadFileBySlices(slice,fileMd5,sliceNo,totalSliceNo);
        if (!StringUtils.isNullOrEmpty(filePath)){
            dbFile = new File();
            dbFile.setUrl(filePath);
            dbFile.setType(fastDFSUtil.getFileType(slice));
            dbFile.setMd5(this.getFileMD5(slice));
            dbFile.setCreateTime(new Date());
            fileMapper.addFile(dbFile);
        }
        return filePath;
    }
    */

    @Override
    public String getFileMD5(MultipartFile file) throws Exception{
        return MD5Util.getFileMD5(file);
    }

    /**
     * 上传文件
     * @param multipartFile     上传文件
     * @param bucketName        桶名
     * @return
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile multipartFile, String bucketName) throws Exception {
        if (multipartFile == null || bucketName == null){
            throw new ConditionException("参数异常！");
        }
        //查询数据库中是否已存在该文件
        String fileMD5 = MD5Util.getFileMD5(multipartFile);
        File dbfile = fileMapper.selectFileByMD5(fileMD5);
        if (dbfile == null) {     //不存在
            //上传文件
            String fileUrl = minioUtil.uploadFile(multipartFile, bucketName);
            //封装数据
            File file = new File();
            file.setFileName(multipartFile.getOriginalFilename());
            file.setBucket(bucketName);
            file.setUrl(fileUrl);
            file.setType(minioUtil.getFileType(multipartFile));
            file.setMd5(MD5Util.getFileMD5(multipartFile));
            file.setCreateTime(new Date());
            //将上传的文件信息保存到mysql中
            fileMapper.insertFile(file);
            return fileUrl;
        }
        return dbfile.getUrl();
    }

    /**
     * 删除文件
     * @param bucketName       桶名
     * @param fileName      文件名
     * @throws Exception
     */
    @Override
    public void deleteFile(String bucketName, String fileName) throws Exception{
        if (StringUtils.isNullOrEmpty(bucketName) || StringUtils.isNullOrEmpty(fileName)){
            throw new ConditionException("参数异常！");
        }
        //判断服务器中是否存在指定文件
        InputStream inputStream;
        try {
            inputStream = minioUtil.getObject(bucketName, fileName);

        }catch (Exception e){
            throw new ConditionException("服务器中不存在该文件！");
        }
        //将二进制文件流转换成MultipartFile对象
        MultipartFile multipartFile = minioUtil.getMultipartFile(inputStream);
        //获取文件的唯一标识（MD5码）
        String fileMD5 = MD5Util.getFileMD5(multipartFile);
        //删除mysql文件数据
        fileMapper.deleteFile(fileMD5);
        //删除服务器文件数据
        minioUtil.removeObject(bucketName,fileName);
    }

}
