<template>
  <div>
    <div class="row-md">
      <div class="col-md-3 text-center">
        <svg width="120" height="120" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
          <rect width="100" height="100" rx="20" fill="#dc3545" opacity="0.15" />
          <text x="50" y="68" text-anchor="middle" font-family="Arial, sans-serif" font-size="48" font-weight="900" fill="#dc3545">!</text>
        </svg>
      </div>
      <div class="col-md-9">
        <h1>Error page!</h1>

        <div v-if="errorMessage">
          <div class="alert alert-danger">{{ errorMessage }}</div>
        </div>
        <div v-if="error403" class="alert alert-danger">You are not authorized to access this page.</div>
        <div v-if="error404" class="alert alert-warning">The page does not exist.</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./error.component.ts"></script>
