import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'

createApp(App).use(store).use(router).use(ElementPlus).mount('#app')

localStorage.setItem("Addr", 'http://192.168.31.157:3000') // 后端地址