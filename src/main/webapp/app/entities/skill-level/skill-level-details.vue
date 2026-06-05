<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="skillLevel">
        <h2 class="jh-entity-heading" data-cy="skillLevelDetailsHeading">
          <span>{{ $t('entity.skillLevel.detail.title') }}</span> {{ skillLevel.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.skillLevel.field.code') }}</span>
          </dt>
          <dd>
            <span>{{ skillLevel.code }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillLevel.field.levelName') }}</span>
          </dt>
          <dd>
            <span>{{ skillLevel.levelName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillLevel.field.definition') }}</span>
          </dt>
          <dd>
            <span>{{ skillLevel.definition }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillLevel.field.observableEvidence') }}</span>
          </dt>
          <dd>
            <span>{{ skillLevel.observableEvidence }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillLevel.field.sortOrder') }}</span>
          </dt>
          <dd>
            <span>{{ skillLevel.sortOrder }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="skillLevel.id"
          :to="{ name: 'SkillLevelEdit', params: { skillLevelId: skillLevel.id } }"
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

<script lang="ts" src="./skill-level-details.component.ts"></script>
