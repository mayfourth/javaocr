/*
 * Copyright (c) 2003-2012, Ronald B. Cemer , Konstantin Pribluda, William Whitney, Andrea De Pasquale
 *
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.javaocr.matcher;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * test proper function of free space matching
 */
public class FreeSpacesMatcherTest {

    /**
     * matcher shall be configured properly when container list is set
     */
    @Test
    public void testContainerSetting() {
        FreeSpacesMatcher matcher = new FreeSpacesMatcher();

        FreeSpacesContainer container = new FreeSpacesContainer();
        container.setCharacters(new Character[]{'a'});
        container.setCount(10);

        FreeSpacesContainer anotherContainer = new FreeSpacesContainer();
        anotherContainer.setCharacters(new Character[]{'b'});
        anotherContainer.setCount(23);


        matcher.setContainers(Arrays.asList(container,anotherContainer));

        List<Match> matchList = matcher.classify(new double[]{5});
        assertEquals(0, matchList.size());

        matchList = matcher.classify(new double[]{23});
        assertEquals(1, matchList.size());
        assertEquals((Object) 'b', matchList.get(0).getChr());

        matchList = matcher.classify(new double[]{10});
        assertEquals(1, matchList.size());
        assertEquals((Object) 'a', matchList.get(0).getChr());
    }

}
