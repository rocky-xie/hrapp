<template>
  <div>
    <h2 id="page-heading">
      <span v-text="$t('entity.dataQuality.home.title')"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info" @click="runChecks" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('entity.dataQuality.action.runChecks')"></span>
        </button>
      </div>
    </h2>

    <div class="mb-3">
      <ul class="nav nav-tabs">
        <li class="nav-item">
          <a class="nav-link" :class="{ active: activeTab === 'all' }" @click="activeTab = 'all'" href="#">
            {{ $t('entity.dataQuality.tab.all') }} ({{ issues.length }})
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: activeTab === 'ERROR' }" @click="activeTab = 'ERROR'" href="#">
            <span class="badge bg-danger me-1">{{ errorCount() }}</span
            >{{ $t('entity.dataQuality.severity.error') }}
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: activeTab === 'WARNING' }" @click="activeTab = 'WARNING'" href="#">
            <span class="badge bg-warning text-dark me-1">{{ warningCount() }}</span
            >{{ $t('entity.dataQuality.severity.warning') }}
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: activeTab === 'INFO' }" @click="activeTab = 'INFO'" href="#">
            <span class="badge bg-info me-1">{{ infoCount() }}</span
            >{{ $t('entity.dataQuality.severity.info') }}
          </a>
        </li>
      </ul>
    </div>

    <div class="alert alert-warning" v-if="!isFetching && issues.length === 0">
      <span v-text="$t('entity.dataQuality.message.noIssues')"></span>
    </div>

    <div class="table-responsive" v-if="filteredIssues().length > 0">
      <table class="table table-striped" aria-describedby="data-quality-issues">
        <thead>
          <tr>
            <th scope="col" v-text="$t('entity.dataQuality.field.severity')"></th>
            <th scope="col" v-text="$t('entity.dataQuality.field.entityType')"></th>
            <th scope="col" v-text="$t('entity.dataQuality.field.entityLabel')"></th>
            <th scope="col" v-text="$t('entity.dataQuality.field.message')"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="issue in filteredIssues()" :key="issue.entityType + '-' + issue.entityId + '-' + issue.field" data-cy="issueRow">
            <td>
              <span class="badge" :class="severityBadgeClass(issue.severity)" v-text="issue.severity"></span>
            </td>
            <td>
              <font-awesome-icon :icon="entityTypeIcon(issue.entityType)" class="me-1"></font-awesome-icon>
              <span v-text="issue.entityType"></span>
            </td>
            <td>
              <span v-text="issue.entityLabel"></span>
              <small class="text-muted ms-1">(#{{ issue.entityId }})</small>
            </td>
            <td v-text="issue.message"></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./data-quality.component.ts"></script>
