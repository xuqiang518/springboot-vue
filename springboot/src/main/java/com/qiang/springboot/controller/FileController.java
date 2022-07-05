package com.qiang.springboot.controller;

/**
 * @author XuQiang
 * @creat 2022-06-29 21:01
 */

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiang.springboot.common.Result;
import com.qiang.springboot.entity.Files;
import com.qiang.springboot.mapper.FileMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**文件上传接口*/
@RequestMapping("/file")
@RestController
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    /**文件上传接口 file 前端传递过来的文件*/
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        //先存储到磁盘
        File uploadParentFile = new File(fileUploadPath);
        if(!uploadParentFile.exists()){  //判断配置文件的目录是否存在，若不存在则创建一个新的文件目录
            uploadParentFile.mkdir();
        }
        String uuid = IdUtil.fastSimpleUUID(); //定义文件的唯一标识码
        String fileUUID = uuid + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);

        //获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());

//        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
//        filesQueryWrapper.eq("md5", md5);
//        Files one = fileMapper.selectOne(filesQueryWrapper);
        //从数据库中查询是否存在相同的记录
        Files one = getFileByMd5(md5);

        //获取文件url
        String url;
        if(one != null){
            url = one.getUrl();
        }else {
            //将获取的文件存储到磁盘中
            file.transferTo(uploadFile);
            url = "http://localhost:8090/file/" + fileUUID;
        }

        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;

    }

    /**
     * 文件下载接口 http://localhost:8090/file/{fileUUID}
     *
     * */

    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment; filename" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    private Files getFileByMd5(String md5){  //查询文件的md5是否存在
        QueryWrapper<Files> wrapper = new QueryWrapper<>();
        wrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(wrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    @GetMapping("/front/all")
    public Result frontAll(){
        return Result.success(fileMapper.selectList(null));
    }


    /**更新*/
    @PostMapping("/update")
    public Result save(@RequestBody Files files){
        return Result.success(fileMapper.updateById(files));
    }

    @GetMapping("/detail/{id}")
    public Result getById(@PathVariable Integer id){
        Files files = fileMapper.selectById(id);
        return Result.success(files);
    }



    /**  分页查询  */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if(!"".equals(name)){
            queryWrapper.like("name", name);
        }
        Page<Files> filesPage = fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(filesPage);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);                  //假删除
        fileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")    //批量删除
    public Result deleteBatch(@RequestBody List<Integer> ids){
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for(Files file : files){
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

}
