<template>
  <div>
    <h2 class="mb-4">
      <font-awesome-icon icon="tachometer-alt" class="me-2" />
      <span>{{ $t('dashboard.title') }}</span>
    </h2>

    <div class="row g-4 mb-4">
      <div class="col-md-6">
        <div class="card h-100">
          <div class="card-header d-flex align-items-center gap-2">
            <font-awesome-icon icon="chart-bar" style="color: #1a5276" />
            <strong>{{ $t('dashboard.summary.title') }}</strong>
          </div>
          <div class="card-body">
            <div v-if="summaryLoading" class="text-center py-3">
              <b-spinner small></b-spinner>
            </div>
            <div v-else class="row text-center g-2">
              <div class="col-4 mb-3">
                <div class="fs-4 fw-bold" style="color: #1a5276">{{ positionCount }}</div>
                <small class="text-muted">{{ $t('dashboard.summary.positions') }}</small>
              </div>
              <div class="col-4 mb-3">
                <div class="fs-4 fw-bold" style="color: #2e86c1">{{ personCount }}</div>
                <small class="text-muted">{{ $t('dashboard.summary.persons') }}</small>
              </div>
              <div class="col-4 mb-3">
                <div class="fs-4 fw-bold" style="color: #28b463">{{ skillCount }}</div>
                <small class="text-muted">{{ $t('dashboard.summary.skills') }}</small>
              </div>
              <div class="col-4">
                <div class="fs-4 fw-bold" style="color: #e74c3c">{{ highRiskCount }}</div>
                <small class="text-muted">{{ $t('dashboard.summary.highRisk') }}</small>
              </div>
              <div class="col-4">
                <div class="fs-4 fw-bold" style="color: #f39c12">{{ trainingCount }}</div>
                <small class="text-muted">{{ $t('dashboard.summary.trainings') }}</small>
              </div>
              <div class="col-4">
                <div class="fs-4 fw-bold" style="color: #8e44ad">{{ substitutionCount }}</div>
                <small class="text-muted">{{ $t('dashboard.summary.substitutions') }}</small>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card h-100">
          <div class="card-header d-flex align-items-center gap-2">
            <font-awesome-icon icon="tasks" style="color: #e67e22" />
            <strong>{{ $t('actionItem.title') }}</strong>
            <span class="ms-auto badge bg-warning">{{ openActionCount }}</span>
          </div>
          <div class="card-body p-0">
            <div v-if="actionItemsLoading" class="text-center py-3">
              <b-spinner small></b-spinner>
            </div>
            <div v-else-if="openActionItems.length === 0" class="text-center py-4 text-muted">
              <font-awesome-icon icon="check-circle" class="fs-3 mb-2" style="color: #28b463" />
              <div>{{ $t('dashboard.actionItems.empty') }}</div>
            </div>
            <table v-else class="table table-hover table-sm mb-0">
              <thead class="table-light">
                <tr>
                  <th>{{ $t('actionItem.description') }}</th>
                  <th>{{ $t('actionItem.priority') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="item in (openActionItems || []).slice(0, 8)"
                  :key="item.id"
                  style="cursor: pointer"
                  @click="navigateTo('/action-items')"
                >
                  <td class="text-truncate" style="max-width: 200px">{{ item.description }}</td>
                  <td>
                    <span
                      v-if="item.priority"
                      class="badge"
                      :class="item.priority === 'P0_CRITICAL' ? 'bg-danger' : item.priority === 'P1_HIGH' ? 'bg-warning' : 'bg-info'"
                      >{{ item.priority }}</span
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="card-footer text-end p-2">
            <router-link to="/action-items" class="btn btn-sm btn-outline-primary">{{ $t('dashboard.actionItems.viewAll') }}</router-link>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card h-100">
          <div class="card-header d-flex align-items-center gap-2">
            <font-awesome-icon icon="exclamation-triangle" style="color: #e74c3c" />
            <strong>{{ $t('dashboard.risk.title') }}</strong>
            <span class="ms-auto text-muted small">{{ $t('dashboard.risk.count', { n: highRiskPositions.length }) }}</span>
          </div>
          <div class="card-body p-0">
            <div v-if="riskLoading" class="text-center py-3">
              <b-spinner small></b-spinner>
            </div>
            <div v-else-if="riskError" class="alert alert-danger m-3">{{ $t('dashboard.error') }}</div>
            <div v-else-if="highRiskPositions.length === 0" class="text-center py-4 text-muted">
              <font-awesome-icon icon="check-circle" class="fs-3 mb-2" style="color: #28b463" />
              <div>{{ $t('dashboard.risk.empty') }}</div>
            </div>
            <table v-else class="table table-hover table-sm mb-0">
              <thead class="table-light">
                <tr>
                  <th>{{ $t('entity.position.detail.title') }}</th>
                  <th>{{ $t('entity.positionRiskEvaluation.field.ownerCount') }}</th>
                  <th>{{ $t('entity.positionRiskEvaluation.field.hasSubstitute') }}</th>
                  <th>{{ $t('entity.positionRiskEvaluation.field.evaluationDate') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="item in highRiskPositions"
                  :key="item.id"
                  style="cursor: pointer"
                  @click="navigateTo(`/position-risk-evaluation/${item.id}/edit`)"
                >
                  <td>{{ item.position?.positionName }}</td>
                  <td>{{ item.ownerCount }}</td>
                  <td>
                    <b-badge :variant="item.hasSubstitute ? 'success' : 'danger'">
                      {{ item.hasSubstitute ? $t('global.yes') : $t('global.no') }}
                    </b-badge>
                  </td>
                  <td>{{ item.evaluationDate }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div class="row g-4 mb-4">
      <div class="col-md-6">
        <div class="card h-100">
          <div class="card-header d-flex align-items-center gap-2">
            <font-awesome-icon icon="exchange-alt" style="color: #f39c12" />
            <strong>{{ $t('dashboard.coverage.title') }}</strong>
            <span class="ms-auto text-muted small">{{ $t('dashboard.coverage.count', { n: coverageGaps.length }) }}</span>
          </div>
          <div class="card-body p-0">
            <div v-if="coverageLoading" class="text-center py-3">
              <b-spinner small></b-spinner>
            </div>
            <div v-else-if="coverageError" class="alert alert-danger m-3">{{ $t('dashboard.error') }}</div>
            <div v-else-if="coverageGaps.length === 0" class="text-center py-4 text-muted">
              <font-awesome-icon icon="check-circle" class="fs-3 mb-2" style="color: #28b463" />
              <div>{{ $t('dashboard.coverage.empty') }}</div>
            </div>
            <table v-else class="table table-hover table-sm mb-0">
              <thead class="table-light">
                <tr>
                  <th>{{ $t('entity.position.detail.title') }}</th>
                  <th>{{ $t('entity.person.detail.title') }}</th>
                  <th>{{ $t('entity.staffSubstitution.field.coverageRate') }}</th>
                  <th>{{ $t('entity.staffSubstitution.field.missingSkills') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="item in coverageGaps"
                  :key="item.id"
                  style="cursor: pointer"
                  @click="navigateTo(`/staff-substitution/${item.id}/edit`)"
                >
                  <td>{{ item.position?.positionName }}</td>
                  <td>{{ item.candidatePerson?.personName }}</td>
                  <td>
                    <span :class="item.coverageRate < 50 ? 'text-danger fw-bold' : 'text-warning fw-bold'"> {{ item.coverageRate }}% </span>
                  </td>
                  <td class="text-truncate" style="max-width: 200px">{{ item.missingSkills || '-' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <div class="card h-100">
          <div class="card-header d-flex align-items-center gap-2">
            <font-awesome-icon icon="calendar-alt" style="color: #8e44ad" />
            <strong>{{ $t('dashboard.review.title') }}</strong>
            <span class="ms-auto text-muted small">{{ $t('dashboard.review.count', { n: dueSkills.length }) }}</span>
          </div>
          <div class="card-body p-0">
            <div v-if="reviewLoading" class="text-center py-3">
              <b-spinner small></b-spinner>
            </div>
            <div v-else-if="reviewError" class="alert alert-danger m-3">{{ $t('dashboard.error') }}</div>
            <div v-else-if="dueSkills.length === 0" class="text-center py-4 text-muted">
              <font-awesome-icon icon="check-circle" class="fs-3 mb-2" style="color: #28b463" />
              <div>{{ $t('dashboard.review.empty') }}</div>
            </div>
            <table v-else class="table table-hover table-sm mb-0">
              <thead class="table-light">
                <tr>
                  <th>{{ $t('entity.person.detail.title') }}</th>
                  <th>{{ $t('entity.skill.detail.title') }}</th>
                  <th>{{ $t('global.entity.field.currentLevel') }}</th>
                  <th>{{ $t('entity.personSkill.field.nextReviewDate') }}</th>
                  <th>{{ $t('dashboard.review.status.label') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in dueSkills" :key="item.id" style="cursor: pointer" @click="navigateTo(`/person/${item.person?.id}/edit`)">
                  <td>{{ item.person?.personName }}</td>
                  <td>{{ item.skill?.skillName }}</td>
                  <td>{{ item.currentLevel?.code }}</td>
                  <td>{{ item.nextReviewDate }}</td>
                  <td>
                    <template v-if="getReviewStatus(item.nextReviewDate).days !== null">
                      <b-badge :variant="getReviewStatus(item.nextReviewDate).variant">
                        <template v-if="getReviewStatus(item.nextReviewDate).days < 0">
                          {{ $t(getReviewStatus(item.nextReviewDate).label) }}
                        </template>
                        <template v-else>
                          {{ $t(getReviewStatus(item.nextReviewDate).label, { n: getReviewStatus(item.nextReviewDate).days }) }}
                        </template>
                      </b-badge>
                    </template>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div class="row mt-4">
      <div class="col-12">
        <div class="card">
          <div class="card-header d-flex align-items-center gap-2">
            <font-awesome-icon icon="sitemap" style="color: #1a5276" />
            <strong>{{ $t('dashboard.successionMap.title') }}</strong>
            <span class="ms-auto text-muted small">{{ $t('dashboard.successionMap.count', { n: successionMap.length }) }}</span>
          </div>
          <div class="card-body p-0">
            <div v-if="successionLoading" class="text-center py-3">
              <b-spinner small></b-spinner>
            </div>
            <div v-else-if="successionError" class="alert alert-danger m-3">{{ $t('dashboard.error') }}</div>
            <div v-else-if="successionMap.length === 0" class="text-center py-4 text-muted">
              <font-awesome-icon icon="info-circle" class="fs-3 mb-2" style="color: #2e86c1" />
              <div>{{ $t('dashboard.successionMap.empty') }}</div>
            </div>
            <table v-else class="table table-hover table-sm mb-0">
              <thead class="table-light">
                <tr>
                  <th>{{ $t('entity.position.detail.title') }}</th>
                  <th>{{ $t('entity.positionAssignment.field.primaryOwner') }}</th>
                  <th>{{ $t('dashboard.successionMap.candidates') }}</th>
                  <th>{{ $t('entity.successionCandidate.field.successionReadiness') }}</th>
                  <th>{{ $t('entity.successionCandidate.field.priority') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="entry in successionMap"
                  :key="entry.positionId"
                  style="cursor: pointer"
                  @click="navigateTo(`/position/${entry.positionId}/edit`)"
                >
                  <td>{{ entry.positionName }}</td>
                  <td>{{ entry.currentOwnerName || '—' }}</td>
                  <td>
                    <span v-for="(c, i) in entry.candidates" :key="c.candidateId">
                      {{ c.candidateName }}<span v-if="i < entry.candidates.length - 1">, </span>
                    </span>
                    <span v-if="!entry.candidates?.length" class="text-muted">—</span>
                  </td>
                  <td>
                    <span v-for="(c, i) in entry.candidates" :key="c.candidateId">
                      <b-badge :variant="readinessBadge(c.readiness)">{{ c.readiness }}</b-badge>
                      <span v-if="i < entry.candidates.length - 1"> </span>
                    </span>
                    <span v-if="!entry.candidates?.length" class="text-muted">—</span>
                  </td>
                  <td>
                    <span v-for="(c, i) in entry.candidates" :key="c.candidateId">
                      #{{ c.priority }}<span v-if="i < entry.candidates.length - 1">, </span>
                    </span>
                    <span v-if="!entry.candidates?.length" class="text-muted">—</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./dashboard.component.ts"></script>
