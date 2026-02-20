import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

const httpClient: AxiosInstance = axios.create({
  baseURL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

httpClient.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

httpClient.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  (error) => {
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.message

      switch (status) {
        case 400:
          ElMessage.error(`请求错误: ${message}`)
          break
        case 401:
          ElMessage.error('未授权，请重新登录')
          break
        case 403:
          ElMessage.error('拒绝访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error(`服务器错误: ${message}`)
          break
        default:
          ElMessage.error(`请求失败: ${message}`)
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElMessage.error(`请求失败: ${error.message}`)
    }

    return Promise.reject(error)
  }
)

export const api = {
  get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return httpClient.get(url, config).then(res => res.data)
  },

  post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return httpClient.post(url, data, config).then(res => res.data)
  },

  put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return httpClient.put(url, data, config).then(res => res.data)
  },

  delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return httpClient.delete(url, config).then(res => res.data)
  }
}

export default httpClient
