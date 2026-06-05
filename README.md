# HR Capability Map (hrapp)

面向新人培养与人员能力管理的工作台工具。系统围绕**职位（Position）** 和**人员（Person）** 两大核心实体，管理技能定义、技能等级、职位所需技能、任职人员、人员技能、替代关系、继任计划、岗位/人员风险、培训目标/记录、技能评估、信任观察、评价考核等业务数据。

> **当前阶段**：个人试行项目，用于辅助进行人员能力观察、岗位替代风险判断和培养方向整理。输出仅作为个人判断辅助，不作为正式人事裁决依据。

---

## 技术栈

| 层次           | 技术                      |
| -------------- | ------------------------- |
| 前端框架       | Vue 3 + TypeScript        |
| UI 组件        | BootstrapVue Next         |
| 表单验证       | Vuelidate                 |
| 国际化         | vue-i18n@9 (中/日/英)     |
| 构建工具       | Vite + esbuild            |
| 后端框架       | Spring Boot 3.4 / 4.0.6   |
| Java           | JDK 21                    |
| ORM            | Hibernate + JPA           |
| DTO 映射       | MapStruct                 |
| 数据库迁移     | Liquibase                 |
| 数据库（开发） | H2 (LEGACY)               |
| 数据库（生产） | MariaDB                   |
| 代码生成基底   | JHipster 9.1 (JDL → 实体) |

---

## 核心功能

- **职位与技能管理** — 定义职位、技能、技能等级；管理职位所需的技能要求及最低等级
- **人员技能跟踪** — 录入人员技能与等级，记录技能升级/评定变更历史
- **替代关系计算** — 根据职位技能要求与候选人员技能等级计算覆盖率（默认阈值 80%），判断替代是否成立
- **岗位风险评价** — 综合关键属性、任职人数、替代数据、文档状态、依赖度、后继者准备度，输出风险等级（HIGH / MEDIUM / LOW / UNKNOWN）
- **Dashboard** — 系统概览、高风险职位、覆盖缺口、技能复核提醒
- **技能缺口报告** — 多职位技能差距分析，自动生成培训建议（P0-P3 优先级）
- **继任地图** — 职位后继者候选列表与准备度
- **培训闭环** — 培训目标创建、培训记录跟踪、技能评估
- **信任观察与评价** — 记录对人员的信任观察和综合评价

---

## 快速开始

### 前提条件

- JDK 21
- Node.js ≥ 20
- Docker（可选，用于数据库服务）

### 开发模式

```bash
# 安装前端依赖
npm install

# 终端 1：启动后端
./mvnw

# 终端 2：启动前端开发服务器
npm run start
```

浏览器访问 `http://localhost:9000`，API 代理到 `http://localhost:8080`。

### 生产构建

```bash
./mvnw -Pprod clean verify
java -jar target/*.jar
```

---

## 测试

```bash
# 后端测试
./mvnw verify

# 前端测试
npx vitest run
```

---

## 项目结构

```
hrapp/
├── src/main/java/top/btmdc/hr/     # 后端源码
│   ├── domain/                      # JPA 实体 (19 个业务实体)
│   ├── repository/                  # 数据访问层
│   ├── service/                     # 业务逻辑（含替代计算、风险评价、缺口报告）
│   ├── web/rest/                    # REST 控制器
│   └── config/                      # 安全、应用配置
├── src/main/webapp/app/             # 前端源码
│   ├── core/                        # Dashboard、报告页面
│   ├── entities/                    # 实体 CRUD 页面
│   ├── shared/                      # 共享组件与工具
│   └── i18n/                        # 国际化文件 (en/zh-cn/ja)
├── hr-capability-map.jdl            # JHipster JDL 实体定义
├── hr-capability-map-requirements.md # 需求分析文档
└── hr-capability-map-design.md      # 设计与实现文档
```

---

## 实体全景

系统包含 **19 个业务实体**：

| 分组       | 实体                                                                               | 说明             |
| ---------- | ---------------------------------------------------------------------------------- | ---------------- |
| 基础主数据 | Position, Person, Skill, SkillLevel                                                | 核心业务定义     |
| 能力事实   | PersonSkill, PositionSkillRequirement, SkillAssessment, SkillUpgradeRecord         | 技能与评估       |
| 岗位连续性 | PositionAssignment, StaffSubstitution, SuccessionCandidate, PositionRiskEvaluation | 替代、继任、风险 |
| 培训闭环   | TrainingGoal, TrainingRecord, ImprovementPlan                                      | 培训管理         |
| 敏感观察   | Evaluation, TrustObservation, CandidateProfile, PersonRisk, PositionMatch          | 观察与画像       |

---

## 关键业务规则

- **替代覆盖率**：`coveredSkillCount / totalSkillCount × 100%`。REQUIRED 技能任一缺失则覆盖率直接归零
- **风险分级**：基于 7 个输入维度（任职人数、最小人数、关键标记、替代存在、文档状态、依赖度、后继准备度）的决策表
- **训练建议优先级**：P0=缺失 REQUIRED 技能 → P1=REQUIRED 差距≥2级 → P2=IMPORTANT 差距≥1级 → P3=OPTIONAL

---

## 设计文档

参见 [`hr-capability-map-design.md`](./hr-capability-map-design.md) 和 [`hr-capability-map-requirements.md`](./hr-capability-map-requirements.md)。

---

## 许可证

本项目为内部工具，未指定开源许可证。
