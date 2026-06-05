<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.positionAssignment.home.createOrEditLabel" data-cy="PositionAssignmentCreateUpdateHeading">
          {{ $t('entity.positionAssignment.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="positionAssignment.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="positionAssignment.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-assignment-primaryOwner">{{
              $t('entity.positionAssignment.field.primaryOwner')
            }}</label>
            <b-form-checkbox
              id="position-assignment-primaryOwner"
              name="primaryOwner"
              data-cy="primaryOwner"
              v-model="v$.primaryOwner.$model"
              :class="{ valid: !v$.primaryOwner.$invalid, invalid: v$.primaryOwner.$invalid }"
            >
            </b-form-checkbox>
            <div v-if="v$.primaryOwner.$anyDirty && v$.primaryOwner.$invalid">
              <small class="form-text text-danger" v-for="error of v$.primaryOwner.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-assignment-startDate">{{ $t('entity.improvementPlan.field.startDate') }}</label>
            <b-form-input
              id="position-assignment-startDate"
              data-cy="startDate"
              type="date"
              class="form-control"
              name="startDate"
              :class="{ valid: !v$.startDate.$invalid, invalid: v$.startDate.$invalid }"
              v-model="v$.startDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-assignment-endDate">{{ $t('entity.positionAssignment.field.endDate') }}</label>
            <b-form-input
              id="position-assignment-endDate"
              data-cy="endDate"
              type="date"
              class="form-control"
              name="endDate"
              :class="{ valid: !v$.endDate.$invalid, invalid: v$.endDate.$invalid }"
              v-model="v$.endDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-assignment">{{
              $t('entity.positionAssignment.field.responsibilityScope')
            }}</label>
            <textarea
              class="form-control"
              name="responsibilityScope"
              id="position-assignment-responsibilityScope"
              data-cy="responsibilityScope"
              :class="{ valid: !v$.responsibilityScope.$invalid, invalid: v$.responsibilityScope.$invalid }"
              v-model="v$.responsibilityScope.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-assignment-active">{{ $t('entity.positionAssignment.field.active') }}</label>
            <b-form-checkbox
              id="position-assignment-active"
              name="active"
              data-cy="active"
              v-model="v$.active.$model"
              :class="{ valid: !v$.active.$invalid, invalid: v$.active.$invalid }"
            >
            </b-form-checkbox>
            <div v-if="v$.active.$anyDirty && v$.active.$invalid">
              <small class="form-text text-danger" v-for="error of v$.active.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-assignment">{{ $t('entity.person.detail.title') }}</label>
            <select
              class="form-control"
              id="position-assignment-person"
              data-cy="person"
              name="person"
              v-model="positionAssignment.person"
              required
            >
              <option v-if="!positionAssignment.person" :value="null" selected></option>
              <option
                :value="
                  positionAssignment.person && personOption.id === positionAssignment.person.id ? positionAssignment.person : personOption
                "
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
            <label class="form-control-label" for="position-assignment">{{ $t('entity.position.detail.title') }}</label>
            <select
              class="form-control"
              id="position-assignment-position"
              data-cy="position"
              name="position"
              v-model="positionAssignment.position"
              required
            >
              <option v-if="!positionAssignment.position" :value="null" selected></option>
              <option
                :value="
                  positionAssignment.position && positionOption.id === positionAssignment.position.id
                    ? positionAssignment.position
                    : positionOption
                "
                v-for="positionOption in positions"
                :key="positionOption.id"
              >
                {{ positionOption.positionName }}
              </option>
            </select>
          </div>
          <div v-if="v$.position.$anyDirty && v$.position.$invalid">
            <small class="form-text text-danger" v-for="error of v$.position.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./position-assignment-update.component.ts"></script>
