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

import static org.easymock.EasyMock.createStrictControl;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.ExternalUsersProvider.Context;
import org.sonar.api.security.UserDetails;

import com.taskadapter.redmineapi.bean.User;

/**
 * @author Raphael Jolly
 */
public class RedmineUsersProviderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineUsersProviderTest.class);

    /**
     * Constructor
     */
    public RedmineUsersProviderTest() {}

    /**
     * Test method {@link RedmineUsersProvider#doGetUserDetails(Context)}
     */
    @Test
    public void testDoGetUserDetailsContext() {
        LOGGER.info("------ testDoGetUserDetailsContext -------");
        try {
            IMocksControl control = createStrictControl();
            UsersManager usersManager = control.createMock(UsersManager.class);

            Map<String, User> users = new HashMap<String, User>();

            User user = new User();
            user.setFullName("To To");
            user.setMail("to@to");
            users.put("toto", user);

            ExternalUsersProvider provider = new RedmineUsersProvider(usersManager);
            Context context = new Context("toto", null);

            control.reset();
            EasyMock.expect(usersManager.getUser("toto")).andReturn(user);
            control.replay();
            UserDetails details = provider.doGetUserDetails(context);

            assertTrue("To To".equals(details.getName()));
            assertTrue("to@to".equals(details.getEmail()));

            control.verify();
        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test method {@link RedmineUsersProvider#doGetUserDetails(Context)}
     */
    @Test
    public void testNoUser() {
        try {
            IMocksControl control = createStrictControl();
            UsersManager usersManager = control.createMock(UsersManager.class);

            ExternalUsersProvider provider = new RedmineUsersProvider(usersManager);
            Context context = new Context("toto", null);

            control.reset();
            EasyMock.expect(usersManager.getUser("toto")).andReturn(null);
            control.replay();

            provider.doGetUserDetails(context);
            control.verify();
        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }
}
