import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'

createApp(App).use(store).use(router).use(ElementPlus).mount('#app')

localStorage.setItem("Addr", 'http://127.0.0.1:3000') // 后端地址
localStorage.setItem("WsAddr", 'ws://127.0.0.1:3000') // 后端ws地址