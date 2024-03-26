import {createRouter, createWebHistory} from 'vue-router'
import LoginOrRegisterView from "@/views/LoginOrRegisterView.vue";
import store from "@/store"
import {ElMessage} from "element-plus";
import HomeView from "@/views/HomeView.vue";
import UserProfileView from "@/views/UserProfileView.vue";
import MyRepositoriesView from "@/views/MyRepositoriesView.vue";
import AllRepositoriesView from "@/views/AllRepositoriesView.vue";
import RepoInnerView from "@/views/RepoInnerView.vue";
import ReportView from "@/views/ReportView.vue"
import CodeMatchingView from "@/views/CodeMatchingView.vue";

const routes = [
	{
		path: '/',
		name: 'HomeView',
		component: HomeView,
		meta: { // 名字随意
			requestAuth: true,
		}
	},
	{
		path: '/profile',
		name: 'UserProfileView',
		component: UserProfileView,
		meta: {
			requestAuth: true,
		}
	},
	{
		path: '/my-repo',
		name: 'MyRepositoriesView',
		component: MyRepositoriesView,
		meta: {
			requestAuth: true,
		}
	},
	{
		path: '/all-repo',
		name: 'AllRepositoriesView',
		component: AllRepositoriesView,
		meta: {
			requestAuth: true,
		}
	},
	{
		path: '/repo/:repoName/:detectionTime',
		name: 'RepoInnerView',
		component: RepoInnerView,
		meta: {
			requestAuth: true,
		}
	},
	{
		path: '/report/:repoName',
		name: 'ReportView',
		component: ReportView,
		meta: {
			requestAuth: true,
		}
	},
	{
		path: '/report/:repoName/code-matching',
		name: 'CodeMatchingView',
		component: CodeMatchingView,
		meta: {
			requestAuth: true,
		}
	},
	{
	path: '/login',
	name: 'LoginView', // 不能重复
	component: LoginOrRegisterView,
	meta: {
			requestAuth: false,
		},
	},
	{
	path: '/register',
	name: 'RegisterView',
	component: LoginOrRegisterView,
	meta: {
			requestAuth: false,
		},
	}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
	if(to.meta.requestAuth && store.state.user.is_login === false) {
		const jwt_token = localStorage.getItem("jwt_token");
		if(jwt_token){
			store.commit("updateToken", jwt_token);
			store.dispatch("getUserInfo", {
				success(resp){
					ElMessage.success("获取用户信息成功");
					console.log("userInfo: ", resp);
					if(to.path !== '/report') // 排除不需要跳转到主页的 url
						router.push("/");
					else next();
				},
				error() {
					ElMessage.error("未获取到用户信息");
					router.push("/login")
				},
			})
		} else {
			router.push("/login");
		}

	} else {
		next(); // 执行默认跳转
	}
})

export default router
