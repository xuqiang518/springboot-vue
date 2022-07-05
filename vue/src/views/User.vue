<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
            <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email" class="ml-5"></el-input>
            <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" v-model="address" class="ml-5"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">添加<i class="el-icon-circle-plus-outline" style="margin-left: 5px"></i></el-button>

            <el-popconfirm confirm-button-text='确定'
                           cancel-button-text='再想想'
                           icon="el-icon-info"
                           icon-color="red"
                           title="您确定删除这些数据吗？" style="margin-left: 5px" @confirm="delBatch">
                <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline" style="margin-left: 5px"></i></el-button>
            </el-popconfirm>
            <el-upload :action="'http://' +serverIp + ':8090/user/import'" style="display: inline-block"
                       :show-file-list="false" accept="xlsx" :on-success="handleImportSuccess">

                <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom" style="margin-left: 5px"></i></el-button>

            </el-upload>

                <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top" style="margin-left: 5px"></i></el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">

            <el-table-column type="selection" width="55" ></el-table-column>

            <el-table-column prop="id" label="ID" width="70"></el-table-column>
            <el-table-column prop="username" label="用户名" width="140"></el-table-column>
            <el-table-column prop="role" label="角色信息"></el-table-column>
            <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column prop="phone" label="电话"></el-table-column>
            <el-table-column prop="address" label="地址"></el-table-column>

            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit" style="margin-left: 5px"></i></el-button>
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
                    :page-sizes="[2, 5, 10, 15]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="用户信息" :visible.sync="outerVisible" width="30%">
            <el-form label-width="80px" size="small">

                <el-form-item label="用户名">
                    <el-input v-model="form.username"></el-input>
                </el-form-item>

                <el-form-item label="角色">
                    <el-select clearable v-model="form.role" placeholder="请选择" style="width: 100%;">
                        <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="昵称">
                    <el-input v-model="form.nickname"></el-input>
                </el-form-item>

                <el-form-item label="邮箱">
                    <el-input v-model="form.email"></el-input>
                </el-form-item>

                <el-form-item label="电话">
                    <el-input v-model="form.phone"></el-input>
                </el-form-item>

                <el-form-item label="地址">
                    <el-input v-model="form.address"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="outerVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {serverIp} from "../../public/config";

    export default {
        name: "User",
        data(){
            return{
                tableData: [],
                total: 0,
                pageNum: 1,   //当前页码
                pageSize: 5,   //每页显示多少数据
                username: "",
                email: "",
                address: "",
                outerVisible: false,
                form: {},
                multipleSelection: [],
                headerBg: 'headerBg',
                serverIp: serverIp,
                roles: []
            }
        },
        created() {
            //页面创建的时候执行
            this.load()
        },
        methods:{
            handleSizeChange(pageSize){   //每页显示多少条数据，即页面大小
                console.log(pageSize)
                this.pageSize = pageSize
                this.load()
            },
            handleCurrentChange(pageNum){  //当前页码
                console.log(pageNum)
                this.pageNum = pageNum
                this.load()
            },
            handleAdd(){
                this.outerVisible = true
                this.form = {}
            },
            load(){
                /*fetch("http://localhost:8090/user/page?pageNum="+ this.pageNum +"&pageSize=" + this.pageSize).then(res => res.json()).then(res => {
                  console.log(res)
                  this.tableData = res.data
                  this.total = res.total
                })*/
                this.request.get("/user/page", {
                    params:{
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        username: this.username,
                        email: this.email,
                        address: this.address
                    }
                }).then(res => {
                    console.log(res)
                    this.tableData = res.records
                    this.total = res.total
                })

                this.request.get("/role").then(res => {
                    this.roles = res.data
                })
            },
            save(){
                this.request.post("/user", this.form).then(res =>{
                    if(res){
                        this.$message.success("保存成功")
                        this.outerVisible = false
                        this.load()
                    }else {
                        this.$message.error("保存失败")
                    }

                })
            },
            handleEdit(row){
                this.form = row
                this.outerVisible = true
            },
            deleteById(id){
                this.request.delete("/user/" + id).then(res => {
                    if(res){
                        this.$message.success("删除成功")
                        this.load()
                    }else {
                        this.$message.error("删除失败")
                    }

                })
            },
            handleSelectionChange(val){  //多选框
                console.log(val)
                this.multipleSelection = val
            },
            delBatch(){
                let ids = this.multipleSelection.map(v => v.id)  //  [{}, {}, {}] => [1, 2, 3]
                this.request.post("/user/del/batch", ids).then(res => {
                    if(res){
                        this.$message.success("批量删除成功")
                        this.load()
                    }else {
                        this.$message.error("批量删除失败")
                    }
                })
            },
            reset(){
                this.username = ""
                this.email = ""
                this.address = ""
                this.load()
            },
            exp(){
                window.open(`http://${serverIp}:8090/user/export`)
            },
            handleImportSuccess(){
                this.$message.success("文件导入成功")
                this.load()
            }
        }
    }
</script>

<style scoped>
    .headerBg{
        background: #cccccc !important;
    }
</style>