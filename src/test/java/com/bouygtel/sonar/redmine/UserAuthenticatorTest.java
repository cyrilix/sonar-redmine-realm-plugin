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

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.security.Authenticator.Context;

/**
 * Test of class {@link UserAuthenticator}
 * 
 * @author Cyrille Nofficial
 */
public class UserAuthenticatorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticatorTest.class);

    /**
     * Default Constructor
     */
    public UserAuthenticatorTest() {}

    /**
     * Test de la méthode {@link UserAuthenticator#doAuthenticate(org.sonar.api.security.Authenticator.Context)}
     */
    @Test
    public void testDoAuthenticate() {
        LOGGER.info("------ testDoAuthenticate -------");

        try {
            IMocksControl control = EasyMock.createStrictControl();
            UsersManager usersManager = control.createMock(UsersManager.class);
            UserAuthenticator instance = new UserAuthenticator(usersManager);

            Context context = new Context("username", "password", control.createMock(HttpServletRequest.class));

            control.reset();
            expect(usersManager.auth("username", "password")).andReturn(true);
            control.replay();

            assertTrue(instance.doAuthenticate(context));

            control.verify();

        } catch (Exception e) {
            LOGGER.error("Erreur imprévue: " + e.getMessage(), e);
            fail("Erreur imprévue: " + e.getMessage());
        }
    }
}
