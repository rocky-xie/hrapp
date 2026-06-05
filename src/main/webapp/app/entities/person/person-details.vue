<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="person">
        <h2 class="jh-entity-heading" data-cy="personDetailsHeading">
          <span>{{ $t('entity.person.detail.title') }}</span> {{ person.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.person.field.employeeCode') }}</span>
          </dt>
          <dd>
            <span>{{ person.employeeCode }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.personName') }}</span>
          </dt>
          <dd>
            <span>{{ person.personName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.age') }}</span>
          </dt>
          <dd>
            <span>{{ person.age }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.gender') }}</span>
          </dt>
          <dd>
            <span>{{ person.gender }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.department') }}</span>
          </dt>
          <dd>
            <span>{{ person.department }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.currentRole') }}</span>
          </dt>
          <dd>
            <span>{{ person.currentRole }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.employmentStatus') }}</span>
          </dt>
          <dd>
            <span>{{ person.employmentStatus }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.joinDate') }}</span>
          </dt>
          <dd>
            <span>{{ person.joinDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.mentorFlag') }}</span>
          </dt>
          <dd>
            <span>{{ person.mentorFlag }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.coreCandidateFlag') }}</span>
          </dt>
          <dd>
            <span>{{ person.coreCandidateFlag }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.field.note') }}</span>
          </dt>
          <dd>
            <span>{{ person.note }}</span>
          </dd>
        </dl>

        <hr />
        <h4>Training History</h4>
        <div v-if="trainingLoading"><b-spinner small></b-spinner></div>
        <table v-else-if="trainingRecords.length" class="table table-sm">
          <thead>
            <tr>
              <th>{{ $t('entity.trainingRecord.field.trainingDate') }}</th>
              <th>{{ $t('entity.trainingRecord.field.trainingType') }}</th>
              <th>{{ $t('entity.trainingRecord.field.topic') }}</th>
              <th>Mentor</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="rec in trainingRecords" :key="rec.id">
              <td>{{ rec.trainingDate }}</td>
              <td>{{ rec.trainingType }}</td>
              <td>{{ rec.topic }}</td>
              <td>{{ rec.mentorName || '—' }}</td>
            </tr>
          </tbody>
        </table>
        <div v-else class="text-muted">No training records found.</div>

        <hr />
        <h4>Trust Observation Timeline</h4>
        <div v-if="trustLoading"><b-spinner small></b-spinner></div>
        <div v-else-if="trustObservations.length" class="list-group mb-3">
          <div v-for="obs in trustObservations" :key="obs.id" class="list-group-item">
            <div class="d-flex w-100 justify-content-between">
              <h6 class="mb-1">{{ trustStageLabel(obs.trustStage) }}</h6>
              <small>{{ obs.observationDate }}</small>
            </div>
            <p v-if="obs.observedBehavior" class="mb-1">{{ obs.observedBehavior }}</p>
            <small v-if="obs.observer" class="text-muted"
              >{{ $t('entity.trustObservation.field.observer') }}: {{ obs.observer.personName }}</small
            >
          </div>
        </div>
        <div v-else class="text-muted">No trust observations yet.</div>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link v-if="person.id" :to="{ name: 'PersonEdit', params: { personId: person.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./person-details.component.ts"></script>
