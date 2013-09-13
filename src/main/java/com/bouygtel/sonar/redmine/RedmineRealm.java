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

import java.util.Map;

import org.sonar.api.config.Settings;
import org.sonar.api.security.Authenticator;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.SecurityRealm;
import org.sonar.api.security.UserDetails;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.User;

public class RedmineRealm extends SecurityRealm {
	private final RedmineSettingsManager settingsManager;
	private Map<String, User> users;

	public RedmineRealm(Settings settings) {
		settingsManager = new RedmineSettingsManager(settings);
	}

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
	public String getName() {
		return "REDMINE";
	}
}
