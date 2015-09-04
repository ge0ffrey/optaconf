/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaconf.bridge.devoxx;

import java.io.File;
import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.optaconf.domain.Conference;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class DevoxxImporterTest {

    @Deployment
    public static WebArchive createDeployment() {
        File file = new File("pom.xml");
        if (!file.exists()) {
            throw new IllegalStateException("The file (" + file + ") does not exist.\n"
                    + "This test needs to be run with the working directory optaconf-webapp-test.");
        }
        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
            throw new IllegalStateException("Could not get cannonical file for file (" + file + ").", e);
        }
        if (!file.getParentFile().getName().equals("optaconf-webapp-test")) {
            throw new IllegalStateException("The file (" + file + ") is not correct.\n"
                    + "This test needs to be run with the working directory optaconf-webapp-test.");
        }
        return Maven.resolver()
                        .loadPomFromFile(file)
                        .resolve("org.optaconf:optaconf-webapp:war:?")
                        .withoutTransitivity()
                        .asSingle(WebArchive.class);
    }

    @Test
    public void importConference() {
        DevoxxImporter devoxxImporter = new DevoxxImporter();
        Conference conference = devoxxImporter.importConference(false);
        assertNotNull(conference);

    }

}
