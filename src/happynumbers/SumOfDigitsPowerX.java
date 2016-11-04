package happynumbers;

public class SumOfDigitsPowerX implements IMapper {

    private final int _exponent;

    public SumOfDigitsPowerX(int exponent) {
        this._exponent = exponent;
    }

    @Override
    public String getLabel() {
        return "Sum of (Digits powered to " + this._exponent + ")";
    }

    @Override
    public int map(int input, int radix) {
        String nr = Integer.toString(input, radix);

        int sum = 0;
        for (int i = 0; i < nr.length(); i++) {
            String digit = nr.substring(i, i + 1);
            int base10 = Integer.parseInt(digit, radix);
            int square = (int) Math.pow(base10, this._exponent);

            sum += square;
        }

        return sum;
    }

}
