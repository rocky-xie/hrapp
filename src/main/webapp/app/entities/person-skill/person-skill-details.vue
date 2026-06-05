<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="personSkill">
        <h2 class="jh-entity-heading" data-cy="personSkillDetailsHeading">
          <span>{{ $t('entity.personSkill.detail.title') }}</span> {{ personSkill.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.skillAssessment.field.assessmentDate') }}</span>
          </dt>
          <dd>
            <span>{{ personSkill.assessmentDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personSkill.field.nextReviewDate') }}</span>
          </dt>
          <dd>
            <span>{{ personSkill.nextReviewDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
          </dt>
          <dd>
            <span>{{ personSkill.evidence }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personSkill.field.confidence') }}</span>
          </dt>
          <dd>
            <span>{{ personSkill.confidence }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personSkill.field.growthDirection') }}</span>
          </dt>
          <dd>
            <span>{{ personSkill.growthDirection }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="personSkill.person">
              <router-link :to="{ name: 'PersonView', params: { personId: personSkill.person.id } }">{{
                personSkill.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="personSkill.skill">
              <router-link :to="{ name: 'SkillView', params: { skillId: personSkill.skill.id } }">{{
                personSkill.skill.skillName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.currentLevel') }}</span>
          </dt>
          <dd>
            <div v-if="personSkill.currentLevel">
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: personSkill.currentLevel.id } }">{{
                personSkill.currentLevel.code
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.previousLevel') }}</span>
          </dt>
          <dd>
            <div v-if="personSkill.previousLevel">
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: personSkill.previousLevel.id } }">{{
                personSkill.previousLevel.code
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="personSkill.id"
          :to="{ name: 'PersonSkillEdit', params: { personSkillId: personSkill.id } }"
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

<script lang="ts" src="./person-skill-details.component.ts"></script>
