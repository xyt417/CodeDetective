<template>

    <video src="@/assets/Pixabay.mp4" autoplay muted loop id="background_video"/>
    <div class="outer">

        <div class="shell">
            <div class="container register_container" id="register-container" :class="{'is-txl': isTxl}">
                <form action="" class="form" id="register-form">
                    <img src="../assets/Code_Detective.svg" class="headline" alt=""/>
                    <h2 class="form_title title title1">注册</h2>
                    <input type="text" class="form-input" placeholder="用户名" v-model="username">
                    <input type="password" class="form-input" placeholder="密码" v-model="password">
                    <input type="password" class="form-input" placeholder="确认密码" v-model="confirmedPassword">
                    <button class="form_button button submit" @click="register" @click.prevent>注册账号</button>
                </form>
            </div>

            <div class="container login_container" id="login-container" :class="{'is-txl': isTxl, 'is-z': isZ}">
                <form action="" class="form" id="login-form">
                    <img src="../assets/Code_Detective.svg" class="headline" alt=""/>
                    <h2 class="form_title title title1">登录</h2>
                    <input type="text" class="form-input" placeholder="用户名" v-model="username">
                    <input type="password" class="form-input" placeholder="密码" v-model="password">
                    <a href="" class="form_link">忘记密码?</a>
                    <button class="form_button button submit" @click="login" @click.prevent>登录账号</button>
                </form>
            </div>


            <div class="switch" id="switch-cnt" :class="{'is-gx': isGx, 'is-txr': isTxr}">
                <div class="switch_circle" :class="{'is-txr': isTxr}"></div>
                <div class="switch_circle switch_circle-t" :class="{'is-txr': isTxr}"></div>

                <div class="switch_container" id="switch-c1" :class="{'is-hidden': isHidden}">
                    <h2 class="switch_title title" style="letter-spacing: 0;">Hello!</h2>
                    <p class="switch_description description">登入已有账号</p>
                    <button class="switch_button button" @click="changeForm">登录账号</button>
                </div>

                <div class="switch_container" id="switch-c2" :class="{'is-hidden': !isHidden}">
                    <h2 class="switch_title title" style="letter-spacing: 0;">Welcome Back!</h2>
                    <p class="switch_description description">还没有账号？立刻注册吧</p>
                    <button class="switch_button button" @click="changeForm">注册账号</button>
                </div>
            </div>
        </div>
    </div>



</template>

<script setup>

import { ref } from "vue";
import $ from 'jquery'
import {ElMessage} from "element-plus";
import {useStore} from "vuex";
import router from "@/router";

const store = useStore();

if (store.state.user.is_login === true) router.push("/"); // 已登录跳转

const isGx = ref(false);
const isTxr = ref(false);
const isHidden = ref(false);
const isTxl = ref(false);
const isZ = ref(false);

let username = ref('');
let password = ref('');
let confirmedPassword = ref('');

const clearContent = () => {
	username.value = '';
	password.value = '';
	confirmedPassword.value = '';
}

const login = () => {
	$.ajax({
		url: localStorage.getItem('Addr') + "/login",
		type: 'post',
		data: {
			username: username.value,
			password: password.value,
		},
		success(resp) {
			if(resp.message === 'success') {
                store.commit("updateToken", resp.token);
				ElMessage.success("登录成功，已获取用户token");
				localStorage.setItem("jwt_token", resp.token);
                store.dispatch("getUserInfo", {
					success(resp){
                        ElMessage.success("获取用户信息成功");
                        console.log("userInfo: ", resp);
						router.push("/");
                    },
                    error() {
                        ElMessage.error("未获取到用户信息");
                    },
                })
			} else {
				ElMessage.error(resp.message);
			}
		},
		error(err) {
			console.log(err);
		}
	})
}

const register = () => {
    $.ajax({
        url: localStorage.getItem('Addr') + "/register",
        type: 'post',
        data: {
			username: username.value,
            password: password.value,
            confirmed_password: confirmedPassword.value,
        },
        success(resp) {
            if(resp.message === 'success') {
				ElMessage.success("注册成功！");
				changeForm();
            } else {
				ElMessage.error(resp.message);
            }
        },
        error(err) {
			console.log(err);
        }
    })
}

const changeForm = () => {
	clearContent();
	isGx.value = true;
	setTimeout(() => {
		isGx.value = false;
    }, 1250)
    isTxr.value = !isTxr.value;
	isHidden.value = !isHidden.value;
	isTxl.value = !isTxl.value;
	isZ.value = !isZ.value;
}


setTimeout(() => {
    changeForm();
}, 800)


</script>

<style scoped>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    /* 字体无法选中 */
    user-select: none;
}

.outer {
    width: 100vw;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #ecf0f3;
    color: #a0a5a8;
}

.headline {
    margin-top: 0;
    margin-bottom: 30px;
    width: 350px;
}

