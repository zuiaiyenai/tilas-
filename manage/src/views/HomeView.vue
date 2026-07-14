<script setup>
import { ElMessage } from 'element-plus'
const notify = () => ElMessage.info('教学后台首页')
</script>

<template>
  <section class="welcome-panel" @click="notify">
    <div class="welcome-copy"><h1>教学后台</h1><p>教学业务管理服务平台</p></div>
    <div class="system-map" aria-label="教学后台业务模块">
      <div class="map-node node-left-top">考务管理</div>
      <div class="map-node node-right-top">教务管理</div>
      <div class="map-node node-left-mid">教学数据</div>
      <div class="map-node node-right-mid">就业管理</div>
      <div class="map-node node-left-bottom">反馈管理</div>
      <div class="map-node node-right-bottom">就业数据档案</div>
      <div class="illustration" aria-hidden="true">
        <div class="screen"></div><div class="building building-one"></div><div class="building building-two"></div>
        <div class="person person-one"></div><div class="person person-two"></div><div class="person person-three"></div>
      </div>
    </div>
  </section>
</template>
