import {getMatchColorCount} from "@/utils/ColorUtils";

export class Extractor {
	// 提取提交者的名字，例如从 "submissions[123]" 提取 "123"
	static extractSubmitter(submission) {
		const matches = submission.match(/\[(.*?)]/);
		return matches ? matches[1] : null;
	}

	static extractSubmissionName(submission) {
		return submission.split('[')[0];
	}

	static extractTopComparisonsFromMetrics(metrics) {
		const topComparisons = [];

		const maxSimilarity = new Map;
		for (const _comparison of metrics[1].topComparisons) {
			maxSimilarity.set(Extractor.extractSubmissionName(_comparison.first_submission) + "-"
				+ Extractor.extractSubmissionName(_comparison.second_submission), _comparison.similarity);
		}
		for (const _comparison of metrics[0].topComparisons) {
			const max = maxSimilarity.get(Extractor.extractSubmissionName(_comparison.first_submission) + "-"
				+ Extractor.extractSubmissionName(_comparison.second_submission));
			const comparison = {
				firstSubmissionId: _comparison.first_submission,
				secondSubmissionId: _comparison.second_submission,
				firstSubmissionName: Extractor.extractSubmissionName(_comparison.first_submission),
				secondSubmissionName: Extractor.extractSubmissionName(_comparison.second_submission),
				firstSubmitter: Extractor.extractSubmitter(_comparison.first_submission),
				secondSubmitter: Extractor.extractSubmitter(_comparison.second_submission),
				similarity: {
					AVG: (_comparison.similarity * 100).toFixed(6) + "%",
					MAX: (max * 100).toFixed(6) + "%",
				}
			}
			topComparisons.push(comparison);
		}
		return topComparisons;
	}

	static extractSubmissionIds2ConparisonFileName(json) {
		const submissionIdsToComparisonName = json.submission_ids_to_comparison_file_name;
		const submissionIds = Object.entries(submissionIdsToComparisonName);
		const comparisonMap = new Map();
		for (const [key, value] of submissionIds) {
			comparisonMap.set(key, new Map(Object.entries(value)));
		}
		return comparisonMap;
	}

	static extractSubmissionIdList(json) {
		return Object.keys(json.submission_id_to_display_name);
	}

	static extractFileDisplayName(file) {
		const filePathLength = file.fileName.length
		return filePathLength > 35
			? '...' + file.fileName.substring(filePathLength - 35, filePathLength)
			: file.fileName
	}

	static extractComparison(json) {
		const firstSubmissionId = json.id1;
		const secondSubmissionId = json.id2;
		const similarity = json.similarity;
		const matches = json.matches;
		const uncoloredMatches = matches.map((match) => this.getMatch(match));

		return {
			firstSubmissionId,
			secondSubmissionId,
			similarity,
			matches: Extractor.colorMatches(uncoloredMatches),
		}
	}

	static getMatch(match) {
		return {
			firstFile: match.file1,
			secondFile: match.file2,
			startInFirst: match.start1,
			endInFirst: match.end1,
			startInSecond: match.start2,
			endInSecond: match.end2,
			token: match.token,
		}
	}

	static colorMatches(matches) {
		const maxColorCount = getMatchColorCount()
		let currentColorIndex = 0
		const matchesOrderedByFirst = Array.from(matches)
			.sort((a, b) => a.startInFirst - b.startInFirst)
			.sort((a, b) => (a.firstFile > b.firstFile ? 1 : -1))
		const matchesOrderedBySecond = Array.from(matches)
			.sort((a, b) => a.startInSecond - b.startInSecond)
			.sort((a, b) => (a.secondFile > b.secondFile ? 1 : -1))
		const matchesOrderedByToken = Array.from(matches).sort((a, b) => b.tokens - a.tokens)

		function isColorAvailable(matchList, index) {
			return (
				(index === 0 || matchList[index - 1].colorIndex !== currentColorIndex) &&
				(index === matchList.length - 1 || matchList[index + 1].colorIndex !== currentColorIndex)
			)
		}

		for (let i = 0; i < matches.length; i++) {
			const firstIndex = matchesOrderedByFirst.findIndex((match) => match === matches[i])
			const secondIndex = matchesOrderedBySecond.findIndex((match) => match === matches[i])
			const sortedIndex = matchesOrderedByToken.findIndex((match) => match === matches[i])
			const startCounter = currentColorIndex
			while (
				!isColorAvailable(matchesOrderedByFirst, firstIndex) ||
				!isColorAvailable(matchesOrderedBySecond, secondIndex) ||
				!isColorAvailable(matchesOrderedByToken, sortedIndex)
				) {
				currentColorIndex = (currentColorIndex + 1) % maxColorCount

				if (currentColorIndex === startCounter) {
					// This case should never happen, this is just a safety measure
					throw currentColorIndex
				}
			}
			matches[i].colorIndex = currentColorIndex
			currentColorIndex = (currentColorIndex + 1) % maxColorCount
		}
		return {
			matchesOrderedByFirst,
			matchesOrderedBySecond,
			matchesOrderedByToken
		}
	}

	static groupMatches(matches, index) {
		const group = new Map();
		matches.forEach((val) => {
			const name = (index === 1 ? val.firstFile : val.secondFile);
			val.index = index; // 记录所属哪个提交
			if(!group.get(name)) group.set(name, []);
			group.get(name).push(val);
		})
		return group;
	}

	static getMatchStart(match) {
		if (match.index === 1) {
			return match.startInFirst;
		} else {
			return match.startInSecond;
		}
	}

	static getMatchEnd(match) {
		if (match.index === 1) {
			return match.endInFirst;
		} else {
			return match.endInSecond;
		}
	}
}