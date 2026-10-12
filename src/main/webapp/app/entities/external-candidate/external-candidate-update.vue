<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.externalCandidate.home.createOrEditLabel" data-cy="ExternalCandidateCreateUpdateHeading">
          {{ $t('entity.externalCandidate.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="externalCandidate.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="externalCandidate.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.fullName') }}</label>
            <input
              type="text"
              class="form-control"
              name="fullName"
              id="external-candidate-fullName"
              data-cy="fullName"
              :class="{ valid: !v$.fullName.$invalid, invalid: v$.fullName.$invalid }"
              v-model="v$.fullName.$model"
              required
            />
            <div v-if="v$.fullName.$anyDirty && v$.fullName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fullName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.nameReading') }}</label>
            <input
              type="text"
              class="form-control"
              name="nameReading"
              id="external-candidate-nameReading"
              data-cy="nameReading"
              :class="{ valid: !v$.nameReading.$invalid, invalid: v$.nameReading.$invalid }"
              v-model="v$.nameReading.$model"
            />
            <div v-if="v$.nameReading.$anyDirty && v$.nameReading.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nameReading.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.englishName') }}</label>
            <input
              type="text"
              class="form-control"
              name="englishName"
              id="external-candidate-englishName"
              data-cy="englishName"
              :class="{ valid: !v$.englishName.$invalid, invalid: v$.englishName.$invalid }"
              v-model="v$.englishName.$model"
            />
            <div v-if="v$.englishName.$anyDirty && v$.englishName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.englishName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.birthDate') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="external-candidate-birthDate"
                  v-model="v$.birthDate.$model"
                  name="birthDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="external-candidate-birthDate"
                data-cy="birthDate"
                type="text"
                class="form-control"
                name="birthDate"
                :class="{ valid: !v$.birthDate.$invalid, invalid: v$.birthDate.$invalid }"
                v-model="v$.birthDate.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.gender') }}</label>
            <input
              type="text"
              class="form-control"
              name="gender"
              id="external-candidate-gender"
              data-cy="gender"
              :class="{ valid: !v$.gender.$invalid, invalid: v$.gender.$invalid }"
              v-model="v$.gender.$model"
            />
            <div v-if="v$.gender.$anyDirty && v$.gender.$invalid">
              <small class="form-text text-danger" v-for="error of v$.gender.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.phone') }}</label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="external-candidate-phone"
              data-cy="phone"
              :class="{ valid: !v$.phone.$invalid, invalid: v$.phone.$invalid }"
              v-model="v$.phone.$model"
            />
            <div v-if="v$.phone.$anyDirty && v$.phone.$invalid">
              <small class="form-text text-danger" v-for="error of v$.phone.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.email') }}</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="external-candidate-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.address') }}</label>
            <textarea
              class="form-control"
              name="address"
              id="external-candidate-address"
              data-cy="address"
              :class="{ valid: !v$.address.$invalid, invalid: v$.address.$invalid }"
              v-model="v$.address.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.educationSummary') }}</label>
            <textarea
              class="form-control"
              name="educationSummary"
              id="external-candidate-educationSummary"
              data-cy="educationSummary"
              :class="{ valid: !v$.educationSummary.$invalid, invalid: v$.educationSummary.$invalid }"
              v-model="v$.educationSummary.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.workSummary') }}</label>
            <textarea
              class="form-control"
              name="workSummary"
              id="external-candidate-workSummary"
              data-cy="workSummary"
              :class="{ valid: !v$.workSummary.$invalid, invalid: v$.workSummary.$invalid }"
              v-model="v$.workSummary.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.convertedPersonId') }}</label>
            <input
              type="number"
              class="form-control"
              name="convertedPersonId"
              id="external-candidate-convertedPersonId"
              data-cy="convertedPersonId"
              :class="{ valid: !v$.convertedPersonId.$invalid, invalid: v$.convertedPersonId.$invalid }"
              v-model.number="v$.convertedPersonId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.createdAt') }}</label>
            <div class="d-flex">
              <input
                id="external-candidate-createdAt"
                data-cy="createdAt"
                type="datetime-local"
                class="form-control"
                name="createdAt"
                :class="{ valid: !v$.createdAt.$invalid, invalid: v$.createdAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.createdAt.$model)"
                @change="updateInstantField('createdAt', $event)"
              />
            </div>
            <div v-if="v$.createdAt.$anyDirty && v$.createdAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.createdAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="external-candidate">{{ $t('recruitment.fields.updatedAt') }}</label>
            <div class="d-flex">
              <input
                id="external-candidate-updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                class="form-control"
                name="updatedAt"
                :class="{ valid: !v$.updatedAt.$invalid, invalid: v$.updatedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.updatedAt.$model)"
                @change="updateInstantField('updatedAt', $event)"
              />
            </div>
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
<script lang="ts" src="./external-candidate-update.component.ts"></script>
