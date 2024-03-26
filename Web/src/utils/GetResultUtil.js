import $ from "jquery";

export const endPoint =  "https://code-detective-bucket.oss-cn-beijing.aliyuncs.com"
export const resultsDirBaseUrl = "https://code-detective-bucket.oss-cn-beijing.aliyuncs.com/Results/"
export const getFileContent = async (fileUrl) => {
	// 返回一个新的Promise对象
	return new Promise((resolve, reject) => {
		$.ajax({
			url: encodeURI(fileUrl),
			type: 'GET',
			success: function(data) {
				// 当请求成功时，解决Promise
				resolve(data);
			},
			error: function(xhr, status, error) {
				// 当请求失败时，拒绝Promise
				console.error("getFileContent Err: " + error);
				reject(error);
			}
		});
	});
};

export const getSubmissionFilesList = async (store, repoName, submissionId) => {
	return new Promise((resolve, reject) => {
		$.ajax({
			url:  localStorage.getItem('Addr') + "/files",
			type: 'POST',
			data: {
				repo_name: repoName,
				submission_id: submissionId,
			},
			headers: {
				Authorization: "Bearer " + store.state.user.token
			},
			success: function (data) {
				resolve(data)
			},
			error: function (xhr, status, error) {
				console.error("getFileContent Err: " + error);
				reject(error);
			}
		});
	});
}

// 注意: useStore() 只能在setup中使用，故这里只能传入store
export const storeSubmissionFiles = async (store, repoName, submissionId) => {
	getSubmissionFilesList(store, repoName, submissionId).then((data) => {
		for(let filePath of data) {
			filePath = filePath.replace(/\[\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}]$/, '');
			getFileContent(endPoint + "/" + filePath).then((fileContent) => {
				// 每次创建一个新对象，而不能只修改对象内容，否则是同一个对象的引用
				const newSubmissionFile = {
					submissionId: submissionId,
					fileName: filePath,
					content: fileContent
				};
				store.dispatch("saveSubmissionFile", newSubmissionFile);
				// console.log("submissionFile: " + submissionFile.fileName + " saved" + " Content: " + submissionFile.content);
			}).catch((err) => {
				console.error("getFileContent Err: " + err);
			});
		}
		store.state.result.finishedNum ++;
	}).catch((err) => {
		console.error("getSubmissionFilesList Err: " + err);
	});
}



