<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.dispatchReview.home.createOrEditLabel" data-cy="DispatchReviewCreateUpdateHeading">
          {{ $t('entity.dispatchReview.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="dispatchReview.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dispatchReview.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-review">{{ $t('recruitment.fields.reviewedAt') }}</label>
            <div class="d-flex">
              <input
                id="dispatch-review-reviewedAt"
                data-cy="reviewedAt"
                type="datetime-local"
                class="form-control"
                name="reviewedAt"
                :class="{ valid: !v$.reviewedAt.$invalid, invalid: v$.reviewedAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.reviewedAt.$model)"
                @change="updateInstantField('reviewedAt', $event)"
              />
            </div>
            <div v-if="v$.reviewedAt.$anyDirty && v$.reviewedAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.reviewedAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-review">{{ $t('recruitment.fields.reviewerUsername') }}</label>
            <input
              type="text"
              class="form-control"
              name="reviewerUsername"
              id="dispatch-review-reviewerUsername"
              data-cy="reviewerUsername"
              :class="{ valid: !v$.reviewerUsername.$invalid, invalid: v$.reviewerUsername.$invalid }"
              v-model="v$.reviewerUsername.$model"
              required
            />
            <div v-if="v$.reviewerUsername.$anyDirty && v$.reviewerUsername.$invalid">
              <small class="form-text text-danger" v-for="error of v$.reviewerUsername.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-review">{{ $t('recruitment.fields.result') }}</label>
            <textarea
              class="form-control"
              name="result"
              id="dispatch-review-result"
              data-cy="result"
              :class="{ valid: !v$.result.$invalid, invalid: v$.result.$invalid }"
              v-model="v$.result.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-review">{{ $t('recruitment.fields.recommendation') }}</label>
            <input
              type="text"
              class="form-control"
              name="recommendation"
              id="dispatch-review-recommendation"
              data-cy="recommendation"
              :class="{ valid: !v$.recommendation.$invalid, invalid: v$.recommendation.$invalid }"
              v-model="v$.recommendation.$model"
            />
            <div v-if="v$.recommendation.$anyDirty && v$.recommendation.$invalid">
              <small class="form-text text-danger" v-for="error of v$.recommendation.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-review">{{ $t('recruitment.fields.extensionEndDate') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="dispatch-review-extensionEndDate"
                  v-model="v$.extensionEndDate.$model"
                  name="extensionEndDate"
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
                id="dispatch-review-extensionEndDate"
                data-cy="extensionEndDate"
                type="text"
                class="form-control"
                name="extensionEndDate"
                :class="{ valid: !v$.extensionEndDate.$invalid, invalid: v$.extensionEndDate.$invalid }"
                v-model="v$.extensionEndDate.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-review">{{ $t('recruitment.fields.finalDecision') }}</label>
            <input
              type="text"
              class="form-control"
              name="finalDecision"
              id="dispatch-review-finalDecision"
              data-cy="finalDecision"
              :class="{ valid: !v$.finalDecision.$invalid, invalid: v$.finalDecision.$invalid }"
              v-model="v$.finalDecision.$model"
            />
            <div v-if="v$.finalDecision.$anyDirty && v$.finalDecision.$invalid">
              <small class="form-text text-danger" v-for="error of v$.finalDecision.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-review">{{ $t('recruitment.fields.dispatchAssignment') }}</label>
            <select
              class="form-control"
              id="dispatch-review-dispatchAssignment"
              data-cy="dispatchAssignment"
              name="dispatchAssignment"
              v-model="dispatchReview.dispatchAssignment"
            >
              <option :value="null"></option>
              <option
                :value="
                  dispatchReview.dispatchAssignment && dispatchAssignmentOption.id === dispatchReview.dispatchAssignment.id
                    ? dispatchReview.dispatchAssignment
                    : dispatchAssignmentOption
                "
                v-for="dispatchAssignmentOption in dispatchAssignments"
                :key="dispatchAssignmentOption.id"
              >
                {{ dispatchAssignmentOption.department }} — {{ dispatchAssignmentOption.startDate }}
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
<script lang="ts" src="./dispatch-review-update.component.ts"></script>
