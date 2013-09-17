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

import org.sonar.api.security.Authenticator;

/**
 * Generic authenticator for all plugin implementing {@link UsersManager}
 * 
 * @author Raphael Jolly
 */
public class UserAuthenticator extends Authenticator {
    private final UsersManager userManager;

    /**
     * Constructor
     * 
     * @param userManager
     */
    public UserAuthenticator(UsersManager userManager) {
        this.userManager = userManager;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.sonar.api.security.Authenticator#doAuthenticate(org.sonar.api.security.Authenticator.Context)
     */
    @Override
    public boolean doAuthenticate(Context context) {
        return userManager.auth(context.getUsername(), context.getPassword());
    }
}