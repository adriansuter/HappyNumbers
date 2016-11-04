package happynumbers;

public class SumOfDigitsCubedMapper implements IMapper {

    @Override
    public String getLabel() {
        return "Sum of the digits cubed";
    }

    @Override
    public int map(int input, int radix) {
        String nr = Integer.toString(input, radix);

        int sum = 0;
        for (int i = 0; i < nr.length(); i++) {
            String digit = nr.substring(i, i + 1);
            int base10 = Integer.parseInt(digit, radix);
            int square = (int) Math.pow(base10, 3);

            sum += square;
        }

        return sum;
    }

}
