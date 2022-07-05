<template>
    <el-container style="min-height: 100vh;">
    <!-- 侧边栏-->
      <el-aside :width="sideWidth + 'px'" style="background-color: rgb(238, 241, 246);" >
        <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"/>
      </el-aside>

      <el-container>

        <!--头部-->
        <el-header style=" border-bottom: 1px solid #cccccc;" >

          <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :user="user"/>

        </el-header>


        <!--主体部分-->
        <el-main>
          <!-- 当前页面的子路由会在router-view 里面展示-->
          <router-view  @refreshUser="getUser" />

        </el-main>
      </el-container>

    </el-container>

</template>

<script>
  import Aside from "@/components/Aside";
  import Header from "@/components/Header";
export default {
  name: 'Manage',
  components: {
    Aside,
    Header
  },
  data(){

    return{
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      user: {}
    }
  },
  created() {
    //从后台获取最新的数据
    this.getUser()
  },

  methods:{

    collapse(){  //点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if(this.isCollapse){  //收缩
        this.sideWidth = 64;
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      }else {   //展开
        this.sideWidth = 200;
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    getUser(){
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if(username){
        //从后台获取用户信息
        this.request.get("/user/username/" + username).then(res => {
          //重新赋值后台的最新User数据
          this.user = res.data
        })
      }

    }

  }
  //sdd

}
</script>
<style>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }
.headerBg{
  background: #cccccc !important;
}
</style>