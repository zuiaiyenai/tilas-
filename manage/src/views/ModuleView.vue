<script setup>
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageData } from '../data/pages'
import { createDepartment, createEmployee, deleteDepartment, deleteEmployee, getDepartments, getEmployees, updateDepartment, updateEmployee, uploadAvatar } from '../api'

const props = defineProps({ pageKey: { type: String, required: true } })
const page = computed(() => pageData[props.pageKey] || pageData.classes)
const isDepartments = computed(() => props.pageKey === 'departments')
const isEmployees = computed(() => props.pageKey === 'employees')
const rows = ref([]), total = ref(0), loading = ref(false), currentPage = ref(1), pageSize = ref(10)
const keyword = ref(''), dialogVisible = ref(false), editing = ref(false), saving = ref(false), uploading = ref(false)
const formRef = ref(), departments = ref([])
const selectedEmployees = ref([])
const searchEmp = ref({ name: '', gender: '', date: [], begin: '', end: '' })
const form = ref({})
const jobMap = { 1: '班主任', 2: '讲师', 3: '学工主管', 4: '教研主管', 5: '咨询师' }
const genderText = value => Number(value) === 1 ? '男' : Number(value) === 2 ? '女' : '-'
const formatTime = value => { if (!value) return '-'; const d = new Date(value); return Number.isNaN(d.getTime()) ? String(value).replace('T', ' ') : d.toLocaleString('zh-CN', { hour12: false }).replaceAll('/', '-') }
const departmentRules = { name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }] }
const employeeRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度必须为 2-20 个字符', trigger: 'blur' },
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度必须为 2-10 个字符', trigger: 'blur' },
  ],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的 11 位手机号', trigger: 'blur' },
  ],
}

