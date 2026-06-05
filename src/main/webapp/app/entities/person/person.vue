<template>
  <div>
    <h2 id="page-heading" data-cy="PersonHeading">
      <span id="person">{{ $t('entity.person.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PersonCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-person"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.person.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && people?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.person.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="people?.length > 0">
      <table class="table table-striped" aria-describedby="people">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('employeeCode')">
              <span>{{ $t('entity.person.field.employeeCode') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'employeeCode'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('personName')">
              <span>{{ $t('entity.person.field.personName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('age')">
              <span>{{ $t('entity.person.field.age') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'age'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('gender')">
              <span>{{ $t('entity.person.field.gender') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'gender'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('department')">
              <span>{{ $t('entity.person.field.department') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'department'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('currentRole')">
              <span>{{ $t('entity.person.field.currentRole') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'currentRole'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('employmentStatus')">
              <span>{{ $t('entity.person.field.employmentStatus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'employmentStatus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('joinDate')">
              <span>{{ $t('entity.person.field.joinDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'joinDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('mentorFlag')">
              <span>{{ $t('entity.person.field.mentorFlag') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mentorFlag'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('coreCandidateFlag')">
              <span>{{ $t('entity.person.field.coreCandidateFlag') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coreCandidateFlag'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('note')">
              <span>{{ $t('entity.person.field.note') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'note'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="person in people" :key="person.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PersonView', params: { personId: person.id } }">{{ person.id }}</router-link>
            </td>
            <td>{{ person.employeeCode }}</td>
            <td>{{ person.personName }}</td>
            <td>{{ person.age }}</td>
            <td>{{ person.gender }}</td>
            <td>{{ person.department }}</td>
            <td>{{ person.currentRole }}</td>
            <td>{{ person.employmentStatus }}</td>
            <td>{{ person.joinDate }}</td>
            <td>{{ person.mentorFlag }}</td>
            <td>{{ person.coreCandidateFlag }}</td>
            <td>{{ person.note }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PersonView', params: { personId: person.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PersonEdit', params: { personId: person.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(person)"
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
        <span id="hrappApp.person.delete.question" data-cy="personDeleteDialogHeading">{{ $t('global.entity.deleteTitle') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-person-heading">{{ $t('global.entity.deleteQuestion', { entity: $t('entity.person.name'), id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-person"
            data-cy="entityConfirmDeleteButton"
            @click="removePerson"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="people?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./person.component.ts"></script>
