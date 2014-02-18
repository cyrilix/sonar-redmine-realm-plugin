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

import java.util.Map;

import com.taskadapter.redmineapi.bean.User;

/**
 * Interface to manage users
 * 
 * @author Cyrille Nofficial
 */
public interface UsersManager {

    /**
     * Get all the @link{User}s available in the target.
     * 
     * @return A @link{Map} with all the @link{User} objects. The key is the login of the user.
     */
    Map<String, User> getUsers();

    /**
     * Return user details from username
     * 
     * @param username user details
     * 
     * @return user details
     */
    User getUser(String username);

    /**
     * Authenticates a user.
     * 
     * @param username login of the user
     * @param password password to authenticate
     * 
     * @return <code>true</code> if the user is recognized; <code>false</code> otherwise.
     */
    boolean auth(String username, String password);

}
