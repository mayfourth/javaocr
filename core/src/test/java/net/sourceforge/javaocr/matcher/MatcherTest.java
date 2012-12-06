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

import static org.junit.Assert.assertEquals;

/**
 * test capabilities of matcher
 */
public class MatcherTest {

    /**
     * test proper comparison
     */
    @Test
    public void testComparison() {
        Match match = new Match();
        match.setDistance(0.1);
        Match anotherMatch = new Match();
        anotherMatch.setDistance(0.3);
        assertEquals(-1, match.compareTo(anotherMatch));
        assertEquals(1, anotherMatch.compareTo(match));
        assertEquals(0, match.compareTo(match));

    }

}
