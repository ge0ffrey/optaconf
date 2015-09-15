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

package org.optaconf.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public abstract class AbstractArquillianTest {

    private static final String POM_DIRECTORY_NAME = "optaconf-webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        File file = findPomFile();
        return ShrinkWrap.create(MavenImporter.class)
                .loadPomFromFile(file)
                .importBuildOutput()
                .as(WebArchive.class);
    }

    private static File findPomFile() {
        File file = new File("pom.xml");
        if (!file.exists()) {
            throw new IllegalStateException("The file (" + file + ") does not exist.\n"
                    + "This test needs to be run with the working directory " +  POM_DIRECTORY_NAME + ".");
        }
        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
            throw new IllegalStateException("Could not get cannonical file for file (" + file + ").", e);
        }
        if (!file.getParentFile().getName().equals(POM_DIRECTORY_NAME)) {
            throw new IllegalStateException("The file (" + file + ") is not correct.\n"
                    + "This test needs to be run with the working directory " + POM_DIRECTORY_NAME + ".");
        }
        return file;
    }

    protected String postAndReadSingleMessage(String url) {
        InputStream contentIn = null;
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
            contentIn = response.getEntity().getContent();
            List<String> lines = IOUtils.readLines(contentIn);
            assertEquals(1, lines.size());
            return lines.get(0);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to connect to url (" + url + ").", e);
        } finally {
            IOUtils.closeQuietly(contentIn);
        }
    }

}
