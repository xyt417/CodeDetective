import $ from 'jquery'
import {ElMessage} from "element-plus";
import router from "@/router";

export default {
	state: {
		token: "",
		id: "",
		username: "",
		photo: "",
		is_login: false,
	},
	getter: {

	},
	mutations: {
		updateToken(state, token) {
			state.token = token;
		},
		updateUserInfo(state, data) {
			state.id = data.id;
			state.username = data.username;
			state.photo = data.photo;
			state.is_login = data.is_login;
		},
	},
	actions: {
		getUserInfo(context, data) {
			$.ajax({
				url: localStorage.getItem('Addr') + "/user/info",
				type: "get",
				headers: {
					Authorization: "Bearer " + context.state.token
				},
				success(resp) {
					if (resp.message === "success"){
						context.commit("updateUserInfo", {
							...resp,
							is_login: true
						})
						data.success(resp);
					} else {
						data.error(resp);
					}
				},
				error(err) {
					data.error(err);
					console.log("[ajax]getUserInfoErr: ", err);
				}
			})
		},
		getUserRepositories(context, data) {
			$.ajax({
				url: localStorage.getItem('Addr') + "/user/repositories",
				type: "get",
				headers: {
					Authorization: "Bearer " + context.state.token
				},
				success(resp) {
					if (resp.message === "success"){
						data.success(resp);
					} else {
						data.error(resp);
					}
				},
				error(err) {
					data.error(err);
					console.log("[ajax]getUserRepositoriesErr: ", err);
				}
			})
		},
		logout(context) {
			localStorage.removeItem('jwt_token');
			context.commit("updateUserInfo", {
				id: "",
				username: "",
				photo: "",
				is_login: false,
			})
			context.commit("updateToken", "");
			ElMessage.success("退出登录成功");
			router.push("/login") // 退出就强制跳转
		}

	},
	modules: {

	}
}