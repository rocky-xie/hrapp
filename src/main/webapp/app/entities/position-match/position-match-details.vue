<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="positionMatch">
        <h2 class="jh-entity-heading" data-cy="positionMatchDetailsHeading">
          <span>{{ $t('entity.positionMatch.detail.title') }}</span> {{ positionMatch.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.positionMatch.field.matchScore') }}</span>
          </dt>
          <dd>
            <span>{{ positionMatch.matchScore }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionMatch.field.matchedSkills') }}</span>
          </dt>
          <dd>
            <span>{{ positionMatch.matchedSkills }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionMatch.field.gapSkills') }}</span>
          </dt>
          <dd>
            <span>{{ positionMatch.gapSkills }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionMatch.field.readiness') }}</span>
          </dt>
          <dd>
            <span>{{ positionMatch.readiness }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionMatch.field.recommendation') }}</span>
          </dt>
          <dd>
            <span>{{ positionMatch.recommendation }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionMatch.field.analysisDate') }}</span>
          </dt>
          <dd>
            <span>{{ positionMatch.analysisDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionMatch.field.remark') }}</span>
          </dt>
          <dd>
            <span>{{ positionMatch.remark }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="positionMatch.person">
              <router-link :to="{ name: 'PersonView', params: { personId: positionMatch.person.id } }">{{
                positionMatch.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="positionMatch.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: positionMatch.position.id } }">{{
                positionMatch.position.positionName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="positionMatch.id"
          :to="{ name: 'PositionMatchEdit', params: { positionMatchId: positionMatch.id } }"
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

<script lang="ts" src="./position-match-details.component.ts"></script>
