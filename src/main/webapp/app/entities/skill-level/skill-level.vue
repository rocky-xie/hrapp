<template>
  <div>
    <h2 id="page-heading" data-cy="SkillLevelHeading">
      <span id="skill-level">{{ $t('entity.skillLevel.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'SkillLevelCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-skill-level"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.skillLevel.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && skillLevels?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.skillLevel.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="skillLevels?.length > 0">
      <table class="table table-striped" aria-describedby="skillLevels">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('code')">
              <span>{{ $t('entity.skillLevel.field.code') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'code'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('levelName')">
              <span>{{ $t('entity.skillLevel.field.levelName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'levelName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('definition')">
              <span>{{ $t('entity.skillLevel.field.definition') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'definition'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observableEvidence')">
              <span>{{ $t('entity.skillLevel.field.observableEvidence') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observableEvidence'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sortOrder')">
              <span>{{ $t('entity.skillLevel.field.sortOrder') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sortOrder'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="skillLevel in skillLevels" :key="skillLevel.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: skillLevel.id } }">{{ skillLevel.id }}</router-link>
            </td>
            <td>{{ skillLevel.code }}</td>
            <td>{{ skillLevel.levelName }}</td>
            <td>{{ skillLevel.definition }}</td>
            <td>{{ skillLevel.observableEvidence }}</td>
            <td>{{ skillLevel.sortOrder }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: skillLevel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SkillLevelEdit', params: { skillLevelId: skillLevel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(skillLevel)"
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
        <span id="hrappApp.skillLevel.delete.question" data-cy="skillLevelDeleteDialogHeading">{{ $t('global.entity.deleteTitle') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-skillLevel-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.skillLevel.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-skillLevel"
            data-cy="entityConfirmDeleteButton"
            @click="removeSkillLevel"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="skillLevels?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./skill-level.component.ts"></script>
