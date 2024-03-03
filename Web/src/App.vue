<template>
    <NaviBar v-if="$store.state.user.is_login"/>
    <router-view></router-view>
</template>

<style>
html,
body,
#app {
    height: 100%;
    margin: 0;
    padding: 0;
} /* 防止由于margin导致内容超出视口 以及出现滚动条 */

.swi-btn {
    z-index: 100;
    height: 50px;
    width: 200px;
    position: relative;
    background-color: #eeeeee;
    font-size: 16px;
    cursor: pointer;
    border: 1px solid #252525;
    overflow: hidden;
    border-radius: 30px;
    color: #333;
    transition: all 0.2s ease-in-out;
}

.swi-btn-txt {
    z-index: 1;
    font-weight: 800;
    letter-spacing: 4px;
}

.swi-btn-type::after {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    transition: all 0.5s ease-in-out;
    background-color: rgb(103, 77, 250, 0.9);
    border-radius: 30px;
    visibility: hidden;
    height: 10px;
    width: 10px;
    z-index: -1;
}

.swi-btn:hover {
    box-shadow: 1px 1px 200px #252525;
    color: #f9f9f9;
    border: none;
}

.swi-btn-type:hover::after {
    visibility: visible;
    transform: scale(100) translateX(2px);
}
</style>

<script setup>
import 'element-plus/dist/index.css' // 引入才有样式
import NaviBar from "@/components/NaviBar.vue";
</script>