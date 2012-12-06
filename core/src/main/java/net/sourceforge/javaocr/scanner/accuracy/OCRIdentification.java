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
package net.sourceforge.javaocr.scanner.accuracy;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Provides a data structure to manage an OCR character recognition attempt.
 * @author William Whitney
 */
public class OCRIdentification
{

    private OCRComp ocrType;
    private ArrayList<Character> chars;
    private ArrayList<Double> identErrors;

    public OCRIdentification(OCRComp ocrType)
    {
        chars = new ArrayList<Character>();
        identErrors = new ArrayList<Double>();
        this.ocrType = ocrType;
    }

    public void addChar(char newChar, double identError)
    {
        this.chars.add(newChar);
        this.identErrors.add(identError);
    }

    public int getNumChars()
    {
        return chars.size();
    }

    public char getCharIdx(int idx)
    {
        return chars.get(idx);
    }

    public double getIdentErrorIdx(int idx)
    {
        return identErrors.get(idx);
    }

    public OCRComp getOCRType()
    {
        return ocrType;
    }

    @Override
    public String toString()
    {
        String retStr = "----- OCRIdentification  -----\n";
        retStr += "OCR Type: " + ocrType + "\n";
        for (int i = 0; i < this.chars.size(); i++)
        {
            retStr += "Char: " + this.chars.get(i) + " " + this.identErrors.get(i) + "\n";
        }

        return retStr;
    }
    private static final Logger LOG = Logger.getLogger(OCRIdentification.class.getName());
}
