<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="staffSubstitution">
        <h2 class="jh-entity-heading" data-cy="staffSubstitutionDetailsHeading">
          <span>{{ $t('entity.staffSubstitution.detail.title') }}</span> {{ staffSubstitution.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.coverageRate') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.coverageRate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.thresholdRate') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.thresholdRate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.totalSkillCount') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.totalSkillCount }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.coveredSkillCount') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.coveredSkillCount }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.missingSkills') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.missingSkills }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.substitutable') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.substitutable }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.evaluationDate') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.evaluationDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.reviewDate') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.reviewDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.expiryDate') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.expiryDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.reason') }}</span>
          </dt>
          <dd>
            <span>{{ staffSubstitution.reason }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="staffSubstitution.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: staffSubstitution.position.id } }">{{
                staffSubstitution.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.candidatePerson') }}</span>
          </dt>
          <dd>
            <div v-if="staffSubstitution.candidatePerson">
              <router-link :to="{ name: 'PersonView', params: { personId: staffSubstitution.candidatePerson.id } }">{{
                staffSubstitution.candidatePerson.personName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="staffSubstitution.id"
          :to="{ name: 'StaffSubstitutionEdit', params: { staffSubstitutionId: staffSubstitution.id } }"
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

<script lang="ts" src="./staff-substitution-details.component.ts"></script>
