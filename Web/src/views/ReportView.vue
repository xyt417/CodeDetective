<template>
    <div class="common-layout">
        <el-container>
            <el-header  class="report-container" style="font-size: 25px;">报告概览：{{route.params.repoName}}</el-header>
            <el-container class="overview-body">
                <el-aside class="report-container" style="width: 50%">
                    <div style="height: 90%">
                        检测项相似度分布
                        <DistributionDiagram v-if="overviewJson" :overview="overviewJson"/>
                    </div>
                </el-aside>
                <el-main  class="report-container" style="width: 50%; text-align: center;">
                    检测项详细报告
                    <el-table :data="tableData" class="report-table-common" style="width: 100%; margin-top: 30px">
                        <el-table-column prop="comparison" label="相似项目对" width="180" />
                        <el-table-column prop="similarity" label="相似度" width="180" >
                            <el-table-column label="最大相似度"></el-table-column>
                            <el-table-column label="平均相似度"></el-table-column>
                        </el-table-column>
                        <el-table-column prop="submitters" label="项目提交者" />
                    </el-table>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script setup>
import DistributionDiagram from "@/components/DistributionDiagram.vue";
import {useRoute} from "vue-router";
import {getFileContent} from "@/utils/getFileContentUtil";
import {ref} from "vue";
const route = useRoute();

const resultBaseUrl = "https://code-detective-bucket.oss-cn-beijing.aliyuncs.com/Results"
const reportBaseUrl = resultBaseUrl + "/" + route.params.repoName;

// 提取提交者的名字，例如从 "submissions[123]" 提取 "123"
function extractSubmitter(submission) {
	const matches = submission.match(/\[(.*?)]/);
	return matches ? matches[1] : null;
}

// 转换函数
function transformComparisons(data) {
	const transformed = [];

	data.forEach(item => {
			// 提取提交者名字
			const submitter1 = extractSubmitter(item.first_submission);
			const submitter2 = extractSubmitter(item.second_submission);

			// 构造新的对象格式
			const transformedComparison = {
				comparison: item.first_submission.split('[')[0] + " - " + item.second_submission.split('(')[0],
				similarity: item.similarity,
				submitters: submitter1 + ' - ' + submitter2,
			};

			// 添加到结果数组
			transformed.push(transformedComparison);
		});

	return transformed;
}

// 使用转换函数
const transformedArray = [];
console.log(transformedArray);

let overviewJson = ref(null);
let tableData = ref([])
getFileContent(reportBaseUrl + "/overview.json")
	.then(data => {
		// 在这里处理解决后的数据
		overviewJson.value = data;
		tableData.value  = transformComparisons(data.metrics[0].topComparisons);
	})
	.catch(error => {
		// 在这里处理错误
		console.error("获取文件内容时出错:", error);
	});

</script>

<style>
.overview-body {
    height: 75vh;
}

.report-container {
    margin: 20px;
    padding: 20px;
    height: 99%;
    border-radius: 20px;
    border: 1px solid #a0a5a8aa;
    background-color: #f9f9f9;
    text-align: center;
}

.report-table-common, .el-table thead.is-group th.el-table__cell {
    text-align: center;
    background-color: #f9f9f9;
}


</style>