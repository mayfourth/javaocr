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

/**
 * conrainer to encapsulate mathichg result, distance and quality thresholds.
 *
 * @author Konstantin Pribluda
 */
public class Match implements Comparable<Match> {
    double distance;
    Character chr;
    double yellow;
    double red;


    public Match() {
    }

    public Match(Character chr, double distance, double yellow, double red) {
        this.chr = chr;
        this.distance = distance;
        this.red = red;
        this.yellow = yellow;
    }

    /**
     * mnatched character
     */
    public Character getChr() {
        return chr;
    }

    public void setChr(Character chr) {
        this.chr = chr;
    }

    /**
     * cgaracter distance provided by metric
     *
     * @return
     */
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * threshold marking invalid match
     *
     * @return
     */
    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    /**
     * threshold marking problematic (low quality) match
     *
     * @return
     */
    public double getYellow() {
        return yellow;
    }

    public void setYellow(double yellow) {
        this.yellow = yellow;
    }

    public int compareTo(Match o) {
        return new Double(distance).compareTo(new Double(o.getDistance()));
    }
}
