<template>
    <RoundedContainer class="flex flex-col print:!px-1">
        <div class="mb-2 mr-2 flex items-center space-x-5">
            <h3 class="flex-grow text-left text-lg font-bold">
                {{ props.submissionId }} 代码:
            </h3>
        </div>

        <ScrollableComponent class="flex-grow">
            <VueDraggableNext>
                <CodePanel
                        v-for="(file, index) in props.files"
                        ref="codePanels"
                        :key="index"
                        :file="file[1]"
                        :matches="matches.get(file[1].fileName) ? matches.get(file[1].fileName) : []"
                        @line-selected="(match) => $emit('lineSelected', match)"
                        class="mt-1 first:mt-0"
                />
            </VueDraggableNext>
        </ScrollableComponent>
    </RoundedContainer>
</template>

<script setup>
import {VueDraggableNext} from "vue-draggable-next";
import ScrollableComponent from "@/components/CodeMatchingComponent/ScrollableComponent.vue";
import CodePanel from "@/components/CodeMatchingComponent/CodePanel.vue";
import RoundedContainer from "@/components/CodeMatchingComponent/RoundedContainer.vue";
import {ref} from "vue";

// eslint-disable-next-line no-undef
const props = defineProps({
    files: Array,
    matches: Map,
    submissionId: String,
})

const codePanels = ref([]);

const scrollTo = (fileName, line) => {
    const fileIndex = Array.from(props.files).findIndex((f) => f[1].fileName === fileName)
    if (fileIndex !== -1) {
		codePanels.value[fileIndex].scrollTo(line);
    }
}

// eslint-disable-next-line no-undef
defineEmits(['lineSelected'])

// eslint-disable-next-line no-undef
defineExpose({
	scrollTo,
})
</script>