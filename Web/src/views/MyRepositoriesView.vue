<template>
    <div class="outer">
        <el-affix offset="-35">
            <button class="swi-btn swi-btn-type" style="margin-top: 50px;" @click="createRepositoryDialogVisible = true">
                <span class="swi-btn-txt">新建仓库</span>
            </button>
        </el-affix>

        <RepoCard
                v-for="(repoInfo, index) in $store.state.user.repos"
                :key="index"
                :repoInfo="repoInfo"
        />



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
    </div>
</template>

<script setup>
import RepoCard from "@/components/RepoCard.vue";
import {reactive, ref} from "vue";
import $ from "jquery";
import {ElMessage} from "element-plus";
import {useStore} from "vuex";

const store = useStore();

const createRepositoryDialogVisible = ref(false);
const loading = ref(false);

const createRepositoryForm = reactive({
	repositoryName: "",
	description: "",
})

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
				store.dispatch("getUserRepositories", store.state.user.username);
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
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    min-height: calc(100vh - 60px);

    --s: 37px; /* control the size */
    --c: #0000, #282828 0.5deg 119.5deg, #0000 120deg;
    --g1: conic-gradient(from 60deg at 56.25% calc(425% / 6), var(--c));
    --g2: conic-gradient(from 180deg at 43.75% calc(425% / 6), var(--c));
    --g3: conic-gradient(from -60deg at 50% calc(175% / 12), var(--c));
    background: var(--g1), var(--g1) var(--s) calc(1.73 * var(--s)), var(--g2),
    var(--g2) var(--s) calc(1.73 * var(--s)), var(--g3) var(--s) 0,
    var(--g3) 0 calc(1.73 * var(--s)) #1e1e1e;
    background-size: calc(2 * var(--s)) calc(3.46 * var(--s));
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

</style>