<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="skillAssessment">
        <h2 class="jh-entity-heading" data-cy="skillAssessmentDetailsHeading">
          <span>{{ $t('entity.skillAssessment.detail.title') }}</span> {{ skillAssessment.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.skillAssessment.field.assessmentDate') }}</span>
          </dt>
          <dd>
            <span>{{ skillAssessment.assessmentDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.result') }}</span>
          </dt>
          <dd>
            <span>{{ skillAssessment.result }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
          </dt>
          <dd>
            <span>{{ skillAssessment.evidence }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillAssessment.field.comment') }}</span>
          </dt>
          <dd>
            <span>{{ skillAssessment.comment }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="skillAssessment.person">
              <router-link :to="{ name: 'PersonView', params: { personId: skillAssessment.person.id } }">{{
                skillAssessment.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="skillAssessment.skill">
              <router-link :to="{ name: 'SkillView', params: { skillId: skillAssessment.skill.id } }">{{
                skillAssessment.skill.skillName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.assessor') }}</span>
          </dt>
          <dd>
            <div v-if="skillAssessment.assessor">
              <router-link :to="{ name: 'PersonView', params: { personId: skillAssessment.assessor.id } }">{{
                skillAssessment.assessor.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.newLevel') }}</span>
          </dt>
          <dd>
            <div v-if="skillAssessment.newLevel">
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: skillAssessment.newLevel.id } }">{{
                skillAssessment.newLevel.code
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="skillAssessment.id"
          :to="{ name: 'SkillAssessmentEdit', params: { skillAssessmentId: skillAssessment.id } }"
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

<script lang="ts" src="./skill-assessment-details.component.ts"></script>
