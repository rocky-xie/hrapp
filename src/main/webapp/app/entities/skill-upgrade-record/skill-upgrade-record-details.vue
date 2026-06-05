<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="skillUpgradeRecord">
        <h2 class="jh-entity-heading" data-cy="skillUpgradeRecordDetailsHeading">
          <span>{{ $t('entity.skillUpgradeRecord.detail.title') }}</span> {{ skillUpgradeRecord.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.skillUpgradeRecord.field.changeType') }}</span>
          </dt>
          <dd>
            <span>{{ skillUpgradeRecord.changeType }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillUpgradeRecord.field.changeDate') }}</span>
          </dt>
          <dd>
            <span>{{ skillUpgradeRecord.changeDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.staffSubstitution.field.reason') }}</span>
          </dt>
          <dd>
            <span>{{ skillUpgradeRecord.reason }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillUpgradeRecord.field.beforeLevelLabel') }}</span>
          </dt>
          <dd>
            <span>{{ skillUpgradeRecord.beforeLevelLabel }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillUpgradeRecord.field.afterLevelLabel') }}</span>
          </dt>
          <dd>
            <span>{{ skillUpgradeRecord.afterLevelLabel }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
          </dt>
          <dd>
            <span>{{ skillUpgradeRecord.evidence }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.skillAssessment.field.comment') }}</span>
          </dt>
          <dd>
            <span>{{ skillUpgradeRecord.comment }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="skillUpgradeRecord.person">
              <router-link :to="{ name: 'PersonView', params: { personId: skillUpgradeRecord.person.id } }">{{
                skillUpgradeRecord.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="skillUpgradeRecord.skill">
              <router-link :to="{ name: 'SkillView', params: { skillId: skillUpgradeRecord.skill.id } }">{{
                skillUpgradeRecord.skill.skillName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.oldLevel') }}</span>
          </dt>
          <dd>
            <div v-if="skillUpgradeRecord.oldLevel">
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: skillUpgradeRecord.oldLevel.id } }">{{
                skillUpgradeRecord.oldLevel.code
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.newLevel') }}</span>
          </dt>
          <dd>
            <div v-if="skillUpgradeRecord.newLevel">
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: skillUpgradeRecord.newLevel.id } }">{{
                skillUpgradeRecord.newLevel.code
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.assessor') }}</span>
          </dt>
          <dd>
            <div v-if="skillUpgradeRecord.assessor">
              <router-link :to="{ name: 'PersonView', params: { personId: skillUpgradeRecord.assessor.id } }">{{
                skillUpgradeRecord.assessor.personName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="skillUpgradeRecord.id"
          :to="{ name: 'SkillUpgradeRecordEdit', params: { skillUpgradeRecordId: skillUpgradeRecord.id } }"
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

<script lang="ts" src="./skill-upgrade-record-details.component.ts"></script>
