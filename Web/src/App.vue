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

</style>

<script setup>
import 'element-plus/dist/index.css' // 引入才有样式
import NaviBar from "@/components/NaviBar.vue";
</script>