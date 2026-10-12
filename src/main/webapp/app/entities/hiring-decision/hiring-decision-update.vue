<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.hiringDecision.home.createOrEditLabel" data-cy="HiringDecisionCreateUpdateHeading">
          {{ $t('entity.hiringDecision.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="hiringDecision.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="hiringDecision.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="hiring-decision">{{ $t('recruitment.fields.decision') }}</label>
            <select
              class="form-control"
              name="decision"
              :class="{ valid: !v$.decision.$invalid, invalid: v$.decision.$invalid }"
              v-model="v$.decision.$model"
              id="hiring-decision-decision"
              data-cy="decision"
              required
            >
              <option v-for="applicationDecision in applicationDecisionValues" :key="applicationDecision" :value="applicationDecision">
                {{ $t('recruitment.enums.' + applicationDecision) }}
              </option>
            </select>
            <div v-if="v$.decision.$anyDirty && v$.decision.$invalid">
              <small class="form-text text-danger" v-for="error of v$.decision.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="hiring-decision">{{ $t('recruitment.fields.decisionSource') }}</label>
            <input
              type="text"
              class="form-control"
              name="decisionSource"
              id="hiring-decision-decisionSource"
              data-cy="decisionSource"
              :class="{ valid: !v$.decisionSource.$invalid, invalid: v$.decisionSource.$invalid }"
              v-model="v$.decisionSource.$model"
              required
            />
            <div v-if="v$.decisionSource.$anyDirty && v$.decisionSource.$invalid">
              <small class="form-text text-danger" v-for="error of v$.decisionSource.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="hiring-decision">{{ $t('recruitment.fields.decidedBy') }}</label>
            <input
              type="text"
              class="form-control"
              name="decidedBy"
              id="hiring-decision-decidedBy"
              data-cy="decidedBy"
              :class="{ valid: !v$.decidedBy.$invalid, invalid: v$.decidedBy.$invalid }"
              v-model="v$.decidedBy.$model"
              required
            />
            <div v-if="v$.decidedBy.$anyDirty && v$.decidedBy.$invalid">
              <small class="form-text text-danger" v-for="error of v$.decidedBy.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="hiring-decision">{{ $t('recruitment.fields.decidedAt') }}</label>
            <div class="d-flex">
              <input
                id="hiring-decision-decidedAt"
                data-cy="decidedAt"
                type="datetime-local"
                class="form-control"
                name="decidedAt"
                :class="{ valid: !v$.decidedAt.$invalid, invalid: v$.decidedAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.decidedAt.$model)"
                @change="updateInstantField('decidedAt', $event)"
              />
            </div>
            <div v-if="v$.decidedAt.$anyDirty && v$.decidedAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.decidedAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="hiring-decision">{{ $t('recruitment.fields.reason') }}</label>
            <textarea
              class="form-control"
              name="reason"
              id="hiring-decision-reason"
              data-cy="reason"
              :class="{ valid: !v$.reason.$invalid, invalid: v$.reason.$invalid }"
              v-model="v$.reason.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="hiring-decision">{{ $t('recruitment.fields.correctionOfId') }}</label>
            <input
              type="number"
              class="form-control"
              name="correctionOfId"
              id="hiring-decision-correctionOfId"
              data-cy="correctionOfId"
              :class="{ valid: !v$.correctionOfId.$invalid, invalid: v$.correctionOfId.$invalid }"
              v-model.number="v$.correctionOfId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="hiring-decision">{{ $t('recruitment.fields.active') }}</label>
            <input
              type="checkbox"
              class="form-control"
              name="active"
              id="hiring-decision-active"
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
            <label class="form-control-label" for="hiring-decision">{{ $t('recruitment.fields.application') }}</label>
            <select
              class="form-control"
              id="hiring-decision-application"
              data-cy="application"
              name="application"
              v-model="hiringDecision.application"
            >
              <option :value="null"></option>
              <option
                :value="
                  hiringDecision.application && recruitmentApplicationOption.id === hiringDecision.application.id
                    ? hiringDecision.application
                    : recruitmentApplicationOption
                "
                v-for="recruitmentApplicationOption in recruitmentApplications"
                :key="recruitmentApplicationOption.id"
              >
                {{ recruitmentApplicationOption.applicationCode }}
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
<script lang="ts" src="./hiring-decision-update.component.ts"></script>
