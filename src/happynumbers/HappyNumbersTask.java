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

import happynumbers.provider.NumberProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.concurrent.Task;

/**
 *
 * @author Adrian Suter, https://github.com/adriansuter/
 */
public class HappyNumbersTask extends Task<HappyNumbersResult> {

    private final NumberProvider _numberProvider;
    private final ArrayList<Integer> _selectedBases;

    public HappyNumbersTask(NumberProvider numberProvider, ArrayList<Integer> selectedBases) {
        this._numberProvider = numberProvider;
        this._selectedBases = selectedBases;
    }

    @Override
    protected HappyNumbersResult call() throws Exception {
        HappyNumbersResult result = new HappyNumbersResult(99, 99);

        Iterator<Integer> basesIterator = this._selectedBases.iterator();
        while (basesIterator.hasNext()) {
            if (isCancelled()) {
                this.updateMessage("Cancelled");
                break;
            }
            Integer base = basesIterator.next();

            HashMap<Integer, String> numbers = this._numberProvider.getNumbers(base);
            this.updateMessage("Handling base " + base + ". #numbers: " + numbers.size());

            Iterator<Map.Entry<Integer, String>> numberIterator = numbers.entrySet().iterator();
            while (numberIterator.hasNext()) {
                if (isCancelled()) {
                    this.updateMessage("Cancelled");
                    break;
                }

                Map.Entry<Integer, String> number = numberIterator.next();
                System.out.println(number.getKey() + ": " + number.getValue());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        return result;
    }

}
