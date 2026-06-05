<template>
  <div>
    <h2 id="page-heading" data-cy="StaffSubstitutionHeading">
      <span id="staff-substitution">{{ $t('entity.staffSubstitution.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'StaffSubstitutionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-staff-substitution"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.staffSubstitution.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && staffSubstitutions?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.staffSubstitution.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="staffSubstitutions?.length > 0">
      <table class="table table-striped" aria-describedby="staffSubstitutions">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('coverageRate')">
              <span>{{ $t('entity.staffSubstitution.field.coverageRate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coverageRate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('thresholdRate')">
              <span>{{ $t('entity.staffSubstitution.field.thresholdRate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'thresholdRate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('totalSkillCount')">
              <span>{{ $t('entity.staffSubstitution.field.totalSkillCount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'totalSkillCount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('coveredSkillCount')">
              <span>{{ $t('entity.staffSubstitution.field.coveredSkillCount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coveredSkillCount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('missingSkills')">
              <span>{{ $t('entity.staffSubstitution.field.missingSkills') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'missingSkills'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('substitutable')">
              <span>{{ $t('entity.staffSubstitution.field.substitutable') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'substitutable'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evaluationDate')">
              <span>{{ $t('entity.evaluation.field.evaluationDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evaluationDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reviewDate')">
              <span>{{ $t('entity.staffSubstitution.field.reviewDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reviewDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('expiryDate')">
              <span>{{ $t('entity.staffSubstitution.field.expiryDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'expiryDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reason')">
              <span>{{ $t('entity.staffSubstitution.field.reason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('candidatePerson.personName')">
              <span>{{ $t('global.entity.field.candidatePerson') }}</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'candidatePerson.personName'"
              ></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="staffSubstitution in staffSubstitutions" :key="staffSubstitution.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'StaffSubstitutionView', params: { staffSubstitutionId: staffSubstitution.id } }">{{
                staffSubstitution.id
              }}</router-link>
            </td>
            <td>{{ staffSubstitution.coverageRate }}</td>
            <td>{{ staffSubstitution.thresholdRate }}</td>
            <td>{{ staffSubstitution.totalSkillCount }}</td>
            <td>{{ staffSubstitution.coveredSkillCount }}</td>
            <td>{{ staffSubstitution.missingSkills }}</td>
            <td>{{ staffSubstitution.substitutable }}</td>
            <td>{{ staffSubstitution.evaluationDate }}</td>
            <td>{{ staffSubstitution.reviewDate }}</td>
            <td>{{ staffSubstitution.expiryDate }}</td>
            <td>{{ staffSubstitution.reason }}</td>
            <td>
              <div v-if="staffSubstitution.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: staffSubstitution.position.id } }">{{
                  staffSubstitution.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="staffSubstitution.candidatePerson">
                <router-link :to="{ name: 'PersonView', params: { personId: staffSubstitution.candidatePerson.id } }">{{
                  staffSubstitution.candidatePerson.personName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'StaffSubstitutionView', params: { staffSubstitutionId: staffSubstitution.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'StaffSubstitutionEdit', params: { staffSubstitutionId: staffSubstitution.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(staffSubstitution)"
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
        <span id="hrappApp.staffSubstitution.delete.question" data-cy="staffSubstitutionDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-staffSubstitution-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.staffSubstitution.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-staffSubstitution"
            data-cy="entityConfirmDeleteButton"
            @click="removeStaffSubstitution"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="staffSubstitutions?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./staff-substitution.component.ts"></script>
