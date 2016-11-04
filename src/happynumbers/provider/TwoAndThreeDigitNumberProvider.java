package happynumbers.provider;

import java.util.ArrayList;

public class TwoAndThreeDigitNumberProvider implements INumberProvider {

    @Override
    public ArrayList<Integer> getNumbers(int radix) {
        ArrayList<Integer> numbers = new ArrayList<>();

        int numberStart = radix;
        int numberStop = (int) Math.pow(radix, 3);

        for (int i = numberStart; i < numberStop; i++) {
            numbers.add(i);
        }

        return numbers;

    }

}
