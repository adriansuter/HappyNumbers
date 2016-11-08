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
import java.util.SortedMap;
import java.util.TreeMap;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javax.script.Invocable;
import javax.script.ScriptException;

/**
 *
 * @author Adrian Suter, https://github.com/adriansuter/
 */
public class HappyNumbersTask extends Task<HappyNumbersResult> {

    private final NumberProvider _numberProvider;
    private final int _base;
    private final Invocable _invocable;

    /**
     * Holds the mappings for the numbers.
     *
     * For example (in base 10 and in default "square the digits and sum up"):
     * 10 => 1, 11 => 2, 12 => 5, etc.
     */
    private final HashMap<String, String> _mappings = new HashMap<>();

    private final TreeMap<Integer, String> _happyNumbers = new TreeMap<>();

    public HappyNumbersTask(NumberProvider numberProvider, int base, Invocable invocable) {
        this._numberProvider = numberProvider;
        this._base = base;
        this._invocable = invocable;
    }

    @Override
    protected HappyNumbersResult call() throws Exception {
        HashMap<Integer, String> numbers = this._numberProvider.getNumbers(_base);
        this.updateMessage("Handling base " + _base + ". #numbers: " + numbers.size());

        // Holds a list of all happy candidates.
        HashMap<Integer, String> happyCandidates = new HashMap<>();

        Iterator<Map.Entry<Integer, String>> numberIterator = numbers.entrySet().iterator();
        while (numberIterator.hasNext()) {
            if (isCancelled()) {
                this.updateMessage("Cancelled");
                break;
            }

            happyCandidates.clear();

            // Get the next number as integer and in base representation.
            Map.Entry<Integer, String> number = numberIterator.next();

            int nr = number.getKey();
            String nrBase = number.getValue();

            while (true) {
                // Check if we already calculated the map for this number.
                if (_mappings.containsKey(nrBase)) {
                    if (nr == 1 || _happyNumbers.containsKey(nr)) {
                        // This is a happy number.
                        Iterator<Map.Entry<Integer, String>> iterator = happyCandidates.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Integer, String> next = iterator.next();
                            _happyNumbers.put(next.getKey(), next.getValue());
                        }
                    }
                    break;
                }

                happyCandidates.put(nr, nrBase);

                try {
                    Object answer = _invocable.invokeFunction("mapper", nr, nrBase, _base);
                    if (answer instanceof Double || answer instanceof Integer) {
                        int temporary = (int) Double.parseDouble(answer.toString());
                        String temporaryBase = Integer.toString(temporary, _base).toUpperCase();

                        _mappings.put(nrBase, temporaryBase);

                        nr = temporary;
                        nrBase = temporaryBase;
                    }
                } catch (NoSuchMethodException | ScriptException exception) {
                    throw new Exception("Mapping could not applied");
                }
            }
        }

        ArrayList<String> finalHappyNumbers = new ArrayList<>();
        Iterator<Map.Entry<Integer, String>> happyIterator = _happyNumbers.entrySet().iterator();
        while (happyIterator.hasNext()) {
            Map.Entry<Integer, String> happyNumber = happyIterator.next();

            if (numbers.containsKey(happyNumber.getKey())) {
                // We are only interested in those numbers that were actually in the input number set.
                finalHappyNumbers.add(happyNumber.getValue());
            }
        }

        return new HappyNumbersResult(_base, numbers.size(), finalHappyNumbers);
    }

}
