<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="candidateResume">
        <h2 class="jh-entity-heading" data-cy="candidateResumeDetailsHeading">
          <span>{{ $t('entity.candidateResume.detail.title') }}</span> {{ candidateResume.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.versionNumber') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.versionNumber }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.receivedAt') }}</span>
          </dt>
          <dd>
            <span v-if="candidateResume.receivedAt">{{ formatDateLong(candidateResume.receivedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.originalFileName') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.originalFileName }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.mediaType') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.mediaType }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.fileSize') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.fileSize }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.sha256') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.sha256 }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.storageKey') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.storageKey }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.structuredSummary') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.structuredSummary }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.activeVersion') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.activeVersion }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.uploadedBy') }}</span>
          </dt>
          <dd>
            <span>{{ candidateResume.uploadedBy }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.candidate') }}</span>
          </dt>
          <dd>
            <div v-if="candidateResume.candidate">
              <router-link :to="{ name: 'ExternalCandidateView', params: { externalCandidateId: candidateResume.candidate.id } }">{{
                candidateResume.candidate.fullName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="candidateResume.id"
          :to="{ name: 'CandidateResumeEdit', params: { candidateResumeId: candidateResume.id } }"
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

<script lang="ts" src="./candidate-resume-details.component.ts"></script>
