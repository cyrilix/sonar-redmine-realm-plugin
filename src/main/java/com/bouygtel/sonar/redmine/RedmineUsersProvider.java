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

import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.UserDetails;

import com.taskadapter.redmineapi.bean.User;

/**
 * {@link ExternalUsersProvider} based on redmine WS-REST api
 * 
 * @author Raphael Jolly
 */
public class RedmineUsersProvider extends ExternalUsersProvider {

    private UsersManager usersManager;

    /**
     * Constructeur
     * 
     * @param usersManager
     */
    public RedmineUsersProvider(UsersManager usersManager) {
        this.usersManager = usersManager;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.sonar.api.security.ExternalUsersProvider#doGetUserDetails(org.sonar.api.security.ExternalUsersProvider.Context)
     */
    @Override
    public UserDetails doGetUserDetails(Context context) {
        User user = usersManager.getUser(context.getUsername());
        if (user == null) return null;
        UserDetails details = new UserDetails();
        details.setName(user.getFullName());
        details.setEmail(user.getMail());
        return details;
    }
}
