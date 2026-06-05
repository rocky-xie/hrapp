<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="successionCandidate">
        <h2 class="jh-entity-heading" data-cy="successionCandidateDetailsHeading">
          <span>{{ $t('entity.successionCandidate.detail.title') }}</span> {{ successionCandidate.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.successionCandidate.field.successionReadiness') }}</span>
          </dt>
          <dd>
            <span>{{ successionCandidate.successionReadiness }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.successionCandidate.field.requiredTraining') }}</span>
          </dt>
          <dd>
            <span>{{ successionCandidate.requiredTraining }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.successionCandidate.field.estimatedTimeToReady') }}</span>
          </dt>
          <dd>
            <span>{{ successionCandidate.estimatedTimeToReady }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.successionCandidate.field.riskAfterTraining') }}</span>
          </dt>
          <dd>
            <span>{{ successionCandidate.riskAfterTraining }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.successionCandidate.field.reviewDate') }}</span>
          </dt>
          <dd>
            <span>{{ successionCandidate.reviewDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.successionCandidate.field.priority') }}</span>
          </dt>
          <dd>
            <span>{{ successionCandidate.priority }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="successionCandidate.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: successionCandidate.position.id } }">{{
                successionCandidate.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.currentOwner') }}</span>
          </dt>
          <dd>
            <div v-if="successionCandidate.currentOwner">
              <router-link :to="{ name: 'PersonView', params: { personId: successionCandidate.currentOwner.id } }">{{
                successionCandidate.currentOwner.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.candidate') }}</span>
          </dt>
          <dd>
            <div v-if="successionCandidate.candidate">
              <router-link :to="{ name: 'PersonView', params: { personId: successionCandidate.candidate.id } }">{{
                successionCandidate.candidate.personName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="successionCandidate.id"
          :to="{ name: 'SuccessionCandidateEdit', params: { successionCandidateId: successionCandidate.id } }"
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

<script lang="ts" src="./succession-candidate-details.component.ts"></script>
