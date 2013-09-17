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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taskadapter.redmineapi.NotFoundException;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.User;

/**
 * The RedmineUsersManager will parse the settings. This class is also responsible for authentication.
 */
public class RedmineUsersManager implements UsersManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineUsersManager.class);
    private final String url;
    private final RedmineManager manager;

    /**
     * Create an instance of the {@link RedmineUsersManager}.
     * 
     * @param url redmine url.
     * @param apiKey remine api key to use to authenticate
     */
    public RedmineUsersManager(String url, String apiKey) {
        this.url = url;
        manager = new RedmineManager(url, apiKey);
    }

    /**
     * Create an instance of the {@link RedmineUsersManager}.
     * 
     * @param url redmine url.
     * @param redmineManager manager to connect to redmine with administrator rights.
     */
    public RedmineUsersManager(String url, RedmineManager redmineManager) {
        this.url = url;
        manager = redmineManager;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.bouygtel.sonar.redmine.UsersManager#getUsers()
     */
    @Override
    public Map<String, User> getUsers() {
        Map<String, User> result = new HashMap<String, User>();
        try {
            for (User user : manager.getUsers()) {
                result.put(user.getLogin(), user);
            }
        } catch (RedmineException e) {
            LOGGER.error("Error during fetch users: {}" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.bouygtel.sonar.redmine.UsersManager#getUser(java.lang.String)
     */
    @Override
    public User getUser(String username) {
        try {
            return manager.getUserByLogin(username);
        } catch (NotFoundException e) {
            LOGGER.info("User not found {}: {}", username, e.getMessage());
        } catch (RedmineException e) {
            LOGGER.error("Error during fetch users: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.bouygtel.sonar.redmine.UsersManager#auth(java.lang.String, java.lang.String)
     */
    @Override
    public boolean auth(String username, String password) {
        try {
            getRedmineManagerForAuth(url, username, password).getCurrentUser();
            return true;
        } catch (RedmineException e) {
            LOGGER.info("Authentication refused for user {}: {}", username, e.getMessage());
        }
        return false;
    }

    /**
     * Return {@link RedmineManager} to test authentication
     * 
     * This method is principaly used for unit test
     * 
     * @param url redmine url
     * @param username login of the user
     * @param password password to authenticate
     * 
     * @return {@link RedmineManager}
     */
    RedmineManager getRedmineManagerForAuth(String redmineUrl, String username, String password) {
        return new RedmineManager(redmineUrl, username, password);
    }
}
