<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
<!--            <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email" class="ml-5"></el-input>-->
<!--            <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" v-model="address" class="ml-5"></el-input>-->
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
<!--            <el-upload :action="'http://' +serverIp + ':8090/user/import'" style="display: inline-block"-->
<!--                       :show-file-list="false" accept="xlsx" :on-success="handleImportSuccess">-->

<!--                <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom" style="margin-left: 5px"></i></el-button>-->

<!--            </el-upload>-->

<!--                <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top" style="margin-left: 5px"></i></el-button>-->
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">

            <el-table-column type="selection" width="55" ></el-table-column>

            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column prop="flag" label="唯一标识"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>

            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="info" @click="selectMenu(scope.row)">分配菜单<i class="el-icon-menu" style="margin-left: 5px"></i></el-button>
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

        <el-dialog title="角色信息" :visible.sync="outerVisible" width="30%">
            <el-form label-width="80px" size="small">

                <el-form-item label="名称">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>

                <el-form-item label="唯一标识">
                    <el-input v-model="form.flag"></el-input>
                </el-form-item>

                <el-form-item label="描述">
                    <el-input v-model="form.description"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="outerVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="菜单分配" :visible.sync="menuDialogVisible" width="30%">
            <el-tree
                    :data="menuData"
                    :props="props"
                    show-checkbox
                    node-key="id"
                    ref="tree"
                    :default-expanded-keys="expends"
                    :default-checked-keys="checks">
                <span class="custom-tree-node" slot-scope="{node, data}">
                    <span><i :class="data.icon"></i> {{data.name}}</span>
                </span>
            </el-tree>
            <div slot="footer" class="dialog-footer">
                <el-button @click="menuDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRoleMenu">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {serverIp} from "../../public/config";

    export default {
        name: "Role",
        data(){
            return{
                tableData: [],
                total: 0,
                pageNum: 1,   //当前页码
                pageSize: 5,   //每页显示多少数据
                name: "",
                outerVisible: false,
                menuDialogVisible: false,
                form: {},
                multipleSelection: [],
                headerBg: 'headerBg',
                serverIp: serverIp,
                menuData:[],
                props:{
                    label:'name'
                },
                expends:[],
                checks:[],
                roleId: 0,
                roleFlag:'',
                //ids:[]
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
                this.request.get("/role/page", {
                    params:{
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        name: this.name,
                    }
                }).then(res => {
                    console.log(res)
                    this.tableData = res.data.records
                    this.total = res.data.total
                })

                // this.request.get("/menu/ids").then(res => {
                //     this.ids = res.data
                // })
            },
            save(){
                this.request.post("/role", this.form).then(res =>{
                    if(res.code === '200'){
                        this.$message.success("保存成功")
                        this.outerVisible = false
                        this.load()
                    }else {
                        this.$message.error("保存失败")
                    }

                })
            },
            saveRoleMenu(){
                this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
                    console.log(res)
                    if(res.code === '200'){
                        this.$message.success("绑定成功")
                        this.menuDialogVisible = false
                        if(this.roleFlag === 'ROLE_ADMIN'){   //操作管理员角色的时候需要重新登录
                            this.$store.commit("logout")////////////////////////////////
                        }

                    }else {
                        this.$message.error(res.msg)
                    }
                })
            },

            handleEdit(row){
                this.form = JSON.parse(JSON.stringify(row))
                this.outerVisible = true
            },
            deleteById(id){
                this.request.delete("/role/" + id).then(res => {
                    if(res.code === '200'){
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
                this.request.post("/role/del/batch", ids).then(res => {
                    if(res.code === '200'){
                        this.$message.success("批量删除成功")
                        this.load()
                    }else {
                        this.$message.error("批量删除失败")
                    }
                })
            },


            reset(){
                this.name = ""
                this.load()
            },
            selectMenu(role){
                this.menuDialogVisible = true
                this.roleId = role.id
                this.roelFlag = role.flag
                //请求菜单数据
                this.request.get("/menu").then(res => {
                    console.log(res)
                    this.menuData = res.data

                    //把后台返回的数据处理成ID数组
                    this.expends = this.menuData.map(v => v.id)
                })

                this.request.get("/role/roleMenu/" + this.roleId).then(res => {

                    this.menuDialogVisible = true
                    this.checks = res.data

                    this.request.get("/menu/ids").then( r => {
                        const ids = r.data
                        ids.forEach(id => {
                            if(!this.checks.includes(id)){
                                this.$refs.tree.setChecked(id, false)
                            }
                        })

                    })

                })
            },
            // handleCheckChange(data, checked, indeterminate) {
            //     console.log(data, checked, indeterminate);
            // },
            // exp(){
            //     window.open(`http://${serverIp}:8090/user/export`)
            // },
            // handleImportSuccess(){
            //     this.$message.success("文件导入成功")
            //     this.load()
            // }
        }
    }
</script>

<style scoped>
    .headerBg{
        background: #cccccc !important;
    }
</style>