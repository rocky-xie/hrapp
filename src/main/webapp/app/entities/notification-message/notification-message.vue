<template>
  <div>
    <h2 id="page-heading" data-cy="NotificationMessageHeading">
      <span id="notification-message">{{ $t('entity.notificationMessage.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'NotificationMessageCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-notification-message"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.notificationMessage.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && notificationMessages?.length === 0">
      <span>{{ $t('entity.notificationMessage.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="notificationMessages?.length > 0">
      <table class="table table-striped" aria-describedby="notificationMessages">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('eventType')">
              <span>{{ $t('recruitment.fields.eventType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('templateId')">
              <span>{{ $t('recruitment.fields.templateId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'templateId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('status')">
              <span>{{ $t('recruitment.fields.status') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sendMode')">
              <span>{{ $t('recruitment.fields.sendMode') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sendMode'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('finalSubject')">
              <span>{{ $t('recruitment.fields.finalSubject') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'finalSubject'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('finalBody')">
              <span>{{ $t('recruitment.fields.finalBody') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'finalBody'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('recipientsJson')">
              <span>{{ $t('recruitment.fields.recipientsJson') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'recipientsJson'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('retryCount')">
              <span>{{ $t('recruitment.fields.retryCount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'retryCount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('maxRetries')">
              <span>{{ $t('recruitment.fields.maxRetries') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxRetries'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('lastError')">
              <span>{{ $t('recruitment.fields.lastError') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastError'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('createdBy')">
              <span>{{ $t('recruitment.fields.createdBy') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdBy'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('confirmedBy')">
              <span>{{ $t('recruitment.fields.confirmedBy') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'confirmedBy'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('createdAt')">
              <span>{{ $t('recruitment.fields.createdAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('confirmedAt')">
              <span>{{ $t('recruitment.fields.confirmedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'confirmedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sentAt')">
              <span>{{ $t('recruitment.fields.sentAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sentAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('application.id')">
              <span>{{ $t('recruitment.fields.application') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'application.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="notificationMessage in notificationMessages" :key="notificationMessage.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'NotificationMessageView', params: { notificationMessageId: notificationMessage.id } }">{{
                notificationMessage.id
              }}</router-link>
            </td>
            <td>{{ notificationMessage.eventType }}</td>
            <td>{{ notificationMessage.templateId }}</td>
            <td>{{ $t('recruitment.enums.' + notificationMessage.status) }}</td>
            <td>{{ $t('recruitment.enums.' + notificationMessage.sendMode) }}</td>
            <td>{{ notificationMessage.finalSubject }}</td>
            <td>{{ notificationMessage.finalBody }}</td>
            <td>{{ notificationMessage.recipientsJson }}</td>
            <td>{{ notificationMessage.retryCount }}</td>
            <td>{{ notificationMessage.maxRetries }}</td>
            <td>{{ notificationMessage.lastError }}</td>
            <td>{{ notificationMessage.createdBy }}</td>
            <td>{{ notificationMessage.confirmedBy }}</td>
            <td>{{ formatDateShort(notificationMessage.createdAt) || '' }}</td>
            <td>{{ formatDateShort(notificationMessage.confirmedAt) || '' }}</td>
            <td>{{ formatDateShort(notificationMessage.sentAt) || '' }}</td>
            <td>
              <div v-if="notificationMessage.application">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: notificationMessage.application.id } }"
                  >{{ notificationMessage.application.applicationCode }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'NotificationMessageView', params: { notificationMessageId: notificationMessage.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'NotificationMessageEdit', params: { notificationMessageId: notificationMessage.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(notificationMessage)"
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
        <span id="hrappApp.notificationMessage.delete.question" data-cy="notificationMessageDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-notification-message-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.notificationMessage.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-notificationMessage"
            data-cy="entityConfirmDeleteButton"
            @click="removeNotificationMessage"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="notificationMessages?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./notification-message.component.ts"></script>
