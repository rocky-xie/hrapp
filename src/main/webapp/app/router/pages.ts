const Dashboard = () => import('@/core/dashboard/dashboard.vue');
const SkillGapReport = () => import('@/core/reports/skill-gap-report.vue');
const ActionItem = () => import('@/entities/action-item/action-item.vue');
const DataQuality = () => import('@/core/data-quality/data-quality.vue');
const BatchImportExport = () => import('@/core/batch/batch.vue');

export default [
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
  },
  {
    path: '/reports/skill-gaps',
    name: 'SkillGapReport',
    component: SkillGapReport,
  },
  {
    path: '/action-items',
    name: 'ActionItem',
    component: ActionItem,
  },
  {
    path: '/data-quality',
    name: 'DataQuality',
    component: DataQuality,
  },
  {
    path: '/batch',
    name: 'BatchImportExport',
    component: BatchImportExport,
  },
];
