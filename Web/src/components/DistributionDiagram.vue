<template>
    <Bar
        :options="chartOptions"
        :data="chartData"
    />

</template>

<script setup>
import {defineProps, ref} from 'vue';
import { Chart, registerables } from 'chart.js';
import { Bar } from 'vue-chartjs';
Chart.register(...registerables);

const props = defineProps({
	overview: {
		required: true
	},
})

const distribution = ref(props.overview.metrics[0].distribution);
console.log("distribution", distribution.value)

const maxBarLength = Math.max(...distribution.value); // 使用扩展运算符来从数据集中获取最大值
const additionalSpace =  0.1;

const labels = [
	'91-100%',
	'81-90%',
	'71-80%',
	'61-70%',
	'51-60%',
	'41-50%',
	'31-40%',
	'21-30%',
	'11-20%',
	'0-10%'
]
const chartData = {
	labels: labels,
    type: 'line',
	datasets: [
		{
			label: "相似项目对数",
			backgroundColor: 'rgba(190, 22, 35, 0.8)',
            borderColor: 'rgb(127, 15, 25)',
            borderWidth: 1,
			data: distribution.value,
		},
	],
};

const chartOptions = {
	indexAxis: 'y',
    plugins: {legend: {display: false}},
	responsive: true,
	maintainAspectRatio: false,
	scales: {
		x: {
			beginAtZero: true,
			max: maxBarLength + maxBarLength * additionalSpace
		},
	}
};


</script>

<style scoped>
/* Your styles here */
</style>