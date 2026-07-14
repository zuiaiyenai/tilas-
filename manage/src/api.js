const request = async (url, options) => {
  const response = await fetch(`/api${url}`, options)
  let result = null
  try {
    result = await response.json()
  } catch {
    // 服务器没有返回 JSON 时，下面统一抛出请求错误。
  }

  if (!response.ok) {
    const error = new Error(result?.msg || `请求失败：${response.status}`)
    error.status = response.status
    throw error
  }
  if (!result || result.code !== 1) throw new Error(result?.msg || '后端返回失败')
  return result.data
}

const DEPARTMENT_STORAGE_KEY = 'manage_departments'
const defaultDepartments = [
  { id: 1, name: '学工部', createTime: '2024-09-01T23:06:29', updateTime: '2024-09-01T23:06:29' },
  { id: 2, name: '教研部', createTime: '2024-09-01T23:06:29', updateTime: '2024-09-01T23:06:29' },
]

const readLocalDepartments = () => {
  try {
    const data = JSON.parse(localStorage.getItem(DEPARTMENT_STORAGE_KEY) || 'null')
    return Array.isArray(data) ? data : structuredClone(defaultDepartments)
  } catch {
    return structuredClone(defaultDepartments)
  }
}

const writeLocalDepartments = (data) => {
  localStorage.setItem(DEPARTMENT_STORAGE_KEY, JSON.stringify(data))
  return data
}

const useLocalDepartments = (operation) => {
  try {
    return operation(readLocalDepartments())
  } catch {
    return operation(structuredClone(defaultDepartments))
  }
}

export const getDepartments = async () => {
  try {
    return await request('/depts')
  } catch (error) {
    console.warn('部门后端接口不可用，已切换为本地演示数据：', error.message)
    return useLocalDepartments((data) => data)
  }
}

export const createDepartment = async (data) => {
  try {
    return await request('/depts', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) })
  } catch (error) {
    console.warn('部门新增接口不可用，已保存到本地：', error.message)
    return useLocalDepartments((list) => {
      const now = new Date().toISOString().slice(0, 19)
      const item = { id: Date.now(), name: data.name, createTime: now, updateTime: now }
      writeLocalDepartments([...list, item])
      return item
    })
  }
}

export const updateDepartment = async (data) => {
  try {
    return await request('/depts', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) })
  } catch (error) {
    console.warn('部门修改接口不可用，已保存到本地：', error.message)
    return useLocalDepartments((list) => {
      const updated = list.map((item) => item.id === data.id ? { ...item, name: data.name, updateTime: new Date().toISOString().slice(0, 19) } : item)
      writeLocalDepartments(updated)
      return updated.find((item) => item.id === data.id)
    })
  }
}

export const deleteDepartment = async (id) => {
  try {
    return await request(`/depts?id=${id}`, { method: 'DELETE' })
  } catch (error) {
    console.warn('部门删除接口不可用，已从本地删除：', error.message)
    return useLocalDepartments((list) => writeLocalDepartments(list.filter((item) => item.id !== id)))
  }
}

export const getEmployees = (params = {}) => {
  const query = new URLSearchParams({ page: params.page || 1, pageSize: params.pageSize || 10 })
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') query.set(key, value)
  })
  return request(`/emps?${query.toString()}`)
}
export const createEmployee = (data) => request('/emps', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) })
export const updateEmployee = (data) => request('/emps', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) })
export const deleteEmployee = (id) => request(`/emps/${id}`, { method: 'DELETE' })
export const uploadAvatar = (file) => {
  const body = new FormData()
  body.append('file', file)
  return request('/upload', { method: 'POST', body })
}
