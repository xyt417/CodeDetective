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
            <button class="button" data-text="Awesome">
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
                    <el-input v-model="createRepositoryForm.repositoryName" autocomplete="off" />
                </el-form-item>
                <el-form-item label="Zones">
                    <el-select placeholder="Please select a zone">
                        <el-option label="Zone No.1" value="shanghai" />
                        <el-option label="Zone No.2" value="beijing" />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer" v-loading="loading">
                    <el-button @click="uploadFilesDialogVisible = false" style="margin-right: 20px;">取消</el-button>
                    <el-button type="primary" @click="uploadFiles()">上传文件</el-button>
                </div>
            </template>
        </el-dialog>

    </div>

</template>

<script setup>

import {reactive, ref} from "vue";
import {useStore} from "vuex";
import $ from "jquery";
import {ElMessage} from "element-plus";

const store = useStore();

const createRepositoryForm = reactive({
    repositoryName: "",
    description: "",
})

const createRepositoryDialogVisible = ref(false);
const uploadFilesDialogVisible = ref(false);


const loading = ref(false);

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

const uploadFiles = () => {
	console.log("uploadFiles");
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