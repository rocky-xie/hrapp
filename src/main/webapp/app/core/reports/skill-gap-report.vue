<template>
  <div class="d-flex justify-content-center">
    <div class="col-10">
      <h2 class="jh-entity-heading">
        <span>{{ $t('skillGapReport.title') }}</span>
      </h2>

      <div class="card mb-3">
        <div class="card-body">
          <div class="row">
            <div class="col-md-4">
              <label class="form-label">{{ $t('skillGapReport.filter.position') }}</label>
              <select v-model="selectedPositionIds" multiple class="form-select" size="4">
                <option v-for="pos in allPositions" :key="pos.id" :value="pos.id">
                  {{ pos.positionName }}
                </option>
              </select>
            </div>
            <div class="col-md-3">
              <label class="form-label">{{ $t('skillGapReport.filter.importance') }}</label>
              <select v-model="minImportance" class="form-select">
                <option value="">{{ $t('global.all') }}</option>
                <option value="REQUIRED">REQUIRED</option>
                <option value="IMPORTANT">IMPORTANT</option>
                <option value="OPTIONAL">OPTIONAL</option>
              </select>
            </div>
            <div class="col-md-2 d-flex align-items-end">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="includeOwners" v-model="includeOwners" />
                <label class="form-check-label" for="includeOwners">{{ $t('skillGapReport.filter.includeOwners') }}</label>
              </div>
            </div>
            <div class="col-md-2 d-flex align-items-end">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="includeCandidates" v-model="includeCandidates" />
                <label class="form-check-label" for="includeCandidates">{{ $t('skillGapReport.filter.includeCandidates') }}</label>
              </div>
            </div>
            <div class="col-md-1 d-flex align-items-end">
              <button class="btn btn-primary" @click="generateReport" :disabled="loading || selectedPositionIds.length === 0">
                <b-spinner v-if="loading" small></b-spinner>
                <span v-else>{{ $t('skillGapReport.generate') }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="error" class="alert alert-danger">{{ $t('global.error') }}</div>

      <div v-if="report" class="mb-3">
        <div class="alert alert-info">
          {{ $t('skillGapReport.summary', { n: report.totalPositions }) }}
          | {{ $t('skillGapReport.gapCount', { n: totalGaps() }) }}
        </div>

        <div v-for="pos in report.positions" :key="pos.positionId" class="card mb-3">
          <div class="card-header">
            <strong>{{ pos.positionName }}</strong>
            <span v-if="pos.riskLevel" :class="'badge ms-2 ' + riskBadge(pos.riskLevel)">{{ pos.riskLevel }}</span>
            <span class="ms-3 text-muted">{{ pos.totalRequiredSkills }} {{ $t('skillGapReport.requiredSkills') }}</span>
          </div>
          <div class="card-body">
            <h6>{{ $t('skillGapReport.owners') }}</h6>
            <table v-if="pos.owners && pos.owners.length" class="table table-sm">
              <thead>
                <tr>
                  <th>{{ $t('skillGapReport.person') }}</th>
                  <th>{{ $t('skillGapReport.coverage') }}</th>
                  <th>{{ $t('skillGapReport.gapSkills') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="owner in pos.owners" :key="owner.personId">
                  <td>{{ owner.personName }}</td>
                  <td>
                    <span :class="coverageBadge(owner.coverageRate)"
                      >{{ owner.coverageRate }}% ({{ owner.coveredCount }}/{{ owner.totalRequired }})</span
                    >
                  </td>
                  <td>
                    <span v-if="!hasMissingSkill(owner)" class="text-success">{{ $t('skillGapReport.noGaps') }}</span>
                    <span v-else v-for="gap in owner.gaps" :key="gap.skillId" class="d-block">
                      {{ gap.skillName }} ({{ gap.currentLevelCode || '—' }} &lt; {{ gap.requiredLevelCode }})
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
            <div v-else class="text-muted">{{ $t('skillGapReport.noOwners') }}</div>

            <h6 class="mt-3">{{ $t('skillGapReport.candidates') }}</h6>
            <table v-if="pos.candidates && pos.candidates.length" class="table table-sm">
              <thead>
                <tr>
                  <th>{{ $t('skillGapReport.person') }}</th>
                  <th>{{ $t('skillGapReport.coverage') }}</th>
                  <th>{{ $t('skillGapReport.gapSkills') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="cand in pos.candidates" :key="cand.personId">
                  <td>{{ cand.personName }}</td>
                  <td>
                    <span :class="coverageBadge(cand.coverageRate)"
                      >{{ cand.coverageRate }}% ({{ cand.coveredCount }}/{{ cand.totalRequired }})</span
                    >
                  </td>
                  <td>
                    <span v-if="!hasMissingSkill(cand)" class="text-success">{{ $t('skillGapReport.noGaps') }}</span>
                    <span v-else v-for="gap in cand.gaps" :key="gap.skillId" class="d-block">
                      {{ gap.skillName }} ({{ gap.currentLevelCode || '—' }} &lt; {{ gap.requiredLevelCode }})
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
            <div v-else class="text-muted">{{ $t('skillGapReport.noCandidates') }}</div>

            <h6 class="mt-3">{{ $t('skillGapReport.aggregatedGaps') }}</h6>
            <table v-if="pos.aggregatedGaps && pos.aggregatedGaps.length" class="table table-sm">
              <thead>
                <tr>
                  <th>{{ $t('skillGapReport.skill') }}</th>
                  <th>{{ $t('skillGapReport.importance') }}</th>
                  <th>{{ $t('skillGapReport.requiredLevel') }}</th>
                  <th>{{ $t('skillGapReport.deficientCount') }}</th>
                  <th>{{ $t('skillGapReport.maxDeficit') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="agg in pos.aggregatedGaps" :key="agg.skillId">
                  <td>{{ agg.skillName }}</td>
                  <td>
                    <span :class="'badge ' + importanceBadge(agg.importance)">{{ agg.importance }}</span>
                  </td>
                  <td>{{ agg.requiredLevelCode }}</td>
                  <td>{{ agg.totalDeficient }}</td>
                  <td>{{ agg.maxDeficitLevel }}</td>
                </tr>
              </tbody>
            </table>
            <div v-else class="text-muted">{{ $t('skillGapReport.noAggregatedGaps') }}</div>
          </div>
        </div>
      </div>

      <div v-if="suggestions.length" class="card mb-3">
        <div class="card-header">
          <strong>{{ $t('trainingSuggestion.title') }}</strong>
          <span class="ms-2 text-muted">{{ $t('trainingSuggestion.count', { n: suggestions.length }) }}</span>
        </div>
        <div class="card-body">
          <table class="table table-sm">
            <thead>
              <tr>
                <th>{{ $t('skillGapReport.person') }}</th>
                <th>{{ $t('skillGapReport.skill') }}</th>
                <th>{{ $t('trainingSuggestion.current') }}</th>
                <th>{{ $t('trainingSuggestion.target') }}</th>
                <th>{{ $t('trainingSuggestion.priorityLabel') }}</th>
                <th>{{ $t('trainingSuggestion.action') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(s, idx) in suggestions" :key="idx">
                <td>{{ s.personName }}</td>
                <td>{{ s.skillName }}</td>
                <td>{{ s.currentLevelCode || '—' }}</td>
                <td>{{ s.targetLevelCode }}</td>
                <td>
                  <span :class="'badge ' + priorityBadge(s.priority)">{{ $t('trainingSuggestion.priority.' + s.priority) }}</span>
                </td>
                <td>
                  <button v-if="s.status === 'PENDING'" class="btn btn-sm btn-outline-primary" @click="createGoal(s)">
                    {{ $t('trainingSuggestion.createGoal') }}
                  </button>
                  <span v-else class="text-success">{{ $t('trainingSuggestion.created') }}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./skill-gap-report.component.ts"></script>
