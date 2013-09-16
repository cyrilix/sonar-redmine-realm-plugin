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

import org.sonar.api.config.Settings;
import org.sonar.api.security.Authenticator;
import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.SecurityRealm;

/**
 * @author Raphael Jolly
 */
public class RedmineRealm extends SecurityRealm {

    /** Key for read redmine url from sonar configuration */
    public final static String URL_KEY = "redmine.url";
    /** Key for read redmine api key from sonar configuration */
    public final static String API_KEY = "redmine.key";

    private final UsersManager userManager;

    private Authenticator authenticator;
    private ExternalUsersProvider usersProvider;
    private ExternalGroupsProvider groupsProvider;

    /**
     * Constructeur
     * 
     * @param settings
     */
    public RedmineRealm(Settings settings) {
        userManager = new RedmineUsersManager(settings.getString(URL_KEY), settings.getString(API_KEY));
    }

    /**
     * Initializes Redmine authenticator and users and groups providers.
     * 
     */
    @Override
    public void init() {
        authenticator = new UserAuthenticator(userManager);
        usersProvider = new RedmineUsersProvider(userManager);
        groupsProvider = new RedmineGroupsProvider(userManager);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.sonar.api.security.SecurityRealm#doGetAuthenticator()
     */
    @Override
    public Authenticator doGetAuthenticator() {
        return authenticator;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.sonar.api.security.SecurityRealm#getUsersProvider()
     */
    @Override
    public ExternalUsersProvider getUsersProvider() {
        return usersProvider;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.sonar.api.security.SecurityRealm#getGroupsProvider()
     */
    @Override
    public ExternalGroupsProvider getGroupsProvider() {
        return groupsProvider;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.sonar.api.security.SecurityRealm#getName()
     */
    @Override
    public String getName() {
        return "REDMINE";
    }
}
