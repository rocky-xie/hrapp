<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="positionSkillRequirement">
        <h2 class="jh-entity-heading" data-cy="positionSkillRequirementDetailsHeading">
          <span>{{ $t('entity.positionSkillRequirement.detail.title') }}</span> {{ positionSkillRequirement.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.positionSkillRequirement.field.importance') }}</span>
          </dt>
          <dd>
            <span>{{ positionSkillRequirement.importance }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionMatch.field.remark') }}</span>
          </dt>
          <dd>
            <span>{{ positionSkillRequirement.remark }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="positionSkillRequirement.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: positionSkillRequirement.position.id } }">{{
                positionSkillRequirement.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="positionSkillRequirement.skill">
              <router-link :to="{ name: 'SkillView', params: { skillId: positionSkillRequirement.skill.id } }">{{
                positionSkillRequirement.skill.skillName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.requiredLevel') }}</span>
          </dt>
          <dd>
            <div v-if="positionSkillRequirement.requiredLevel">
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: positionSkillRequirement.requiredLevel.id } }">{{
                positionSkillRequirement.requiredLevel.code
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.preferredLevel') }}</span>
          </dt>
          <dd>
            <div v-if="positionSkillRequirement.preferredLevel">
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: positionSkillRequirement.preferredLevel.id } }">{{
                positionSkillRequirement.preferredLevel.code
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="positionSkillRequirement.id"
          :to="{ name: 'PositionSkillRequirementEdit', params: { positionSkillRequirementId: positionSkillRequirement.id } }"
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

<script lang="ts" src="./position-skill-requirement-details.component.ts"></script>
