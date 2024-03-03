<template>
    <div class="outer">

        <el-card style="text-align: center" v-loading="loading">
            <template #header>
                <div class="card-header">
                    <span style="font-size: 20px;">检测项目列表</span>
                </div>
            </template>
            <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="submissionName" label="项目名称" width="180" />
                <el-table-column prop="submitter" label="提交者" width="180" />
                <el-table-column prop="lastModified" label="最近修改日期" />
            </el-table>
            <template #footer>
                <button class="swi-btn swi-btn-type" style="width: 150px; margin-right: 50px;" @click="uploadFilesDialogVisible = true">上传项目</button>
                <button class="swi-btn swi-btn-type green" style="width: 150px;" @click="send2detect">提交检测</button>
                <button class="swi-btn swi-btn-type green" style="width: 150px;" @click="seeFile">查看java文件</button>
            </template>
        </el-card>

        <el-dialog
                v-model="uploadFilesDialogVisible"
                title="上传项目文件"
                width="500"
                align-center
                class="home-view-dialog"
        >
            <el-form style="margin-top: 15px;">
                <el-upload
                        class="upload-demo"
                        drag
                        :action="uploadInfo.host"
                        :data="uploadInfo"
                        :before-upload="beforeUploadHandler"
                        :on-error="handleUploadError"
                        :on-success="handleUploadSuccess"
                        ref="upload"
                >
                    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                    <div class="el-upload__text">
                        拖放或<em>点击</em>上传待检测项目文件
                    </div>
                    <template #tip>
                        <div class="el-upload__tip">

                        </div>
                    </template>
                </el-upload>

            </el-form>
        </el-dialog>
    </div>

</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import $ from "jquery";
import {ElMessage} from "element-plus";
import {UploadFilled} from "@element-plus/icons-vue";

const store = useStore();
const route = useRoute();

const repoName = route.params.repoName;

const tableData = ref([]);
const loading = ref(false);
const upload = ref(null);

let socket = null;

onMounted (() => {
    getPolicy();
	getItems();
	connectWebsocket();
})

// Test
const seeFile = () => {
	let fileUrl = "https://code-detective-bucket.oss-cn-beijing.aliyuncs.com/Results/user1的仓库/files/submissions[123]/submissions/submission1/backend/src/main/java/com/kos/backend/BackendApplication.java"
	$.ajax({
		url: fileUrl,
		type: 'GET',
		success: function(data) {
			// data参数包含了文件内容
			console.log(data); // 这里的data就是你的.java文件内容
			// 在这里你可以处理文件内容，例如显示在页面上
		},
		error: function(xhr, status, error) {
			// 处理错误情况
			console.error("Error: " + error);
		}
	});
}

const send2detect = () => {
    socket.send(JSON.stringify({
        event: "detect-start",
        repo_name: repoName,
    }));
}

const connectWebsocket = () => {
	socket = new WebSocket(`${localStorage.getItem('WsAddr')}/websocket/${store.state.user.token}`);

	socket.onopen = () => {
		ElMessage.success("socket 已连接")
        store.commit("updateWebsocket", socket);
    }

	socket.onmessage = msg => {
		const data = JSON.parse(msg.data);
		console.log("websocket message:", data);
		if (data.event === "detect-start") {
			ElMessage.success("已提交检测")
        } else if (data.event === "detect-finished") {
            ElMessage.success("检测完成")
        } else if (data.event === "detect-error") {
            ElMessage.error("检测异常：" + data.message)
		}
    }

	socket.onclose = () => {
		ElMessage.warning("socket 已关闭")
    }
}

const getItems = () => {
	tableData.value = []; // clear
	loading.value = true;
	$.ajax({
		url: localStorage.getItem('Addr') + "/items/list",
		type: "post",
		headers: {
			Authorization: "Bearer " + store.state.user.token
		},
		data: {
			repo_name: repoName
		},
		success(resp) {
			console.log("[ajax]getRepoInfoResp: ", resp);
			for (let i = 0; i < resp.length; i++) {
				let item = {
					submissionName: resp[i].submissionName.split('[')[0],
					lastModified: resp[i].submissionName.split('[')[1].split(']')[0],
					submitter: resp[i].username,
				};
				tableData.value.push(item);
			}
			loading.value = false;
			ElMessage.success("已获取项目列表")
		},
		error(err) {
			console.log("[ajax]getRepoInfoErr: ", err);
			ElMessage.error("获取项目列表异常：" + err)
		}
	})
}


const handleUploadError = (err) => {
	ElMessage.error("上传文件异常：" + err);
}
const handleUploadSuccess = (resp) => {
	ElMessage.success("上传文件成功：" + resp);
	getItems();
}

const uploadInfo = reactive({
	OSSAccessKeyId: "",
	policy: "",
	signature: "",
	key: "",
	host: "",
})

const uploadFilesDialogVisible = ref(false);

const beforeUploadHandler = (file) => {
    if (file.size > 1024 * 1024 * 1024) {
        ElMessage.error("文件大小不要超过 1GB");
        return false;
    }
	const isZip = file.type === 'application/zip' || file.name.endsWith('.zip');
	if (!isZip) {
		ElMessage.error("暂时只支持 .zip 格式的文件");
		return false; // 返回 false 阻止文件上传
	}
    return true;

}

const getPolicy = () => {
    $.ajax({
        url: localStorage.getItem('Addr') + "/oss/policy",
        type: "post",
        headers: {
            Authorization: "Bearer " + store.state.user.token
        },
        data: {
            repoName: repoName,
        },
        success(resp) {
            uploadInfo.OSSAccessKeyId = resp.access_id;
            uploadInfo.policy = resp.policy;
            uploadInfo.signature = resp.signature;
            uploadInfo.key = resp.dir + "${filename}";
            uploadInfo.host = resp.host;
            console.log("[ajax]getOSSPolicy: ", resp);
			ElMessage.success("已获取文件上传 Policy");
        },
        error(err) {
            console.log("[ajax]getOSSPolicyErr: ", err);
            ElMessage.error("获取文件上传 Policy 异常：" + err);
        }
    })
}

</script>


<style scoped>
.outer {
    margin: 30px 150px;
    background-color: #f9f9f9;
}

.outer >>> .el-dialog {
    padding: 25px 60px;
    border-radius: 15px;
    text-align: center;
    justify-content: center;
    font-weight: 500;
}

.outer >>> .el-dialog .dialog-footer {
    text-align: center;
}

.swi-btn-type::after {
    background-color: #409eff;
}

.green::after {
    background-color: #67c23a;
}

</style>