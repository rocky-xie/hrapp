<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.keyResponsibilityCategory.home.createOrEditLabel" data-cy="KeyResponsibilityCategoryCreateUpdateHeading">
          {{ $t('entity.keyResponsibilityCategory.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="keyResponsibilityCategory.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="keyResponsibilityCategory.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="key-responsibility-category">{{
              $t('entity.keyResponsibilityCategory.field.categoryName')
            }}</label>
            <input
              type="text"
              class="form-control"
              name="categoryName"
              id="key-responsibility-category-categoryName"
              data-cy="categoryName"
              :class="{ valid: !v$.categoryName.$invalid, invalid: v$.categoryName.$invalid }"
              v-model="v$.categoryName.$model"
              required
            />
            <div v-if="v$.categoryName.$anyDirty && v$.categoryName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.categoryName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="key-responsibility-category">{{
              $t('entity.keyResponsibilityCategory.field.examples')
            }}</label>
            <textarea
              class="form-control"
              name="examples"
              id="key-responsibility-category-examples"
              data-cy="examples"
              :class="{ valid: !v$.examples.$invalid, invalid: v$.examples.$invalid }"
              v-model="v$.examples.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="key-responsibility-category">{{
              $t('entity.keyResponsibilityCategory.field.riskFocus')
            }}</label>
            <textarea
              class="form-control"
              name="riskFocus"
              id="key-responsibility-category-riskFocus"
              data-cy="riskFocus"
              :class="{ valid: !v$.riskFocus.$invalid, invalid: v$.riskFocus.$invalid }"
              v-model="v$.riskFocus.$model"
            ></textarea>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./key-responsibility-category-update.component.ts"></script>
