const matchColors = [
	{ red: 255, green: 122, blue: 0 },
	{ red: 0, green: 133, blue: 255 },
	{ red: 255, green: 0, blue: 122 },
	{ red: 255, green: 245, blue: 0 },
	{ red: 0, green: 255, blue: 255 },
	{ red: 112, green: 0, blue: 255 },
	{ red: 0, green: 255, blue: 133 }
]

export function getMatchColorCount() {
	return matchColors.length
}

export function getMatchColor(index, alpha) {
	if (index === undefined) {
		return 'rgba(0,0,0,0)'
	}
	return `rgba(${matchColors[index].red}, ${matchColors[index].green}, ${matchColors[index].blue}, ${alpha})`
}