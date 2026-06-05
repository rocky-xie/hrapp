<template>
  <b-navbar data-cy="navbar" toggleable="md" variant="dark" data-bs-theme="dark" class="shadow-sm">
    <b-navbar-brand class="logo" b-link to="/">
      <span class="logo-img"></span>
      <span class="navbar-title">{{ $t('global.brand.title') }}</span>
      <span class="navbar-subtitle d-none d-sm-inline">{{ $t('global.brand.subtitle') }}</span>
    </b-navbar-brand>
    <b-navbar-toggle
      right
      class="jh-navbar-toggler d-lg-none"
      href="javascript:void(0);"
      data-toggle="collapse"
      target="header-tabs"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <font-awesome-icon icon="bars" />
    </b-navbar-toggle>

    <b-collapse is-nav id="header-tabs">
      <b-navbar-nav class="ms-auto">
        <b-nav-item to="/" exact>
          <span>
            <font-awesome-icon icon="fa-solid fa-home" />
            <span>{{ $t('global.menu.home') }}</span>
          </span>
        </b-nav-item>
        <b-nav-item to="/dashboard" v-if="authenticated">
          <span>
            <font-awesome-icon icon="tachometer-alt" />
            <span>{{ $t('global.menu.dashboard') }}</span>
          </span>
        </b-nav-item>
        <b-nav-item to="/reports/skill-gaps" v-if="authenticated">
          <span>
            <font-awesome-icon icon="chart-bar" />
            <span>{{ $t('global.menu.skillGapReport') }}</span>
          </span>
        </b-nav-item>
        <b-nav-item to="/action-items" v-if="authenticated">
          <span>
            <font-awesome-icon icon="tasks" />
            <span>{{ $t('actionItem.title') }}</span>
          </span>
        </b-nav-item>
        <b-nav-item to="/data-quality" v-if="authenticated">
          <span>
            <font-awesome-icon icon="check-double" />
            <span>{{ $t('global.menu.dataQuality') }}</span>
          </span>
        </b-nav-item>
        <b-nav-item-dropdown
          :no-size="true"
          end
          id="entity-menu"
          v-if="authenticated"
          active-class="active"
          class="pointer"
          data-cy="entity"
        >
          <template #button-content>
            <span class="navbar-dropdown-menu">
              <font-awesome-icon icon="th-list" />
              <span class="no-bold">{{ $t('global.menu.entities') }}</span>
            </span>
          </template>
          <entities-menu></entities-menu>
        </b-nav-item-dropdown>
        <b-nav-item-dropdown
          right
          id="admin-menu"
          v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
          :class="{ 'router-link-active': subIsActive('/admin') }"
          active-class="active"
          class="pointer"
          data-cy="adminMenu"
        >
          <template #button-content>
            <span class="navbar-dropdown-menu">
              <font-awesome-icon icon="users-cog" />
              <span class="no-bold">{{ $t('global.menu.administration') }}</span>
            </span>
          </template>
          <b-dropdown-item to="/admin/user-management" active-class="active">
            <font-awesome-icon icon="users" />
            <span>{{ $t('global.menu.user-management') }}</span>
          </b-dropdown-item>
          <b-dropdown-item to="/admin/metrics" active-class="active">
            <font-awesome-icon icon="tachometer-alt" />
            <span>{{ $t('global.menu.metrics') }}</span>
          </b-dropdown-item>
          <b-dropdown-item to="/admin/health" active-class="active">
            <font-awesome-icon icon="heart" />
            <span>{{ $t('global.menu.health') }}</span>
          </b-dropdown-item>
          <b-dropdown-item to="/admin/configuration" active-class="active">
            <font-awesome-icon icon="cogs" />
            <span>{{ $t('global.menu.configuration') }}</span>
          </b-dropdown-item>
          <b-dropdown-item to="/admin/logs" active-class="active">
            <font-awesome-icon icon="tasks" />
            <span>{{ $t('global.menu.logs') }}</span>
          </b-dropdown-item>
          <b-dropdown-item v-if="openAPIEnabled" to="/admin/docs" active-class="active">
            <font-awesome-icon icon="book" />
            <span>{{ $t('global.menu.api') }}</span>
          </b-dropdown-item>
          <b-dropdown-item v-if="!inProduction" href="./h2-console/" target="_tab">
            <font-awesome-icon icon="database" />
            <span>{{ $t('global.menu.database') }}</span>
          </b-dropdown-item>
        </b-nav-item-dropdown>
        <b-nav-item-dropdown :no-size="true" end id="language-menu" active-class="active" class="pointer" data-cy="languageMenu">
          <template #button-content>
            <span class="navbar-dropdown-menu">
              <font-awesome-icon icon="globe" />
              <span class="no-bold">{{ $t('global.menu.language') }}</span>
            </span>
          </template>
          <b-dropdown-item
            v-for="lang in languages"
            :key="lang.key"
            @click="changeLanguage(lang.key)"
            :active="currentLanguage === lang.key"
          >
            {{ lang.name }}
          </b-dropdown-item>
        </b-nav-item-dropdown>
        <b-nav-item-dropdown
          right
          href="javascript:void(0);"
          id="account-menu"
          :class="{ 'router-link-active': subIsActive('/account') }"
          active-class="active"
          class="pointer"
          data-cy="accountMenu"
        >
          <template #button-content>
            <span class="navbar-dropdown-menu">
              <font-awesome-icon icon="user" />
              <span class="no-bold">{{ $t('global.menu.account') }}</span>
            </span>
          </template>
          <b-dropdown-item data-cy="settings" to="/account/settings" v-if="authenticated" active-class="active">
            <font-awesome-icon icon="wrench" />
            <span>{{ $t('global.menu.settings') }}</span>
          </b-dropdown-item>
          <b-dropdown-item data-cy="passwordItem" to="/account/password" v-if="authenticated" active-class="active">
            <font-awesome-icon icon="lock" />
            <span>{{ $t('global.menu.password') }}</span>
          </b-dropdown-item>
          <b-dropdown-item data-cy="logout" v-if="authenticated" @click="logout()" id="logout" active-class="active">
            <font-awesome-icon icon="sign-out-alt" />
            <span>{{ $t('global.menu.logout') }}</span>
          </b-dropdown-item>
          <b-dropdown-item data-cy="login" v-if="!authenticated" @click="showLogin()" id="login" active-class="active">
            <font-awesome-icon icon="sign-in-alt" />
            <span>{{ $t('global.menu.login') }}</span>
          </b-dropdown-item>
          <b-dropdown-item data-cy="register" to="/register" id="register" v-if="!authenticated" active-class="active">
            <font-awesome-icon icon="user-plus" />
            <span>{{ $t('global.menu.register') }}</span>
          </b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script lang="ts" src="./jhi-navbar.component.ts"></script>

<style scoped>
.navbar-title {
  display: inline-block;
  color: white;
  font-weight: 700;
  font-size: 1.2rem;
}

.navbar-subtitle {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.8rem;
  margin-left: 8px;
  font-weight: 400;
}

.navbar .navbar-nav .nav-item {
  margin-right: 0.5rem;
}

@media screen and (min-width: 768px) {
  .jh-navbar-toggler {
    display: none;
  }
}

@media screen and (min-width: 768px) and (max-width: 1150px) {
  span span {
    display: none;
  }
}

.navbar-brand.logo {
  padding: 0 7px;
}

.logo .logo-img {
  height: 40px;
  display: inline-block;
  vertical-align: middle;
  width: 40px;
}

.logo-img {
  height: 100%;
  background: url('/content/images/logo-btmdc.svg') no-repeat center center;
  background-size: contain;
  width: 100%;
  filter: drop-shadow(0 0 0.05rem white);
  margin: 0 5px;
}
</style>
