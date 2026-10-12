<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.recruitmentRequisitionMember.home.createOrEditLabel" data-cy="RecruitmentRequisitionMemberCreateUpdateHeading">
          {{ $t('entity.recruitmentRequisitionMember.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="recruitmentRequisitionMember.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="recruitmentRequisitionMember.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-member">{{ $t('recruitment.fields.username') }}</label>
            <input
              type="text"
              class="form-control"
              name="username"
              id="recruitment-requisition-member-username"
              data-cy="username"
              :class="{ valid: !v$.username.$invalid, invalid: v$.username.$invalid }"
              v-model="v$.username.$model"
              required
            />
            <div v-if="v$.username.$anyDirty && v$.username.$invalid">
              <small class="form-text text-danger" v-for="error of v$.username.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-member">{{ $t('recruitment.fields.memberType') }}</label>
            <input
              type="text"
              class="form-control"
              name="memberType"
              id="recruitment-requisition-member-memberType"
              data-cy="memberType"
              :class="{ valid: !v$.memberType.$invalid, invalid: v$.memberType.$invalid }"
              v-model="v$.memberType.$model"
              required
            />
            <div v-if="v$.memberType.$anyDirty && v$.memberType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.memberType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-member">{{ $t('recruitment.fields.active') }}</label>
            <input
              type="checkbox"
              class="form-control"
              name="active"
              id="recruitment-requisition-member-active"
              data-cy="active"
              :class="{ valid: !v$.active.$invalid, invalid: v$.active.$invalid }"
              v-model="v$.active.$model"
              required
            />
            <div v-if="v$.active.$anyDirty && v$.active.$invalid">
              <small class="form-text text-danger" v-for="error of v$.active.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-member">{{ $t('recruitment.fields.createdAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-requisition-member-createdAt"
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
            <label class="form-control-label" for="recruitment-requisition-member">{{ $t('recruitment.fields.removedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-requisition-member-removedAt"
                data-cy="removedAt"
                type="datetime-local"
                class="form-control"
                name="removedAt"
                :class="{ valid: !v$.removedAt.$invalid, invalid: v$.removedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.removedAt.$model)"
                @change="updateInstantField('removedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition-member">{{ $t('recruitment.fields.requisition') }}</label>
            <select
              class="form-control"
              id="recruitment-requisition-member-requisition"
              data-cy="requisition"
              name="requisition"
              v-model="recruitmentRequisitionMember.requisition"
            >
              <option :value="null"></option>
              <option
                :value="
                  recruitmentRequisitionMember.requisition &&
                  recruitmentRequisitionOption.id === recruitmentRequisitionMember.requisition.id
                    ? recruitmentRequisitionMember.requisition
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
<script lang="ts" src="./recruitment-requisition-member-update.component.ts"></script>
