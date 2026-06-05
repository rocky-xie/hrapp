<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.trainingGoal.home.createOrEditLabel" data-cy="TrainingGoalCreateUpdateHeading">
          {{ $t('entity.trainingGoal.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="trainingGoal.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="trainingGoal.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('entity.trainingGoal.field.goalName') }}</label>
            <input
              type="text"
              class="form-control"
              name="goalName"
              id="training-goal-goalName"
              data-cy="goalName"
              :class="{ valid: !v$.goalName.$invalid, invalid: v$.goalName.$invalid }"
              v-model="v$.goalName.$model"
              required
            />
            <div v-if="v$.goalName.$anyDirty && v$.goalName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.goalName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('entity.trainingGoal.field.goalDescription') }}</label>
            <textarea
              class="form-control"
              name="goalDescription"
              id="training-goal-goalDescription"
              data-cy="goalDescription"
              :class="{ valid: !v$.goalDescription.$invalid, invalid: v$.goalDescription.$invalid }"
              v-model="v$.goalDescription.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('entity.trainingGoal.field.targetLevelDescription') }}</label>
            <textarea
              class="form-control"
              name="targetLevelDescription"
              id="training-goal-targetLevelDescription"
              data-cy="targetLevelDescription"
              :class="{ valid: !v$.targetLevelDescription.$invalid, invalid: v$.targetLevelDescription.$invalid }"
              v-model="v$.targetLevelDescription.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('entity.improvementPlan.field.startDate') }}</label>
            <b-form-input
              id="training-goal-startDate"
              data-cy="startDate"
              type="date"
              class="form-control"
              name="startDate"
              :class="{ 'is-valid': !v$.startDate.$invalid, 'is-invalid': v$.startDate.$invalid }"
              v-model="v$.startDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('entity.improvementPlan.field.targetDate') }}</label>
            <b-form-input
              id="training-goal-targetDate"
              data-cy="targetDate"
              type="date"
              class="form-control"
              name="targetDate"
              :class="{ 'is-valid': !v$.targetDate.$invalid, 'is-invalid': v$.targetDate.$invalid }"
              v-model="v$.targetDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('entity.trainingGoal.field.status') }}</label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="training-goal-status"
              data-cy="status"
              required
            >
              <option v-for="planStatus in planStatusValues" :key="planStatus" :value="planStatus">{{ planStatus }}</option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal-person">{{ $t('global.entity.field.people') }}</label>
            <div class="d-flex gap-2 mb-2">
              <select class="form-control" v-model="personToAdd" data-cy="personToAdd">
                <option :value="null"></option>
                <option v-for="personOption in availablePeople" :key="personOption.id" :value="personOption">
                  {{ personOption.personName }}
                </option>
              </select>
              <button type="button" class="btn btn-outline-primary" data-cy="addPerson" @click="addPerson">
                <font-awesome-icon icon="plus"></font-awesome-icon>&nbsp;{{ $t('global.entity.action.addPerson') }}
              </button>
            </div>
          </div>
          <div class="mb-3">
            <h3 class="h5">{{ $t('global.entity.section.selectedPeople') }}</h3>
            <div class="table-responsive">
              <table class="table table-sm align-middle">
                <thead>
                  <tr>
                    <th>{{ $t('entity.person.field.personName') }}</th>
                    <th>{{ $t('entity.person.field.currentRole') }}</th>
                    <th>{{ $t('global.entity.field.skill') }}</th>
                    <th>{{ $t('global.entity.field.targetLevel') }}</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(selectedPerson, index) in selectedPeople" :key="selectedPerson.id">
                    <td>{{ selectedPerson.personName }}</td>
                    <td>{{ selectedPerson.currentRole }}</td>
                    <td>{{ trainingGoal.skill?.skillName }}</td>
                    <td>{{ trainingGoal.targetLevel?.code }}</td>
                    <td class="text-end">
                      <button type="button" class="btn btn-outline-danger btn-sm" data-cy="removePerson" @click="removePerson(index)">
                        <font-awesome-icon icon="trash"></font-awesome-icon>
                      </button>
                    </td>
                  </tr>
                  <tr v-if="selectedPeople.length === 0">
                    <td colspan="5" class="text-muted">{{ $t('global.entity.message.noPeopleSelected') }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('entity.position.detail.title') }}</label>
            <select class="form-control" id="training-goal-position" data-cy="position" name="position" v-model="trainingGoal.position">
              <option :value="null"></option>
              <option
                :value="trainingGoal.position && positionOption.id === trainingGoal.position.id ? trainingGoal.position : positionOption"
                v-for="positionOption in positions"
                :key="positionOption.id"
              >
                {{ positionOption.positionName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('entity.skill.detail.title') }}</label>
            <select class="form-control" id="training-goal-skill" data-cy="skill" name="skill" v-model="trainingGoal.skill">
              <option :value="null"></option>
              <option
                :value="trainingGoal.skill && skillOption.id === trainingGoal.skill.id ? trainingGoal.skill : skillOption"
                v-for="skillOption in skills"
                :key="skillOption.id"
              >
                {{ skillOption.skillName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-goal">{{ $t('global.entity.field.targetLevel') }}</label>
            <select
              class="form-control"
              id="training-goal-targetLevel"
              data-cy="targetLevel"
              name="targetLevel"
              v-model="trainingGoal.targetLevel"
            >
              <option :value="null"></option>
              <option
                :value="
                  trainingGoal.targetLevel && skillLevelOption.id === trainingGoal.targetLevel.id
                    ? trainingGoal.targetLevel
                    : skillLevelOption
                "
                v-for="skillLevelOption in skillLevels"
                :key="skillLevelOption.id"
              >
                {{ skillLevelOption.code }}
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
<script lang="ts" src="./training-goal-update.component.ts"></script>
