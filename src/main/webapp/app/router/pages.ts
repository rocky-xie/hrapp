const Dashboard = () => import('@/core/dashboard/dashboard.vue');
const SkillGapReport = () => import('@/core/reports/skill-gap-report.vue');

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
];
