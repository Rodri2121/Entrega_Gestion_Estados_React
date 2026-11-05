import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// En desarrollo, Vite reenvía todo lo que empiece con /api a la API Spring Boot.
// El navegador solo ve el origen http://localhost:5173, por eso no hay problemas de CORS.
// Si la API del grupo usa otro puerto, cambiar target.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:9090',
        changeOrigin: true,
      },
    },
  },
});
