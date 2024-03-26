<template>
    <InteractableComponent  class="mx-2 !shadow print:!mx-0 print:!border-0 print:!p-0 bg-gruvbox-bg2">
        <div @click="collapsed = !collapsed" class="flex px-2 font-bold print:whitespace-pre-wrap">
            <span class="flex-1">{{Extractor.extractFileDisplayName(props.file)}}</span>
        </div>

        <div class="mx-1 overflow-x-auto print:!mx-0 print:overflow-x-hidden">
            <div class="print:display-initial w-fit min-w-full !text-xs" :class="{ hidden: collapsed }">
                <table  class="w-full print:table-auto">
                    <tr
                        class="w-full cursor-default"
                        v-for="(line, index) in codeLines"
                        :key="index"
                        :class="{'cursor-pointer': line.match !== null}"
                        @click="lineSelected(index)"
                    >
                        <!-- line number -->
                        <td class="float-right pr-3">
                            {{ index + 1 }}
                        </td>
                        <!-- code line -->
                        <td
                            class="print-excact w-full"
                            :style="{
                                  background: line.match !== null ?
                                  getMatchColor(line.match.colorIndex, 0.1) :
                                  'hsla(0, 0%, 0%, 0)'
                            }"
                        >
                             <pre
                                     v-html="line.line"
                                     class="code-font print-excact break-child !bg-transparent print:whitespace-pre-wrap"
                                     ref="lineRefs"
                             ></pre>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </InteractableComponent>

</template>

<script setup>
import InteractableComponent from "@/components/CodeMatchingComponent/InteractableComponent.vue";
import {Extractor} from "@/utils/Extractor";
import {highlight} from "@/utils/CodeHighLighter";
import {computed, nextTick, ref} from "vue";
import {getMatchColor} from "@/utils/ColorUtils";

// eslint-disable-next-line no-undef
const props = defineProps({
    file: Object,
    matches: Map,
});

const collapsed = ref(true);

const codeLines = computed(() =>
	highlight(props.file.content).map((line, index) => {
		let match = props.matches.find((m) => Extractor.getMatchStart(m) <= index + 1 &&
            index + 1 <= Extractor.getMatchEnd(m))
		return {
			line,
            match: match === undefined ? null : match
        }
    })
)

const lineRefs = ref([]);

const scrollTo = (lineNumber) => {
	collapsed.value = false;
	nextTick(function() {
        lineRefs.value[lineNumber - 1].scrollIntoView({ block: 'start', behavior: 'smooth' })
    })
}

// eslint-disable-next-line no-undef
const emit = defineEmits(['lineSelected'])
const lineSelected = (lineIndex) => {
	console.log("LineSelected: ", lineIndex, "  Match: ", codeLines.value[lineIndex].match)
	if(codeLines.value[lineIndex].match !== null) {
		emit('lineSelected', codeLines.value[lineIndex].match);
    }
}

// eslint-disable-next-line no-undef
defineExpose({
    scrollTo,
})

</script>

<style scoped>
.code-font {
    font-family: 'JetBrains Mono', monospace !important;
}

@media print {
    .break-child *,
    .break-child {
        word-break: break-word;
    }
}

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