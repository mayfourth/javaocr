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
package net.sourceforge.javaocr.ocrPlugins.aspectRatio;

import java.util.logging.Logger;

/**
 * Provides a way to store information about a character and its ratio.
 * @author William Whitney
 */
public class CharacterRatio implements Comparable<CharacterRatio>
{

    private char character;
    private double ratio;
    private static final Logger LOG = Logger.getLogger(CharacterRatio.class.getName());

    public CharacterRatio(char character, double ratio)
    {
        this.character = character;
        this.ratio = ratio;
    }

    public char getCharacter()
    {
        return character;
    }

    public double getRatio()
    {
        return ratio;
    }

    public int compareTo(CharacterRatio other)
    {
        if (ratio < other.getRatio())
        {
            return -1;
        }
        else if (ratio == other.getRatio())
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
}
