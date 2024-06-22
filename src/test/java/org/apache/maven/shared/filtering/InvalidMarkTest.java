/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.shared.filtering;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.apache.maven.api.di.Inject;
import org.apache.maven.api.di.testing.MavenDITest;
import org.apache.maven.api.plugin.testing.stubs.ProjectStub;
import org.apache.maven.di.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.maven.api.di.testing.MavenDIExtension.getBasedir;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Mikolaj Izdebski
 */
@MavenDITest
public class InvalidMarkTest {

    @Inject
    Injector container;

    Path outputDirectory = Paths.get(getBasedir(), "target/LongLineTest");

    @BeforeEach
    protected void setUp() throws Exception {
        if (Files.exists(outputDirectory)) {
            IOUtils.deleteDirectory(outputDirectory);
        }
        Files.createDirectories(outputDirectory);
    }

    @Test
    public void testEscape() throws Exception {
        MavenResourcesFiltering mavenResourcesFiltering = container.getInstance(MavenResourcesFiltering.class);

        Resource resource = new Resource();
        resource.setDirectory("src/test/units-files/MSHARED-325");
        resource.setFiltering(true);

        MavenResourcesExecution mavenResourcesExecution = new MavenResourcesExecution(
                Collections.singletonList(resource),
                outputDirectory,
                new ProjectStub().setBasedir(Paths.get(".")),
                "UTF-8",
                Collections.<String>emptyList(),
                Collections.<String>emptyList(),
                new StubSession());

        try {
            mavenResourcesFiltering.filterResources(mavenResourcesExecution);
        } catch (MavenFilteringException e) {
            fail();
        }
    }
}
