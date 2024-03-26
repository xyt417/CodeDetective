<template>
    <div class="repo_card" @click="toRepo">
        <h3 class="repo_card__title">{{props.repoInfo.repoName}}</h3>
        <p class="repo_card__content">描述：{{props.repoInfo.description}}</p>
        <p class="repo_card__content">所有者：{{props.repoInfo.ownerName}}</p>
        <p class="repo_card__content">最近检测时间：{{props.repoInfo.detectionTime ?  props.repoInfo.detectionTime: '无'}}</p>
        <div class="repo_card__date">创建时间：{{props.repoInfo.createTime}}</div>
        <div class="repo_card__arrow">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" height="20" width="20">
                <path fill="#fff" d="M13.4697 17.9697C13.1768 18.2626 13.1768 18.7374 13.4697 19.0303C13.7626 19.3232 14.2374 19.3232 14.5303 19.0303L20.3232 13.2374C21.0066 12.554 21.0066 11.446 20.3232 10.7626L14.5303 4.96967C14.2374 4.67678 13.7626 4.67678 13.4697 4.96967C13.1768 5.26256 13.1768 5.73744 13.4697 6.03033L18.6893 11.25H4C3.58579 11.25 3.25 11.5858 3.25 12C3.25 12.4142 3.58579 12.75 4 12.75H18.6893L13.4697 17.9697Z"></path>
            </svg>
        </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { useRouter } from "vue-router";

const router = useRouter();
const props = defineProps({
    repoInfo: Object,
})

const toRepo = () => {
    router.push({name: 'RepoInnerView', params: {repoName: props.repoInfo.repoName, detectionTime: props.repoInfo.detectionTime ? props.repoInfo.detectionTime : 'null'}});
}


</script>

<style>

.repo_card {
    --border-radius: 1rem;
    --primary-color: #7257fa;
    --secondary-color: #3c3852;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    text-align: left;
    width: 60vw;
    max-width: 500px;
    height: 180px;
    min-height: 130px;
    font-family: "微软雅黑",monospace;
    padding: 1.2rem;
    cursor: pointer;
    border-radius: var(--border-radius);
    background: #f3f3f3f3;
    box-shadow: 0 0 30px 10px rgb(256 256 256 / 10%);
    position: relative;
    margin-top: 60px;
    transition: transform 200ms ease, box-shadow 200ms ease, border 200ms ease; /* 更新过渡效果为200ms */
    letter-spacing: 1px;
}

.repo_card > * + * {
    margin-top: 0;
}

.repo_card .repo_card__content {
    color: var(--secondary-color);
    font-size: 0.9rem;
    margin: 0;
    /* 显示省略号 */
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.repo_card .repo_card__title {
    padding: 0;
    margin: 0;
    font-size: 1.5rem;
    font-weight: bold;
}

.repo_card .repo_card__date {
    color: #6e6b80;
    font-size: 0.9rem;
}

.repo_card .repo_card__arrow {
    position: absolute;
    background: var(--primary-color);
    padding: 0.5rem;
    border-top-left-radius: var(--border-radius);
    border-bottom-right-radius: var(--border-radius);
    bottom: 0;
    right: 0;
    transition: 0.2s;
    display: flex;
    justify-content: center;
    align-items: center;
}

.repo_card svg {
    transition: 0.2s;
}

/* hover */
.repo_card:hover .repo_card__title {
    color: var(--primary-color);
    text-decoration: underline;
}

/* 新增的:hover伪类 */
.repo_card:hover {
    transform: translateY(-6px); /* 向上移动 */
    box-shadow: 0 0 80px 30px rgb(256 256 256 / 10%);
}

</style>