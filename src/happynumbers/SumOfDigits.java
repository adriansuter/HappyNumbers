package happynumbers;

public class SumOfDigits implements IMapper {

    @Override
    public String getLabel() {
        return "Sum of digits";
    }

    @Override
    public int map(int input, int radix) {
        String nr = Integer.toString(input, radix);

        int sum = 0;
        for (int i = 0; i < nr.length(); i++) {
            String digit = nr.substring(i, i + 1);
            sum += Integer.parseInt(digit, radix);
        }

        return sum;
    }

}
