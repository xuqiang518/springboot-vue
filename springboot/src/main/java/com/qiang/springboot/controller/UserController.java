package com.qiang.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiang.springboot.common.Constants;
import com.qiang.springboot.common.Result;
import com.qiang.springboot.controller.dto.UserDTO;
import com.qiang.springboot.controller.dto.UserPasswordDTO;
import com.qiang.springboot.entity.User;
import com.qiang.springboot.exception.ServiceException;
import com.qiang.springboot.mapper.UserMapper;
import com.qiang.springboot.service.UserService;
import com.qiang.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XuQiang
 * @creat 2022-06-26 9:32
 */

@RestController
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private UserService userService;
    @Value("${files.upload.path}")
    private String filesUploadPath;

    @PostMapping("/login")  //@RequestBody将前端传过来的Json转换为Java对象
    public Result login(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDTO));
    }

    /**修改密码**/
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO){
        userPasswordDTO.setPassword(SecureUtil.md5(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(SecureUtil.md5(userPasswordDTO.getNewPassword()));
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    @GetMapping   //查询所有数据
    public List<User> findAll(){
        //return userMapper.findAll();
        return userService.list();
    }

    @PostMapping  //更新或者新增
    public boolean save(@RequestBody User user){
        if(user.getId() == null && user.getPassword() == null){
            user.setPassword(SecureUtil.md5("123"));
        }
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")   //两个id参数名要一致
    public boolean delete(@PathVariable Integer id){
        //return userMapper.deleteById(id);
        return userService.removeById(id);
    }

    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User one = userService.getOne(queryWrapper);
        return Result.success(one);
    }

    @PostMapping("/del/batch")   //批量删除
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        //return userMapper.deleteById(id);
        return userService.removeByIds(ids);
    }


    //@RequestParam 接收 ？pageNum=1&pageSize=10
//    @GetMapping("/page")   //分页查询接口
//    //(pageNum - 1) * pageSize = limit 第一个参数
//    public Map<String, Object> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
//        pageNum = (pageNum - 1) * pageSize;
//        int total = userMapper.selectTotal();
//        List<User> data = userMapper.selectPage(pageNum, pageSize);
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", data);
//        res.put("total", total);
//        return res;
//    }
    @GetMapping("/page")   //分页查询接口
    public IPage<User> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String email,
                                @RequestParam(defaultValue = "") String address){
        IPage<User> page = new Page<User>(pageNum, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if(!"".equals(username)){
            wrapper.like("username", username);
        }
        if(!"".equals(email)){

            wrapper.like("email", email);
        }
        if (!"".equals(address)) {

            wrapper.like("address", address);
        }

        //获取当前用户信息
        User currentUser = TokenUtils.getCurrentUser();
        System.out.println("获取当前用户：===========================" + currentUser.getNickname());

        wrapper.orderByDesc("id");     //倒序
        return userService.page(page, wrapper);

    }

    /**导出接口*/
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<User> list = userService.list();
        //ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname","昵称");
        writer.addHeaderAlias("email", "邮件");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("time", "创建时间");
        writer.addHeaderAlias("avatar", "头像");   //需要和 entity 中 User 对象一一对应

        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方式1. 通过JavaBean的方式读取Excel内的对象，但是要求表头必须是英文 //去掉Id列
        //List<User> list = reader.readAll(User.class);

        //方式2. 忽略表头的中文，直接读取表的内容； 要去掉Id列
        List<List<Object>> list = reader.read(1);
        ArrayList<User> users = CollUtil.newArrayList();
        for(List<Object> row : list){
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatar(row.get(6).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        return true;
    }
}
