<template>
  <div>
    <h2 id="page-heading" data-cy="NotificationTemplateHeading">
      <span id="notification-template">{{ $t('entity.notificationTemplate.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'NotificationTemplateCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-notification-template"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.notificationTemplate.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && notificationTemplates?.length === 0">
      <span>{{ $t('entity.notificationTemplate.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="notificationTemplates?.length > 0">
      <table class="table table-striped" aria-describedby="notificationTemplates">
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
            <th scope="col" @click="changeOrder('recruitmentType')">
              <span>{{ $t('recruitment.fields.recruitmentType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'recruitmentType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('language')">
              <span>{{ $t('recruitment.fields.language') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'language'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('versionNumber')">
              <span>{{ $t('recruitment.fields.versionNumber') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'versionNumber'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('subjectTemplate')">
              <span>{{ $t('recruitment.fields.subjectTemplate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subjectTemplate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('bodyTemplate')">
              <span>{{ $t('recruitment.fields.bodyTemplate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'bodyTemplate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('externalSafe')">
              <span>{{ $t('recruitment.fields.externalSafe') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'externalSafe'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('published')">
              <span>{{ $t('recruitment.fields.published') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'published'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="notificationTemplate in notificationTemplates" :key="notificationTemplate.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'NotificationTemplateView', params: { notificationTemplateId: notificationTemplate.id } }">{{
                notificationTemplate.id
              }}</router-link>
            </td>
            <td>{{ $t('recruitment.enums.' + notificationTemplate.eventType) }}</td>
            <td>{{ $t('recruitment.enums.' + notificationTemplate.recruitmentType) }}</td>
            <td>{{ notificationTemplate.language }}</td>
            <td>{{ notificationTemplate.versionNumber }}</td>
            <td>{{ notificationTemplate.subjectTemplate }}</td>
            <td>{{ notificationTemplate.bodyTemplate }}</td>
            <td>{{ notificationTemplate.externalSafe }}</td>
            <td>{{ notificationTemplate.published }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'NotificationTemplateView', params: { notificationTemplateId: notificationTemplate.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'NotificationTemplateEdit', params: { notificationTemplateId: notificationTemplate.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(notificationTemplate)"
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
        <span id="hrappApp.notificationTemplate.delete.question" data-cy="notificationTemplateDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-notification-template-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.notificationTemplate.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-notificationTemplate"
            data-cy="entityConfirmDeleteButton"
            @click="removeNotificationTemplate"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="notificationTemplates?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./notification-template.component.ts"></script>
