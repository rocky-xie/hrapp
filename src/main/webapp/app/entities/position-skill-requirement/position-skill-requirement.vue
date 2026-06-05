<template>
  <div>
    <h2 id="page-heading" data-cy="PositionSkillRequirementHeading">
      <span id="position-skill-requirement">{{ $t('entity.positionSkillRequirement.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PositionSkillRequirementCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-position-skill-requirement"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.positionSkillRequirement.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && positionSkillRequirements?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.positionSkillRequirement.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="positionSkillRequirements?.length > 0">
      <table class="table table-striped" aria-describedby="positionSkillRequirements">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('importance')">
              <span>{{ $t('entity.positionSkillRequirement.field.importance') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'importance'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('remark')">
              <span>{{ $t('entity.positionMatch.field.remark') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'remark'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('skill.skillName')">
              <span>{{ $t('entity.skill.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'skill.skillName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('requiredLevel.code')">
              <span>{{ $t('global.entity.field.requiredLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'requiredLevel.code'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('preferredLevel.code')">
              <span>{{ $t('global.entity.field.preferredLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'preferredLevel.code'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="positionSkillRequirement in positionSkillRequirements" :key="positionSkillRequirement.id" data-cy="entityTable">
            <td>
              <router-link
                :to="{ name: 'PositionSkillRequirementView', params: { positionSkillRequirementId: positionSkillRequirement.id } }"
                >{{ positionSkillRequirement.id }}</router-link
              >
            </td>
            <td>{{ positionSkillRequirement.importance }}</td>
            <td>{{ positionSkillRequirement.remark }}</td>
            <td>
              <div v-if="positionSkillRequirement.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: positionSkillRequirement.position.id } }">{{
                  positionSkillRequirement.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="positionSkillRequirement.skill">
                <router-link :to="{ name: 'SkillView', params: { skillId: positionSkillRequirement.skill.id } }">{{
                  positionSkillRequirement.skill.skillName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="positionSkillRequirement.requiredLevel">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: positionSkillRequirement.requiredLevel.id } }">{{
                  positionSkillRequirement.requiredLevel.code
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="positionSkillRequirement.preferredLevel">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: positionSkillRequirement.preferredLevel.id } }">{{
                  positionSkillRequirement.preferredLevel.code
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PositionSkillRequirementView', params: { positionSkillRequirementId: positionSkillRequirement.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PositionSkillRequirementEdit', params: { positionSkillRequirementId: positionSkillRequirement.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(positionSkillRequirement)"
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
        <span id="hrappApp.positionSkillRequirement.delete.question" data-cy="positionSkillRequirementDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-positionSkillRequirement-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.positionSkillRequirement.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-positionSkillRequirement"
            data-cy="entityConfirmDeleteButton"
            @click="removePositionSkillRequirement"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="positionSkillRequirements?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position-skill-requirement.component.ts"></script>
