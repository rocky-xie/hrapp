<template>
  <div class="container-fluid py-3">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <div>
        <h2>{{ $t('recruitment.workflow.title') }}</h2>
        <p class="text-muted mb-0">{{ $t('recruitment.workflow.description') }}</p>
      </div>
      <button class="btn btn-outline-primary" @click="load">{{ $t('recruitment.workflow.refresh') }}</button>
    </div>
    <div v-if="error" class="alert alert-danger" role="alert">{{ error }}</div>
    <div v-if="notice" class="alert alert-info" role="status">{{ notice }}</div>
    <div class="table-responsive">
      <table class="table table-striped align-middle">
        <thead>
          <tr>
            <th>{{ $t('recruitment.workflow.application') }}</th>
            <th>{{ $t('recruitment.workflow.candidate') }}</th>
            <th>{{ $t('recruitment.workflow.requirement') }}</th>
            <th>{{ $t('recruitment.workflow.stage') }}</th>
            <th>{{ $t('recruitment.workflow.status') }}</th>
            <th>{{ $t('recruitment.workflow.actions') }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="application in applications" :key="application.id">
            <td>{{ application.applicationCode }}</td>
            <td>{{ application.candidate?.fullName }}</td>
            <td>{{ application.requisition?.title }}</td>
            <td>{{ application.currentStage ? $t('recruitment.enums.' + application.currentStage) : '—' }}</td>
            <td>{{ $t('recruitment.enums.' + application.status) }}</td>
            <td class="workflow-actions">
              <template v-if="application.status === 'IN_PROGRESS'">
                <button class="btn btn-sm btn-primary" @click="recordStage(application, true)">
                  {{ stageAction(application) }}
                </button>
                <button
                  v-if="
                    !(
                      (application.recruitmentType === 'MID_CAREER' && application.currentStage === 'RESUME_SCREENING') ||
                      (application.recruitmentType === 'MID_CAREER' && application.currentStage === 'WRITTEN_TEST')
                    )
                  "
                  class="btn btn-sm btn-outline-danger ms-1"
                  @click="recordStage(application, false)"
                >
                  {{ $t('recruitment.workflow.reject') }}
                </button>
              </template>
              <template v-else-if="application.status === 'SELECTION_COMPLETED'">
                <template v-if="application.recruitmentType === 'GRADUATE'">
                  <button class="btn btn-sm btn-success" @click="decide(application, 'HIRED')">
                    {{ $t('recruitment.workflow.recordHired') }}
                  </button>
                  <button class="btn btn-sm btn-outline-secondary ms-1" @click="decide(application, 'NOT_HIRED')">
                    {{ $t('recruitment.workflow.recordNotHired') }}
                  </button>
                </template>
                <template v-else>
                  <input
                    v-model="reviewer"
                    class="form-control form-control-sm reviewer-input"
                    :placeholder="$t('recruitment.workflow.executiveReviewer')"
                  />
                  <button class="btn btn-sm btn-success mt-1" @click="decide(application, 'HIRED')">
                    {{ $t('recruitment.workflow.recordHired') }}
                  </button>
                  <button class="btn btn-sm btn-outline-secondary ms-1 mt-1" @click="decide(application, 'NOT_HIRED')">
                    {{ $t('recruitment.workflow.recordNotHired') }}
                  </button>
                  <button class="btn btn-sm btn-outline-primary ms-1 mt-1" @click="decide(application, 'ACCEPT_AS_DISPATCHED')">
                    {{ $t('recruitment.workflow.recordDispatch') }}
                  </button>
                </template>
              </template>
              <button
                v-else-if="application.recruitmentType === 'GRADUATE' && application.decision === 'HIRED' && !application.convertedPersonId"
                class="btn btn-sm btn-primary"
                @click="convert(application)"
              >
                {{ $t('recruitment.workflow.convert') }}
              </button>
              <template
                v-else-if="
                  application.status === 'DECIDED' && (application.decision === 'HIRED' || application.decision === 'ACCEPT_AS_DISPATCHED')
                "
              >
                <input v-model="joinDate" type="date" class="form-control form-control-sm join-date-input" />
                <button class="btn btn-sm btn-outline-primary mt-1" @click="checkIn(application)">
                  {{ $t('recruitment.workflow.checkIn') }}
                </button>
              </template>
            </td>
          </tr>
          <tr v-if="!applications.length">
            <td colspan="6" class="text-center text-muted">{{ $t('recruitment.workflow.empty') }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="workflow-notes">
      <label for="workflow-notes" class="form-label">{{ $t('recruitment.workflow.notes') }}</label>
      <textarea id="workflow-notes" v-model="notes" class="form-control" rows="2"></textarea>
    </div>
  </div>
</template>

<script lang="ts" src="./recruitment-workflow.component.ts"></script>

<style scoped>
.workflow-actions {
  min-width: 24rem;
}
.workflow-notes {
  max-width: 50rem;
}
.reviewer-input,
.join-date-input {
  max-width: 16rem;
}
</style>
