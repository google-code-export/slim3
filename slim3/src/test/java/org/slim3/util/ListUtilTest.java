/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class ListUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void splitWithMod() throws Exception {
        List<String> list = Arrays.asList("1", "2", "3", "4", "5", "6", "7");
        List<List<String>> ret = ListUtil.split(list, 2);
        assertThat(ret.size(), is(4));
        assertThat(ret.get(0), is(Arrays.asList("1", "2")));
        assertThat(ret.get(1), is(Arrays.asList("3", "4")));
        assertThat(ret.get(2), is(Arrays.asList("5", "6")));
        assertThat(ret.get(3), is(Arrays.asList("7")));
    }

    /**
     * @throws Exception
     */
    @Test
    public void splitWithoutMod() throws Exception {
        List<String> list = Arrays.asList("1", "2", "3", "4", "5", "6");
        List<List<String>> ret = ListUtil.split(list, 2);
        assertThat(ret.size(), is(3));
        assertThat(ret.get(0), is(Arrays.asList("1", "2")));
        assertThat(ret.get(1), is(Arrays.asList("3", "4")));
        assertThat(ret.get(2), is(Arrays.asList("5", "6")));
    }
}
