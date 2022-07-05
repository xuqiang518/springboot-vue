<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-upload :action="'http://' + serverIp + ':8090/file/upload'" :show-file-list="false"
                       :on-success="handleFileUploadSuccess" style="display: inline-block">
                <el-button type="primary" class="ml-5">上传文件</el-button>
            </el-upload>
            <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="再想想"
                           icon="el-icon-info" icon-color="red" title="您确定删除这些数据吗？" @confirm="delBatch">
                <el-button  type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">

            <el-table-column type="selection" width="55" ></el-table-column>

            <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
            <el-table-column prop="name" label="文件名" width="80" align="center"></el-table-column>
            <el-table-column prop="type" label="文件类型"  align="center"></el-table-column>
            <el-table-column prop="size" label="文件大小" align="center"></el-table-column>

            <el-table-column label="预览" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" @click="preview(scope.row.url)">预览</el-button>
                </template>
            </el-table-column>

            <el-table-column label="下载" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
                </template>
            </el-table-column>

            <el-table-column label="启用" align="center">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc"
                               @change="changeEnable(scope.row)">下载</el-switch>
                </template>
            </el-table-column>

            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-popconfirm title="确定删除吗？" style="margin-left: 5px" @confirm="deleteById(scope.row.id)">
                        <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
                    </el-popconfirm>

                </template>
            </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

    </div>
</template>

<script>
    import {serverIp} from "../../public/config";
    export default {
        name: "File",
        data(){
            return{
                serverIp: serverIp,
                tableData: [],
                name: '',
                multipleSelection:[],
                pageNum: 1,
                pageSize: 10,
                total: 0,
                headerBg: 'headerBg'
            }
        },
        created() {
            this.load()
        },
        methods:{
            load(){
                this.request.get("/file/page", {
                    params:{
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        name: this.name
                    }
                }).then(res => {
                    console.log(res)
                    this.tableData = res.data.records
                    this.total = res.data.total
                })
            },

            changeEnable(row){
                this.request.post("/file/update", row).then(res => {
                    if(res.code === '200'){
                        this.$message.success("操作成功")
                    }

                })
            },
            deleteById(id){
                this.request.delete("/file/" + id).then(res => {
                    if(res.code === '200'){
                        this.$message.success("删除成功")
                        this.load()
                    }else {
                        this.$message.error("删除失败")
                    }
                })
            },

            handleSelectionChange(val){    //多选框
                this.multipleSelection = val
            },
            delBatch(){   //批量删除
                let ids = this.multipleSelection.map(v => v.id)  //  [{}, {}, {}] => [1, 2, 3]
                this.request.post("/file/del/batch", ids).then(res => {
                    if(res.code === '200'){
                        this.$message.success("批量删除成功")
                        this.load()
                    }else {
                        this.$message.error("批量删除失败")
                    }
                })
            },

            reset(){   //重置
                this.name = ""
                this.load()
            },

            handleSizeChange(pageSize){   //获取页面大小(每个页面放几条数据)
                console.log(pageSize)
                this.pageSize = pageSize
                this.load()
            },

            handleCurrentChange(pageNum){   //获取当前页面
                console.log(pageNum)
                this.pageNum = pageNum
                this.load()
            },
            handleFileUploadSuccess(res){   //文件上传成功显示提示信息
                console.log(res)
                this.$message.success("上传成功")
                this.load()
            },
            download(url){  // 文件下载
                window.open(url)
            },
            preview(url){  //文件预览
                window.open('http://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa(url)))
            }

        }
    }
</script>

<style scoped>
    .headerBg{
        background: #cccccc !important;
    }
</style>