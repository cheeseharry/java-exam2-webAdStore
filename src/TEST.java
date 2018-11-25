import java.math.BigDecimal;

public class TEST {
    public static void main(String[] args) {
        BigDecimal calcOne = new BigDecimal(1);
        BigDecimal divideBy100 = new BigDecimal(100);

        BigDecimal unitPrice = new BigDecimal(Math.ceil(Math.random()*100));
        System.out.println(unitPrice);

        BigDecimal discountRate = new BigDecimal(Math.ceil(Math.random()*50));
        BigDecimal discountRateDivide = discountRate.divide(divideBy100);
        BigDecimal discountRateCalc = calcOne.subtract(discountRateDivide); // 1-0.25=0.75
        System.out.println(discountRate);

        BigDecimal taxRate = new BigDecimal(8.00);
        BigDecimal taxRateDivide = taxRate.divide(divideBy100); //0.08
        BigDecimal taxRateCalc = taxRateDivide.add(calcOne); // *1.08
        System.out.println(taxRate);


        BigDecimal adjustedPrice = new BigDecimal(1);

        adjustedPrice= adjustedPrice.multiply(unitPrice);
        adjustedPrice= adjustedPrice.multiply(discountRateCalc);
        adjustedPrice= adjustedPrice.multiply(taxRateCalc);

        System.out.println(adjustedPrice);
    }
}
