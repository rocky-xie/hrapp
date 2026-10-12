<template>
  <div>
    <h2 id="page-heading" data-cy="RecruitmentRequisitionMemberHeading">
      <span id="recruitment-requisition-member">{{ $t('entity.recruitmentRequisitionMember.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'RecruitmentRequisitionMemberCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-recruitment-requisition-member"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.recruitmentRequisitionMember.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && recruitmentRequisitionMembers?.length === 0">
      <span>{{ $t('entity.recruitmentRequisitionMember.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="recruitmentRequisitionMembers?.length > 0">
      <table class="table table-striped" aria-describedby="recruitmentRequisitionMembers">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('username')">
              <span>{{ $t('recruitment.fields.username') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'username'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('memberType')">
              <span>{{ $t('recruitment.fields.memberType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'memberType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('active')">
              <span>{{ $t('recruitment.fields.active') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('createdAt')">
              <span>{{ $t('recruitment.fields.createdAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('removedAt')">
              <span>{{ $t('recruitment.fields.removedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'removedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('requisition.id')">
              <span>{{ $t('recruitment.fields.requisition') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'requisition.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="recruitmentRequisitionMember in recruitmentRequisitionMembers"
            :key="recruitmentRequisitionMember.id"
            data-cy="entityTable"
          >
            <td>
              <router-link
                :to="{
                  name: 'RecruitmentRequisitionMemberView',
                  params: { recruitmentRequisitionMemberId: recruitmentRequisitionMember.id },
                }"
                >{{ recruitmentRequisitionMember.id }}</router-link
              >
            </td>
            <td>{{ recruitmentRequisitionMember.username }}</td>
            <td>{{ $t('recruitment.enums.' + recruitmentRequisitionMember.memberType) }}</td>
            <td>{{ recruitmentRequisitionMember.active }}</td>
            <td>{{ formatDateShort(recruitmentRequisitionMember.createdAt) || '' }}</td>
            <td>{{ formatDateShort(recruitmentRequisitionMember.removedAt) || '' }}</td>
            <td>
              <div v-if="recruitmentRequisitionMember.requisition">
                <router-link
                  :to="{
                    name: 'RecruitmentRequisitionView',
                    params: { recruitmentRequisitionId: recruitmentRequisitionMember.requisition.id },
                  }"
                  >{{ recruitmentRequisitionMember.requisition.title }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{
                    name: 'RecruitmentRequisitionMemberView',
                    params: { recruitmentRequisitionMemberId: recruitmentRequisitionMember.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{
                    name: 'RecruitmentRequisitionMemberEdit',
                    params: { recruitmentRequisitionMemberId: recruitmentRequisitionMember.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(recruitmentRequisitionMember)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">{{ $t('recruitment.ui.delete') }}</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="hrappApp.recruitmentRequisitionMember.delete.question" data-cy="recruitmentRequisitionMemberDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-recruitmentRequisitionMember-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.recruitmentRequisitionMember.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-recruitmentRequisitionMember"
            data-cy="entityConfirmDeleteButton"
            @click="removeRecruitmentRequisitionMember"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="recruitmentRequisitionMembers?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./recruitment-requisition-member.component.ts"></script>
