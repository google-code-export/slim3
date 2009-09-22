/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.gen.task;

import java.io.File;
import java.net.URLDecoder;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class WebConfigTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testGetRootPackageName_javaee() throws Exception {
        String path =
            getClass().getPackage().getName().replace(".", "/")
                + "/"
                + "web_javaee.xml";
        final File webXml = getFile(path);
        WebConfig webConfig = new WebConfig(new File("dummy")) {

            @Override
            protected File createWebXml() {
                return webXml;
            }
        };
        assertEquals("aaa.bbb", webConfig.getRootPackageName());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetRootPackageName_javaee_not_found() throws Exception {
        String path =
            getClass().getPackage().getName().replace(".", "/")
                + "/"
                + "web_javaee_not_found.xml";
        final File webXml = getFile(path);
        WebConfig webConfig = new WebConfig(new File("dummy")) {

            @Override
            protected File createWebXml() {
                return webXml;
            }
        };
        try {
            webConfig.getRootPackageName();
            fail();
        } catch (RuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetRootPackageName_j2ee() throws Exception {
        String path =
            getClass().getPackage().getName().replace(".", "/")
                + "/"
                + "web_j2ee.xml";
        final File webXml = getFile(path);
        WebConfig webConfig = new WebConfig(new File("dummy")) {

            @Override
            protected File createWebXml() {
                return webXml;
            }
        };
        assertEquals("aaa.bbb", webConfig.getRootPackageName());
    }

    /**
     * 
     * @throws Exception
     */
    public void testIsGWTServiceServletDefined_javaee() throws Exception {
        String path =
            getClass().getPackage().getName().replace(".", "/")
                + "/"
                + "web_javaee.xml";
        final File webXml = getFile(path);
        WebConfig webConfig = new WebConfig(new File("dummy")) {

            @Override
            protected File createWebXml() {
                return webXml;
            }
        };
        assertTrue(webConfig.isGWTServiceServletDefined());
    }

    /**
     * 
     * @throws Exception
     */
    public void testIsGWTServiceServletDefined_javaee_not_found()
            throws Exception {
        String path =
            getClass().getPackage().getName().replace(".", "/")
                + "/"
                + "web_javaee_not_found.xml";
        final File webXml = getFile(path);
        WebConfig webConfig = new WebConfig(new File("dummy")) {

            @Override
            protected File createWebXml() {
                return webXml;
            }
        };
        assertFalse(webConfig.isGWTServiceServletDefined());
    }

    /**
     * 
     * @throws Exception
     */
    public void testIsGWTServiceServletDefined_j2ee() throws Exception {
        String path =
            getClass().getPackage().getName().replace(".", "/")
                + "/"
                + "web_j2ee.xml";
        final File webXml = getFile(path);
        WebConfig webConfig = new WebConfig(new File("dummy")) {

            @Override
            protected File createWebXml() {
                return webXml;
            }
        };
        assertTrue(webConfig.isGWTServiceServletDefined());
    }

    /**
     * 
     * @param path
     * @return a file
     * @throws Exception
     */
    protected File getFile(String path) throws Exception {
        ClassLoader classLoader =
            Thread.currentThread().getContextClassLoader();
        String fileName = classLoader.getResource(path).getFile();
        String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
        return new File(decodedFileName);
    }
}