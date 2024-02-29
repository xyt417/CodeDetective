<template>
    <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            :ellipsis="false"
            :router="true"
            style="background-color: #f9f9f9;"
    >
        <div style="width: 150px"></div>
        <el-menu-item index="/">
            <img
                    style="width: 150px;"
                    src="../assets/Code_Detective.svg"
                    alt="Element logo"
            />
        </el-menu-item>
        <el-menu-item index="/all-repo">{{"仓库列表"}}</el-menu-item>
        <div class="flex-grow" />
        <el-sub-menu index=""> <!-- index 属性必须设置，否则会有奇怪的warning -->
            <template #title>{{$store.state.user.username}}</template>
            <el-menu-item style="display: block; text-align: center" index="/profile">{{"主页"}}</el-menu-item>
            <el-menu-item style="display: block; text-align: center" index="/my-repo">{{"我的代码库"}}</el-menu-item>
            <el-menu-item style="display: block; text-align: center" index="/login" @click="logout">{{"退出登录"}}</el-menu-item>
        </el-sub-menu>
        <div style="width: 150px"></div>
    </el-menu>
</template>

<script setup>
import { ref } from 'vue'
import {useStore} from "vuex";

const store = useStore();
const activeIndex = ref('/')

const logout = () => {
	console.log("logout");
    store.dispatch('logout');
}
</script>

<style>
.flex-grow {
    flex-grow: 1;
}

/* 下拉菜单最小宽度 */
.el-menu--popup-bottom-start {
    min-width: 150px;
}
.el-menu-item {
    padding: 0 10px;
    font-size: 17px;
}
.el-sub-menu__title {
    font-size: 17px;
}
</style>