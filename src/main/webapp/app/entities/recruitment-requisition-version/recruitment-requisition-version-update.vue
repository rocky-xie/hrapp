<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.recruitmentRequisitionVersion.home.createOrEditLabel" data-cy="RecruitmentRequisitionVersionCreateUpdateHeading">
          {{ $t('entity.recruitmentRequisitionVersion.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="recruitmentRequisitionVersion.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="recruitmentRequisitionVersion.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-version">{{ $t('recruitment.fields.versionNumber') }}</label>
            <input
              type="number"
              class="form-control"
              name="versionNumber"
              id="recruitment-requisition-version-versionNumber"
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
            <label class="form-control-label" for="recruitment-requisition-version">{{ $t('recruitment.fields.snapshotJson') }}</label>
            <textarea
              class="form-control"
              name="snapshotJson"
              id="recruitment-requisition-version-snapshotJson"
              data-cy="snapshotJson"
              :class="{ valid: !v$.snapshotJson.$invalid, invalid: v$.snapshotJson.$invalid }"
              v-model="v$.snapshotJson.$model"
              required
            ></textarea>
            <div v-if="v$.snapshotJson.$anyDirty && v$.snapshotJson.$invalid">
              <small class="form-text text-danger" v-for="error of v$.snapshotJson.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-version">{{ $t('recruitment.fields.lockedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-requisition-version-lockedAt"
                data-cy="lockedAt"
                type="datetime-local"
                class="form-control"
                name="lockedAt"
                :class="{ valid: !v$.lockedAt.$invalid, invalid: v$.lockedAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.lockedAt.$model)"
                @change="updateInstantField('lockedAt', $event)"
              />
            </div>
            <div v-if="v$.lockedAt.$anyDirty && v$.lockedAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.lockedAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-version">{{ $t('recruitment.fields.lockedBy') }}</label>
            <input
              type="text"
              class="form-control"
              name="lockedBy"
              id="recruitment-requisition-version-lockedBy"
              data-cy="lockedBy"
              :class="{ valid: !v$.lockedBy.$invalid, invalid: v$.lockedBy.$invalid }"
              v-model="v$.lockedBy.$model"
              required
            />
            <div v-if="v$.lockedBy.$anyDirty && v$.lockedBy.$invalid">
              <small class="form-text text-danger" v-for="error of v$.lockedBy.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-version">{{ $t('recruitment.fields.changeReason') }}</label>
            <textarea
              class="form-control"
              name="changeReason"
              id="recruitment-requisition-version-changeReason"
              data-cy="changeReason"
              :class="{ valid: !v$.changeReason.$invalid, invalid: v$.changeReason.$invalid }"
              v-model="v$.changeReason.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-version">{{ $t('recruitment.fields.requisition') }}</label>
            <select
              class="form-control"
              id="recruitment-requisition-version-requisition"
              data-cy="requisition"
              name="requisition"
              v-model="recruitmentRequisitionVersion.requisition"
            >
              <option :value="null"></option>
              <option
                :value="
                  recruitmentRequisitionVersion.requisition &&
                  recruitmentRequisitionOption.id === recruitmentRequisitionVersion.requisition.id
                    ? recruitmentRequisitionVersion.requisition
                    : recruitmentRequisitionOption
                "
                v-for="recruitmentRequisitionOption in recruitmentRequisitions"
                :key="recruitmentRequisitionOption.id"
              >
                {{ recruitmentRequisitionOption.title }} — {{ recruitmentRequisitionOption.department }} ({{
                  recruitmentRequisitionOption.requisitionCode
                }})
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
<script lang="ts" src="./recruitment-requisition-version-update.component.ts"></script>
