<template>
    <video src="@/assets/Pixabay.mp4" autoplay muted loop id="background_video"/>
    <div class="outer">
        <img class="header" src="@/assets/欢迎使用源码相似性检测平台.svg" alt=""/>
        <a class="swipe" style="margin-top: 10vh;" @click="$router.push('/my-repo')">即刻开始
            <span class="container">
                <svg height="24" width="24" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="M0 0h24v24H0z" fill="none"></path>
                    <path d="M16.172 11l-5.364-5.364 1.414-1.414L20 12l-7.778 7.778-1.414-1.414L16.172 13H4v-2z"
                          fill="currentColor"></path>
                </svg>
            </span>
        </a>

    </div>

</template>

<script setup>


</script>

<style scoped>

.outer {
    width: 100vw;
    height: calc(100vh - 60px);
    display: flex;
    flex-direction: column;
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

.header {
    margin-top: 20vh;
    width: 50vw;
    z-index: 100;
}

.swipe {
    position: relative;
    background-color: #a3a6adaa;
    font-size: 20px;
    width: 30vw;
    height: 10vw;
    min-width: 160px;
    min-height: 50px;
    max-width: 260px;
    max-height: 80px;
    color: black;
    border-radius: 60px;
    display: flex;
    justify-content: center;
    align-items: center;
    text-decoration: none;
    letter-spacing: 2px;
    border-top: 0.5px solid rgba(0, 0, 0, 0.35);
    border-left: 0.5px solid rgba(0, 0, 0, 0.35);
    padding-left: 40px;
    cursor: pointer;
    transition: 0.35s ease;
}

.swipe:hover {
    padding-left: 0;
    padding-right: 40px;
    color: #ffff00;
}

.container {
    position: absolute;
    left: 5px;
    width: 50px;
    height: 50px;
    background: yellow;
    border-radius: 50%;
    transition: 0.35s ease;
    display: flex;
    justify-content: center;
    align-items: center;
    color: black;
}

.swipe:hover .container {
    left: calc(100% - 55px);
    color: black;
}


</style>