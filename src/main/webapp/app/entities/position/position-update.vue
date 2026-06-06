<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.position.home.createOrEditLabel" data-cy="PositionCreateUpdateHeading">
          {{ $t('entity.position.home.createOrEditLabel') }}
        </h2>
        <ul class="nav nav-tabs mb-4">
          <li class="nav-item">
            <a class="nav-link" :class="{ active: activeTab === 'basic' }" @click.prevent="activeTab = 'basic'" href="#">
              {{ $t('entity.position.related.basicInfo') }}
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" :class="{ active: activeTab === 'skills' }" @click.prevent="activeTab = 'skills'" href="#">
              {{ $t('global.entity.section.requiredSkills') }}
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" :class="{ active: activeTab === 'owners' }" @click.prevent="activeTab = 'owners'" href="#">
              {{ $t('global.entity.section.positionOwners') }}
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" :class="{ active: activeTab === 'risk' }" @click.prevent="activeTab = 'risk'" href="#">
              {{ $t('entity.position.related.riskEvaluation') }}
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" :class="{ active: activeTab === 'succession' }" @click.prevent="activeTab = 'succession'" href="#">
              {{ $t('entity.position.related.succession') }}
            </a>
          </li>
        </ul>
        <div v-show="activeTab === 'basic'">
          <div class="mb-3" v-if="position.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="position.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.position.field.positionCode') }}</label>
            <input
              type="text"
              class="form-control"
              name="positionCode"
              id="position-positionCode"
              data-cy="positionCode"
              :class="{ valid: !v$.positionCode.$invalid, invalid: v$.positionCode.$invalid }"
              v-model="v$.positionCode.$model"
              required
            />
            <div v-if="v$.positionCode.$anyDirty && v$.positionCode.$invalid">
              <small class="form-text text-danger" v-for="error of v$.positionCode.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.position.field.positionName') }}</label>
            <input
              type="text"
              class="form-control"
              name="positionName"
              id="position-positionName"
              data-cy="positionName"
              :class="{ valid: !v$.positionName.$invalid, invalid: v$.positionName.$invalid }"
              v-model="v$.positionName.$model"
              required
            />
            <div v-if="v$.positionName.$anyDirty && v$.positionName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.positionName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.position.field.positionType') }}</label>
            <select
              class="form-control"
              name="positionType"
              :class="{ valid: !v$.positionType.$invalid, invalid: v$.positionType.$invalid }"
              v-model="v$.positionType.$model"
              id="position-positionType"
              data-cy="positionType"
              required
            >
              <option v-for="positionType in positionTypeValues" :key="positionType" :value="positionType">{{ positionType }}</option>
            </select>
            <div v-if="v$.positionType.$anyDirty && v$.positionType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.positionType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.position.field.businessImportance') }}</label>
            <select
              class="form-control"
              name="businessImportance"
              :class="{ valid: !v$.businessImportance.$invalid, invalid: v$.businessImportance.$invalid }"
              v-model="v$.businessImportance.$model"
              id="position-businessImportance"
              data-cy="businessImportance"
              required
            >
              <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                {{ importanceLevel }}
              </option>
            </select>
            <div v-if="v$.businessImportance.$anyDirty && v$.businessImportance.$invalid">
              <small class="form-text text-danger" v-for="error of v$.businessImportance.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.position.field.keyPosition') }}</label>
            <b-form-checkbox
              v-model="v$.keyPosition.$model"
              name="keyPosition"
              id="position-keyPosition"
              data-cy="keyPosition"
              :class="{ 'is-valid': !v$.keyPosition.$invalid, 'is-invalid': v$.keyPosition.$invalid }"
              required
            />
            <div v-if="v$.keyPosition.$anyDirty && v$.keyPosition.$invalid">
              <small class="form-text text-danger" v-for="error of v$.keyPosition.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.skill.field.description') }}</label>
            <textarea
              class="form-control"
              name="description"
              id="position-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.position.field.plannedHeadcount') }}</label>
            <input
              type="number"
              class="form-control"
              name="plannedHeadcount"
              id="position-plannedHeadcount"
              data-cy="plannedHeadcount"
              :class="{ valid: !v$.plannedHeadcount.$invalid, invalid: v$.plannedHeadcount.$invalid }"
              v-model.number="v$.plannedHeadcount.$model"
            />
            <div v-if="v$.plannedHeadcount.$anyDirty && v$.plannedHeadcount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.plannedHeadcount.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.position.field.minimumOwnerCount') }}</label>
            <input
              type="number"
              class="form-control"
              name="minimumOwnerCount"
              id="position-minimumOwnerCount"
              data-cy="minimumOwnerCount"
              :class="{ valid: !v$.minimumOwnerCount.$invalid, invalid: v$.minimumOwnerCount.$invalid }"
              v-model.number="v$.minimumOwnerCount.$model"
            />
            <div v-if="v$.minimumOwnerCount.$anyDirty && v$.minimumOwnerCount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.minimumOwnerCount.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.position.field.reviewCycle') }}</label>
            <select
              class="form-control"
              name="reviewCycle"
              :class="{ valid: !v$.reviewCycle.$invalid, invalid: v$.reviewCycle.$invalid }"
              v-model="v$.reviewCycle.$model"
              id="position-reviewCycle"
              data-cy="reviewCycle"
            >
              <option v-for="reviewCycle in reviewCycleValues" :key="reviewCycle" :value="reviewCycle">{{ reviewCycle }}</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position">{{ $t('entity.positionAssignment.field.active') }}</label>
            <b-form-checkbox
              v-model="v$.active.$model"
              name="active"
              id="position-active"
              data-cy="active"
              :class="{ 'is-valid': !v$.active.$invalid, 'is-invalid': v$.active.$invalid }"
              required
            />
            <div v-if="v$.active.$anyDirty && v$.active.$invalid">
              <small class="form-text text-danger" v-for="error of v$.active.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
        </div>
        <div v-show="activeTab === 'skills'">
          <div v-if="position.positionCode" class="alert alert-info py-2 mb-3">
            <strong>{{ position.positionCode }}</strong> — {{ position.positionName }}
          </div>
          <div class="mb-4 pt-3 border-top">
            <div class="d-flex align-items-center justify-content-between mb-2">
              <h3 class="h5 mb-0">{{ $t('global.entity.section.requiredSkills') }}</h3>
              <button
                type="button"
                class="btn btn-outline-primary btn-sm"
                data-cy="addPositionSkillRequirement"
                @click="addPositionSkillRequirement()"
              >
                <font-awesome-icon icon="plus"></font-awesome-icon>&nbsp;<span>{{ $t('global.entity.action.addRequirement') }}</span>
              </button>
            </div>
            <div class="table-responsive">
              <table class="table table-sm align-middle">
                <thead>
                  <tr>
                    <th>{{ $t('entity.skill.detail.title') }}</th>
                    <th>{{ $t('global.entity.field.requiredLevel') }}</th>
                    <th>{{ $t('global.entity.field.preferredLevel') }}</th>
                    <th>{{ $t('entity.positionSkillRequirement.field.importance') }}</th>
                    <th>{{ $t('entity.positionMatch.field.remark') }}</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(requirement, index) in positionSkillRequirements" :key="requirement.id ?? index">
                    <td style="min-width: 12rem">
                      <select class="form-control form-control-sm" v-model="requirement.skill" data-cy="positionRequirementSkill">
                        <option :value="null"></option>
                        <option
                          v-for="skillOption in skills"
                          :key="skillOption.id"
                          :value="requirement.skill && skillOption.id === requirement.skill.id ? requirement.skill : skillOption"
                        >
                          {{ skillOption.skillName }}
                        </option>
                      </select>
                    </td>
                    <td style="min-width: 10rem">
                      <select
                        class="form-control form-control-sm"
                        v-model="requirement.requiredLevel"
                        data-cy="positionRequirementRequiredLevel"
                      >
                        <option :value="null"></option>
                        <option
                          v-for="skillLevelOption in skillLevels"
                          :key="skillLevelOption.id"
                          :value="
                            requirement.requiredLevel && skillLevelOption.id === requirement.requiredLevel.id
                              ? requirement.requiredLevel
                              : skillLevelOption
                          "
                        >
                          {{ skillLevelOption.code }}
                        </option>
                      </select>
                    </td>
                    <td style="min-width: 10rem">
                      <select
                        class="form-control form-control-sm"
                        v-model="requirement.preferredLevel"
                        data-cy="positionRequirementPreferredLevel"
                      >
                        <option :value="null"></option>
                        <option
                          v-for="skillLevelOption in skillLevels"
                          :key="skillLevelOption.id"
                          :value="
                            requirement.preferredLevel && skillLevelOption.id === requirement.preferredLevel.id
                              ? requirement.preferredLevel
                              : skillLevelOption
                          "
                        >
                          {{ skillLevelOption.code }}
                        </option>
                      </select>
                    </td>
                    <td style="min-width: 9rem">
                      <select class="form-control form-control-sm" v-model="requirement.importance" data-cy="positionRequirementImportance">
                        <option
                          v-for="requirementImportance in requirementImportanceValues"
                          :key="requirementImportance"
                          :value="requirementImportance"
                        >
                          {{ requirementImportance }}
                        </option>
                      </select>
                    </td>
                    <td style="min-width: 14rem">
                      <input
                        type="text"
                        class="form-control form-control-sm"
                        v-model="requirement.remark"
                        data-cy="positionRequirementRemark"
                      />
                    </td>
                    <td class="text-end">
                      <button
                        type="button"
                        class="btn btn-outline-danger btn-sm"
                        data-cy="removePositionSkillRequirement"
                        @click="removePositionSkillRequirement(index)"
                      >
                        <font-awesome-icon icon="trash"></font-awesome-icon>
                      </button>
                    </td>
                  </tr>
                  <tr v-if="positionSkillRequirements.length === 0">
                    <td colspan="6" class="text-muted">{{ $t('global.entity.message.noSkillRequirements') }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div v-show="activeTab === 'owners'">
          <div v-if="position.positionCode" class="alert alert-info py-2 mb-3">
            <strong>{{ position.positionCode }}</strong> — {{ position.positionName }}
          </div>
          <div class="mb-4 pt-3 border-top">
            <div class="d-flex align-items-center justify-content-between mb-2">
              <h3 class="h5 mb-0">{{ $t('global.entity.section.positionOwners') }}</h3>
              <button type="button" class="btn btn-outline-primary btn-sm" data-cy="addPositionAssignment" @click="addPositionAssignment()">
                <font-awesome-icon icon="plus"></font-awesome-icon>&nbsp;<span>{{ $t('global.entity.action.addOwner') }}</span>
              </button>
            </div>
            <div class="table-responsive">
              <table class="table table-sm align-middle">
                <thead>
                  <tr>
                    <th>{{ $t('entity.person.detail.title') }}</th>
                    <th>{{ $t('global.entity.field.primary') }}</th>
                    <th>{{ $t('entity.positionAssignment.field.active') }}</th>
                    <th>{{ $t('entity.improvementPlan.field.startDate') }}</th>
                    <th>{{ $t('entity.positionAssignment.field.endDate') }}</th>
                    <th>{{ $t('entity.positionAssignment.field.responsibilityScope') }}</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(assignment, index) in positionAssignments" :key="assignment.id ?? index">
                    <td style="min-width: 12rem">
                      <select class="form-control form-control-sm" v-model="assignment.person" data-cy="positionAssignmentPerson">
                        <option :value="null"></option>
                        <option
                          v-for="personOption in people"
                          :key="personOption.id"
                          :value="assignment.person && personOption.id === assignment.person.id ? assignment.person : personOption"
                        >
                          {{ personOption.personName }}
                        </option>
                      </select>
                    </td>
                    <td class="text-center">
                      <input type="checkbox" v-model="assignment.primaryOwner" data-cy="positionAssignmentPrimaryOwner" />
                    </td>
                    <td class="text-center">
                      <input type="checkbox" v-model="assignment.active" data-cy="positionAssignmentActive" />
                    </td>
                    <td style="min-width: 10rem">
                      <input
                        type="date"
                        class="form-control form-control-sm"
                        v-model="assignment.startDate"
                        data-cy="positionAssignmentStartDate"
                      />
                    </td>
                    <td style="min-width: 10rem">
                      <input
                        type="date"
                        class="form-control form-control-sm"
                        v-model="assignment.endDate"
                        data-cy="positionAssignmentEndDate"
                      />
                    </td>
                    <td style="min-width: 14rem">
                      <input
                        type="text"
                        class="form-control form-control-sm"
                        v-model="assignment.responsibilityScope"
                        data-cy="positionAssignmentResponsibilityScope"
                      />
                    </td>
                    <td class="text-end">
                      <button
                        type="button"
                        class="btn btn-outline-danger btn-sm"
                        data-cy="removePositionAssignment"
                        @click="removePositionAssignment(index)"
                      >
                        <font-awesome-icon icon="trash"></font-awesome-icon>
                      </button>
                    </td>
                  </tr>
                  <tr v-if="positionAssignments.length === 0">
                    <td colspan="7" class="text-muted">{{ $t('global.entity.message.noPositionOwners') }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div v-show="activeTab === 'risk'">
          <div v-if="position.positionCode" class="alert alert-info py-2 mb-3">
            <strong>{{ position.positionCode }}</strong> — {{ position.positionName }}
          </div>
          <fieldset class="border rounded p-3 mb-4">
            <legend class="h5 w-auto px-2 mb-0">{{ $t('entity.positionRiskEvaluation.section.evaluationResult') }}</legend>
            <div class="mb-3">
              <label class="form-control-label" for="risk-evaluationDate">{{ $t('entity.evaluation.field.evaluationDate') }}</label>
              <b-form-input
                id="risk-evaluationDate"
                type="date"
                class="form-control"
                v-model="positionRiskEvaluation.evaluationDate"
                readonly
              />
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="risk-ownerCount">{{ $t('entity.positionRiskEvaluation.field.ownerCount') }}</label>
              <input type="number" class="form-control" id="risk-ownerCount" v-model.number="positionRiskEvaluation.ownerCount" readonly />
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="risk-substitutableOwnerCount">{{
                $t('entity.positionRiskEvaluation.field.substitutableOwnerCount')
              }}</label>
              <input
                type="number"
                class="form-control"
                id="risk-substitutableOwnerCount"
                v-model.number="positionRiskEvaluation.substitutableOwnerCount"
                readonly
              />
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="risk-hasSubstitute">{{
                $t('entity.positionRiskEvaluation.field.hasSubstitute')
              }}</label>
              <b-form-checkbox v-model="positionRiskEvaluation.hasSubstitute" id="risk-hasSubstitute" disabled />
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="risk-riskLevel">{{ $t('entity.positionRiskEvaluation.field.riskLevel') }}</label>
              <input type="text" class="form-control" id="risk-riskLevel" v-model="positionRiskEvaluation.riskLevel" readonly />
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="risk-riskReason">{{ $t('entity.positionRiskEvaluation.field.riskReason') }}</label>
              <textarea class="form-control" id="risk-riskReason" v-model="positionRiskEvaluation.riskReason" readonly></textarea>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="risk-recommendedAction">{{
                $t('entity.positionRiskEvaluation.field.recommendedAction')
              }}</label>
              <textarea
                class="form-control"
                id="risk-recommendedAction"
                v-model="positionRiskEvaluation.recommendedAction"
                readonly
              ></textarea>
            </div>
          </fieldset>
          <fieldset class="border rounded p-3 mb-4">
            <legend class="h5 w-auto px-2 mb-0">{{ $t('global.entity.section.inputFields') }}</legend>
            <div class="mb-3">
              <label class="form-control-label" for="risk-documentStatus">{{
                $t('entity.positionRiskEvaluation.field.documentStatus')
              }}</label>
              <select class="form-control" id="risk-documentStatus" v-model="documentStatusValue" @change="onRiskInputChange">
                <option :value="null"></option>
                <option v-for="docStatus in documentStatusValues" :key="docStatus" :value="docStatus">{{ docStatus }}</option>
              </select>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="risk-customerDependency">{{
                $t('entity.positionRiskEvaluation.field.customerOrSystemDependency')
              }}</label>
              <select
                class="form-control"
                id="risk-customerDependency"
                v-model="customerOrSystemDependencyValue"
                @change="onRiskInputChange"
              >
                <option :value="null"></option>
                <option v-for="impLevel in importanceLevelValues" :key="impLevel" :value="impLevel">{{ impLevel }}</option>
              </select>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="risk-successionReadiness">{{
                $t('entity.positionRiskEvaluation.field.successionReadiness')
              }}</label>
              <select class="form-control" id="risk-successionReadiness" v-model="successionReadinessValue" @change="onRiskInputChange">
                <option :value="null"></option>
                <option v-for="rdnLevel in readinessLevelValues" :key="rdnLevel" :value="rdnLevel">{{ rdnLevel }}</option>
              </select>
            </div>
            <div class="mt-3">
              <button
                type="button"
                class="btn btn-primary"
                :disabled="
                  !position.id || isEvaluating || !documentStatusValue || !customerOrSystemDependencyValue || !successionReadinessValue
                "
                @click="evaluateAndSave"
              >
                <font-awesome-icon icon="save" :spin="isEvaluating"></font-awesome-icon>&nbsp;<span>{{
                  $t('entity.positionRiskEvaluation.action.evaluateAndSave')
                }}</span>
              </button>
            </div>
          </fieldset>
        </div>
        <div v-show="activeTab === 'succession'">
          <div v-if="position.positionCode" class="alert alert-info py-2 mb-3">
            <strong>{{ position.positionCode }}</strong> — {{ position.positionName }}
          </div>
          <div class="mb-4 pt-3 border-top">
            <h3 class="h5 mb-2">{{ $t('entity.position.related.succession') }}</h3>
            <div v-if="successionLoading"><b-spinner small></b-spinner></div>
            <table v-else-if="successionCandidates.length" class="table table-sm">
              <thead>
                <tr>
                  <th>#</th>
                  <th>{{ $t('entity.person.detail.title') }}</th>
                  <th>{{ $t('entity.successionCandidate.field.successionReadiness') }}</th>
                  <th>{{ $t('entity.successionCandidate.field.requiredTraining') }}</th>
                  <th>{{ $t('entity.successionCandidate.field.riskAfterTraining') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="sc in successionCandidates" :key="sc.id">
                  <td>{{ sc.priority }}</td>
                  <td>{{ sc.candidate?.personName }}</td>
                  <td>{{ sc.successionReadiness }}</td>
                  <td>{{ sc.requiredTraining || '—' }}</td>
                  <td>{{ sc.riskAfterTraining || '—' }}</td>
                </tr>
              </tbody>
            </table>
            <div v-else class="text-muted">{{ $t('global.entity.message.noSuccessionCandidates') }}</div>
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
<script lang="ts" src="./position-update.component.ts"></script>
