<template>
    <div class="wrapper">
        <div style="margin: 200px auto; background-color: #fff; width: 350px; padding: 20px; border-radius: 10px">
            <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登录</b></div>
            <el-form :model="user" :rules="rules" ref="userForm">

                <el-form-item prop="username">
                    <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
                </el-form-item>

                <el-form-item prop="password">
                    <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" v-model="user.password" show-password></el-input>
                </el-form-item>
                <el-form-item style="margin: 10px 0; text-align: right">
                    <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
                    <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script >
    import {setRoutes} from "@/router";

    export default {
        name: "Login",
        data(){
          return{
              user:{},
              rules:{
                  username:[     //prop中username对应
                      {required: true, message: '请输入用户名', trigger:'blur'},
                      {min: 1, max: 8, message: '长度在 1 到 8 个字符', trigger: 'blur'}
                  ],
                  password:[
                      {required: true, message: '请输入密码', trigger: 'blur'},
                      {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                  ]
              }
          }
        },
        methods:{
            login(){
                this.$refs['userForm'].validate((valid) => {
                    if(valid){  //表单校验合法
                        this.request.post("/user/login", this.user).then(res => {
                            if(res.code === '200'){
                                localStorage.setItem("user", JSON.stringify(res.data)) // 存储用户信息到浏览器
                                localStorage.setItem("menus", JSON.stringify(res.data.menus)) // 存储用户菜单信息到浏览器

                                setRoutes()
                                this.$message.success("登录成功！")
                                if(res.data.role === 'ROLE_STU' || res.data.role === 'ROLE_USER' || res.data.role === null){
                                    this.$router.push("/front/home")
                                }else {
                                    this.$router.push("/")
                                }


                            }else{
                                this.$message.error(res.msg)
                            }
                        })
                    }
                })
            }
        },
        mounted() {
        }
    }
</script>

<style scoped>
    .wrapper {
        height: 100vh;
        background-image: linear-gradient(to bottom right, #9a4e5e, #6e81e3);
        overflow: hidden;
    }
</style>