import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const apiTarget = env.VITE_API_URL || 'http://localhost:8080'

  return {
    plugins: [react()],
    server: {
      // En desarrollo, hace proxy de /compile y /health al backend Java
      // para evitar problemas de CORS sin necesitar el backend configurado con CORS local
      proxy: {
        '/compile': { target: apiTarget, changeOrigin: true },
        '/health':  { target: apiTarget, changeOrigin: true },
      },
    },
    build: {
      outDir: 'dist',
      sourcemap: false,
    },
  }
})
