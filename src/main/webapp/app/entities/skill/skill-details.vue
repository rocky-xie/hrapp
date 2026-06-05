<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="skill">
        <h2 class="jh-entity-heading" data-cy="skillDetailsHeading">
          <span>{{ $t('entity.skill.detail.title') }}</span> {{ skill.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.skill.field.skillCode') }}</span>
          </dt>
          <dd>
            <span>{{ skill.skillCode }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.field.skillName') }}</span>
          </dt>
          <dd>
            <span>{{ skill.skillName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.field.skillType') }}</span>
          </dt>
          <dd>
            <span>{{ skill.skillType }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.field.measurableFlag') }}</span>
          </dt>
          <dd>
            <span>{{ skill.measurableFlag }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.field.description') }}</span>
          </dt>
          <dd>
            <span>{{ skill.description }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.field.evidenceType') }}</span>
          </dt>
          <dd>
            <span>{{ skill.evidenceType }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link v-if="skill.id" :to="{ name: 'SkillEdit', params: { skillId: skill.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./skill-details.component.ts"></script>
