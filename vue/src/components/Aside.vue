<template>
    <el-menu :default-openeds="opens" style="min-height: 100%; overflow-x: hidden"
             background-color="rgb(48, 65, 86)" text-color="#fff"
             active-text-color="#ffd04b" :collapse-transition="false"
             :collapse="isCollapse"
             router>
        <div style="height:60px; line-height: 60px; text-align: center">
            <img src="../assets/tu.png" style="width: 25px; position: relative; top: 5px; margin-right: 5px">
            <b style="color: deepskyblue" v-show="logoTextShow">后台管理系统</b>
        </div>
        <div v-for="item in menus" :key="item.id">
            <div v-if="item.path">
                <el-menu-item :index="item.path">
                    <i :class="item.icon"></i>
                    <span slot="title">{{item.name}}</span>
                </el-menu-item>
            </div>
            <div v-else>
                <el-submenu :index="item.id + ''">
                    <template slot="title">
                        <i :class="item.icon"></i>
                        <span slot="title">{{item.name}}</span>
                    </template>
                    <div v-for="subItem in item.children" :key="subItem.id">
                        <el-menu-item :index="subItem.path">
                            <i :class="subItem.icon"></i>
                            <span slot="title">{{subItem.name}}</span>
                        </el-menu-item>
                    </div>
                </el-submenu>
            </div>
        </div>

        <!--<el-menu-item index="/">
            <template>
                <i class="el-icon-house"></i>
                <span slot="title">主页</span>
            </template>
        </el-menu-item>

        <el-submenu index="2">
            <template slot="title">
                <i class="el-icon-s-data"></i>
                <span slot="title">系统管理</span>
            </template>
            <el-menu-item index="/user">
                <i class="el-icon-s-custom"></i>
                <span slot="title">用户管理</span>
            </el-menu-item>
            <el-menu-item index="/file">
                <i class="el-icon-folder"></i>
                <span slot="title">文件管理</span>
            </el-menu-item>
            <el-menu-item index="/role">
                <i class="el-icon-user-solid"></i>
                <span slot="title">角色管理</span>
            </el-menu-item>
            <el-menu-item index="/menu">
                <i class="el-icon-menu"></i>
                <span slot="title">菜单管理</span>
            </el-menu-item>
        </el-submenu>-->


        <el-submenu index="3">
            <template slot="title">
                <i class="el-icon-setting"></i>
                <span slot="title">导航三</span>
            </template>
            <el-menu-item-group>
                <template slot="title">分组一</template>
                <el-menu-item index="/map">地图</el-menu-item>
                <el-menu-item index="3-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
                <el-menu-item index="3-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="3-4">
                <template slot="title">选项4</template>
                <el-menu-item index="3-4-1">选项4-1</el-menu-item>
            </el-submenu>
        </el-submenu>
    </el-menu>
</template>

<script>
    export default {
        name: "Aside",
        props:{
            isCollapse: Boolean,
            logoTextShow: Boolean,
        },
        data(){
            return{
                menus:localStorage.getItem("menus")? JSON.parse(localStorage.getItem("menus")):[],
                opens:localStorage.getItem("menus")? JSON.parse(localStorage.getItem("menus")).map(v => v.id + ''):[]
            }
        }
    }
</script>

<style>
    .el-menu--collapse span{
        visibility: hidden;
    }

</style>