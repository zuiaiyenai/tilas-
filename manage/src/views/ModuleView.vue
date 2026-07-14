<script setup>
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageData } from '../data/pages'
import {
  createDepartment,
  createEmployee,
  deleteDepartment,
  deleteEmployee,
  getDepartments,
  getEmployees,
  updateDepartment,
  updateEmployee,
} from '../api'

const props = defineProps({ pageKey: { type: String, required: true } })
const page = computed(() => pageData[props.pageKey] || pageData.classes)
const isDepartments = computed(() => props.pageKey === 'departments')
const isEmployees = computed(() => props.pageKey === 'employees')

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const keyword = ref('')
const currentPage = ref(1)
const dialogVisible = ref(false)
const editing = ref(false)
const saving = ref(false)
const formRef = ref()
const form = ref({ name: '' })

const departmentRules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 20, message: '部门名称长度为 2-20 个字符', trigger: 'blur' },
  ],
}

const formatTime = (value) => {
  if (!value) return '-'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return String(value).replace('T', ' ')
  return date.toLocaleString('zh-CN', { hour12: false }).replaceAll('/', '-')
}

const normalizeDepartment = (item) => ({
  _raw: item,
  id: item.id,
  name: item.name || '',
  createTime: formatTime(item.createTime),
  updateTime: formatTime(item.updateTime || item.createTime),
  employeeCount: item.employeeCount ?? item.empCount ?? 0,
  status: item.status || '正常',
})

const loadDepartments = async () => {
  const data = await getDepartments()
  const list = Array.isArray(data) ? data : (data?.rows || [])
  const search = keyword.value.trim().toLowerCase()
  const result = list.map(normalizeDepartment).filter((item) => !search || item.name.toLowerCase().includes(search))
  rows.value = result
  total.value = result.length
}

const load = async () => {
  loading.value = true
  try {
    if (isDepartments.value) {
      await loadDepartments()
    } else if (isEmployees.value) {
      const data = await getEmployees({ page: currentPage.value, pageSize: 10, name: keyword.value.trim() || undefined })
      rows.value = (data?.rows || []).map((item) => ({
        _raw: item,
        values: [item.name, item.job || '-', item.deptName || item.deptId || '-', item.phone || '-', '在职'],
      }))
      total.value = data?.total || rows.value.length
    } else {
      rows.value = page.value.rows.map((values) => ({ values }))
      total.value = rows.value.length
    }
  } catch (error) {
    rows.value = []
    total.value = 0
    ElMessage.error(error.message || '加载数据失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const openCreate = () => {
  editing.value = false
  form.value = isDepartments.value
    ? { name: '' }
    : { username: '', name: '', password: '123456', phone: '', job: '', deptId: null }
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

const openEdit = (row) => {
  editing.value = true
  form.value = { ...(row._raw || {}) }
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

const saveDepartment = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (editing.value) await updateDepartment({ id: form.value.id, name: form.value.name.trim() })
    else await createDepartment({ name: form.value.name.trim() })
    dialogVisible.value = false
    ElMessage.success(editing.value ? '修改成功' : '新增成功')
    await load()
  } catch (error) {
    ElMessage.error(error.message || '保存失败，请稍后重试')
  } finally {
    saving.value = false
  }
}

const save = async () => {
  if (isDepartments.value) return saveDepartment()
  try {
    if (editing.value) await updateEmployee(form.value)
    else await createEmployee(form.value)
    dialogVisible.value = false
    ElMessage.success(editing.value ? '修改成功' : '新增成功')
    await load()
  } catch (error) {
    ElMessage.error(error.message || '保存失败，请稍后重试')
  }
}

const remove = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除部门“${row.name}”吗？删除后不可恢复。`, '删除确认', {
      type: 'warning',
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
    })
    if (isDepartments.value) await deleteDepartment(row.id)
    else await deleteEmployee(row._raw.id)
    ElMessage.success('删除成功')
    await load()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') ElMessage.error(error.message || '删除失败')
  }
}

const reset = () => {
  keyword.value = ''
  currentPage.value = 1
  load()
}

watch(() => props.pageKey, () => {
  currentPage.value = 1
  keyword.value = ''
  load()
})
onMounted(load)
</script>

<template>
  <div class="module-panel">
    <div class="module-heading">
      <div>
        <div class="module-crumb">系统管理 / {{ page.title }}</div>
        <h1>{{ page.title }}</h1>
        <p>{{ page.desc }}</p>
      </div>
      <el-button v-if="isDepartments || isEmployees" type="primary" @click="openCreate">
        新增{{ isDepartments ? '部门' : '员工' }}
      </el-button>
    </div>

    <div class="module-toolbar">
      <el-input v-model="keyword" placeholder="请输入关键词搜索" clearable @keyup.enter="load" />
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="rows" stripe class="module-table" empty-text="暂无数据">
      <template v-if="isDepartments">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="name" label="部门名称" min-width="220" />
        <el-table-column prop="createTime" label="创建时间" min-width="190" />
        <el-table-column prop="updateTime" label="最后修改时间" min-width="190" />
        <el-table-column prop="employeeCount" label="员工人数" width="110" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center" />
        <el-table-column label="操作" width="140" fixed="right" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="openEdit(scope.row)">修改</el-button>
            <el-button link type="danger" @click="remove(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </template>
      <template v-else>
        <el-table-column v-for="(column, index) in page.columns" :key="column" :label="column">
          <template #default="scope">{{ scope.row.values[index] }}</template>
        </el-table-column>
        <el-table-column v-if="isEmployees" label="操作" width="150">
          <template #default="scope">
            <el-button link type="primary" @click="openEdit(scope.row)">修改</el-button>
            <el-button link type="danger" @click="remove(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <div class="module-footer">
      <span>共 {{ total }} 条记录</span>
      <el-pagination v-model:current-page="currentPage" layout="prev, pager, next" :total="total" :page-size="10" @current-change="load" />
    </div>

    <el-dialog v-model="dialogVisible" :title="editing ? '修改部门' : '新增部门'" width="460px" destroy-on-close>
      <el-form v-if="isDepartments" ref="formRef" :model="form" :rules="departmentRules" label-width="90px" @submit.prevent>
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" maxlength="20" show-word-limit placeholder="请输入部门名称" @keyup.enter="saveDepartment" />
        </el-form-item>
      </el-form>
      <el-form v-else :model="form" label-width="90px">
        <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="职位"><el-input v-model="form.job" /></el-form-item>
        <el-form-item label="部门"><el-input v-model="form.deptId" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
