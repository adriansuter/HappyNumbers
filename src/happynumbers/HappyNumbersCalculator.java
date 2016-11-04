package happynumbers;

import happynumbers.mapper.IMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import happynumbers.provider.INumberProvider;

public class HappyNumbersCalculator {

    /**
     *
     */
    private final int _radix;

    /**
     *
     */
    private final INumberProvider _numberProvider;

    /**
     *
     */
    private final IMapper _mapper;

    /**
     * Holds the mappings for the numbers.
     *
     * For example (in base 10): 10 => 1, 11 => 2, 12 => 5, etc.
     */
    private final HashMap<String, String> _numberMap;

    /**
     * Holds all the numbers that would eventually map into 1, i.e. the happy
     * numbers.
     */
    private final ArrayList<String> _happyNumbers;

    /**
     *
     */
    private final ArrayList<Integer> _numbers;

    /**
     * Constructor.
     *
     * @param radix
     * @param numberProvider
     * @param mapper
     */
    public HappyNumbersCalculator(int radix, INumberProvider numberProvider, IMapper mapper) {
        this._radix = radix;
        this._numberProvider = numberProvider;
        this._mapper = mapper;

        this._numberMap = new HashMap<>();
        this._happyNumbers = new ArrayList<>();
        this._numbers = numberProvider.getNumbers(radix);

        this.detect();
    }

    public int getRadix() {
        return this._radix;
    }

    public ArrayList<String> getHappyNumbers() {
        return this.getHappyNumbers(false);
    }

    public ArrayList<String> getHappyNumbers(boolean onlyInputNumbers) {
        if (!onlyInputNumbers) {
            return this._happyNumbers;
        }

        ArrayList<String> happyNumbers = new ArrayList<>();
        Iterator<String> iterator = this._happyNumbers.iterator();
        while (iterator.hasNext()) {
            String n = iterator.next();

            int nr = Integer.parseInt(n, _radix);
            if (this._numbers.contains(nr)) {
                happyNumbers.add(n);
            }
        }

        Collections.sort(happyNumbers);

        return happyNumbers;
    }

    public HashMap<String, String> getNumberMap() {
        return this._numberMap;
    }

    public IMapper getMapper() {
        return this._mapper;
    }

    public final void detect() {
        int number;

        // Holds a list of all happy candidates.
        ArrayList<String> happyCandidates = new ArrayList<>();

        Iterator<Integer> numberIterator = this._numbers.iterator();
        while (numberIterator.hasNext()) {
            number = numberIterator.next();

            happyCandidates.clear();
            while (true) {
                // Get the string representation in the given base of the current number.
                String numberRadix = Integer.toString(number, _radix).toUpperCase();

                // Check if we already calculated the map for this number.
                if (_numberMap.containsKey(numberRadix)) {
                    if (number == 1 || _happyNumbers.contains(numberRadix)) {
                        // This is a happy number.
                        Iterator<String> iterator = happyCandidates.iterator();
                        while (iterator.hasNext()) {
                            _happyNumbers.add(iterator.next());
                        }
                    }
                    break;
                }

                int temporary = _mapper.map(number, _radix);

                String temporaryRadix = Integer.toString(temporary, _radix).toUpperCase();
                happyCandidates.add(numberRadix);

                // System.out.println(number + " => *" + numberRadix + " maps into " + temporary + " => *" + temporaryRadix);
                _numberMap.put(numberRadix, temporaryRadix);

                number = temporary;
            }
        }
    }
}
