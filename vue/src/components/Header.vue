<template>
    <div style="font-size: 12px; line-height: 60px; display: flex">
        <div style="flex: 1; font-size: 20px">
            <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>

            <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px" >
                <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
                <el-breadcrumb-item>{{currentPathName}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-dropdown style="width: 150px; cursor: pointer; text-align: right" >
            <div style="display: inline-block">
                <img :src="user.avatar"
                     alt="" style="width: 35px; border-radius: 50%; position:relative; top: 10px; right: 5px">
                <span>{{user.nickname}}</span>
                <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>

            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                    <span style="text-decoration: none" @click="$router.push('/password')">修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item>
                    <span style="text-decoration: none" @click="$router.push('/person')">个人信息</span>
                </el-dropdown-item>
                <el-dropdown-item >
                    <span style="text-decoration: none" @click="logout">退出</span>
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
    export default {
        name: "Header",
        props:{
            collapseBtnClass: String,
            user: Object

        },
        computed:{
            currentPathName(){
                return this.$store.state.currentPathName;
            }
        },
        watch:{
            currentPathName(newVal, oldVal){

            }
        },
        // watch:{
        //     '$route':function () {
        //         this.currentPathName = localStorage.getItem("currentPathName")   //取出route里面设置的路由信息
        //     }
        // },

        data(){
            return{
                path: [],
                //currentPathName: '',
            }
        },
        methods:{
            collapse(){
                this.$emit("asideCollapse")
            },
            logout(){
                // this.$router.push("/login")
                // localStorage.removeItem("user")
                this.$store.commit("logout")
                this.$message.success("退出成功")
            }
        }
    }
</script>

<style scoped>

</style>