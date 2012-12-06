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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * test proper function of matcher util
 */
public class MatcherUtilTest {

    @Test
    public void testMerge() {
        Match included = new Match('a', 0, 0, 0);
        Match excluded = new Match('b', 0, 0, 0);
        Match mergeFactor = new Match('a', 0, 0, 0);

        final List<Match> merged = MatcherUtil.merge(Arrays.asList(excluded, included), Arrays.asList(mergeFactor));
        assertTrue(merged.contains(included));
        assertFalse(merged.contains(excluded));
    }
}
