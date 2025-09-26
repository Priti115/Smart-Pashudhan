/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './index.html',
    './src/**/*.{ts,tsx,js,jsx}',
  ],
  theme: {
    extend: {
      colors: {
        surface: '#f5f9fb',
        card: '#ffffff',
        primary: {
          50: '#e9f1f3',
          100: '#b7d1da',
          200: '#5ba1b6',
          300: '#2a819a',
          400: '#17637d',
          500: '#124C60',
          600: '#0f3f50',
          700: '#0c3442',
          800: '#092a34',
          900: '#071f27'
        },
        success: {
          100: '#e6f7f0',
          300: '#a8e7cc',
          500: '#3bbf86',
          700: '#2b9a6c'
        }
      },
      boxShadow: {
        soft: '0 2px 12px rgba(16, 24, 40, 0.06)'
      },
      borderRadius: {
        xl: '14px'
      }
    },
  },
  plugins: [],
}
