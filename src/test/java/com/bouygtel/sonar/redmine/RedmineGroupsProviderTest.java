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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.security.ExternalGroupsProvider;

import com.taskadapter.redmineapi.bean.Group;
import com.taskadapter.redmineapi.bean.User;

/**
 * @author Raphael Jolly
 */
public class RedmineGroupsProviderTest {
    private static Logger LOGGER = LoggerFactory.getLogger(RedmineGroupsProviderTest.class);

    /**
     * Test method {@link RedmineGroupsProvider#doGetGroups(String)}
     */
    @Test
    public void testDoGetGroupsString() {

        try {
            IMocksControl control = createStrictControl();
            UsersManager usersManager = control.createMock(UsersManager.class);
            ExternalGroupsProvider provider = new RedmineGroupsProvider(usersManager);

            List<Group> groups = new ArrayList<Group>();

            Group group1 = new Group();
            group1.setName("titi");
            Group group2 = new Group();
            group2.setName("tata");
            groups.add(group1);
            groups.add(group2);
            User user = new User();
            user.setGroups(groups);

            control.reset();
            EasyMock.expect(usersManager.getUser("toto")).andReturn(user);
            control.replay();

            Collection<String> result = provider.doGetGroups("toto");
            assertEquals(result.size(), 2);
            assertTrue(result.contains("titi"));
            assertTrue(result.contains("tata"));

            control.verify();

        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test method {@link RedmineGroupsProvider#doGetGroups(String)} without group found
     */
    @Test
    public void testNoGroup() {
        IMocksControl control = createStrictControl();
        UsersManager usersManager = control.createMock(UsersManager.class);
        ExternalGroupsProvider provider = new RedmineGroupsProvider(usersManager);

        User user = new User();

        control.reset();
        EasyMock.expect(usersManager.getUser("toto")).andReturn(user);
        control.replay();

        Collection<String> result = provider.doGetGroups("toto");
        assertTrue(result.isEmpty());

        control.verify();
    }

    /**
     * Test method {@link RedmineGroupsProvider#doGetGroups(String)} without user found
     */
    @Test
    public void testNoUser() {

        IMocksControl control = createStrictControl();
        UsersManager usersManager = control.createMock(UsersManager.class);
        RedmineGroupsProvider provider = new RedmineGroupsProvider(usersManager);

        // Return null if none user is found
        control.reset();
        EasyMock.expect(usersManager.getUser("toto")).andReturn(null);
        control.replay();

        assertThat(provider.doGetGroups("toto"), nullValue(Collection.class));
        assertThat(provider.doGetGroups(null), nullValue(Collection.class));
        control.verify();

    }
}
