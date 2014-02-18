/*
 * Sonar Redmine Authentification Plugin
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

import static java.util.Arrays.asList;
import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.easymock.IMocksControl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taskadapter.redmineapi.NotFoundException;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.User;

/**
 * Test class {@link RedmineUsersManager}
 * 
 * @author Cyrille Nofficial
 */
public class RedmineUsersManagerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineUsersManagerTest.class);

    /**
     * Default Constructor
     */
    public RedmineUsersManagerTest() {}

    /**
     * Test de la méthode {@link RedmineUsersManager#auth(String, String)}
     */
    @Test
    public void testAuth() {
        LOGGER.info("------ testAuth -------");

        class RedmineUsersManagerMock extends RedmineUsersManager {
            String url = "";
            String username = "";
            String password = "";
            private RedmineManager redmineManager;

            /**
             * Constructeur
             * 
             * @param url
             * @param redmineManager
             */
            public RedmineUsersManagerMock(String url, RedmineManager redmineManager) {
                super(url, redmineManager);
                this.redmineManager = redmineManager;
            }

            /**
             * @param urlRedmine redmine url
             * @param usernameRedmine redmine username
             * @param passwordRedmine redmine password
             * @return
             */
            @Override
            RedmineManager getRedmineManagerForAuth(String urlRedmine, String usernameRedmine, String passwordRedmine) {
                this.url = urlRedmine;
                this.username = usernameRedmine;
                this.password = passwordRedmine;
                return redmineManager;
            }
        }

        try {
            IMocksControl control = createStrictControl();
            RedmineManager redmineManager = control.createMock(RedmineManager.class);
            RedmineUsersManagerMock instance = new RedmineUsersManagerMock("url-redmine", redmineManager);

            assertThat(instance.url, is(""));
            assertThat(instance.username, is(""));
            assertThat(instance.password, is(""));

            control.reset();
            expect(redmineManager.getCurrentUser()).andReturn(new User());
            control.replay();

            assertTrue(instance.auth("username", "password"));
            assertThat(instance.url, is("url-redmine"));
            assertThat(instance.username, is("username"));
            assertThat(instance.password, is("password"));
            control.verify();

            // Test KO
            control.reset();
            expect(redmineManager.getCurrentUser()).andThrow(new NotFoundException("Test invalid authentication"));
            control.replay();

            assertFalse(instance.auth("username", "password"));
            control.verify();

        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test de la méthode {@link RedmineUsersManager#getUsers()}
     */
    @Test
    public void testGetUsers() {
        LOGGER.info("------ testGetUsers -------");

        try {
            IMocksControl control = createStrictControl();
            RedmineManager redmineManager = control.createMock(RedmineManager.class);
            RedmineUsersManager instance = new RedmineUsersManager("url-redmine", redmineManager);

            User user1 = new User();
            user1.setLogin("user1");
            user1.setFullName("user1-fullname");
            User user2 = new User();
            user2.setLogin("user2");
            user2.setFullName("user2-fullname");

            Map<String, User> expected = new HashMap<String, User>();
            expected.put("user1", user1);
            expected.put("user2", user2);

            control.reset();
            expect(redmineManager.getUsers()).andReturn(asList(user1, user2));
            control.replay();

            Map<String, User> result = instance.getUsers();

            assertThat(result, equalTo(expected));

            control.verify();

            // Error case
            control.reset();
            expect(redmineManager.getUsers()).andThrow(new RedmineException("Error test"));
            control.replay();

            assertTrue(instance.getUsers().isEmpty());
            control.verify();
        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }

    /**
     * Test methode {@link RedmineUsersManager#getUser(String)}
     */
    @Test
    public void testGetUser() {
        LOGGER.info("------ testGetUser -------");

        try {
            IMocksControl control = createStrictControl();
            RedmineManager redmineManager = control.createMock(RedmineManager.class);
            RedmineUsersManager instance = new RedmineUsersManager("url-redmine", redmineManager);

            User user1 = new User();
            user1.setLogin("user1");
            user1.setFullName("user1-fullname");

            control.reset();
            expect(redmineManager.getUserByLogin("user1")).andReturn(user1);
            control.replay();

            User result = instance.getUser("user1");

            assertThat(result, equalTo(user1));

            control.verify();

            // Error case
            control.reset();
            expect(redmineManager.getUserByLogin("user1")).andThrow(new RedmineException("Error test"));
            control.replay();

            assertThat(instance.getUser("user1"), nullValue(User.class));
            control.verify();

            // User not found
            control.reset();
            expect(redmineManager.getUserByLogin("user1")).andThrow(new NotFoundException("Error test"));
            control.replay();

            assertThat(instance.getUser("user1"), nullValue(User.class));
            control.verify();
        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }
}
