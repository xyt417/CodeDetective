import $ from "jquery";

export const getFileContent = async (fileUrl) => {
	// 返回一个新的Promise对象
	return new Promise((resolve, reject) => {
		$.ajax({
			url: fileUrl,
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
