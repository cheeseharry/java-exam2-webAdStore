import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class WebAd extends WebItem {

    /**
     * calculates the price based on the Tax and Discount
     * @return BigDecimal
     */

    String specialRequest = "Item Special Request";

    BigDecimal calcOne = new BigDecimal(1);
    BigDecimal divideBy100 = new BigDecimal(100);

    BigDecimal unitPrice = new BigDecimal(Math.ceil(Math.random()*100));  //25

    BigDecimal discountRate = new BigDecimal(Math.ceil(Math.random()*50)); //23
    BigDecimal discountRateDivide = discountRate.divide(divideBy100);
    BigDecimal discountRateCalc = calcOne.subtract(discountRateDivide); // 1-0.23=0.77

    BigDecimal taxRate = new BigDecimal(8.00);  //8.00%
    BigDecimal taxRateDivide = taxRate.divide(divideBy100); //0.08
    BigDecimal taxRateCalc = taxRateDivide.add(calcOne); // *1.08


    BigDecimal adjustedPrice = new BigDecimal(1);


    WebAd(){
        price = getPrice();
    }
    @Override
    public BigDecimal getPrice() {


        adjustedPrice = adjustedPrice.multiply(unitPrice);
        adjustedPrice = adjustedPrice.multiply(discountRateCalc);
        adjustedPrice = adjustedPrice.multiply(taxRateCalc);  //final price 25 23 8 20.7900

        return adjustedPrice;

    }

    String getTypeAsString() {
        String n$ = "";
        switch (type) {
            case 0: n$ = "Parrot Pop"; break;
            case 1: n$ = "Dessert Pizza"; break;
            case 2: n$ = "Chocolate Pirate Bar"; break;
            default: n$ = "Error parsing type!";
        }

        return n$;
    }



    @Override
    String getSizeAsString() {
        String size$ = "";

        return size$;
    }

    @Override
    public String toString() {
        return "[" + quantity + "] " + getSizeAsString() + " " + name + ": " + nf.format(price)
                + (!specialRequest.equals("Item Special Request")? " [SEE NOTE]": "");
    }

    /**
     * GetTableRow()
     * @return
     */
    @Override
    public String[] getRow() {
        String rq$ = "";

        String[] row = {"$"+unitPrice,
                ""+discountRate+"%",
                ""+taxRate+"%",
                ""+adjustedPrice,
                ""+adjustedPrice,
                ""+adjustedPrice,
                ""+adjustedPrice};
        return row;
    }

    /**
     * returns receipt data for pizza food items
     * "Quantity", "Size", "Type", "Price"
     */
    @Override
    public String toReceiptString(int i) {
        String rq$ = "";
        if (specialRequest.length() > 0 && !specialRequest.equals("Item Special Request")) rq$ = "See Request";

        String s = "<html>" + (i+1) + ". "
                + quantity
                + " " + getSizeAsString()
                + " " + getTypeAsString()
                + "..... "
                + " " + nf.format(price)
                + (rq$.equals("See Request")?("<br>"+specialRequest):"")
                + "</html>";
        return s;
    }

    /**
     * compareTo
     * @param o
     * @return
     */
    int compareTo(WebAd o){
        return adjustedPrice.compareTo(o.adjustedPrice);
    }

    /**
     * paintComponent
     * @param g1d
     */
    public void paintComponent(Graphics g1d) {

        CustomPanel cp = new CustomPanel();
            //cp.paintComponent(g1d);

        JFrame frame = new JFrame("My Web Ad");
        Container contentPane = frame.getContentPane();
        contentPane.setBackground(Color.CYAN); // color

        JPanel panel = new JPanel();
        panel.setBackground(Color.yellow);
        JButton printBtn = new JButton("Print");
        panel.add(printBtn);

        contentPane.add(cp, BorderLayout.CENTER);
        frame.setSize(1024, 768);
        frame.setVisible(true);

    }

    class CustomPanel extends JPanel {

        // attributes of this custom panel:
        Polygon p = new Polygon();
        Color fill = Color.GREEN;

        // draw code:
        public void paintComponent(Graphics g1d) {

            Graphics2D g = (Graphics2D) g1d;
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


            g.setColor( Color.ORANGE );
            g.fillRect(216, 5, 320, 320);

            g.setColor( new Color( 0, 0, 0, 255) );

            g.setFont( new Font("Monospaced", 1, 28) );
            g.drawString("Pirate Boat", 222, 60);
            g.drawString("Product No 1922", 222, 90);
            g.drawString("Only" + adjustedPrice.setScale(2,RoundingMode.HALF_EVEN) +"$", 222, 136);

            g.setColor( new Color( 255, 51, 51, 255) );

            Polygon Obj20 = new Polygon();
            Obj20.addPoint(414, 105);
            Obj20.addPoint(514, 205);
            Obj20.addPoint(414, 205);
            g.fillPolygon(Obj20);

            g.setColor( new Color( 0, 0, 0, 255) );
            g.fillRect(370, 204, 160, 19);

        }
    }




}
