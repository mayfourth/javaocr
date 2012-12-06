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

package net.sourceforge.javaocr.plugin.moment;

import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.cluster.FeatureExtractor;

/**
 * class encapsulate Hu moment computations. See Gonzalez/Woods, Digital Image Processing, Prentice Hall
 * or Theodoridis/Koutroumbas, Pattern Recognition, Academic Press
 * <p/>
 * http://www.informatik.haw-hamburg.de/fileadmin/Homepages/ProfMeisel/Vorlesungen/WP_RobotVision/V/RV06.pdf
 * pages 44-46
 * <p/>
 */
public class HuMoments implements FeatureExtractor {


    public double[] extract(Image image) {


        // m00 - used to normalise moments
        RawMomentFilter M00 = new RawMomentFilter(0, 0);
        M00.process(image);
        double m00 = M00.getMoment();


        //  m10
        RawMomentFilter M10 = new RawMomentFilter(1, 0);
        M10.process(image);
        double m10 = M10.getMoment();


        //  m01
        RawMomentFilter M01 = new RawMomentFilter(0, 1);
        M01.process(image);
        double m01 = M01.getMoment();


        // ready to compute image weight center, will be used for central moments computation
        double xMean = m10 / m00;
        double yMean = m01 / m00;
        //System.err.println("xmean: " + xMean + " ymean: " + yMean);


        // Phi1 -> n20 + n02
        CentralMomentFilter N20 = new CentralMomentFilter(2, 0, xMean, yMean);
        CentralMomentFilter N02 = new CentralMomentFilter(0, 2, xMean, yMean);

        N20.process(image);
        double n20 = N20.normalise(m00);

        N02.process(image);
        double n02 = N02.normalise(m00);


        double moments[] = new double[8];

        moments[0] = n20 + n02;

        // Phi2 -> (n20-n02)^2 + n11
        CentralMomentFilter N11 = new CentralMomentFilter(1, 1, xMean, yMean);
        N11.process(image);

        double n11 = N11.normalise(m00);

        moments[1] = Math.pow(n20 - n02, 2) + 4 * Math.pow(n11, 2);

        // Phi3  (n30 - 3n12)^2 + (n03-3n21)^2 
        CentralMomentFilter N30 = new CentralMomentFilter(3, 0, xMean, yMean);
        N30.process(image);

        double n30 = N30.normalise(m00);

        CentralMomentFilter N03 = new CentralMomentFilter(0, 3, xMean, yMean);
        N03.process(image);

        double n03 = N03.normalise(m00);

        CentralMomentFilter N21 = new CentralMomentFilter(2, 1, xMean, yMean);
        N21.process(image);

        double n21 = N21.normalise(m00);

        CentralMomentFilter N12 = new CentralMomentFilter(1, 2, xMean, yMean);
        N12.process(image);

        double n12 = N12.normalise(m00);

        moments[2] = Math.pow(n30 - 3 * n12, 2) + Math.pow(n03 - 3 * n21, 2);

        //Phi4  (n30 + 3n12)^2 + (n03 + 3n21)^2
        moments[3] = Math.pow(n30 + n12, 2) + Math.pow(n03 + n21, 2);

        // Phi5
        moments[4] = (n30 - 3 * n12) * (n30 + n12) * (Math.pow(n30 + n12, 2) - 3 * Math.pow(n21 + n03, 2)) +
                (n03 - 3 * n21) * (n03 + n21) * (Math.pow(n03 + n21, 2) - 3 * Math.pow(n30 + n12, 2));

        // Phi 6
        moments[5] = (n20 - n02) * (Math.pow(n30 + n12, 2) - Math.pow(n21 + n03, 2)) +
                4 * n11 * (n30 + n12) * (n03 + n21);

        // Phi 7
        moments[6] = (3 * n21 - n03) * (n30 + n12) * (Math.pow(n30 + n12, 2) - 3 * Math.pow(n21 + n03, 2)) +
                (n30 - 3 * n12) * (n21 + n03) * (Math.pow(n03 + n21, 2) - 3 * Math.pow(n30 + n12, 2));

        // Phi 8.  Was missing from original Hu moments and proposed later
        // see http://en.wikipedia.org/wiki/Image_moment
        moments[7] = n11 * (Math.pow(n30 + n12, 2) - Math.pow(n03 + n21,2)) - (n20 -n02 )* (n30 + n12) * (n03 + n21);
        
        return moments;
    }

    /**
     * tehre are 7 momemts defined
     *
     * @return
     */
    public int getSize() {
        return 8;
    }

}
