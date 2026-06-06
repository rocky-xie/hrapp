<template>
  <div class="d-flex justify-content-center">
    <div class="col-10">
      <h2>{{ $t('actionItem.title') }}</h2>

      <div v-if="error" class="alert alert-danger alert-dismissible fade show" role="alert">
        {{ error }}
        <button type="button" class="btn-close" @click="error = null"></button>
      </div>

      <div class="mb-3">
        <button class="btn btn-primary" @click="openCreate">
          <font-awesome-icon icon="plus"></font-awesome-icon>
          {{ $t('actionItem.create') }}
        </button>
      </div>

      <!-- Create/Edit Modal -->
      <div v-if="showForm" class="modal d-block" tabindex="-1" style="background-color: rgba(0, 0, 0, 0.5)">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">{{ editingItem ? $t('actionItem.edit') : $t('actionItem.create') }}</h5>
              <button type="button" class="btn-close" @click="closeForm"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label">{{ $t('actionItem.description') }}</label>
                <textarea class="form-control" v-model="form.description" rows="3" required></textarea>
              </div>
              <div class="mb-3">
                <label class="form-label">{{ $t('actionItem.assignee') }}</label>
                <input class="form-control" v-model="form.assignee" />
              </div>
              <div class="mb-3">
                <label class="form-label">{{ $t('actionItem.dueDate') }}</label>
                <input class="form-control" type="date" v-model="form.dueDate" />
              </div>
              <div class="mb-3">
                <label class="form-label">{{ $t('actionItem.priority') }}</label>
                <select class="form-select" v-model="form.priority">
                  <option value="P0_CRITICAL">P0 - Critical</option>
                  <option value="P1_HIGH">P1 - High</option>
                  <option value="P2_MEDIUM">P2 - Medium</option>
                  <option value="P3_LOW">P3 - Low</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">{{ $t('actionItem.sourceType') }}</label>
                <select class="form-select" v-model="form.sourceType">
                  <option value="MANUAL">Manual</option>
                  <option value="HIGH_RISK_POSITION">High Risk Position</option>
                  <option value="SUBSTITUTION_GAP">Substitution Gap</option>
                  <option value="SKILL_REVIEW">Skill Review</option>
                  <option value="TRAINING_GOAL_EXPIRY">Training Goal Expiry</option>
                  <option value="SUCCESSION_REVIEW">Succession Review</option>
                  <option value="SKILL_ASSESSMENT">Skill Assessment</option>
                  <option value="DATA_QUALITY">Data Quality</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">{{ $t('actionItem.note') }}</label>
                <textarea class="form-control" v-model="form.note" rows="2"></textarea>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" @click="closeForm">{{ $t('global.form.cancel') }}</button>
              <button class="btn btn-primary" @click="save" :disabled="saving || !form.description">
                <b-spinner v-if="saving" small></b-spinner>
                {{ $t('global.form.save') }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="loading" class="text-center"><b-spinner></b-spinner></div>
      <table v-else class="table table-sm table-hover">
        <thead>
          <tr>
            <th>{{ $t('actionItem.description') }}</th>
            <th>{{ $t('actionItem.assignee') }}</th>
            <th>{{ $t('actionItem.dueDate') }}</th>
            <th>{{ $t('actionItem.priority') }}</th>
            <th>{{ $t('actionItem.status') }}</th>
            <th>{{ $t('actionItem.sourceType') }}</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in items" :key="item.id">
            <td>{{ item.description }}</td>
            <td>{{ item.assignee || '—' }}</td>
            <td>{{ item.dueDate || '—' }}</td>
            <td>
              <span v-if="item.priority" :class="'badge ' + priorityBadge(item.priority)">{{ item.priority }}</span>
            </td>
            <td>
              <span :class="statusBadge(item.status)">{{ item.status }}</span>
            </td>
            <td>{{ item.sourceType || '—' }}</td>
            <td>
              <button v-if="item.status === 'OPEN'" class="btn btn-sm btn-outline-primary me-1" @click="start(item)">
                {{ $t('actionItem.start') }}
              </button>
              <button
                v-if="item.status === 'OPEN' || item.status === 'IN_PROGRESS'"
                class="btn btn-sm btn-success me-1"
                @click="complete(item)"
              >
                {{ $t('actionItem.complete') }}
              </button>
              <button
                v-if="item.status === 'OPEN' || item.status === 'IN_PROGRESS'"
                class="btn btn-sm btn-secondary me-1"
                @click="cancel(item)"
              >
                {{ $t('actionItem.cancel') }}
              </button>
              <button class="btn btn-sm btn-outline-secondary me-1" @click="openEdit(item)">
                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
              </button>
              <button class="btn btn-sm btn-outline-danger" @click="remove(item)">
                <font-awesome-icon icon="trash"></font-awesome-icon>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./action-item.component.ts"></script>
