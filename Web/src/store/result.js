export default {
	state: {
		submissions: {}, // { submissionId : { fileFullPath : fileContent } ... }
		finishedNum: 0,
	},
	getters: {

	},
	mutations: {


	},
	actions: {
		saveSubmissionFile(context, submissionFile) {
			if (context.state.submissions[submissionFile.submissionId] === undefined) {
				context.state.submissions[submissionFile.submissionId] = new Map()
			}
			context.state.submissions[submissionFile.submissionId].set(
				submissionFile.fileName,
				submissionFile
			)
		},
	},
	modules: {

	}
}