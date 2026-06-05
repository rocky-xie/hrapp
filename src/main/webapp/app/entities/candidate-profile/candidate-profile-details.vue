<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="candidateProfile">
        <h2 class="jh-entity-heading" data-cy="candidateProfileDetailsHeading">
          <span>{{ $t('entity.candidateProfile.detail.title') }}</span> {{ candidateProfile.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.candidateProfile.field.candidateDate') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.candidateDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.cultivateDirection') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.cultivateDirection }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.stability') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.stability }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.learningAbility') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.learningAbility }}</span>
          </dd>
          <dt>
            <span>Communication Coordination</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.communicationCoordination }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.businessUnderstanding') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.businessUnderstanding }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.responsibility') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.responsibility }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.riskAwareness') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.riskAwareness }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.judgement') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.judgement }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
          </dt>
          <dd>
            <span>{{ candidateProfile.evidence }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="candidateProfile.person">
              <router-link :to="{ name: 'PersonView', params: { personId: candidateProfile.person.id } }">{{
                candidateProfile.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="candidateProfile.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: candidateProfile.position.id } }">{{
                candidateProfile.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.observer') }}</span>
          </dt>
          <dd>
            <div v-if="candidateProfile.observer">
              <router-link :to="{ name: 'PersonView', params: { personId: candidateProfile.observer.id } }">{{
                candidateProfile.observer.personName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="candidateProfile.id"
          :to="{ name: 'CandidateProfileEdit', params: { candidateProfileId: candidateProfile.id } }"
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

<script lang="ts" src="./candidate-profile-details.component.ts"></script>
