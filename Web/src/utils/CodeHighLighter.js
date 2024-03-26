import hljs from 'highlight.js'
import 'highlight.js/styles/github-dark.css' // 选择高亮样式
export function highlight(code, lang = 'java') {
	const highlightedCode = hljs.highlight(code, {language: lang}).value;
	const openTags = []
	return highlightedCode
		.replace(/(<span [^>]*>)|(<\/span>)|(\n)/g, (match) => {
			if (match === '\n') {
				return '</span>'.repeat(openTags.length) + '\n' + openTags.join('')
			}

			if (match === '</span>') {
				openTags.pop()
			} else {
				openTags.push(match)
			}

			return match
		})
		.split('\n')
}