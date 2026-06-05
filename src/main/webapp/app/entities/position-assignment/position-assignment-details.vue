<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="positionAssignment">
        <h2 class="jh-entity-heading" data-cy="positionAssignmentDetailsHeading">
          <span>{{ $t('entity.positionAssignment.detail.title') }}</span> {{ positionAssignment.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.positionAssignment.field.primaryOwner') }}</span>
          </dt>
          <dd>
            <span>{{ positionAssignment.primaryOwner }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.startDate') }}</span>
          </dt>
          <dd>
            <span>{{ positionAssignment.startDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionAssignment.field.endDate') }}</span>
          </dt>
          <dd>
            <span>{{ positionAssignment.endDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionAssignment.field.responsibilityScope') }}</span>
          </dt>
          <dd>
            <span>{{ positionAssignment.responsibilityScope }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionAssignment.field.active') }}</span>
          </dt>
          <dd>
            <span>{{ positionAssignment.active }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="positionAssignment.person">
              <router-link :to="{ name: 'PersonView', params: { personId: positionAssignment.person.id } }">{{
                positionAssignment.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="positionAssignment.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: positionAssignment.position.id } }">{{
                positionAssignment.position.positionName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="positionAssignment.id"
          :to="{ name: 'PositionAssignmentEdit', params: { positionAssignmentId: positionAssignment.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position-assignment-details.component.ts"></script>
