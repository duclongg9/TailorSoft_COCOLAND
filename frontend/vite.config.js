import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Proxy API calls to Spring Boot in dev mode
      '/api': 'http://localhost:8080',
      '/uploads': 'http://localhost:8080',
    }
  }
})
