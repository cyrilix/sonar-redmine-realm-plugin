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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.ExternalUsersProvider.Context;
import org.sonar.api.security.UserDetails;

import com.taskadapter.redmineapi.bean.User;

/**
 * @author Raphael Jolly
 */
public class RedmineUsersProviderTest {
	@Test
	public void testDoGetUserDetailsContext() {
		Map<String, User> users = new HashMap<String, User>();
		User user = new User();
		user.setFullName("To To");
		user.setMail("to@to");
		users.put("toto", user);
		ExternalUsersProvider provider = new RedmineUsersProvider(users);
		Context context = new Context("toto", null);
		UserDetails details = provider.doGetUserDetails(context);
		assertTrue("To To".equals(details.getName()));
		assertTrue("to@to".equals(details.getEmail()));
	}
}
