import colors from 'tailwindcss/colors'
/** @type {import('tailwindcss').Config} */
module.exports = {
	content: ["./public/index.html", "./src/**/*.{html,js,vue}"],
	theme: {
		extend: {
			colors: {
				gruvbox: {
					// 使用Gruvbox配色的颜色代码
					'dark-hard': '#1d2021', // gruvbox背景色的暗色版本
					'bg': '#282828', // gruvbox背景色
					'bg0': '#32302f',
					'bg1': '#3c3836',
					'bg2': '#504945',
					'bg3': '#665c54',
					'bg4': '#7c6f64',
					'fg0': '#fbf1c7',
					'fg1': '#ebdbb2',
					'fg2': '#d5c4a1',
					'fg3': '#bdae93',
					'fg4': '#a89984',
					// ...（其他颜色）
					'red': '#fb4934',
					'green': '#b8bb26',
					'yellow': '#fabd2f',
					'blue': '#83a598',
					'purple': '#d3869b',
					'aqua': '#8ec07c',
					'gray': '#928374',
					// ...（其他颜色）
				},
				font: {
					light: '#000000',
					dark: '#ffffff'
				},
				backgorund: {
					light: 'hsl(0, 0%, 97%)',
					dark: 'hsl(180, 80%, 3%)'
				},
				container: {
					light: 'hsl(0, 0%, 98%)',
					dark: 'hsl(200, 20%, 13%)',
					border: {
						light: 'hsl(0, 0%, 80%)',
						dark: 'hsl(0, 0%, 25%)'
					},
					secondary: {
						light: 'hsl(0, 0%, 95%)',
						dark: 'hsl(200, 20%, 18%)'
					}
				},
				interactable: {
					light: 'hsl(0, 0%, 100%)',
					dark: 'hsl(180, 30%, 18%)',
					border: {
						light: 'hsl(0, 0%, 75%)',
						dark: 'hsl(0, 0%, 30%)'
					}
				},
				scrollbar: {
					backgorund: {
						light: colors.slate[100],
						dark: '#30363D'
					},
					thumb: {
						light: colors.slate[400],
						dark: '#505A66'
					}
				},
				accent: {
					DEFAULT: '#be1622',
					dark: '#7F0F18'
				},
				link: {
					DEFAULT: '#0070f3',
					dark: '#00c'
				},
				error: '#dc322f'
			},
			borderWidth: {
				1: '1px'
			}
		}
	},
	plugins: [],
}