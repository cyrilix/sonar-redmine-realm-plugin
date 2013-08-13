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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.sonar.api.config.Settings;
import org.sonar.api.security.Authenticator;
import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.SecurityRealm;
import org.sonar.api.security.UserDetails;

import com.taskadapter.redmineapi.bean.Group;
import com.taskadapter.redmineapi.bean.User;

/**
 * @author Raphael Jolly
 */
public class RedmineRealm extends SecurityRealm {
	private final RedmineSettingsManager settingsManager;
	private Map<String, User> users;

	public RedmineRealm(Settings settings) {
		settingsManager = new RedmineSettingsManager(settings);
	}

	/**
	 * Initializes Redmine realm and populates the users.
	 *
	 */
	@Override
	public void init() {
		users = settingsManager.getUsers();
	}

	@Override
	public Authenticator doGetAuthenticator() {
		return new Authenticator() {
			@Override
			public boolean doAuthenticate(Context context) {
				return settingsManager.auth(context.getUsername(), context.getPassword());
			}
		};
	}

	@Override
	public ExternalUsersProvider getUsersProvider() {
		return new ExternalUsersProvider() {
			@Override
			public UserDetails doGetUserDetails(Context context) {
				User user = users.get(context.getUsername());
				UserDetails details = new UserDetails();
				details.setName(user.getFullName());
				details.setEmail(user.getMail());
				return details;
			}
		};
	}

	@Override
	public ExternalGroupsProvider getGroupsProvider() {
		return new ExternalGroupsProvider() {
			@Override
			public Collection<String> doGetGroups(String username) {
				User user = users.get(username);
				Collection<String> result = new HashSet<String>();
				for (Group group: user.getGroups()) {
					result.add(group.getName());
				}
				return result;
			}
		};
	}

	@Override
	public String getName() {
		return "REDMINE";
	}
}
