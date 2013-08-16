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

import org.sonar.api.security.ExternalGroupsProvider;

import com.taskadapter.redmineapi.bean.Group;
import com.taskadapter.redmineapi.bean.User;

/**
 * @author Raphael Jolly
 */
public class RedmineGroupsProvider extends ExternalGroupsProvider {
	private final Map<String, User> users;

	public RedmineGroupsProvider(Map<String, User> users) {
		this.users = users;
	}

	@Override
	public Collection<String> doGetGroups(String username) {
		User user = users.get(username);
		Collection<String> result = new HashSet<String>();
		for (Group group: user.getGroups()) {
			result.add(group.getName());
		}
		return result;
	}
}
