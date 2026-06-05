<template>
  <div>
    <h2 id="page-heading" data-cy="SkillUpgradeRecordHeading">
      <span id="skill-upgrade-record">{{ $t('entity.skillUpgradeRecord.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'SkillUpgradeRecordCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-skill-upgrade-record"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.skillUpgradeRecord.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && skillUpgradeRecords?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.skillUpgradeRecord.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="skillUpgradeRecords?.length > 0">
      <table class="table table-striped" aria-describedby="skillUpgradeRecords">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('changeType')">
              <span>{{ $t('entity.skillUpgradeRecord.field.changeType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'changeType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('changeDate')">
              <span>{{ $t('entity.skillUpgradeRecord.field.changeDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'changeDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reason')">
              <span>{{ $t('entity.staffSubstitution.field.reason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('beforeLevelLabel')">
              <span>{{ $t('entity.skillUpgradeRecord.field.beforeLevelLabel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'beforeLevelLabel'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('afterLevelLabel')">
              <span>{{ $t('entity.skillUpgradeRecord.field.afterLevelLabel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'afterLevelLabel'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('oldLevel.code')">
              <span>{{ $t('global.entity.field.oldLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'oldLevel.code'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('newLevel.code')">
              <span>{{ $t('global.entity.field.newLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'newLevel.code'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('assessor.personName')">
              <span>{{ $t('global.entity.field.assessor') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'assessor.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="skillUpgradeRecord in skillUpgradeRecords" :key="skillUpgradeRecord.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SkillUpgradeRecordView', params: { skillUpgradeRecordId: skillUpgradeRecord.id } }">{{
                skillUpgradeRecord.id
              }}</router-link>
            </td>
            <td>{{ skillUpgradeRecord.changeType }}</td>
            <td>{{ skillUpgradeRecord.changeDate }}</td>
            <td>{{ skillUpgradeRecord.reason }}</td>
            <td>{{ skillUpgradeRecord.beforeLevelLabel }}</td>
            <td>{{ skillUpgradeRecord.afterLevelLabel }}</td>
            <td>{{ skillUpgradeRecord.evidence }}</td>
            <td>{{ skillUpgradeRecord.comment }}</td>
            <td>
              <div v-if="skillUpgradeRecord.person">
                <router-link :to="{ name: 'PersonView', params: { personId: skillUpgradeRecord.person.id } }">{{
                  skillUpgradeRecord.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="skillUpgradeRecord.skill">
                <router-link :to="{ name: 'SkillView', params: { skillId: skillUpgradeRecord.skill.id } }">{{
                  skillUpgradeRecord.skill.skillName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="skillUpgradeRecord.oldLevel">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: skillUpgradeRecord.oldLevel.id } }">{{
                  skillUpgradeRecord.oldLevel.code
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="skillUpgradeRecord.newLevel">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: skillUpgradeRecord.newLevel.id } }">{{
                  skillUpgradeRecord.newLevel.code
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="skillUpgradeRecord.assessor">
                <router-link :to="{ name: 'PersonView', params: { personId: skillUpgradeRecord.assessor.id } }">{{
                  skillUpgradeRecord.assessor.personName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SkillUpgradeRecordView', params: { skillUpgradeRecordId: skillUpgradeRecord.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SkillUpgradeRecordEdit', params: { skillUpgradeRecordId: skillUpgradeRecord.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(skillUpgradeRecord)"
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
        <span id="hrappApp.skillUpgradeRecord.delete.question" data-cy="skillUpgradeRecordDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-skillUpgradeRecord-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.skillUpgradeRecord.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-skillUpgradeRecord"
            data-cy="entityConfirmDeleteButton"
            @click="removeSkillUpgradeRecord"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="skillUpgradeRecords?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./skill-upgrade-record.component.ts"></script>
