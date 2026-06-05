<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.trainingRecord.home.createOrEditLabel" data-cy="TrainingRecordCreateUpdateHeading">
          {{ $t('entity.trainingRecord.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="trainingRecord.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="trainingRecord.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('entity.trainingRecord.field.trainingDate') }}</label>
            <b-form-input
              id="training-record-trainingDate"
              data-cy="trainingDate"
              type="date"
              class="form-control"
              name="trainingDate"
              :class="{ 'is-valid': !v$.trainingDate.$invalid, 'is-invalid': v$.trainingDate.$invalid }"
              v-model="v$.trainingDate.$model"
            />
            <div v-if="v$.trainingDate.$anyDirty && v$.trainingDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.trainingDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('entity.trainingRecord.field.trainingType') }}</label>
            <select
              class="form-control"
              name="trainingType"
              :class="{ valid: !v$.trainingType.$invalid, invalid: v$.trainingType.$invalid }"
              v-model="v$.trainingType.$model"
              id="training-record-trainingType"
              data-cy="trainingType"
              required
            >
              <option v-for="trainingType in trainingTypeValues" :key="trainingType" :value="trainingType">{{ trainingType }}</option>
            </select>
            <div v-if="v$.trainingType.$anyDirty && v$.trainingType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.trainingType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('entity.trainingRecord.field.topic') }}</label>
            <input
              type="text"
              class="form-control"
              name="topic"
              id="training-record-topic"
              data-cy="topic"
              :class="{ valid: !v$.topic.$invalid, invalid: v$.topic.$invalid }"
              v-model="v$.topic.$model"
              required
            />
            <div v-if="v$.topic.$anyDirty && v$.topic.$invalid">
              <small class="form-text text-danger" v-for="error of v$.topic.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('entity.trainingRecord.field.taskDescription') }}</label>
            <textarea
              class="form-control"
              name="taskDescription"
              id="training-record-taskDescription"
              data-cy="taskDescription"
              :class="{ valid: !v$.taskDescription.$invalid, invalid: v$.taskDescription.$invalid }"
              v-model="v$.taskDescription.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('entity.trainingRecord.field.resultDescription') }}</label>
            <textarea
              class="form-control"
              name="resultDescription"
              id="training-record-resultDescription"
              data-cy="resultDescription"
              :class="{ valid: !v$.resultDescription.$invalid, invalid: v$.resultDescription.$invalid }"
              v-model="v$.resultDescription.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('entity.candidateProfile.field.evidence') }}</label>
            <textarea
              class="form-control"
              name="evidence"
              id="training-record-evidence"
              data-cy="evidence"
              :class="{ valid: !v$.evidence.$invalid, invalid: v$.evidence.$invalid }"
              v-model="v$.evidence.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('entity.trainingRecord.field.nextAction') }}</label>
            <textarea
              class="form-control"
              name="nextAction"
              id="training-record-nextAction"
              data-cy="nextAction"
              :class="{ valid: !v$.nextAction.$invalid, invalid: v$.nextAction.$invalid }"
              v-model="v$.nextAction.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record-person">{{ $t('global.entity.field.people') }}</label>
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
                    <th>{{ $t('global.entity.field.position') }}</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(selectedPerson, index) in selectedPeople" :key="selectedPerson.id">
                    <td>{{ selectedPerson.personName }}</td>
                    <td>{{ selectedPerson.currentRole }}</td>
                    <td>{{ trainingRecord.position?.positionName }}</td>
                    <td class="text-end">
                      <button type="button" class="btn btn-outline-danger btn-sm" data-cy="removePerson" @click="removePerson(index)">
                        <font-awesome-icon icon="trash"></font-awesome-icon>
                      </button>
                    </td>
                  </tr>
                  <tr v-if="selectedPeople.length === 0">
                    <td colspan="4" class="text-muted">{{ $t('global.entity.message.noPeopleSelected') }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('global.entity.field.trainingGoal') }}</label>
            <select
              class="form-control"
              id="training-record-trainingGoal"
              data-cy="trainingGoal"
              name="trainingGoal"
              v-model="trainingRecord.trainingGoal"
            >
              <option :value="null"></option>
              <option
                :value="
                  trainingRecord.trainingGoal && trainingGoalOption.id === trainingRecord.trainingGoal.id
                    ? trainingRecord.trainingGoal
                    : trainingGoalOption
                "
                v-for="trainingGoalOption in trainingGoals"
                :key="trainingGoalOption.id"
              >
                {{ trainingGoalOption.goalName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('entity.position.detail.title') }}</label>
            <select class="form-control" id="training-record-position" data-cy="position" name="position" v-model="trainingRecord.position">
              <option :value="null"></option>
              <option
                :value="
                  trainingRecord.position && positionOption.id === trainingRecord.position.id ? trainingRecord.position : positionOption
                "
                v-for="positionOption in positions"
                :key="positionOption.id"
              >
                {{ positionOption.positionName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="training-record">{{ $t('global.entity.field.mentor') }}</label>
            <select class="form-control" id="training-record-mentor" data-cy="mentor" name="mentor" v-model="trainingRecord.mentor">
              <option :value="null"></option>
              <option
                :value="trainingRecord.mentor && personOption.id === trainingRecord.mentor.id ? trainingRecord.mentor : personOption"
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
<script lang="ts" src="./training-record-update.component.ts"></script>
