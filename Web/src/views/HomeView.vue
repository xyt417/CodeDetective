<template>
    <video src="@/assets/water.mp4" autoplay muted loop id="background_video"/>
    <div class="outer">
        <div class="container">
            <button class="button" data-text="Awesome">
                <span class="actual-text">&nbsp;Create&nbsp;New&nbsp;Repository&nbsp;</span>
                <span aria-hidden="true" class="hover-text">&nbsp;Create&nbsp;New&nbsp;Repository&nbsp;</span>
            </button>
        </div>
        <div class="container">
            <button class="button" data-text="Awesome">
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
</style>