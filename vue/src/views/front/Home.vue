<template>
    <div>
        <div style="margin: 10px 0">
            <el-carousel height="450px" :interval="4000">
                <el-carousel-item v-for="item in imgs" :key="item" >
                    <img :src="item" alt="" style="width: 100%">
                </el-carousel-item>
            </el-carousel>
        </div>


        <div style="margin: 10px 0">
            <el-row :gutter="10">
                <el-col :span="6" v-for="item in files" :key="item.id" style="margin-bottom: 10px">

                    <div style="border: 1px solid #ccc; padding-bottom: 10px">
                        <img :src="item.url" alt="" style="width: 100%">
                        <div style="color: #666; padding: 10px">{{ item.name }}</div>
                        <div style="padding: 10px"><el-button type="primary">展示</el-button></div>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>

<!--
    <div style="margin: 10px 0">
        <el-row :gutter="10">
            <el-col :span="6">
                <img :src="files[0].url" alt="" style="width: 100%;">
            </el-col>
            <el-col :span="6"></el-col>
            <el-col :span="6"></el-col>
            <el-col :span="6"></el-col>
        </el-row>
    </div>-->
</template>

<script>
    export default {
        name: "FrontHome",
        data(){
            return{
                imgs:[
                    'https://img2.baidu.com/it/u=3948441167,4109806023&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500',
                    'https://img1.baidu.com/it/u=2995749024,1292843217&fm=253&fmt=auto&app=138&f=JPEG?w=747&h=500',
                    'https://img1.baidu.com/it/u=3373870719,4215823717&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800',
                    'https://img0.baidu.com/it/u=1326168952,275718980&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800',
                    'https://img1.baidu.com/it/u=2742291686,1591289614&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500',
                    'https://img0.baidu.com/it/u=1003905336,2354703971&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500'
                ],
                files:[]
            }
        },
        created() {
            this.request.get("/echarts/file/front/all").then(res => {
                this.files = res.data.filter(v => v.type ==='png' || v.type === 'jpg' || v.type === 'webp')
            })
        }
    }
</script>

<style scoped>

</style>