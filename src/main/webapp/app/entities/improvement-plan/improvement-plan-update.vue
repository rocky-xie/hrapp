<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.improvementPlan.home.createOrEditLabel" data-cy="ImprovementPlanCreateUpdateHeading">
          {{ $t('entity.improvementPlan.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="improvementPlan.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="improvementPlan.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.planName') }}</label>
            <input
              type="text"
              class="form-control"
              name="planName"
              id="improvement-plan-planName"
              data-cy="planName"
              :class="{ valid: !v$.planName.$invalid, invalid: v$.planName.$invalid }"
              v-model="v$.planName.$model"
              required
            />
            <div v-if="v$.planName.$anyDirty && v$.planName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.planName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.planStatus') }}</label>
            <select
              class="form-control"
              name="planStatus"
              :class="{ valid: !v$.planStatus.$invalid, invalid: v$.planStatus.$invalid }"
              v-model="v$.planStatus.$model"
              id="improvement-plan-planStatus"
              data-cy="planStatus"
              required
            >
              <option v-for="planStatus in planStatusValues" :key="planStatus" :value="planStatus">{{ planStatus }}</option>
            </select>
            <div v-if="v$.planStatus.$anyDirty && v$.planStatus.$invalid">
              <small class="form-text text-danger" v-for="error of v$.planStatus.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.problemSummary') }}</label>
            <textarea
              class="form-control"
              name="problemSummary"
              id="improvement-plan-problemSummary"
              data-cy="problemSummary"
              :class="{ valid: !v$.problemSummary.$invalid, invalid: v$.problemSummary.$invalid }"
              v-model="v$.problemSummary.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.improvementAction') }}</label>
            <textarea
              class="form-control"
              name="improvementAction"
              id="improvement-plan-improvementAction"
              data-cy="improvementAction"
              :class="{ valid: !v$.improvementAction.$invalid, invalid: v$.improvementAction.$invalid }"
              v-model="v$.improvementAction.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.ownerName') }}</label>
            <input
              type="text"
              class="form-control"
              name="ownerName"
              id="improvement-plan-ownerName"
              data-cy="ownerName"
              :class="{ valid: !v$.ownerName.$invalid, invalid: v$.ownerName.$invalid }"
              v-model="v$.ownerName.$model"
            />
            <div v-if="v$.ownerName.$anyDirty && v$.ownerName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ownerName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.startDate') }}</label>
            <b-form-input
              id="improvement-plan-startDate"
              data-cy="startDate"
              type="date"
              class="form-control"
              name="startDate"
              :class="{ 'is-valid': !v$.startDate.$invalid, 'is-invalid': v$.startDate.$invalid }"
              v-model="v$.startDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.targetDate') }}</label>
            <b-form-input
              id="improvement-plan-targetDate"
              data-cy="targetDate"
              type="date"
              class="form-control"
              name="targetDate"
              :class="{ 'is-valid': !v$.targetDate.$invalid, 'is-invalid': v$.targetDate.$invalid }"
              v-model="v$.targetDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.completionDate') }}</label>
            <b-form-input
              id="improvement-plan-completionDate"
              data-cy="completionDate"
              type="date"
              class="form-control"
              name="completionDate"
              :class="{ 'is-valid': !v$.completionDate.$invalid, 'is-invalid': v$.completionDate.$invalid }"
              v-model="v$.completionDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.improvementPlan.field.reviewResult') }}</label>
            <textarea
              class="form-control"
              name="reviewResult"
              id="improvement-plan-reviewResult"
              data-cy="reviewResult"
              :class="{ valid: !v$.reviewResult.$invalid, invalid: v$.reviewResult.$invalid }"
              v-model="v$.reviewResult.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.position.detail.title') }}</label>
            <select
              class="form-control"
              id="improvement-plan-position"
              data-cy="position"
              name="position"
              v-model="improvementPlan.position"
            >
              <option :value="null"></option>
              <option
                :value="
                  improvementPlan.position && positionOption.id === improvementPlan.position.id ? improvementPlan.position : positionOption
                "
                v-for="positionOption in positions"
                :key="positionOption.id"
              >
                {{ positionOption.positionName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="improvement-plan">{{ $t('entity.skill.detail.title') }}</label>
            <select class="form-control" id="improvement-plan-skill" data-cy="skill" name="skill" v-model="improvementPlan.skill">
              <option :value="null"></option>
              <option
                :value="improvementPlan.skill && skillOption.id === improvementPlan.skill.id ? improvementPlan.skill : skillOption"
                v-for="skillOption in skills"
                :key="skillOption.id"
              >
                {{ skillOption.skillName }}
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
<script lang="ts" src="./improvement-plan-update.component.ts"></script>
