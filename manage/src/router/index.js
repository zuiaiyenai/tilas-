import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ModuleView from '../views/ModuleView.vue'

const moduleRoute = (path, pageKey) => ({ path, name: pageKey, component: ModuleView, props: { pageKey } })
const routes = [
  { path: '/', redirect: '/index' },
  { path: '/index', name: 'home', component: HomeView },
  moduleRoute('/clazz', 'classes'), moduleRoute('/stu', 'students'), moduleRoute('/dept', 'departments'), moduleRoute('/emp', 'employees'), moduleRoute('/empReport', 'employee-stats'), moduleRoute('/stuReport', 'student-stats'), moduleRoute('/log', 'log-stats'),
  { path: '/home', redirect: '/index' },
  { path: '/:pageKey(classes|students|departments|employees|employee-stats|student-stats|log-stats)', name: 'legacy-module', component: ModuleView, props: true }
]
export default createRouter({ history: createWebHistory(), routes, scrollBehavior: () => ({ top: 0 }) })
