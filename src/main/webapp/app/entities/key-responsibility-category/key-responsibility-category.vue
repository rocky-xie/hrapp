<template>
  <div>
    <h2 id="page-heading" data-cy="KeyResponsibilityCategoryHeading">
      <span id="key-responsibility-category">{{ $t('entity.keyResponsibilityCategory.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'KeyResponsibilityCategoryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-key-responsibility-category"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.keyResponsibilityCategory.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && keyResponsibilityCategories?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.keyResponsibilityCategory.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="keyResponsibilityCategories?.length > 0">
      <table class="table table-striped" aria-describedby="keyResponsibilityCategories">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('categoryName')">
              <span>{{ $t('entity.keyResponsibilityCategory.field.categoryName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'categoryName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('examples')">
              <span>{{ $t('entity.keyResponsibilityCategory.field.examples') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'examples'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskFocus')">
              <span>{{ $t('entity.keyResponsibilityCategory.field.riskFocus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskFocus'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="keyResponsibilityCategory in keyResponsibilityCategories" :key="keyResponsibilityCategory.id" data-cy="entityTable">
            <td>
              <router-link
                :to="{ name: 'KeyResponsibilityCategoryView', params: { keyResponsibilityCategoryId: keyResponsibilityCategory.id } }"
                >{{ keyResponsibilityCategory.id }}</router-link
              >
            </td>
            <td>{{ keyResponsibilityCategory.categoryName }}</td>
            <td>{{ keyResponsibilityCategory.examples }}</td>
            <td>{{ keyResponsibilityCategory.riskFocus }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'KeyResponsibilityCategoryView', params: { keyResponsibilityCategoryId: keyResponsibilityCategory.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'KeyResponsibilityCategoryEdit', params: { keyResponsibilityCategoryId: keyResponsibilityCategory.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(keyResponsibilityCategory)"
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
        <span id="hrappApp.keyResponsibilityCategory.delete.question" data-cy="keyResponsibilityCategoryDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-keyResponsibilityCategory-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.keyResponsibilityCategory.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-keyResponsibilityCategory"
            data-cy="entityConfirmDeleteButton"
            @click="removeKeyResponsibilityCategory"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="keyResponsibilityCategories?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./key-responsibility-category.component.ts"></script>
