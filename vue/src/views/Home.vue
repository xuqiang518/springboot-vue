<template>
    <div>
        <el-row :gutter="10" style="margin-bottom: 40px">
            <el-col :span="6">
                <el-card style="color: #409EFF">
                    <div >用户总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        20
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color: #67C23A">
                    <div >销售总量</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        120000
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color: #F56C6C">
                    <div s>收益总额</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        ￥20000
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color: #409EFF">
                    <div >门店总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        200
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="12">
                <div id="main" style="width: 500px; height: 400px"></div>
            </el-col>
            <el-col :span="12">
                <div id="pie" style="width: 500px; height: 400px"></div>
            </el-col>
        </el-row>

    </div>
</template>

<script>
    import * as echarts from 'echarts'
    export default {
        name: "Home",
        data(){
            return{

            }
        },
        mounted() {   //页面元素渲染之后触发
            var chartDom = document.getElementById('main');
            var myChart = echarts.init(chartDom);
            var option;

            option = {
                xAxis: {
                    type: 'category',
                    data: ["第一季度","第二季度", "第三季度","第四季度"]
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        data: [],
                        type: 'line'
                    },
                    {
                        data: [],
                        type: 'bar'
                    }
                ]
            };

            this.request.get("/echarts/members").then(res => {

                //填空
                // option.xAxis.data = res.data.x
                option.series[0].data = res.data
                option.series[1].data = res.data
                myChart.setOption(option)

                pieOption.series[0].data = [
                    {name: "第一季度", value: res.data[0]},
                    {name: "第二季度", value: res.data[1]},
                    {name: "第三季度", value: res.data[2]},
                    {name: "第四季度", value: res.data[3]}
                ]
                pieChart.setOption(pieOption)
            })


            //饼图
            var pieOption = {
                title: {
                    text: '各季度注册人数统计',
                    subtext: '比例图',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                series: [
                    {
                        type: 'pie',
                        radius: '50%',
                        data: [],             //填空
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };
            var pieDom = document.getElementById('pie');
            var pieChart = echarts.init(pieDom);

        }
    }
</script>

<style scoped>

</style>