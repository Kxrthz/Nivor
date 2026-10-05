import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
export default defineConfig({
  plugins: [react(), tailwindcss()],
  build: {
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (!id.includes('node_modules')) return
          const normalized = id.replaceAll('\\', '/')
          if (/\/node_modules\/(react|react-dom|scheduler)\//.test(normalized)) return 'react-vendor'
          if (/\/node_modules\/(framer-motion|motion-dom|motion-utils)\//.test(normalized)) return 'motion-vendor'
          if (/\/node_modules\/(react-router|react-router-dom|@remix-run\/router)\//.test(normalized)) return 'router-vendor'
          if (/\/node_modules\/@tanstack\/react-query\//.test(normalized)) return 'query-vendor'
        },
      },
    },
  },
})
