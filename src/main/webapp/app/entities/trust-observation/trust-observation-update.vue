<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.trustObservation.home.createOrEditLabel" data-cy="TrustObservationCreateUpdateHeading">
          {{ $t('entity.trustObservation.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="trustObservation.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="trustObservation.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="trust-observation">{{ $t('entity.trustObservation.field.observationDate') }}</label>
            <b-form-input
              id="trust-observation-observationDate"
              data-cy="observationDate"
              type="date"
              class="form-control"
              name="observationDate"
              :class="{ 'is-valid': !v$.observationDate.$invalid, 'is-invalid': v$.observationDate.$invalid }"
              v-model="v$.observationDate.$model"
            />
            <div v-if="v$.observationDate.$anyDirty && v$.observationDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.observationDate.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="trust-observation">{{ $t('entity.trustObservation.field.trustStage') }}</label>
            <select
              class="form-control"
              name="trustStage"
              :class="{ valid: !v$.trustStage.$invalid, invalid: v$.trustStage.$invalid }"
              v-model="v$.trustStage.$model"
              id="trust-observation-trustStage"
              data-cy="trustStage"
              required
            >
              <option v-for="trustStage in trustStageValues" :key="trustStage" :value="trustStage">{{ trustStage }}</option>
            </select>
            <div v-if="v$.trustStage.$anyDirty && v$.trustStage.$invalid">
              <small class="form-text text-danger" v-for="error of v$.trustStage.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="trust-observation">{{ $t('entity.trustObservation.field.observedBehavior') }}</label>
            <textarea
              class="form-control"
              name="observedBehavior"
              id="trust-observation-observedBehavior"
              data-cy="observedBehavior"
              :class="{ valid: !v$.observedBehavior.$invalid, invalid: v$.observedBehavior.$invalid }"
              v-model="v$.observedBehavior.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="trust-observation">{{ $t('entity.trustObservation.field.positiveSignal') }}</label>
            <textarea
              class="form-control"
              name="positiveSignal"
              id="trust-observation-positiveSignal"
              data-cy="positiveSignal"
              :class="{ valid: !v$.positiveSignal.$invalid, invalid: v$.positiveSignal.$invalid }"
              v-model="v$.positiveSignal.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="trust-observation">{{ $t('entity.trustObservation.field.riskSignal') }}</label>
            <textarea
              class="form-control"
              name="riskSignal"
              id="trust-observation-riskSignal"
              data-cy="riskSignal"
              :class="{ valid: !v$.riskSignal.$invalid, invalid: v$.riskSignal.$invalid }"
              v-model="v$.riskSignal.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="trust-observation">{{ $t('entity.trustObservation.field.nextObservationPoint') }}</label>
            <textarea
              class="form-control"
              name="nextObservationPoint"
              id="trust-observation-nextObservationPoint"
              data-cy="nextObservationPoint"
              :class="{ valid: !v$.nextObservationPoint.$invalid, invalid: v$.nextObservationPoint.$invalid }"
              v-model="v$.nextObservationPoint.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="trust-observation">{{ $t('entity.person.detail.title') }}</label>
            <select
              class="form-control"
              id="trust-observation-person"
              data-cy="person"
              name="person"
              v-model="trustObservation.person"
              required
            >
              <option v-if="!trustObservation.person" :value="null" selected></option>
              <option
                :value="trustObservation.person && personOption.id === trustObservation.person.id ? trustObservation.person : personOption"
                v-for="personOption in people"
                :key="personOption.id"
              >
                {{ personOption.personName }}
              </option>
            </select>
          </div>
          <div v-if="v$.person.$anyDirty && v$.person.$invalid">
            <small class="form-text text-danger" v-for="error of v$.person.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="trust-observation">{{ $t('global.entity.field.observer') }}</label>
            <select
              class="form-control"
              id="trust-observation-observer"
              data-cy="observer"
              name="observer"
              v-model="trustObservation.observer"
            >
              <option :value="null"></option>
              <option
                :value="
                  trustObservation.observer && personOption.id === trustObservation.observer.id ? trustObservation.observer : personOption
                "
                v-for="personOption in people"
                :key="personOption.id"
              >
                {{ personOption.personName }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./trust-observation-update.component.ts"></script>
