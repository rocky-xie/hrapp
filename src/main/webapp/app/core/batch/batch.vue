<template>
  <div>
    <h2 id="page-heading" data-cy="batchHeading">
      <span v-text="$t('entity.batch.home.title')"></span>
    </h2>

    <div class="row">
      <div class="col-md-6 mb-4" v-for="entity in entities" :key="entity.key">
        <div class="card">
          <div class="card-header">
            <h5 class="mb-0" v-text="entity.label"></h5>
          </div>
          <div class="card-body">
            <div class="d-flex gap-2 mb-3">
              <button class="btn btn-success" @click="exportEntity(entity.key)" :disabled="isExporting">
                <font-awesome-icon icon="file-export"></font-awesome-icon>
                <span class="ms-1" v-text="$t('entity.batch.action.export')"></span>
              </button>
            </div>
            <div class="d-flex gap-2 align-items-center">
              <label class="btn btn-primary mb-0" :class="{ disabled: isImporting }">
                <font-awesome-icon icon="file-import"></font-awesome-icon>
                <span class="ms-1" v-text="$t('entity.batch.action.import')"></span>
                <input
                  type="file"
                  class="d-none"
                  accept=".xlsx,.xls"
                  @change="e => onImportFileChange(e, entity.key)"
                  :disabled="isImporting"
                />
              </label>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="importResult" class="alert alert-info mt-3">
      <span v-text="importResult"></span>
    </div>
  </div>
</template>

<script lang="ts" src="./batch.component.ts"></script>
