<template>
  <div>
    <h2 id="page-heading" data-cy="SkillAssessmentHeading">
      <span id="skill-assessment">{{ $t('entity.skillAssessment.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'SkillAssessmentCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-skill-assessment"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.skillAssessment.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && skillAssessments?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.skillAssessment.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="skillAssessments?.length > 0">
      <table class="table table-striped" aria-describedby="skillAssessments">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('assessmentDate')">
              <span>{{ $t('entity.skillAssessment.field.assessmentDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'assessmentDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('result')">
              <span>{{ $t('entity.evaluation.field.result') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'result'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evidence')">
              <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evidence'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('comment')">
              <span>{{ $t('entity.skillAssessment.field.comment') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'comment'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('skill.skillName')">
              <span>{{ $t('entity.skill.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'skill.skillName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('assessor.personName')">
              <span>{{ $t('global.entity.field.assessor') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'assessor.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('newLevel.code')">
              <span>{{ $t('global.entity.field.newLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'newLevel.code'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="skillAssessment in skillAssessments" :key="skillAssessment.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SkillAssessmentView', params: { skillAssessmentId: skillAssessment.id } }">{{
                skillAssessment.id
              }}</router-link>
            </td>
            <td>{{ skillAssessment.assessmentDate }}</td>
            <td>{{ skillAssessment.result }}</td>
            <td>{{ skillAssessment.evidence }}</td>
            <td>{{ skillAssessment.comment }}</td>
            <td>
              <div v-if="skillAssessment.person">
                <router-link :to="{ name: 'PersonView', params: { personId: skillAssessment.person.id } }">{{
                  skillAssessment.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="skillAssessment.skill">
                <router-link :to="{ name: 'SkillView', params: { skillId: skillAssessment.skill.id } }">{{
                  skillAssessment.skill.skillName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="skillAssessment.assessor">
                <router-link :to="{ name: 'PersonView', params: { personId: skillAssessment.assessor.id } }">{{
                  skillAssessment.assessor.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="skillAssessment.newLevel">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: skillAssessment.newLevel.id } }">{{
                  skillAssessment.newLevel.code
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SkillAssessmentView', params: { skillAssessmentId: skillAssessment.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SkillAssessmentEdit', params: { skillAssessmentId: skillAssessment.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(skillAssessment)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">{{ $t('global.form.delete') }}</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="hrappApp.skillAssessment.delete.question" data-cy="skillAssessmentDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-skillAssessment-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.skillAssessment.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-skillAssessment"
            data-cy="entityConfirmDeleteButton"
            @click="removeSkillAssessment"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="skillAssessments?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./skill-assessment.component.ts"></script>
