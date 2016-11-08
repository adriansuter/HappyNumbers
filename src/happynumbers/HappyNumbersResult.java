/*
 * The MIT License
 *
 * Copyright 2016 Adrian Suter, https://github.com/adriansuter/.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package happynumbers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Adrian Suter, https://github.com/adriansuter/
 */
public class HappyNumbersResult {

    public IntegerProperty base = new SimpleIntegerProperty(0);

    public final void setBase(int base) {
        this.base.set(base);
    }

    public int getBase() {
        return this.base.get();
    }

    public IntegerProperty numbersCount = new SimpleIntegerProperty(0);

    public final void setNumbersCount(int numbersCount) {
        this.numbersCount.set(numbersCount);
    }

    public int getNumbersCount() {
        return this.numbersCount.get();
    }

    public StringProperty finalHappyNumbers = new SimpleStringProperty("");

    public final void setFinalHappyNumbers(String finalHappyNumbers) {
        this.finalHappyNumbers.set(finalHappyNumbers);
    }

    public String getFinalHappyNumbers() {
        return this.finalHappyNumbers.get();
    }

    public HappyNumbersResult(int base, int numbersCount, ArrayList<String> finalHappyNumbers) {
        this.setBase(base);
        this.setNumbersCount(numbersCount);

        StringBuilder sb = new StringBuilder();
        finalHappyNumbers.stream().forEach((s) -> {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(s);
        });
        this.setFinalHappyNumbers(sb.toString());
    }

}
