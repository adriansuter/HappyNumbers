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
package happynumbers.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Adrian Suter, https://github.com/adriansuter/
 */
public class NumberProvider {

    private final ArrayList<Integer> _lengths;

    public NumberProvider(boolean length1Enabled, boolean length2Enabled, boolean length3Enabled, boolean length4Enabled) {
        this._lengths = new ArrayList<>();

        if (length1Enabled) {
            this._lengths.add(1);
        }
        if (length2Enabled) {
            this._lengths.add(2);
        }
        if (length3Enabled) {
            this._lengths.add(3);
        }
        if (length4Enabled) {
            this._lengths.add(4);
        }
    }

    public HashMap<Integer, String> getNumbers(int base) {
        HashMap<Integer, String> numbers = new HashMap<>();

        Iterator<Integer> iterator = _lengths.iterator();
        while (iterator.hasNext()) {
            int length = iterator.next();

            for (int number = (int) Math.pow(base, length - 1); number < (int) Math.pow(base, length); number++) {
                numbers.put(number, Integer.toString(number, base));
            }
        }

        return numbers;
    }
}
