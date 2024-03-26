<template>
    <div class="common-layout">
        <el-container>
            <el-header class="report-container" style="font-size: 25px;">报告概览 [代码库: {{ route.params.repoName }}]
            </el-header>
            <el-container class="overview-body">
                <el-aside class="report-container" style="width: 35%">
                    <div style="height: 90%">
                        仓库中各项目间相似度分布情况
                        <DistributionDiagram v-if="overviewJson" :overview="overviewJson"/>
                    </div>
                </el-aside>
                <el-main class="report-container" style="width: 65%; text-align: center;">
                    项目比对结果详细报告

                    <!-- 仓库中的所有提交文件都已经加载完毕 -->
                    <el-table
                            :data="topComparisons"
                            class="report-table-common"
                            style="width: 100%;
                            margin-top: 30px"
                            @row-click="rowClickHandler"
                            v-if="$store.state.result.finishedNum === submissionIdList.length"
                    >
                        <el-table-column label="比对项目对">
                            <el-table-column label="项目1" prop="firstSubmissionName" width="160"></el-table-column>
                            <el-table-column label="项目2" prop="secondSubmissionName" width="160"></el-table-column>
                        </el-table-column>
                        <el-table-column label="相似度" width="120">
                            <el-table-column label="平均相似度" prop="similarity.AVG"></el-table-column>
                            <el-table-column label="最大相似度" prop="similarity.MAX" ></el-table-column>
                        </el-table-column>
                        <el-table-column label="提交者" width="300">
                            <el-table-column label="项目1提交者" prop="firstSubmitter" ></el-table-column>
                            <el-table-column label="项目2提交者" prop="secondSubmitter"></el-table-column>
                        </el-table-column>
                    </el-table>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script setup>
import DistributionDiagram from "@/components/DistributionDiagram.vue";
import {useRoute} from "vue-router";
import {getFileContent, resultsDirBaseUrl, storeSubmissionFiles} from "@/utils/GetResultUtil";
import {ref} from "vue";
import router from "@/router";
import {Extractor} from "@/utils/Extractor";
import {useStore} from "vuex";

const route = useRoute();
const store = useStore();

const reportDirBaseUrl = resultsDirBaseUrl + route.params.repoName + "/";

let overviewJson = ref(null);
const topComparisons = ref([]);
const submissionIds2ComparisonFileName = ref(null);
const submissionIdList = ref([]);
getFileContent(reportDirBaseUrl + "overview.json").then(data => {
        // 从overview.json中提取submissionId、submissionName(不包括提交者的名字)、similarity、submitter等信息
		overviewJson.value = data;
		submissionIdList.value = Extractor.extractSubmissionIdList(data);
		console.log("submissionIdList", submissionIdList);
		topComparisons.value = Extractor.extractTopComparisonsFromMetrics(data.metrics);
		console.log("topComparisons", topComparisons);
		submissionIds2ComparisonFileName.value = Extractor.extractSubmissionIds2ConparisonFileName(data);
		console.log("submissionIds2ComparisonFileName", submissionIds2ComparisonFileName)

        // 拉取并储存仓库中的所有提交文件到store.state.result.submissions[] 中
        store.state.result.finishedNum = 0; // 文件加载进度清空
        for(let i = 0; i < submissionIdList.value.length; i++) {
            storeSubmissionFiles(store, route.params.repoName, submissionIdList.value[i]);
        }
	}).catch(error => {
		// 在这里处理错误
		console.error("获取文件内容时出错:", error);
	});

const rowClickHandler = (row) => {
	const _repoName = route.params.repoName;
	console.log(store.state.result.submissions);
	router.push({
        name: "CodeMatchingView",
        params: {repoName: _repoName},
        query: {comparison: `${row.firstSubmissionId}-${row.secondSubmissionId}`, reportDirBaseUrl}
	});
}

</script>

<style>
.overview-body {
    height: 75vh;
}

.report-container {
    margin: 10px;
    padding: 20px;
    height: 99%;
    border-radius: 20px;
    border: 1px solid #a0a5a8aa;
    background-color: #f9f9f9;
    text-align: center;
}

.common-layout .report-table-common, .el-table thead.is-group th.el-table__cell {
    text-align: center;
    background-color: #f9f9f9;
}
.common-layout .el-table--enable-row-transition .el-table__body td.el-table__cell {
    background-color: #f9f9f9;
    cursor: pointer;
}


</style>