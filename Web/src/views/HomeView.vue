<template>
    <video src="@/assets/water.mp4" autoplay muted loop id="background_video"/>
    <div class="outer">
        <div class="container">
            <button class="button" data-text="Awesome" @click="createRepositoryDialogVisible = true">
                <span class="actual-text">&nbsp;Create&nbsp;New&nbsp;Repository&nbsp;</span>
                <span aria-hidden="true" class="hover-text">&nbsp;Create&nbsp;New&nbsp;Repository&nbsp;</span>
            </button>
        </div>
        <div class="container">
            <button class="button" data-text="Awesome" @click="uploadFilesDialogVisible = true">
                <span class="actual-text">&nbsp;Upload&nbsp;Files&nbsp;</span>
                <span aria-hidden="true" class="hover-text">&nbsp;Upload&nbsp;Files&nbsp;</span>
            </button>
        </div>
        <div class="container">
            <button class="button" data-text="Awesome" @click="$router.push('/my-repo')">
                <span class="actual-text">&nbsp;Code&nbsp;Detection&nbsp;</span>
                <span aria-hidden="true" class="hover-text">&nbsp;Code&nbsp;Detection&nbsp;</span>
            </button>
        </div>


        <el-dialog
                v-model="createRepositoryDialogVisible"
                title="新建代码库"
                width="500"
                align-center
                class="home-view-dialog"
        >
            <el-form :model="createRepositoryForm" style="margin-top: 15px;">
                <el-form-item label="新建代码库名称: " >
                    <el-input v-model="createRepositoryForm.repositoryName" autocomplete="off" />
                </el-form-item>
                <el-form-item label="代码库描述: " >
                    <el-input type="textarea" style="padding-left: 28px;" :autosize="{minRows: 3, maxRows: 5}" v-model="createRepositoryForm.description" autocomplete="off" />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer" v-loading="loading">
                    <el-button @click="createRepositoryDialogVisible = false" style="margin-right: 20px;">取消</el-button>
                    <el-button type="primary" @click="createRepository(createRepositoryForm.repositoryName, createRepositoryForm.description)">创建</el-button>
                </div>
            </template>
        </el-dialog>

        <el-dialog
                v-model="uploadFilesDialogVisible"
                title="上传项目文件"
                width="500"
                align-center
                class="home-view-dialog"
        >
            <el-form :model="createRepositoryForm" style="margin-top: 15px;">
                <el-form-item label="选择上传代码库: " >
                    <el-select v-model="selectedRepo" placeholder="选择上传代码库">
                        <el-option v-for="(repo, index) in $store.state.user.repos" :label="repo.repoName" :key="index" :value="repo.repoName"/>
                    </el-select>
                </el-form-item>
                <el-upload
                        class="upload-demo"
                        drag
                        :action="uploadInfo.host"
                        :data="uploadInfo"
                        :before-upload="getPolicy"
                        :on-error="handleUploadError"
                        :on-success="handleUploadSuccess"
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

import {reactive, ref} from "vue";
import {useStore} from "vuex";
import $ from "jquery";
import {ElMessage} from "element-plus";
import { UploadFilled } from '@element-plus/icons-vue'

const store = useStore();

const handleUploadError = (err) => {
	ElMessage.error("上传文件异常：" + err);
}
const handleUploadSuccess = (resp) => {
    ElMessage.success("上传文件成功：" + resp);
}


const uploadInfo = reactive({
    OSSAccessKeyId: "",
    policy: "",
    signature: "",
    key: "",
    host: "",
})

const getPolicy = (file) => {
	if (selectedRepo.value === undefined) {
        ElMessage.error("请选择上传代码库");
        return false;
    }
	if (file.size > 1024 * 1024 * 1024) {
        ElMessage.error("文件大小超过 1GB");
        return false;
    }
	$.ajax({
		url: localStorage.getItem('Addr') + "/oss/policy",
		type: "post",
		headers: {
			Authorization: "Bearer " + store.state.user.token
		},
        data: {
            repoName: selectedRepo.value
        },
		success(resp) {
            uploadInfo.OSSAccessKeyId = resp.access_id;
			uploadInfo.policy = resp.policy;
			uploadInfo.signature = resp.signature;
			uploadInfo.key = resp.dir + "${filename}";
			uploadInfo.host = resp.host;
			console.log("[ajax]getOSSPolicy: ", resp);
			console.log("[uploadInfo]: ", uploadInfo);
		},
		error(err) {
			console.log("[ajax]getOSSPolicyErr: ", err);
			ElMessage.error("获取上传 Policy 异常：" + err);
		}
	})
}

const createRepositoryForm = reactive({
    repositoryName: "",
    description: "",
})

const createRepositoryDialogVisible = ref(false);
const uploadFilesDialogVisible = ref(false);
const loading = ref(false);

const selectedRepo = ref();


const createRepository = (name, description) => {
	loading.value = true;
	$.ajax({
        url: localStorage.getItem('Addr') + "/repository/create/" + name + "/" + description,
		type: "get",
		headers: {
			Authorization: "Bearer " + store.state.user.token,
		},
		success(resp) {
			if (resp.message === "success"){
				ElMessage.success("已新建代码仓库");
				createRepositoryDialogVisible.value = false;
			} else {
				ElMessage.error(resp.message);
			}
			loading.value = false;
		},
		error(err) {
			console.log("[ajax]createRepositoryErr: ", err);
			loading.value = false;
		}
    })
}

</script>

<style scoped>
.outer {
    width: 100vw;
    height: calc(100vh - 60px);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: #ecf0f3;
    color: #a0a5a8;
}
#background_video {
    position: absolute;
    left: 0;
    top: 60px;
    width: 100vw;
    height: calc(100vh - 60px);
    object-fit: cover;
}

/* === removing default button style ===*/
.button {
    margin: 0;
    height: auto;
    background: transparent;
    padding: 0;
    border: none;
    cursor: pointer;
}

/* button styling */
.button {
    --border-right: 6px;
    --text-stroke-color: rgba(255,255,255,0.6);
    --animation-color: #37FF8B;
    --fs-size: 2em;
    letter-spacing: 3px;
    text-decoration: none;
    font-size: var(--fs-size);
    font-family: "Arial",serif;
    position: relative;
    text-transform: uppercase;
    color: transparent;
    -webkit-text-stroke: 1px var(--text-stroke-color);
}
/* this is the text, when you hover on button */
.hover-text {
    position: absolute;
    box-sizing: border-box;
    content: attr(data-text);
    color: var(--animation-color);
    width: 0;
    inset: 0;
    border-right: var(--border-right) solid var(--animation-color);
    overflow: hidden;
    transition: 0.5s;
    -webkit-text-stroke: 1px var(--animation-color);
}
/* hover */
.button:hover .hover-text {
    width: 100%;
    filter: drop-shadow(0 0 23px var(--animation-color))
}

.container {
    height: 150px;
}

.outer >>> .el-dialog {
    padding: 25px 60px;
    border-radius: 10px;
    text-align: center;
    justify-content: center;
    font-weight: 500;
}

.outer >>> .el-dialog .dialog-footer {
    text-align: center;
}
</style>