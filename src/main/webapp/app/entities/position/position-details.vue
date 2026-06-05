<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="position">
        <h2 class="jh-entity-heading" data-cy="positionDetailsHeading">
          <span>{{ $t('entity.position.detail.title') }}</span> {{ position.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.position.field.positionCode') }}</span>
          </dt>
          <dd>
            <span>{{ position.positionCode }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.field.positionName') }}</span>
          </dt>
          <dd>
            <span>{{ position.positionName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.field.positionType') }}</span>
          </dt>
          <dd>
            <span>{{ position.positionType }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.field.businessImportance') }}</span>
          </dt>
          <dd>
            <span>{{ position.businessImportance }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.field.keyPosition') }}</span>
          </dt>
          <dd>
            <span>{{ position.keyPosition }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.field.description') }}</span>
          </dt>
          <dd>
            <span>{{ position.description }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.field.plannedHeadcount') }}</span>
          </dt>
          <dd>
            <span>{{ position.plannedHeadcount }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.field.minimumOwnerCount') }}</span>
          </dt>
          <dd>
            <span>{{ position.minimumOwnerCount }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.field.reviewCycle') }}</span>
          </dt>
          <dd>
            <span>{{ position.reviewCycle }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionAssignment.field.active') }}</span>
          </dt>
          <dd>
            <span>{{ position.active }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link v-if="position.id" :to="{ name: 'PositionEdit', params: { positionId: position.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position-details.component.ts"></script>
