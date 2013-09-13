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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.sonar.api.security.ExternalGroupsProvider;

import com.taskadapter.redmineapi.bean.Group;
import com.taskadapter.redmineapi.bean.User;

/**
 * @author Raphael Jolly
 */
public class RedmineGroupsProviderTest {
	@Test
	public void testDoGetGroupsString() {
		Map<String, User> users = new HashMap<String, User>();
		List<Group> groups = new ArrayList<Group>();
		Group group1 = new Group();
		group1.setName("titi");
		Group group2 = new Group();
		group2.setName("tata");
		groups.add(group1);
		groups.add(group2);
		User user = new User();
		user.setGroups(groups);
		users.put("toto", user);
		ExternalGroupsProvider provider = new RedmineGroupsProvider(users);
		Collection<String> result = provider.doGetGroups("toto");
		assertTrue(result.size() == 2);
		assertTrue(result.contains("titi"));
		assertTrue(result.contains("tata"));
	}
}
