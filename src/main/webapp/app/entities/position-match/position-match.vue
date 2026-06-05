<template>
  <div>
    <h2 id="page-heading" data-cy="PositionMatchHeading">
      <span id="position-match">{{ $t('entity.positionMatch.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PositionMatchCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-position-match"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.positionMatch.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && positionMatches?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.positionMatch.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="positionMatches?.length > 0">
      <table class="table table-striped" aria-describedby="positionMatches">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('matchScore')">
              <span>{{ $t('entity.positionMatch.field.matchScore') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'matchScore'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('matchedSkills')">
              <span>{{ $t('entity.positionMatch.field.matchedSkills') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'matchedSkills'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('gapSkills')">
              <span>{{ $t('entity.positionMatch.field.gapSkills') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'gapSkills'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('readiness')">
              <span>{{ $t('entity.positionMatch.field.readiness') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'readiness'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('recommendation')">
              <span>{{ $t('entity.positionMatch.field.recommendation') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'recommendation'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('analysisDate')">
              <span>{{ $t('entity.positionMatch.field.analysisDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'analysisDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('remark')">
              <span>{{ $t('entity.positionMatch.field.remark') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'remark'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="positionMatch in positionMatches" :key="positionMatch.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PositionMatchView', params: { positionMatchId: positionMatch.id } }">{{
                positionMatch.id
              }}</router-link>
            </td>
            <td>{{ positionMatch.matchScore }}</td>
            <td>{{ positionMatch.matchedSkills }}</td>
            <td>{{ positionMatch.gapSkills }}</td>
            <td>{{ positionMatch.readiness }}</td>
            <td>{{ positionMatch.recommendation }}</td>
            <td>{{ positionMatch.analysisDate }}</td>
            <td>{{ positionMatch.remark }}</td>
            <td>
              <div v-if="positionMatch.person">
                <router-link :to="{ name: 'PersonView', params: { personId: positionMatch.person.id } }">{{
                  positionMatch.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="positionMatch.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: positionMatch.position.id } }">{{
                  positionMatch.position.positionName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PositionMatchView', params: { positionMatchId: positionMatch.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PositionMatchEdit', params: { positionMatchId: positionMatch.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(positionMatch)"
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
        <span id="hrappApp.positionMatch.delete.question" data-cy="positionMatchDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-positionMatch-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.positionMatch.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-positionMatch"
            data-cy="entityConfirmDeleteButton"
            @click="removePositionMatch"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="positionMatches?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position-match.component.ts"></script>
