<template>
    <div class="absolute bottom-0 left-0 right-0 top-16 flex flex-col bg-backgorund-dark">
        <div class="relative left-0 right-0 top-0 flex space-x-5 p-5 pb-0 print:p-0"> <!-- top -->
            <RoundedContainer  class="flex-grow overflow-hidden print:min-h-fit print:overflow-visible bg-gruvbox-dark-hard">
                <h2 class="font-bold text-2xl text-center">
                    相似代码片段匹配: &nbsp;&nbsp; {{ comparisonFile }}
                </h2>
            </RoundedContainer>
        </div>

        <div class="relative bottom-0 left-0 flex flex-grow justify-between space-x-5 px-5 pb-7 pt-5
            print:space-x-1 print:p-0 print:!pt-2"> <!-- bottom -->
                <FilesContainer
                        class="max-h-0 min-h-full flex-1 overflow-hidden print:max-h-none print:overflow-y-visible
                            bg-gruvbox-dark-hard"
                        v-if="matchesOfFirst"
                        ref="fileContainer1"
                        :submissionId="firstSubmissionId"
                        :files = filesOfFirstSubmission
                        :matches = matchesOfFirst
                        @line-selected="showMatchInSecond"
                >
                </FilesContainer>
                <FilesContainer
                        class="max-h-0 min-h-full flex-1 overflow-hidden print:max-h-none print:overflow-y-visible bg-gruvbox-dark-hard"
                        v-if="matchesOfSecond"
                        ref="fileContainer2"
                        :submissionId="secondSubmissionId"
                        :files = filesOfSecondSubmission
                        :matches = matchesOfSecond
                        @line-selected="showMatchInFirst"
                >
                </FilesContainer>
        </div>

    </div>
</template>

<script setup>

import {useRoute} from "vue-router";
import RoundedContainer from "@/components/CodeMatchingComponent/RoundedContainer.vue";
import FilesContainer from "@/components/CodeMatchingComponent/FilesContainer.vue";
import {getFileContent} from "@/utils/GetResultUtil";
import {ref} from "vue";
import {Extractor} from "@/utils/Extractor";

const route = useRoute();
const comparisonFile = decodeURIComponent(route.query.comparison);
const reportDirBaseUrl = decodeURI(route.query.reportDirBaseUrl);

import {useStore} from "vuex";
const store = useStore();
const firstSubmissionId = comparisonFile.split('-')[0];
const secondSubmissionId = comparisonFile.split('-')[1];
const filesOfFirstSubmission = Array.from(store.state.result.submissions[firstSubmissionId]).map(file => {
	file[1].fileName = file[1].fileName.replace(/Results\/.*?\/files\//, '') // 去掉 prefix 从 submissionId 开始
    return file;
});
const filesOfSecondSubmission = Array.from(store.state.result.submissions[secondSubmissionId]).map(file => {
	file[1].fileName = file[1].fileName.replace(/Results\/.*?\/files\//, '')
    return file;
});

const comparisons = ref(null);
const matchesOfFirst = ref(null);
const matchesOfSecond = ref(null);

getFileContent(reportDirBaseUrl + comparisonFile + ".json").then(data => {
	comparisons.value = Extractor.extractComparison(data);
	console.log("comparisons: ", comparisons.value);
	matchesOfFirst.value = Extractor.groupMatches(comparisons.value.matches.matchesOrderedByToken, 1);
	matchesOfSecond.value = Extractor.groupMatches(comparisons.value.matches.matchesOrderedByToken, 2);
}).catch(error => {
	console.log("URL: ", reportDirBaseUrl + comparisonFile + ".json")
    console.log("getComparisonFile Error: ", error)
})

const fileContainer1 = ref(null);
const fileContainer2 = ref(null);

const showMatchInFirst = (match) => {
	fileContainer1.value.scrollTo(match.firstFile, match.startInFirst);
}
const showMatchInSecond = (match) => {
	fileContainer2.value.scrollTo(match.secondFile, match.startInSecond);
}
// const showMatch = (match) => {
// 	showMatchInFirst(match);
// 	showMatchInSecond(match);
// }

</script>

<style scoped>

</style>