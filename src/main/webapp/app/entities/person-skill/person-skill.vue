<template>
  <div>
    <h2 id="page-heading" data-cy="PersonSkillHeading">
      <span id="person-skill">{{ $t('entity.personSkill.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PersonSkillCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-person-skill"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.personSkill.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && personSkills?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.personSkill.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="personSkills?.length > 0">
      <table class="table table-striped" aria-describedby="personSkills">
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
            <th scope="col" @click="changeOrder('nextReviewDate')">
              <span>{{ $t('entity.personSkill.field.nextReviewDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nextReviewDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evidence')">
              <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evidence'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('confidence')">
              <span>{{ $t('entity.personSkill.field.confidence') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'confidence'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('growthDirection')">
              <span>{{ $t('entity.personSkill.field.growthDirection') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'growthDirection'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('skill.skillName')">
              <span>{{ $t('entity.skill.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'skill.skillName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('currentLevel.code')">
              <span>{{ $t('global.entity.field.currentLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'currentLevel.code'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('previousLevel.code')">
              <span>{{ $t('global.entity.field.previousLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'previousLevel.code'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="personSkill in personSkills" :key="personSkill.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PersonSkillView', params: { personSkillId: personSkill.id } }">{{ personSkill.id }}</router-link>
            </td>
            <td>{{ personSkill.assessmentDate }}</td>
            <td>{{ personSkill.nextReviewDate }}</td>
            <td>{{ personSkill.evidence }}</td>
            <td>{{ personSkill.confidence }}</td>
            <td>{{ personSkill.growthDirection }}</td>
            <td>
              <div v-if="personSkill.person">
                <router-link :to="{ name: 'PersonView', params: { personId: personSkill.person.id } }">{{
                  personSkill.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="personSkill.skill">
                <router-link :to="{ name: 'SkillView', params: { skillId: personSkill.skill.id } }">{{
                  personSkill.skill.skillName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="personSkill.currentLevel">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: personSkill.currentLevel.id } }">{{
                  personSkill.currentLevel.code
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="personSkill.previousLevel">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: personSkill.previousLevel.id } }">{{
                  personSkill.previousLevel.code
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PersonSkillView', params: { personSkillId: personSkill.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PersonSkillEdit', params: { personSkillId: personSkill.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(personSkill)"
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
        <span id="hrappApp.personSkill.delete.question" data-cy="personSkillDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-personSkill-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.personSkill.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-personSkill"
            data-cy="entityConfirmDeleteButton"
            @click="removePersonSkill"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="personSkills?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./person-skill.component.ts"></script>
