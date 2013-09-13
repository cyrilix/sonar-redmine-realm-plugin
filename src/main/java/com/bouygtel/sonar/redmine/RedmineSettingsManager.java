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

import java.util.HashMap;
import java.util.Map;

import org.sonar.api.config.Settings;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.User;

public class RedmineSettingsManager {
	private final String url;
	private final RedmineManager manager;

	public RedmineSettingsManager(Settings settings) {
		url = settings.getString("redmine.url");
		manager =  new RedmineManager(url, settings.getString("redmine.key"));
	}

	public Map<String, User> getUsers() {
		Map<String, User> result = new HashMap<String, User>();
		try {
			for (User user: manager.getUsers()) {
				result.put(user.getLogin(), user);
			}
		} catch (RedmineException e) {}
		return result;
	}

	public boolean auth(String username, String password) {
		try {
			new RedmineManager(url, username, password).getCurrentUser();
			return true;
		} catch (RedmineException e) {}
		return false;
	}
}
