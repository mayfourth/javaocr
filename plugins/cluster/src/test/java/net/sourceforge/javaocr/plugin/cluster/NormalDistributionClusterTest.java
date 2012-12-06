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

package net.sourceforge.javaocr.plugin.cluster;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * test capabilities of match class
 */
public class NormalDistributionClusterTest extends TestCase {

    /**
     * test that constructor paramaters  are stored
     */
    public void testParameterStorage() {
        NormalDistributionCluster normalDistributionCluster = new NormalDistributionCluster(1){
            public double distance(double[] features) {
                return 0;
            }
        };

        assertEquals(1, normalDistributionCluster.getDimensions());
    }

    /**
     * if there are no sampler, expectation and variance shall be null
     */
    @Test
    public void testThatNoSamplesMeansNullExpectationEndVariation() {
        NormalDistributionCluster normalDistributionCluster = new NormalDistributionCluster(1){
            public double distance(double[] features) {
                return 0;
            }
        };

        for (double mx : normalDistributionCluster.center())
            assertEquals(0d, mx);

        for (double var : normalDistributionCluster.getVar())
            assertEquals(0d, var);
    }

    /**
     * all the arrays shall have same size
     */
    @Test
    public void testSizeIsPropagatedToArrays() {
        NormalDistributionCluster normalDistributionCluster = new NormalDistributionCluster(5){
            public double distance(double[] features) {
                return 0;
            }
        };;

        assertEquals(5, normalDistributionCluster.center().length);
        assertEquals(5, normalDistributionCluster.getVar().length);
        assertEquals(5, normalDistributionCluster.getSum().length);
        assertEquals(5, normalDistributionCluster.getQuads().length);
    }

}