watch(() => searchEmp.value.date, value => { searchEmp.value.begin = value?.length === 2 ? value[0] : ''; searchEmp.value.end = value?.length === 2 ? value[1] : '' })
const loadDepartments = async () => { const data = await getDepartments(); departments.value = Array.isArray(data) ? data : data?.rows || [] }
const load = async () => {
  loading.value = true
  try {
    if (isDepartments.value) {
      const data = await getDepartments(); const list = Array.isArray(data) ? data : data?.rows || []; const search = keyword.value.trim().toLowerCase()
      rows.value = list.map(item => ({ _raw: item, ...item, createTime: formatTime(item.createTime), updateTime: formatTime(item.updateTime || item.createTime), status: item.status || '正常', employeeCount: item.employeeCount ?? item.empCount ?? 0 })).filter(item => !search || item.name.toLowerCase().includes(search)); total.value = rows.value.length
    } else if (isEmployees.value) {
      const p = { page: currentPage.value, pageSize: pageSize.value, name: searchEmp.value.name.trim() || undefined, gender: searchEmp.value.gender || undefined, begin: searchEmp.value.begin || undefined, end: searchEmp.value.end || undefined }
      const data = await getEmployees(p); rows.value = (data?.rows || []).map(item => ({ _raw: item, ...item })); total.value = data?.total || rows.value.length
    } else { rows.value = page.value.rows.map(values => ({ values })); total.value = rows.value.length }
  } catch (error) { rows.value = []; total.value = 0; ElMessage.error(error.message || '加载数据失败，请稍后重试') } finally { loading.value = false }
}
const clearSearch = () => { searchEmp.value = { name: '', gender: '', date: [], begin: '', end: '' }; keyword.value = ''; currentPage.value = 1; load() }
const openCreate = () => { editing.value = false; form.value = isDepartments.value ? { name: '' } : { username: '', name: '', password: '123456', phone: '', gender: 1, image: '', entryDate: '', job: '', salary: '', deptId: null, exprList: [] }; dialogVisible.value = true; nextTick(() => formRef.value?.clearValidate()) }
const openEdit = row => { editing.value = true; form.value = { exprList: [], ...(row._raw || row) }; dialogVisible.value = true; nextTick(() => formRef.value?.clearValidate()) }
const handleAvatarUpload = async ({ file }) => {
  if (!file.type.startsWith('image/')) return ElMessage.error('只能上传图片文件')
  if (file.size > 2 * 1024 * 1024) return ElMessage.error('头像大小不能超过2M')
  uploading.value = true
  try { const data = await uploadAvatar(file); form.value.image = data?.url || data; ElMessage.success('头像上传成功') } catch { form.value.image = await new Promise(resolve => { const r = new FileReader(); r.onload = () => resolve(r.result); r.readAsDataURL(file) }); ElMessage.info('上传接口暂不可用，已使用本地预览') } finally { uploading.value = false }
}
const addExperience = () => { form.value.exprList = [...(form.value.exprList || []), { date: [], begin: '', end: '', company: '', job: '' }] }
const removeExperience = index => { form.value.exprList = (form.value.exprList || []).filter((_, i) => i !== index) }
const saveDepartment = async () => { if (!(await formRef.value?.validate().catch(() => false))) return; saving.value = true; try { if (editing.value) await updateDepartment({ id: form.value.id, name: form.value.name.trim() }); else await createDepartment({ name: form.value.name.trim() }); dialogVisible.value = false; ElMessage.success('保存成功'); await load() } catch (error) { ElMessage.error(error.message || '保存失败') } finally { saving.value = false } }
const save = async () => {
  if (isDepartments.value) return saveDepartment()
  if (!(await formRef.value?.validate().catch(() => false))) return
  saving.value = true
  try { if (editing.value) await updateEmployee(form.value); else await createEmployee(form.value); dialogVisible.value = false; ElMessage.success('保存成功'); await load() } catch (error) { ElMessage.error(error.message || '保存失败') } finally { saving.value = false }
}
const remove = async row => { try { await ElMessageBox.confirm(`确定删除“${row.name}”吗？删除后不可恢复。`, '删除确认', { type: 'warning', confirmButtonText: '确定删除', cancelButtonText: '取消' }); if (isDepartments.value) await deleteDepartment(row.id); else await deleteEmployee(row.id); ElMessage.success('删除成功'); await load() } catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error.message || '删除失败') } }
const handleSelectionChange = selection => { selectedEmployees.value = selection }
const batchRemove = async () => {
  if (!selectedEmployees.value.length) return ElMessage.warning('请先选择要删除的员工')
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedEmployees.value.length} 名员工吗？删除后不可恢复。`, '批量删除确认', { type: 'warning', confirmButtonText: '确定删除', cancelButtonText: '取消' })
    await Promise.all(selectedEmployees.value.map(employee => deleteEmployee(employee.id)))
    ElMessage.success('批量删除成功')
    selectedEmployees.value = []
    await load()
  } catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error.message || '批量删除失败') }
}
const handlePageChange = () => load(); const handleSizeChange = () => { currentPage.value = 1; load() }
watch(() => props.pageKey, () => { currentPage.value = 1; keyword.value = ''; load() })
onMounted(async () => { if (isEmployees.value) await loadDepartments(); await load() })
</script>

<template>
  <div class="module-panel">
    <div class="module-heading"><div><div class="module-crumb">系统管理 / {{ page.title }}</div><h1>{{ page.title }}</h1><p>{{ page.desc }}</p></div><div v-if="isDepartments || isEmployees" class="heading-actions"><el-button v-if="isEmployees" type="danger" plain :disabled="!selectedEmployees.length" @click="batchRemove">批量删除</el-button><el-button type="primary" @click="openCreate">新增{{ isDepartments ? '部门' : '员工' }}</el-button></div></div>
    <div v-if="isEmployees" class="employee-toolbar"><el-form :inline="true" :model="searchEmp"><el-form-item label="姓名"><el-input v-model="searchEmp.name" clearable placeholder="请输入员工姓名" @keyup.enter="load" /></el-form-item><el-form-item label="性别"><el-select v-model="searchEmp.gender" clearable placeholder="请选择" style="width:130px"><el-option label="男" value="1" /><el-option label="女" value="2" /></el-select></el-form-item><el-form-item label="入职时间"><el-date-picker v-model="searchEmp.date" type="daterange" value-format="YYYY-MM-DD" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" /></el-form-item><el-form-item><el-button type="primary" @click="currentPage = 1; load()">查询</el-button><el-button @click="clearSearch">清空</el-button></el-form-item></el-form></div>
    <div v-else class="module-toolbar"><el-input v-model="keyword" placeholder="请输入关键词搜索" clearable @keyup.enter="load" /><el-button type="primary" @click="load">查询</el-button><el-button @click="clearSearch">重置</el-button></div>
    <el-table v-loading="loading" :data="rows" stripe class="module-table" empty-text="暂无数据" @selection-change="handleSelectionChange">
      <template v-if="isEmployees"><el-table-column type="selection" width="50" /><el-table-column prop="name" label="姓名" min-width="110" /><el-table-column label="性别" width="80"><template #default="{ row }">{{ genderText(row.gender) }}</template></el-table-column><el-table-column label="头像" width="90"><template #default="{ row }"><el-avatar :size="44" :src="row.image">{{ row.name?.slice(0, 1) }}</el-avatar></template></el-table-column><el-table-column label="所属部门" min-width="120"><template #default="{ row }">{{ row.deptName || row.deptId || '-' }}</template></el-table-column><el-table-column label="职位" min-width="110"><template #default="{ row }">{{ jobMap[row.job] || row.job || '-' }}</template></el-table-column><el-table-column prop="entryDate" label="入职日期" min-width="120" /><el-table-column label="最后操作时间" min-width="180"><template #default="{ row }">{{ formatTime(row.updateTime) }}</template></el-table-column><el-table-column label="操作" width="130" fixed="right"><template #default="{ row }"><el-button link type="primary" @click="openEdit(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template></el-table-column></template>
      <template v-else-if="isDepartments"><el-table-column type="index" label="序号" width="70" align="center" /><el-table-column prop="name" label="部门名称" min-width="180" /><el-table-column prop="createTime" label="创建时间" min-width="170" /><el-table-column prop="updateTime" label="最后修改时间" min-width="170" /><el-table-column prop="employeeCount" label="员工人数" width="100" align="center" /><el-table-column prop="status" label="状态" width="90" align="center" /><el-table-column label="操作" width="130" fixed="right"><template #default="{ row }"><el-button link type="primary" @click="openEdit(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template></el-table-column></template>
      <template v-else><el-table-column v-for="(column, index) in page.columns" :key="column" :label="column"><template #default="{ row }">{{ row.values[index] }}</template></el-table-column></template>
    </el-table>
    <div class="module-footer"><span>共 {{ total }} 条记录</span><el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 30, 40]" layout="total, sizes, prev, pager, next, jumper" :total="total" @current-change="handlePageChange" @size-change="handleSizeChange" /></div>

    <el-dialog v-model="dialogVisible" :title="editing ? `编辑${isDepartments ? '部门' : '员工'}` : `新增${isDepartments ? '部门' : '员工'}`" width="760px" destroy-on-close class="employee-dialog">
      <el-form v-if="isDepartments" ref="formRef" :model="form" :rules="departmentRules" label-width="90px"><el-form-item label="部门名称" prop="name"><el-input v-model="form.name" maxlength="20" /></el-form-item></el-form>
      <el-form v-else ref="formRef" :model="form" :rules="employeeRules" label-width="82px">
        <div class="employee-form-grid"><el-form-item label="用户名" prop="username"><el-input v-model="form.username" placeholder="请输入员工用户名，2-20个字" /></el-form-item><el-form-item label="姓名" prop="name"><el-input v-model="form.name" placeholder="请输入员工姓名，2-10个字" /></el-form-item><el-form-item label="性别" prop="gender"><el-select v-model="form.gender" placeholder="请选择" style="width:100%"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select></el-form-item><el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" placeholder="请输入员工手机号" /></el-form-item><el-form-item label="职位"><el-select v-model="form.job" clearable placeholder="请选择" style="width:100%"><el-option v-for="(label, value) in jobMap" :key="value" :label="label" :value="Number(value)" /></el-select></el-form-item><el-form-item label="薪资"><el-input v-model="form.salary" placeholder="请输入员工薪资" /></el-form-item><el-form-item label="所属部门"><el-select v-model="form.deptId" clearable placeholder="请选择" style="width:100%"><el-option v-for="item in departments" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item><el-form-item label="入职日期"><el-date-picker v-model="form.entryDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择入职日期" style="width:100%" /></el-form-item></div>
        <el-form-item label="头像" class="avatar-form-item"><div class="avatar-upload-row"><el-upload accept="image/png,image/jpeg" :show-file-list="false" :http-request="handleAvatarUpload"><div class="avatar-uploader"><el-avatar :size="112" :src="form.image">{{ form.name?.slice(0, 1) || '+' }}</el-avatar></div></el-upload><div class="avatar-tips"><p>图片大小不超过2M</p><p>仅能上传PNG、JPEG、JPG等图片</p><p>建议上传200*200或300*300尺寸的照片</p></div></div></el-form-item>
        <div class="experience-section"><div class="experience-title"><span>工作经历</span><el-button type="success" plain size="small" @click="addExperience">+ 添加工作经历</el-button></div><div v-for="(experience, index) in form.exprList" :key="index" class="experience-row"><el-date-picker v-model="experience.date" type="daterange" value-format="YYYY-MM-DD" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change="value => { experience.begin = value?.[0] || ''; experience.end = value?.[1] || '' }" /><el-input v-model="experience.company" placeholder="请输入公司名称" /><el-input v-model="experience.job" placeholder="请输入职位" /><el-button type="danger" link @click="removeExperience(index)">删除</el-button></div></div>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="saving" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.employee-toolbar { margin:22px 0; padding:18px 20px 4px; background:#f5f7fa; border-radius:6px; }
.heading-actions { display:flex; gap:12px; }
.employee-toolbar .el-form-item { margin-bottom:14px; }
.employee-form-grid { display:grid; grid-template-columns:1fr 1fr; column-gap:28px; }
.employee-form-grid :deep(.el-form-item) { margin-bottom:18px; }
.avatar-upload-row { display:flex; align-items:center; gap:26px; }
.avatar-uploader { width:112px; height:112px; display:flex; align-items:center; justify-content:center; border:4px solid #dcdfe6; cursor:pointer; background:#f5f7fa; color:#409eff; font-size:28px; }
.avatar-tips { color:#777; font-size:13px; line-height:1.5; }
.avatar-tips p { margin:5px 0; }
.experience-section { margin-left:82px; padding:14px 16px; background:#f5f5f5; }
.experience-title { display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; }
.experience-row { display:grid; grid-template-columns:1.4fr 1fr 1fr auto; gap:8px; align-items:center; margin-bottom:10px; }
@media (max-width:700px) { .employee-form-grid { grid-template-columns:1fr; } .experience-section { margin-left:0; } .experience-row { grid-template-columns:1fr; } }
</style>
