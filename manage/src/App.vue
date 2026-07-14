<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Avatar, Document, HelpFilled, Histogram, HomeFilled, InfoFilled, Menu, Promotion, Share, Tools, UserFilled } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const expanded = ref({ manage: true, system: true, report: true })
const menuGroups = [
  { key: 'manage', path: '/manage', icon: Menu, title: '班级学员管理', children: [{ path: '/clazz', pageKey: 'classes', icon: HomeFilled, label: '班级管理' }, { path: '/stu', pageKey: 'students', icon: UserFilled, label: '学员管理' }] },
  { key: 'system', path: '/system', icon: Tools, title: '系统信息管理', children: [{ path: '/dept', pageKey: 'departments', icon: HelpFilled, label: '部门管理' }, { path: '/emp', pageKey: 'employees', icon: Avatar, label: '员工管理' }] },
  { key: 'report', path: '/report', icon: Histogram, title: '数据统计管理', children: [{ path: '/empReport', pageKey: 'employee-stats', icon: InfoFilled, label: '员工信息统计' }, { path: '/stuReport', pageKey: 'student-stats', icon: Share, label: '学员信息统计' }, { path: '/log', pageKey: 'log-stats', icon: Document, label: '日志信息统计' }] }
]
const activePath = computed(() => route.path)
const selectMenu = path => router.push(path)
const toggleGroup = key => { expanded.value[key] = !expanded.value[key] }
const logout = () => ElMessage.success('已退出登录')
const changePassword = () => ElMessage.info('修改密码功能')
</script>
<template>
  <el-container class="page-layout">
    <el-header class="app-header"><div class="header-brand"><div class="logo">T<small>www.itheima.com</small></div><div class="brand-title">tlias 智能学习辅助系统</div></div><div class="header-actions"><button @click="changePassword">🔧 修改密码</button><button @click="logout">◯ 退出登录</button></div></el-header>
    <el-container class="body-layout">
      <el-aside class="app-aside" width="220px">
        <el-menu :default-active="activePath" class="side-menu">
          <el-menu-item index="/index" @click="selectMenu('/index')"><el-icon><Promotion /></el-icon>首页</el-menu-item>
          <el-sub-menu v-for="group in menuGroups" :key="group.key" :index="group.path">
            <template #title><el-icon><component :is="group.icon" /></el-icon>{{ group.title }}</template>
            <el-menu-item v-for="child in group.children" :key="child.path" :index="child.path" @click="selectMenu(child.path)"><el-icon><component :is="child.icon" /></el-icon>{{ child.label }}</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main class="app-main"><router-view /></el-main>
    </el-container>
  </el-container>
</template>
