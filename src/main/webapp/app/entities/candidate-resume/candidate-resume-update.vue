<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.candidateResume.home.createOrEditLabel" data-cy="CandidateResumeCreateUpdateHeading">
          {{ $t('entity.candidateResume.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="candidateResume.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="candidateResume.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.versionNumber') }}</label>
            <input
              type="number"
              class="form-control"
              name="versionNumber"
              id="candidate-resume-versionNumber"
              data-cy="versionNumber"
              :class="{ valid: !v$.versionNumber.$invalid, invalid: v$.versionNumber.$invalid }"
              v-model.number="v$.versionNumber.$model"
              required
            />
            <div v-if="v$.versionNumber.$anyDirty && v$.versionNumber.$invalid">
              <small class="form-text text-danger" v-for="error of v$.versionNumber.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.receivedAt') }}</label>
            <div class="d-flex">
              <input
                id="candidate-resume-receivedAt"
                data-cy="receivedAt"
                type="datetime-local"
                class="form-control"
                name="receivedAt"
                :class="{ valid: !v$.receivedAt.$invalid, invalid: v$.receivedAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.receivedAt.$model)"
                @change="updateInstantField('receivedAt', $event)"
              />
            </div>
            <div v-if="v$.receivedAt.$anyDirty && v$.receivedAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.receivedAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.originalFileName') }}</label>
            <input
              type="text"
              class="form-control"
              name="originalFileName"
              id="candidate-resume-originalFileName"
              data-cy="originalFileName"
              :class="{ valid: !v$.originalFileName.$invalid, invalid: v$.originalFileName.$invalid }"
              v-model="v$.originalFileName.$model"
              required
            />
            <div v-if="v$.originalFileName.$anyDirty && v$.originalFileName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.originalFileName.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.mediaType') }}</label>
            <input
              type="text"
              class="form-control"
              name="mediaType"
              id="candidate-resume-mediaType"
              data-cy="mediaType"
              :class="{ valid: !v$.mediaType.$invalid, invalid: v$.mediaType.$invalid }"
              v-model="v$.mediaType.$model"
            />
            <div v-if="v$.mediaType.$anyDirty && v$.mediaType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.mediaType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.fileSize') }}</label>
            <input
              type="number"
              class="form-control"
              name="fileSize"
              id="candidate-resume-fileSize"
              data-cy="fileSize"
              :class="{ valid: !v$.fileSize.$invalid, invalid: v$.fileSize.$invalid }"
              v-model.number="v$.fileSize.$model"
              required
            />
            <div v-if="v$.fileSize.$anyDirty && v$.fileSize.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fileSize.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.sha256') }}</label>
            <input
              type="text"
              class="form-control"
              name="sha256"
              id="candidate-resume-sha256"
              data-cy="sha256"
              :class="{ valid: !v$.sha256.$invalid, invalid: v$.sha256.$invalid }"
              v-model="v$.sha256.$model"
              required
            />
            <div v-if="v$.sha256.$anyDirty && v$.sha256.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sha256.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.storageKey') }}</label>
            <input
              type="text"
              class="form-control"
              name="storageKey"
              id="candidate-resume-storageKey"
              data-cy="storageKey"
              :class="{ valid: !v$.storageKey.$invalid, invalid: v$.storageKey.$invalid }"
              v-model="v$.storageKey.$model"
              required
            />
            <div v-if="v$.storageKey.$anyDirty && v$.storageKey.$invalid">
              <small class="form-text text-danger" v-for="error of v$.storageKey.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.structuredSummary') }}</label>
            <textarea
              class="form-control"
              name="structuredSummary"
              id="candidate-resume-structuredSummary"
              data-cy="structuredSummary"
              :class="{ valid: !v$.structuredSummary.$invalid, invalid: v$.structuredSummary.$invalid }"
              v-model="v$.structuredSummary.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.activeVersion') }}</label>
            <input
              type="checkbox"
              class="form-control"
              name="activeVersion"
              id="candidate-resume-activeVersion"
              data-cy="activeVersion"
              :class="{ valid: !v$.activeVersion.$invalid, invalid: v$.activeVersion.$invalid }"
              v-model="v$.activeVersion.$model"
              required
            />
            <div v-if="v$.activeVersion.$anyDirty && v$.activeVersion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.activeVersion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.uploadedBy') }}</label>
            <input
              type="text"
              class="form-control"
              name="uploadedBy"
              id="candidate-resume-uploadedBy"
              data-cy="uploadedBy"
              :class="{ valid: !v$.uploadedBy.$invalid, invalid: v$.uploadedBy.$invalid }"
              v-model="v$.uploadedBy.$model"
              required
            />
            <div v-if="v$.uploadedBy.$anyDirty && v$.uploadedBy.$invalid">
              <small class="form-text text-danger" v-for="error of v$.uploadedBy.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-resume">{{ $t('recruitment.fields.candidate') }}</label>
            <select
              class="form-control"
              id="candidate-resume-candidate"
              data-cy="candidate"
              name="candidate"
              v-model="candidateResume.candidate"
            >
              <option :value="null"></option>
              <option
                :value="
                  candidateResume.candidate && externalCandidateOption.id === candidateResume.candidate.id
                    ? candidateResume.candidate
                    : externalCandidateOption
                "
                v-for="externalCandidateOption in externalCandidates"
                :key="externalCandidateOption.id"
              >
                {{ externalCandidateOption.fullName }} — {{ externalCandidateOption.email }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./candidate-resume-update.component.ts"></script>
