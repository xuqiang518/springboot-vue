<template>
    <div class="wrapper">
        <div style="margin: 100px auto; background-color: #fff; width: 350px; padding: 20px; border-radius: 10px">
            <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注册</b></div>
            <el-form :model="user" :rules="rules" ref="userForm">

                <el-form-item prop="username">
                    <el-input placeholder="请输入账号" size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
                </el-form-item>

                <el-form-item prop="password">
                    <el-input placeholder="请输入密码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" v-model="user.password" show-password></el-input>
                </el-form-item>

                <el-form-item prop="confirmPassword">
                    <el-input placeholder="请确认密码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" v-model="user.confirmPassword" show-password></el-input>
                </el-form-item>

                <el-form-item style="margin: 10px 0; text-align: right">
                    <el-button type="primary" size="small" autocomplete="off" @click="reg">注册</el-button>
                    <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Register",
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
                    ],
                    confirmPassword:[
                        {required: true, message: '请确认密码', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ]
                }
            }
        },
        methods:{
            reg(){
                this.$refs['userForm'].validate((valid) => {
                    if(valid){  //表单校验合法
                        if(this.user.password !== this.user.confirmPassword){
                            this.$message.error("两次输入密码不一致");
                            return false;
                        }
                        this.request.post("/user/register", this.user).then(res => {
                            if(res.code === '200'){
                                localStorage.setItem("user", JSON.stringify(res.data)) // 存储用户信息到浏览器
                                this.$message.success("注册成功！")
                                this.$router.push("/login")
                            }else{
                                this.$message.error(res.msg)
                            }
                        })
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .wrapper {
        height: 100vh;
        background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
        overflow: hidden;
    }
</style>