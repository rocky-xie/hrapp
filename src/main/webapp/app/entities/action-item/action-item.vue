<template>
  <div class="d-flex justify-content-center">
    <div class="col-10">
      <h2>{{ $t('actionItem.title') }}</h2>
      <div v-if="loading" class="text-center"><b-spinner></b-spinner></div>
      <table v-else class="table table-sm">
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
              <button v-if="item.status === 'OPEN' || item.status === 'IN_PROGRESS'" class="btn btn-sm btn-secondary" @click="cancel(item)">
                {{ $t('actionItem.cancel') }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./action-item.component.ts"></script>
