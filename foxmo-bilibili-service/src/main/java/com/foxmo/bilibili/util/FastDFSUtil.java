package com.foxmo.bilibili.util;

import com.foxmo.bilibili.domain.exception.ConditionException;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class FastDFSUtil {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private AppendFileStorageClient appendFileStorageClient;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //默认分组
    private static final String DEFAULT_GROUP = "group1";

    private static final String PATH_KEY = "path-key:";

    private static final String UPLOADED_SIZE_KEY = "uploaded-size-key:";

    private static final String UPLOADED_NO_KEY = "uploaded-no-key:";
    //单片文件的大小
    private static final int SLICE_SIZE = 1024 * 1024 * 2;

    //获取文件类型
    public String getFileType(MultipartFile file){
        if(file == null){
            throw new ConditionException("非法文件！");
        }
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        String type = fileName.substring(index + 1);
        return type;
    }

    //文件上传
    public String uploadCommonFile(MultipartFile file) throws Exception{
        HashSet<MetaData> metaDataSet = new HashSet<>();
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
        //放回文件存储路径
        return storePath.getPath();
    }

    //上传可以断点续传的文件
    public String uploadAppenderFile(MultipartFile file) throws Exception{
        String filename = file.getOriginalFilename();
        String fileType = this.getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);

        return storePath.getPath();
    }

    /**
     * 续传分断文件
     * @param file      上传的分断文件
     * @param filePath  第一分断文件的上传路径
     * @param offset    偏移量
     */
    public void modifyAppenderFile(MultipartFile file,String filePath,Long offset) throws Exception{
        appendFileStorageClient.modifyFile(DEFAULT_GROUP,filePath,file.getInputStream(),file.getSize(),offset);
    }

    /**
     * 分片上传文件
     * @param file   上传的分断文件
     * @param fileMD5   分断文件的MD5码（每个分断文件的唯一标识）
     * @param sliceNo   当前上传文件的片数
     * @param totalSliceNo  要上传的总片数
     * @return  文件上传路径
     */
    public String uploadFileBySlices(MultipartFile file,String fileMD5,Integer sliceNo,Integer totalSliceNo) throws Exception{
        if (file == null || sliceNo == null || totalSliceNo == null){
            throw new ConditionException("参数异常");
        }
        //上传路径key
        String pathKey = PATH_KEY + fileMD5;
        //当前所有已上传分片文件的总大小
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMD5;
        //已上传的总分片数
        String uploadedNoKey = UPLOADED_NO_KEY + fileMD5;
        //获取当前已上传文件总大小
        String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        if (!StringUtils.isNullOrEmpty(uploadedSizeStr)){
            uploadedSize = Long.valueOf(uploadedSizeStr);
        }
        String fileType = this.getFileType(file);
        //判断是否为上传第一个分片文件
        if (sliceNo == 1){
            String filePath = this.uploadAppenderFile(file);
            if (StringUtils.isNullOrEmpty(filePath)){
                throw new ConditionException("上传失败！");
            }
            redisTemplate.opsForValue().set(pathKey,filePath);

            redisTemplate.opsForValue().set(uploadedNoKey,"1");
        }else{
            String filePath = redisTemplate.opsForValue().get(pathKey);
            if (StringUtils.isNullOrEmpty(filePath)){
                throw new ConditionException("上传失败！");
            }
            this.modifyAppenderFile(file,filePath,uploadedSize);
            redisTemplate.opsForValue().increment(uploadedNoKey);
        }
        //修改历史上传分片文件大小
        uploadedSize += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey,String.valueOf(uploadedSize));
        //如果所有分片全部上传完毕，则清空redis里面相关的key和value
        String uploadedNoStr = redisTemplate.opsForValue().get(uploadedNoKey);
        Long uploadedNo = Long.valueOf(uploadedNoStr);
        //上传路径备份
        String filePath = "";
        if (totalSliceNo.equals(uploadedNo)){
            List<String> keyList = Arrays.asList(pathKey, uploadedSizeKey, uploadedNoKey);
            filePath = redisTemplate.opsForValue().get(pathKey);
            redisTemplate.delete(keyList);
        }
        return filePath;
    }

    //文件分片
    public void convertFileToSlices(MultipartFile multipartFile) throws Exception{
        String originalFilename = multipartFile.getOriginalFilename();
        String fileType = this.getFileType(multipartFile);
        File file = this.multipartFileToFile(multipartFile);
        long fileLength = file.length();
        int count = 1;
        for (int i = 0; i < fileLength; i += SLICE_SIZE) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
            //初始位置
            randomAccessFile.seek(i);
            //读取文件
            byte[] bytes = new byte[SLICE_SIZE];
            int len = randomAccessFile.read(bytes);
            //新文件的保存路径
            String path = "D:/电影/" + count + "." + fileType;
            File slice = new File(path);
            FileOutputStream fos = new FileOutputStream(slice);
            fos.write(bytes,0,len);
            fos.close();
            randomAccessFile.close();
            count++;
        }
        file.delete();
    }

    //MultipartFile --> File
    public File multipartFileToFile(MultipartFile multipartFile) throws Exception{
        String originalFilename = multipartFile.getOriginalFilename();
        String[] fileName = originalFilename.split("\\.");
        File file = File.createTempFile(fileName[0], fileName[1]);
        multipartFile.transferTo(file);
        return file;
    }


    //文件删除
    public void deleteFile(String filePath){
        fastFileStorageClient.deleteFile(filePath);
    }
}
