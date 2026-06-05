<template>
  <div>
    <b-dropdown-item disabled
      ><strong>{{ $t('global.menu.sub.coreData') }}</strong></b-dropdown-item
    >
    <b-dropdown-item to="/position">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.position.detail.title') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/person">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.person.detail.title') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/skill">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.skill.detail.title') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/skill-level">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('global.entity.field.skillLevel') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/key-responsibility-category">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.keyResponsibilityCategory.name') }}</span>
    </b-dropdown-item>

    <b-dropdown-divider></b-dropdown-divider>
    <b-dropdown-item disabled
      ><strong>{{ $t('global.menu.sub.continuity') }}</strong></b-dropdown-item
    >
    <b-dropdown-item to="/staff-substitution">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.staffSubstitution.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/position-risk-evaluation">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.positionRiskEvaluation.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/succession-candidate">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.successionCandidate.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/position-risk">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.positionRisk.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/person-risk">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.personRisk.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/candidate-profile">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.candidateProfile.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/position-match">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.positionMatch.name') }}</span>
    </b-dropdown-item>

    <b-dropdown-divider></b-dropdown-divider>
    <b-dropdown-item disabled
      ><strong>{{ $t('global.menu.sub.training') }}</strong></b-dropdown-item
    >
    <b-dropdown-item to="/training-goal">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('global.entity.field.trainingGoal') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/training-record">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.trainingRecord.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/skill-assessment">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.skillAssessment.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/skill-upgrade-record">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.skillUpgradeRecord.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/improvement-plan">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.improvementPlan.name') }}</span>
    </b-dropdown-item>

    <b-dropdown-divider></b-dropdown-divider>
    <b-dropdown-item disabled
      ><strong>{{ $t('global.menu.sub.observation') }}</strong></b-dropdown-item
    >
    <b-dropdown-item to="/evaluation">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.evaluation.name') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/trust-observation">
      <font-awesome-icon icon="asterisk" />
      <span>{{ $t('entity.trustObservation.name') }}</span>
    </b-dropdown-item>

    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
