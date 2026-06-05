<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="keyResponsibilityCategory">
        <h2 class="jh-entity-heading" data-cy="keyResponsibilityCategoryDetailsHeading">
          <span>{{ $t('entity.keyResponsibilityCategory.detail.title') }}</span> {{ keyResponsibilityCategory.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.keyResponsibilityCategory.field.categoryName') }}</span>
          </dt>
          <dd>
            <span>{{ keyResponsibilityCategory.categoryName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.keyResponsibilityCategory.field.examples') }}</span>
          </dt>
          <dd>
            <span>{{ keyResponsibilityCategory.examples }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.keyResponsibilityCategory.field.riskFocus') }}</span>
          </dt>
          <dd>
            <span>{{ keyResponsibilityCategory.riskFocus }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="keyResponsibilityCategory.id"
          :to="{ name: 'KeyResponsibilityCategoryEdit', params: { keyResponsibilityCategoryId: keyResponsibilityCategory.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./key-responsibility-category-details.component.ts"></script>
