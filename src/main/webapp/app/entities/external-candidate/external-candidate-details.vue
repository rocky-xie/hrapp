<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="externalCandidate">
        <h2 class="jh-entity-heading" data-cy="externalCandidateDetailsHeading">
          <span>{{ $t('entity.externalCandidate.detail.title') }}</span> {{ externalCandidate.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.fullName') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.fullName }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.nameReading') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.nameReading }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.englishName') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.englishName }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.birthDate') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.birthDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.gender') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.gender }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.phone') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.phone }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.email') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.email }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.address') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.address }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.educationSummary') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.educationSummary }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.workSummary') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.workSummary }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.convertedPersonId') }}</span>
          </dt>
          <dd>
            <span>{{ externalCandidate.convertedPersonId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.createdAt') }}</span>
          </dt>
          <dd>
            <span v-if="externalCandidate.createdAt">{{ formatDateLong(externalCandidate.createdAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.updatedAt') }}</span>
          </dt>
          <dd>
            <span v-if="externalCandidate.updatedAt">{{ formatDateLong(externalCandidate.updatedAt) }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="externalCandidate.id"
          :to="{ name: 'ExternalCandidateEdit', params: { externalCandidateId: externalCandidate.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./external-candidate-details.component.ts"></script>
