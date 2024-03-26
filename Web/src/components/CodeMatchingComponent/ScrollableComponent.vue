<template>
    <div class="overflow-y-auto print:overflow-y-visible">
        <div class="max-h-0 min-h-full print:max-h-none">
            <!-- ? 非 flex 时该矛盾遵循 max-height 而忽略 min-height 而 flex 又规定 min-height 优先于 max-height -->
            <slot></slot>
        </div>
    </div>
</template>

<script setup>

</script>

<style scoped>
    *::-webkit-scrollbar {
        @apply h-[6px] w-[6px] cursor-pointer;
    }
    *::-webkit-scrollbar-track {
        @apply rounded-full border-2 bg-scrollbar-backgorund-light dark:bg-scrollbar-backgorund-dark;
    }
    *::-webkit-scrollbar-thumb {
        @apply rounded-full border-2 bg-scrollbar-thumb-light dark:bg-scrollbar-thumb-dark;
    }
</style>