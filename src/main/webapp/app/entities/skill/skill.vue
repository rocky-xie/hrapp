<template>
  <div>
    <h2 id="page-heading" data-cy="SkillHeading">
      <span id="skill">{{ $t('entity.skill.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'SkillCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-skill"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.skill.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && skills?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.skill.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="skills?.length > 0">
      <table class="table table-striped" aria-describedby="skills">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('skillCode')">
              <span>{{ $t('entity.skill.field.skillCode') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'skillCode'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('skillName')">
              <span>{{ $t('entity.skill.field.skillName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'skillName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('skillType')">
              <span>{{ $t('entity.skill.field.skillType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'skillType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('measurableFlag')">
              <span>{{ $t('entity.skill.field.measurableFlag') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'measurableFlag'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('description')">
              <span>{{ $t('entity.skill.field.description') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evidenceType')">
              <span>{{ $t('entity.skill.field.evidenceType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evidenceType'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="skill in skills" :key="skill.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SkillView', params: { skillId: skill.id } }">{{ skill.id }}</router-link>
            </td>
            <td>{{ skill.skillCode }}</td>
            <td>{{ skill.skillName }}</td>
            <td>{{ skill.skillType }}</td>
            <td>{{ skill.measurableFlag }}</td>
            <td>{{ skill.description }}</td>
            <td>{{ skill.evidenceType }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'SkillView', params: { skillId: skill.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SkillEdit', params: { skillId: skill.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(skill)"
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
        <span id="hrappApp.skill.delete.question" data-cy="skillDeleteDialogHeading">{{ $t('global.entity.deleteTitle') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-skill-heading">{{ $t('global.entity.deleteQuestion', { entity: $t('entity.skill.name'), id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-skill"
            data-cy="entityConfirmDeleteButton"
            @click="removeSkill"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="skills?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./skill.component.ts"></script>
