<template>
    <div class="outer">
        <RepoCard
                v-for="(repoInfo, index) in repos"
                :key="index"
                :repoInfo="repoInfo"
        />
    </div>
</template>

<script setup>
import RepoCard from "@/components/RepoCard.vue";
import {ref} from "vue";
import $ from "jquery";
import {useStore} from "vuex";
import {ElMessage} from "element-plus";

const store = useStore();
const repos = ref([]);


$.ajax({
	url: localStorage.getItem('Addr') + "/repository/list/" + store.state.user.username,
	type: "get",
	headers: {
		Authorization: "Bearer " + store.state.user.token
	},
	success(resp) {
        console.log("[ajax]getReposListResp: ", resp);
        repos.value = resp;
		ElMessage.success("共找到 " + resp.length + " 个代码仓库");
	},
	error(err) {
		console.log("[ajax]getReposListErr: ", err);
	}
})

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

</style>