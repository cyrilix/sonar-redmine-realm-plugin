/*
 * Sonar Redmine Plugin
 * Copyright (C) 2013 Bouygues Telecom
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package com.bouygtel.sonar.redmine;

import org.sonar.api.config.Settings;
import org.sonar.api.security.Authenticator;
import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.SecurityRealm;

/**
 * @author Raphael Jolly
 */
public class RedmineRealm extends SecurityRealm {
	private final RedmineSettingsManager settingsManager;
	private RedmineAuthenticator authenticator;
	private ExternalUsersProvider usersProvider;
	private ExternalGroupsProvider groupsProvider;

	public RedmineRealm(Settings settings) {
		settingsManager = new RedmineSettingsManager(settings);
	}

	/**
	 * Initializes Redmine authenticator and users and groups providers.
	 *
	 */
	@Override
	public void init() {
		authenticator = new RedmineAuthenticator(settingsManager); 
		usersProvider = new RedmineUsersProvider(settingsManager.getUsers());
		groupsProvider = new RedmineGroupsProvider(settingsManager.getUsers());
	}

	@Override
	public Authenticator doGetAuthenticator() {
		return authenticator;
	}

	@Override
	public ExternalUsersProvider getUsersProvider() {
		return usersProvider;
	}

	@Override
	public ExternalGroupsProvider getGroupsProvider() {
		return groupsProvider;
	}

	@Override
	public String getName() {
		return "REDMINE";
	}
}
