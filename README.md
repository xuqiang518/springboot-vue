# springboot-vue 前后端分离的后台管理系统

本项目是作为学习前后端分离的后台管理系统的练习项目，前端采用Vue+Element UI 框架搭建页面，后端采用了SpringBoot + Mybatis+Mysql技术栈。实现用户登录和注册

![https://github.com/xuqiang518/springboot-vue/blob/master/img/%E6%89%B9%E6%B3%A8%202022-07-05%20213601.jpg]()

注册时用户密码采用md5加密：

```java
public User register(UserDTO userDTO) {
    userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));  //用户密码加密
    User one = getUserInfo(userDTO);
    if(one == null){
        one = new User();
        BeanUtil.copyProperties(userDTO, one, true);
        one.setRole(RoleEnum.ROLE_USER.toString());  //默认一个角色为普通用户
        if(one.getNickname() == null){
            one.setNickname(one.getUsername());
        }
        save(one);
    }else {
        throw new ServiceException(Constants.CODE_600, "用户已经存在");
    }
    return one;
}
```

不同用户登录会进入不同界面，当用户为管理员时，管理员可以对系统中的用户进行管理，修改用户权限（角色），管理员也能进行菜单管理

动态路由添加vue页面：

```js
menus.forEach(item => {
  if(item.path){
    //当且仅当path不为空的时候才设置路由
    let itemMenu = {path:item.path.replace("/", ""), name: item.name, component:() => import('../views/' + item.pagePath + '.vue')}
    mangeRoutes.children.push(itemMenu)
```

嵌入markdown富文本编辑器，实后台对文章的管理功能。



普通用户登录界面



普通用户可在文章列表中对文章进行评论：



参考文献：https://blog.csdn.net/xqnode/article/details/124412328?spm=1001.2014.3001.5501

https://blog.csdn.net/weixin_45070175/article/details/118559272?ops_request_misc=&request_id=&biz_id=102&utm_term=jwt&utm_medium=distribute.pc_search_result.none-task-blog-2~blog~sobaiduweb~default-3-118559272.185^v2^control&spm=1018.2226.3001.4450