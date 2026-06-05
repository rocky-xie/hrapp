<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="trustObservation">
        <h2 class="jh-entity-heading" data-cy="trustObservationDetailsHeading">
          <span>{{ $t('entity.trustObservation.detail.title') }}</span> {{ trustObservation.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.trustObservation.field.observationDate') }}</span>
          </dt>
          <dd>
            <span>{{ trustObservation.observationDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trustObservation.field.trustStage') }}</span>
          </dt>
          <dd>
            <span>{{ trustObservation.trustStage }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trustObservation.field.observedBehavior') }}</span>
          </dt>
          <dd>
            <span>{{ trustObservation.observedBehavior }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trustObservation.field.positiveSignal') }}</span>
          </dt>
          <dd>
            <span>{{ trustObservation.positiveSignal }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trustObservation.field.riskSignal') }}</span>
          </dt>
          <dd>
            <span>{{ trustObservation.riskSignal }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trustObservation.field.nextObservationPoint') }}</span>
          </dt>
          <dd>
            <span>{{ trustObservation.nextObservationPoint }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="trustObservation.person">
              <router-link :to="{ name: 'PersonView', params: { personId: trustObservation.person.id } }">{{
                trustObservation.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.observer') }}</span>
          </dt>
          <dd>
            <div v-if="trustObservation.observer">
              <router-link :to="{ name: 'PersonView', params: { personId: trustObservation.observer.id } }">{{
                trustObservation.observer.personName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="trustObservation.id"
          :to="{ name: 'TrustObservationEdit', params: { trustObservationId: trustObservation.id } }"
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

<script lang="ts" src="./trust-observation-details.component.ts"></script>
