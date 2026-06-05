<template>
  <div class="home-wrapper">
    <div class="row mb-4">
      <div class="col-12 text-center py-4">
        <div class="mb-3">
          <svg width="80" height="80" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="100" height="100" rx="20" fill="#1a5276" />
            <text x="50" y="68" text-anchor="middle" font-family="Arial, sans-serif" font-size="48" font-weight="900" fill="white">B</text>
          </svg>
        </div>
        <h1 class="display-4 fw-bold" style="color: #1a5276">BTMDC</h1>
        <p class="lead text-muted">人力资源能力评价系统</p>
        <p class="text-muted">Human Resource Capability Evaluation Platform</p>
      </div>
    </div>

    <div v-if="authenticated" class="row mb-4">
      <div class="col-12">
        <div class="alert alert-success d-flex align-items-center gap-2">
          <font-awesome-icon icon="user" class="fs-5" />
          <span v-if="username">{{ $t('home.logged.message', { username }) }}</span>
        </div>
      </div>
    </div>

    <div v-if="!authenticated" class="row mb-4">
      <div class="col-12">
        <div class="alert alert-warning d-flex align-items-center gap-2">
          <font-awesome-icon icon="sign-in-alt" class="fs-5" />
          <span>{{ $t('home.login.prompt') }}</span>
          <a class="alert-link ms-1" @click="showLogin()">{{ $t('home.login.signIn') }}</a>
        </div>
        <div class="alert alert-info d-flex align-items-center gap-2">
          <font-awesome-icon icon="user-plus" class="fs-5" />
          <span>{{ $t('home.register.prompt') }}</span>
          <router-link class="alert-link ms-1" to="/register">{{ $t('home.register.link') }}</router-link>
        </div>
      </div>
    </div>

    <div class="row g-4">
      <div class="col-md-4">
        <div class="card home-card h-100">
          <div class="card-body text-center">
            <div class="home-section-icon mb-3">
              <font-awesome-icon icon="users" style="color: #1a5276" />
            </div>
            <h5 class="card-title">{{ $t('home.section.people.title') }}</h5>
            <p class="card-text text-muted">{{ $t('home.section.people.description') }}</p>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card home-card h-100">
          <div class="card-body text-center">
            <div class="home-section-icon mb-3">
              <font-awesome-icon icon="tasks" style="color: #2e86c1" />
            </div>
            <h5 class="card-title">{{ $t('home.section.skills.title') }}</h5>
            <p class="card-text text-muted">{{ $t('home.section.skills.description') }}</p>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card home-card h-100">
          <div class="card-body text-center">
            <div class="home-section-icon mb-3">
              <font-awesome-icon icon="chart-line" style="color: #28b463" />
            </div>
            <h5 class="card-title">{{ $t('home.section.evaluation.title') }}</h5>
            <p class="card-text text-muted">{{ $t('home.section.evaluation.description') }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
