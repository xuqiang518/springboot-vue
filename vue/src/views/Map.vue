<template>
    <div class="home">
        <!--创建地图容器-->
        <div id="container" class="allmap"></div>
    </div>
</template>

<script>
    import {BMPGL} from "@/bmpgl.js"
    export default {
        name: "Map",
        data(){
            return{
                ak: dK4stggixXghOcG4hMmcVu63g2Alj6jG
            }
        },
        methods:{
            initMap(){
                BMPGL(this.ak).then((BMapGL) => {
                    let map = new BMapGL.Map("container")
                    let point = new BMapGL.Point(114.031761, 22.542826)
                    map.centerAndZoom(point, 19)
                    //开启鼠标滚轮缩放
                    map.enableScrollWheelZoom(true)
                    map.setHeading(64.5)
                    map.setTilt(73)
                }).catch((err) => {
                    console.log(err)
                })
            }
        }
    }
</script>

<style scoped>
    .allmap {
        width: 100%;
        height: 100vh;
        position: relative;
        z-index: 1;
    }
</style>