<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="partnerOrganization">
        <h2 class="jh-entity-heading" data-cy="partnerOrganizationDetailsHeading">
          <span>{{ $t('entity.partnerOrganization.detail.title') }}</span> {{ partnerOrganization.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.partnerCode') }}</span>
          </dt>
          <dd>
            <span>{{ partnerOrganization.partnerCode }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.name') }}</span>
          </dt>
          <dd>
            <span>{{ partnerOrganization.name }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.rolesJson') }}</span>
          </dt>
          <dd>
            <span>{{ partnerOrganization.rolesJson }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.dispatchEnabled') }}</span>
          </dt>
          <dd>
            <span>{{ partnerOrganization.dispatchEnabled }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.defaultObservationDays') }}</span>
          </dt>
          <dd>
            <span>{{ partnerOrganization.defaultObservationDays }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.active') }}</span>
          </dt>
          <dd>
            <span>{{ partnerOrganization.active }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="partnerOrganization.id"
          :to="{ name: 'PartnerOrganizationEdit', params: { partnerOrganizationId: partnerOrganization.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./partner-organization-details.component.ts"></script>
