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

import java.util.List;

import org.sonar.api.SonarPlugin;

import com.google.common.collect.ImmutableList;

/**
 * Sonar plugin to authenticate users from redmine rest api.
 * 
 * @author Raphael Jolly
 */
public class RedminePlugin extends SonarPlugin {

    /**
     * Constructor
     */
    public RedminePlugin() {}

    /**
     * {@inheritDoc}
     * 
     * @see org.sonar.api.Plugin#getExtensions()
     * @deprecated Mark as deprecated because Plugin is deprecated
     */
    @Deprecated
    @Override
    public List<?> getExtensions() {
        return ImmutableList.of(RedmineRealm.class);
    }

}