.title {
    font-size: 35px;
    font-weight: 700;
    line-height: 3;
    color: #181818;
    letter-spacing: 10px;
    text-align: center;
}

.title1 {
    font-size: 25px;
}

.shell {
    //border: solid 1px;
    position: relative;
    width: 1000px;
    min-width: 1000px;
    height: 600px;
    min-height: 600px;
    padding: 25px;
    background-color: #ecf0f3;
    box-shadow:  -30px -30px 50px #d1d9e6;
    border-radius: 12px;
    overflow: hidden;
    font-size: 15px;
    font-style: italic;
}

@media (max-width: 1200px) {
    .shell {
        transform: scale(0.7);
    }
}

@media (max-width: 1000px) {
    .shell {
        transform: scale(0.6);
    }
}

@media (max-width: 800px) {
    .shell {
        transform: scale(0.5);
    }
}

@media (max-width: 600px) {
    .shell {
        transform: scale(0.3);
    }
}

@media (max-width: 300px) {
    .shell {
        transform: scale(0.2);
    }
}

.container {
    //border: solid 1px;
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    width: 600px;
    height: 100%;
    background-color: #ecf0f3;
    transition: 1.25s;
}

.form {
    //border: solid 1px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
}

.form-input {
    width: 350px;
    height: 39px;
    margin: 5px 0;
    padding-left: 25px;
    font-size: 15px;
    letter-spacing: 1px;
    border-radius: 10px;
    border: none;
    outline: none;
    background-color: #ecf0f3 !important;
    transition: 0.25s ease;
    box-shadow: inset 2px 2px 3px #d1d9e6, inset -2px -2px 3px #f9f9f9;
}

.form-input:focus {
    box-shadow: inset 5px 5px 6px #d1d9e6;
}

.form_span {
    margin-top: 20px;
    margin-bottom: 12px;
}

.form_link {
    text-decoration: none;
    color: #181818;
    font-size: 15px;
    margin-top: 25px;
    border-bottom: solid 1px #a0a5a8;
    line-height: 1;
}

.description {
    font-size: 15px;
    letter-spacing: 1px;
    text-align: center;
    line-height: 1.6;
}

.button {
    cursor: pointer;
    width: 180px;
    height: 50px;
    border-radius: 25px;
    margin-top: 50px;
    font-weight: 700;
    font-size: 15px;
    letter-spacing: 1px;
    background-color: #4B70E2;
    color: #f9f9f9;
    box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;
    border: none;
    outline: none;
}

.button:hover {
    box-shadow: 6px 6px 10px #d1d9e6, -6px -6px 10px #f9f9f9;
    transform: scale(0.95);
    transition: 0.2s;
}

.button:active {
    box-shadow: 2px 2px 6px #d1d9e6, -2px -2px 6px #f9f9f9;
    transform: scale(0.92);
    transition: 0.1s;
}

.register_container {
    z-index: 100;
    left: calc(100% - 600px);
}

.login_container {
    z-index: 0;
    left: calc(100% - 600px);
}

.switch {
    //border: solid 1px;
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 400px;
    padding: 50px;
    z-index: 200;
    transition: 1.25s;
    background-color: #ecf0f3;
    overflow: hidden;
    box-shadow: 5px 5px 12px #d1d9e6, -5px -5px 12px #d1d9e6;
}

.switch_circle {
    position: absolute;
    width: 500px;
    height: 500px;
    border-radius: 50%;
    background-color: #ecf0f3;
    box-shadow: inset 8px 8px 12px #b8bec7, inset -8px -8px 12px #fff;
    bottom: -60%;
    left: -60%;
    transition: 1.25s;
}

.switch_circle-t {
    top: -30%;
    left: 60%;
    width: 300px;
    height: 300px;
    transition: 1.25s;
}

.switch_container {
    //border: solid 1px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: absolute;
    width: 400px;
    padding: 50px 55px;
    transition: 1.25s;
}

.is-txr {
    left: calc(100% - 400px);
    transition: 1.25s;
    transform-origin: left;
}

.is-txl {
    left: 0;
    transition: 1.25s;
}

.is-z {
    z-index: 200;
    transition: 1.25s;
}

.is-hidden {
    visibility: hidden;
    /* 只设置opacity只是变得透明，仍会触发如鼠标事件，设置为visibility: hidden 后不会响应事件 */
    opacity: 0;
    /* 移出文档流，使其不占用空间 */
    position: absolute;
    /* visibility 无过渡动画但延迟到0.5s后，opacity有过渡动画 持续0.5s */
    transition: 0.5s;
}

.is-gx {
    animation: is-gx 1.25s;
}

@keyframes is-gx {
    0%,
    100% {
        width: 400px;
    }

    /* 中间时刻变宽 */
    30%,
    35% {
        width: 500px;
    }

}

#background_video {
    position: absolute;
    left: 0;
    top: 0;
    width: 100vw;
    height: 100vh;
    object-fit: cover;
}
</style>