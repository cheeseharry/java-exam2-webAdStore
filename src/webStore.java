import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author leizheng 11-25-2018
 */
@SuppressWarnings("serial")
public class webStore extends JFrame implements ActionListener, ListSelectionListener, KeyListener, ItemListener, MouseListener {

	// attributes
	BigDecimal totalPrice;

	//TODO Statics
	BigDecimal lowPrice;
	BigDecimal highPrice;
	BigDecimal avgPrice;


	NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
	DecimalFormat df = new DecimalFormat("####.00");

	ArrayList<WebItem> order = new ArrayList<>();

	// GUI
	// north region:
	JLabel titleLBL = new JLabel();
	JPanel eastPanel = new JPanel();
	Image titleIMG = Toolkit.getDefaultToolkit().getImage("tks-giving.jpeg");
	ImageIcon titleIMGICON = new ImageIcon(titleIMG.getScaledInstance(1024, 120, Image.SCALE_SMOOTH));
	Image title_overIMG = Toolkit.getDefaultToolkit().getImage("tks-giving2.jpeg");
	ImageIcon title_overIMGICON = new ImageIcon(title_overIMG.getScaledInstance(1024, 120, Image.SCALE_SMOOTH));

	// TODO
	JButton addWebAdBtn = new JButton("Add WebAd");
	JButton addBevBtn = new JButton("addBevBtn");
	JButton addDessertBtn = new JButton("addDessertBtn");

	// east region:
	JLabel detailsTitleAreaLbl = new JLabel("  Order Total  ");
	JLabel totalPriceLbl = new JLabel(" $0.00 ");

	JLabel lowPriceLabel = new JLabel("0.00");
    JLabel highPriceLabel = new JLabel("0.00");
    JLabel avgPriceLabel = new JLabel("0.00");

	JLabel itemDetailsTitleLbl = new JLabel("Item Special Request:");
	JTextField itemSpecialRequestTF = new JTextField();

	JButton chgQuantityBtn = new JButton("Change Quantity");
	JButton chgSizeBtn = new JButton("Change Size");
	JButton setSpecialRequest = new JButton("Make Special Request");

	ButtonGroup radioButtons = new ButtonGroup();
	JRadioButton dineInRB, takeAwayRB, deliverRB;
	JTextArea addressTA = new JTextArea();
	JCheckBox extraSauceCB = new JCheckBox("Small jokes", false);
	JCheckBox kidsBookCB = new JCheckBox("Final fantasy", false);
	JCheckBox orderInstructionsCB = new JCheckBox("Special deals!:", false);
	JTextArea orderInstructionsTA = new JTextArea("Order Special deals");

	// south region:

	JButton cancelOrderBtn = new JButton("Cancel Order"); // removes all data
	JButton placeOrderBtn = new JButton("Place Order");

	/*
	@LEI ZHENG
	 */
    JButton removeItemBtn = new JButton("Remove Selected Item");
    JButton removeAllBtn = new JButton("Remove All Items");
	JButton showStatsBtn = new JButton("Show Stats");
    JButton viewSelectAdBtn = new JButton("View Selected Ad");
    JButton printSelectBtn = new JButton("Print Selected Ad");


	// center region:
	DefaultTableModel data = new DefaultTableModel(12,5); // rows, columns
	{ // initializer block:
		Object[] columnNames = {"WebAd Price", "Discount", "Tax", "Total"};
		data.setColumnIdentifiers(columnNames);
	}
	JTable orderTable = new JTable(data);
	{ // initializer block:
		orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	JScrollPane jsp = new JScrollPane(orderTable);

	// sounds:
	AudioClip argh, ahoy, yoho, onTheWay;


	/** setup app characteristics
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		webStore app = new webStore();
		app.setSize(1080, 800);
		app.setTitle("My WebAd Store");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		app.setFocus(); // must set focus after the gui is visible
	}

	webStore() {

		Font smallF = new Font("Helvetica", Font.PLAIN, 11);
		Font smallPirateF = new Font("Black Sam's Gold", Font.PLAIN, 20);


		// ============================== setup gui:
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());

		// ------------------------------ NORTH:
		titleLBL.setIcon(titleIMGICON);
		p.add(titleLBL, BorderLayout.NORTH);


		// ------------------------------ CENTER:
		jsp.setBorder(BorderFactory.createTitledBorder("Order Items"));
		jsp.setBackground(new Color(238, 238, 238)); // match default JPanel background color
		p.add(jsp, BorderLayout.CENTER);

		// adjust table column widths:
		orderTable.getColumnModel().getColumn(0).setPreferredWidth(24);
		orderTable.getColumnModel().getColumn(1).setPreferredWidth(130);
		orderTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		orderTable.getColumnModel().getColumn(3).setPreferredWidth(36);
		orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		// adjust table column alignment for #toppings (center) and price (right):
		// there are at least 2 ways
		// 1. create a reusable class:
		class RightTableCellRenderer extends DefaultTableCellRenderer {
			RightTableCellRenderer() {
				setHorizontalAlignment(JLabel.RIGHT);
			}
		}
		orderTable.getColumnModel().getColumn(3).setCellRenderer(new RightTableCellRenderer());

		// 2. just set the column using an initializer block in the default cell renderer:
		orderTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			{ // initializer block:
				setHorizontalAlignment(JLabel.CENTER);
			}
		});


		// ------------------------------ WEST:
		// load images for the buttons:
		Image temp = Toolkit.getDefaultToolkit().getImage("candle.jpg");
		ImageIcon candleImg = new ImageIcon(temp.getScaledInstance(128, 64, Image.SCALE_SMOOTH));

		addWebAdBtn.setIcon(candleImg);

		JPanel westButtons = new JPanel();
		westButtons.add(addWebAdBtn);
		//westButtons.add(addBevBtn);
		//westButtons.add(addDessertBtn);
		westButtons.setLayout(new GridLayout(westButtons.getComponentCount(),1));
		westButtons.setBorder(BorderFactory.createTitledBorder("Add Items"));

		p.add(westButtons, BorderLayout.WEST);


		// ------------------------------- SOUTH:
		JPanel southP = new JPanel();
		southP.setLayout(new GridLayout(2,1));

		itemDetailsTitleLbl.setFont(smallPirateF);
		itemDetailsTitleLbl.setHorizontalAlignment(JLabel.LEFT);
		itemDetailsTitleLbl.setVerticalAlignment(JLabel.BOTTOM);

		itemSpecialRequestTF.setFont(smallF);
		itemSpecialRequestTF.setText("Item special request");
		itemSpecialRequestTF.setEnabled(false);
		itemSpecialRequestTF.setPreferredSize(new Dimension(400, 32));

		JPanel southButtons = new JPanel();
		southButtons.setLayout(new FlowLayout(FlowLayout.LEFT));

		//southButtons.add(setSpecialRequest);
		//southButtons.add(chgQuantityBtn);
		//southButtons.add(chgSizeBtn);
		southButtons.add(removeItemBtn);
		southButtons.add(removeAllBtn);



		southButtons.add(showStatsBtn);
		southButtons.add(viewSelectAdBtn);
		southButtons.add(printSelectBtn);



		JPanel southDetails = new JPanel();
		southDetails.setLayout(new FlowLayout(FlowLayout.LEFT));
		//southDetails.add(itemDetailsTitleLbl);
		southDetails.add(itemSpecialRequestTF);

		southP.add(southButtons);
		southP.add(southDetails);

		southP.setBorder(BorderFactory.createTitledBorder("Edit Selected Item"));

		p.add(southP, BorderLayout.SOUTH);


		// --------------------------------- EAST:
		// set the width to override default borderlayout width:
		eastPanel.setPreferredSize(new Dimension(200, getPreferredSize().height));

		eastPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));

		detailsTitleAreaLbl.setFont(smallPirateF);

		totalPriceLbl.setFont(new Font("Courier", Font.PLAIN, 36));
		totalPriceLbl.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		totalPriceLbl.setForeground(Color.GREEN);
		totalPriceLbl.setBackground(Color.BLACK);
		totalPriceLbl.setOpaque(true);
		totalPriceLbl.setPreferredSize(new Dimension(180, 40));
		totalPriceLbl.setHorizontalAlignment(JLabel.CENTER);


		// radio buttons:
		dineInRB = new JRadioButton("Pick up", true);
		dineInRB.setFont(smallF);

		takeAwayRB = new JRadioButton("Take away", false);
		takeAwayRB.setFont(smallF);

		deliverRB = new JRadioButton("Deliver:", false);
		deliverRB.setFont(smallF);

		radioButtons.add(dineInRB);
		radioButtons.add(takeAwayRB);
		radioButtons.add(deliverRB);

		// address text area:
		addressTA.setFont(smallF);
		addressTA.setPreferredSize(new Dimension(180, 48));
		addressTA.setText("Delivery Address");
		addressTA.setEnabled(false);

		// check boxes:
		extraSauceCB.setFont(smallF);
		kidsBookCB.setFont(smallF);
		orderInstructionsCB.setFont(smallF);

		// order instructions text area:
		orderInstructionsTA.setFont(smallF);
		orderInstructionsTA.setPreferredSize(new Dimension(180, 60));
		orderInstructionsTA.setEnabled(false);

		// panels to group ui components on east panel:
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(3,1));
		radioPanel.setPreferredSize(new Dimension(180, 66));
		radioPanel.setAlignmentY(RIGHT_ALIGNMENT);
		radioPanel.add(dineInRB);
		radioPanel.add(takeAwayRB);
		radioPanel.add(deliverRB);

		JPanel checkPanel = new JPanel();
		checkPanel.setLayout(new GridLayout(3,1));
		checkPanel.setPreferredSize(new Dimension(180, 66));
		checkPanel.add(extraSauceCB);
		checkPanel.add(kidsBookCB);
		checkPanel.add(orderInstructionsCB);

		// size the place order button to fill remaining space:
		placeOrderBtn.setPreferredSize(new Dimension(180, 180));
		cancelOrderBtn.setPreferredSize(new Dimension(180, 40));
		// add components:
		eastPanel.add(detailsTitleAreaLbl);
		eastPanel.add(totalPriceLbl);
		eastPanel.add(radioPanel);
		eastPanel.add(addressTA);
		eastPanel.add(checkPanel);
		eastPanel.add(orderInstructionsTA);
		//eastPanel.add(placeOrderBtn);
		//eastPanel.add(cancelOrderBtn);


		p.add(eastPanel, BorderLayout.EAST);

		add(p);


		// register default listeners last:
		registerListeners(p);

		// register other specific listeners:
		titleLBL.addMouseListener(this);

	}

	/** utility methods to attach default listeners to all
	 *  panel components (recursive)
	 *
	 * @param p
	 */
	void registerListeners(JComponent p) {
		for (int j = 0; j < p.getComponentCount(); j++) {
			Object o = p.getComponent(j);
			if (o.getClass() == JButton.class) {
				((JButton) o).addActionListener(this);
				((JButton) o).addKeyListener(this);

			} else if (o.getClass() == JTextArea.class) {
				((JTextArea) o).addKeyListener(this);

			} else if(o.getClass() == JTextField.class) {
				((JTextField) o).addKeyListener(this);
				((JTextField) o).addActionListener(this);

			} else if (o.getClass() == JCheckBox.class) {
				((JCheckBox) o).addKeyListener(this);
				((JCheckBox) o).addItemListener(this);

			} else if (o.getClass() == JRadioButton.class) {
				((JRadioButton) o).addKeyListener(this);
				((JRadioButton) o).addItemListener(this);

			} else if (o.getClass() == JTable.class) {
				((JTable) o).addKeyListener(this);
				((JTable) o).getSelectionModel().addListSelectionListener(this);

			} else if (o.getClass() == JPanel.class || o.getClass() == JScrollPane.class || o.getClass() == JViewport.class) {
				registerListeners((JComponent) o);

			}
		}
	}


	/**
	 * note that focus can't be set in the constructor before the gui is visible
	 */
	private void setFocus() {
		addWebAdBtn.requestFocus();
	}

    /**
     * get Low Price
     * @leizheng 11/24/2018
     */

    void getLowPrice(){
        BigDecimal min = new BigDecimal(Double.MAX_VALUE);

        // scans through the order to find the lowest price
        for (int i = 0; i < order.size(); i++) {
            Object o = order.get(i);
            if (o.getClass() == WebAd.class) {
                WebAd p = (WebAd) o;

                if (p.price.compareTo(min) == -1) {
                    min = p.price;

                }

                //order.set(i, p); // TODO: need on windows??

            }
        }

        lowPrice = min;
        //System.out.println(LowPrice);

    }

    /**
     * get High Price
     * @leizheng 11/24/2018
     */

    void getHighPrice(){
        BigDecimal max = new BigDecimal(Double.MIN_VALUE);

        // scans through the order to find the lowest price
        for (int i = 0; i < order.size(); i++) {
            Object o = order.get(i);
            if (o.getClass() == WebAd.class) {
                WebAd p = (WebAd) o;

                if (p.price.compareTo(max) == 1) {
                    max = p.price;

                }

                //order.set(i, p); // TODO: need on windows??

            }
        }

        highPrice = max;
        //System.out.println(HighPrice);

    }

	/**
	 * processed the orders in the list and recalculates totalPrice
	 */
	public void updateOrder() {

		//updateLowPrice();

        getLowPrice();
        getHighPrice();

		// clear item details
		itemSpecialRequestTF.setText("");

		// update the order and total
		if (data.getRowCount() > 0) data.setRowCount(0);
		totalPrice = BigDecimal.ZERO;

		for (int i = 0; i < order.size(); i++) {
			data.addRow(order.get(i).getRow()); //(order.get(i));
            //TODO Order Total
			totalPrice = totalPrice.add(new BigDecimal(df.format(order.get(i).price)));
		}

		totalPrice = totalPrice.setScale(2, RoundingMode.HALF_EVEN);
		totalPriceLbl.setText(nf.format(totalPrice));

		BigDecimal numberOfItem = new BigDecimal(df.format(order.size()));
        // System.out.println(numberOfItem);

        BigDecimal zero = new BigDecimal("0.00");

        MathContext mc = new MathContext(2, RoundingMode.HALF_DOWN);
        /**
         * @author leizheng
         */
        // avoid zero division error
        if (numberOfItem.equals(zero)){
            avgPrice = zero;

        }else {
            avgPrice = totalPrice.divide(numberOfItem,2, BigDecimal.ROUND_HALF_UP);
        }

		avgPriceLabel.setText(nf.format(avgPrice));
        lowPriceLabel.setText(nf.format(lowPrice));
        highPriceLabel.setText(nf.format(highPrice));

	}

	/**
	 * clears the order and resets the GUI
	 */
	public void clearOrder() {
		
		removeAllItems();

		// reset ui:
		this.addressTA.setText("");
		this.dineInRB.setSelected(true);
		this.extraSauceCB.setSelected(false);
		this.kidsBookCB.setSelected(false);
		this.orderInstructionsCB.setSelected(false);
		this.orderInstructionsTA.setText("");
		this.orderInstructionsTA.setEnabled(false);

	}
	
	/** 
	 * removes all items in the cart and updates total
	 */
	public void removeAllItems() {
		data.setRowCount(0);
		order.clear();
		updateOrder();
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == addWebAdBtn) {

            // TODO
			WebAd ad = new WebAd();
			order.add(ad);


			//food = new Pizza();
			//order.add(food);

		}  else if (e.getSource() == showStatsBtn) {
            if (order.size()>0){
                // @leizheng
                // create window for receipt panel and action buttons:
                JLabel label1 = new JLabel("Low Price = $" + lowPrice);
                JLabel label2 = new JLabel("High Price = $" + highPrice);
                JLabel label3 = new JLabel("Avg Price = $" + avgPrice);

                JFrame jf = new JFrame();

                jf.setSize(250, 150);
                jf.setLocationRelativeTo(this);
                jf.setTitle("Summary Statistics");

                jf.setVisible(true);

                jf.add(label1,BorderLayout.NORTH);
                jf.add(label2,BorderLayout.CENTER);
                jf.add(label3,BorderLayout.SOUTH);
            }else{

                JOptionPane.showMessageDialog(this, "Add WebAd prior to see Summary Statistics", "Attention", 0);
            }


        } else if (e.getSource() == viewSelectAdBtn) {

            int i = orderTable.getSelectedRow();

            if (i > -1) {

                WebAd ad1 = new WebAd();
                ad1.paintComponent(getGraphics());


                /*
                JLabel label1 = new JLabel("PIRATE SHIPS & BATH WATER");
                JLabel label2 = new JLabel("Ad Price = $" + order.get(i).price);
                JLabel label3 = new JLabel("Smells Like Teen Spirit!!");

                JFrame frame = new JFrame("My Web Ad");
                Container contentPane = frame.getContentPane();
                contentPane.setBackground(Color.CYAN); // color

                JPanel panel = new JPanel();
                panel.setBackground(Color.yellow);
                JButton printBtn = new JButton("Print");
                panel.add(printBtn);

                contentPane.add(panel, BorderLayout.SOUTH);
                frame.setSize(320, 320);
                frame.setVisible(true);

                frame.add(label2,BorderLayout.CENTER);
                frame.add(label3,BorderLayout.NORTH);

                ReceiptPanel p = new ReceiptPanel(this.totalPrice, this.order);

                printBtn.addActionListener(action -> {
                    // submit print job:
                    PrinterJob job = PrinterJob.getPrinterJob();
                    job.setPrintable(p);
                    if (job.printDialog()) {
                        try {
                            job.print();
                        } catch(PrinterException x_x) {
                            System.out.println("Error printing: " + x_x);
                        }
                    }
                });

                */

                /*
                WebAdGenerator2 app = new WebAdGenerator2();
                app.setSize(1024, 768);
                app.setDefaultCloseOperation(EXIT_ON_CLOSE);
                app.setTitle("WebAdGenerator2");
                app.setVisible(true);
                */


                /*
                JFrame jf = new JFrame();
                jf.setSize(320,320);
                JLabel label1 = new JLabel("PIRATE SHIPS & BATH WATER");
                JLabel label2 = new JLabel("Ad Price = $" + order.get(i).price);
                JLabel label3 = new JLabel("Smells Like Teen Spirit!!");

                jf.setTitle("WebAd Purchase");

                jf.setVisible(true);

                jf.add(label1,BorderLayout.NORTH);
                jf.add(label2,BorderLayout.CENTER);
                jf.add(label3,BorderLayout.SOUTH);
                */


            } else {

                JOptionPane.showMessageDialog(this, "Please select an item to view!", "Attention", 0);
            }


        } else if (e.getSource() == printSelectBtn) {
            int i = orderTable.getSelectedRow();

            if (i > -1) {

                JLabel label1 = new JLabel("PIRATE SHIPS & BATH WATER");
                JLabel label2 = new JLabel("Ad Price = $" + order.get(i).price);
                JLabel label3 = new JLabel("Smells Like Teen Spirit!!");

                JFrame frame = new JFrame("My Web Ad");
                Container contentPane = frame.getContentPane();
                contentPane.setBackground(Color.CYAN); // color

                JPanel panel = new JPanel();
                panel.setBackground(Color.yellow);
                JButton printBtn = new JButton("Print");
                panel.add(printBtn);

                contentPane.add(panel, BorderLayout.SOUTH);
                frame.setSize(320, 320);
                frame.setVisible(true);

                frame.add(label1,BorderLayout.NORTH);
                frame.add(label2,BorderLayout.CENTER);

                //ReceiptPanel p = new ReceiptPanel(this.totalPrice, this.order);

                printBtn.addActionListener(action -> {
                    // submit print job:
                    PrinterJob job = PrinterJob.getPrinterJob();
                    //job.setPrintable(p);
                    if (job.printDialog()) {
                        try {
                            job.print();
                        } catch(PrinterException x_x) {
                            System.out.println("Error printing: " + x_x);
                        }
                    }
                });


            } else {

                JOptionPane.showMessageDialog(this, "Please select an item to print!", "Attention", 0);
            }



        } else if (e.getSource() == removeItemBtn) {

			if (removeItemBtn.getText().equals("Remove ALL Items")) {
				//clearOrder();
				removeAllItems();
				
			} else {
				int i = orderTable.getSelectedRow();

				if (i > -1) {
					itemDetailsTitleLbl.setText("Item Special Request:");
					order.remove(i);
				} else {

					JOptionPane.showMessageDialog(this, "Please select an item to remove!", "Attention", 0); 
				}
			}

		} else if (e.getSource() == removeAllBtn) {

            int i = orderTable.getSelectedRow();
            if (data.getRowCount() > 0) {
                removeAllItems();
            } else {

                JOptionPane.showMessageDialog(this, "Please add items before remove all!", "Attention", 0);
            }


        } else if (e.getSource() == placeOrderBtn) {

			if (order.size() > 0) {
				
				// create window for receipt panel and action buttons:
				JFrame f = new JFrame();
				f.setSize(260, 600);
				f.setLocationRelativeTo(this);
				f.setTitle("Receipt");
				//ReceiptPanel p = new ReceiptPanel(this.totalPrice, this.order);
				//f.add(p, BorderLayout.CENTER);
				JButton printBtn = new JButton("Print Receipt");
				JButton doneBtn = new JButton("Done");
				JPanel btnPanel = new JPanel();
				btnPanel.setLayout(new GridLayout(1,2));
				btnPanel.add(printBtn);
				btnPanel.add(doneBtn);
				f.add(btnPanel, BorderLayout.SOUTH);

				printBtn.addActionListener(action -> {
					// submit print job:
					PrinterJob job = PrinterJob.getPrinterJob();
					//job.setPrintable(p);
					if (job.printDialog()) {
						try {
							job.print();
						} catch(PrinterException x_x) {
							System.out.println("Error printing: " + x_x);
						}
					}
				});

				doneBtn.addActionListener(action -> {
					// close the order:

					totalPrice = totalPrice.setScale(2, RoundingMode.HALF_EVEN);
					
					if (this.deliverRB.isSelected()) {
						JOptionPane.showMessageDialog(this, "Payment of " + nf.format(totalPrice) + " received. Your pizzas are on the way!");
					} else {
						JOptionPane.showMessageDialog(this, "Payment of " + nf.format(totalPrice) + " accepted. Please have a seat!");
					}
					f.dispose();
					clearOrder();
				});
				
				f.show();

				
			} else {

				JOptionPane.showMessageDialog(this, "Add items to your order prior to checking out, Matey.", "Attention", 0);

			}

		}


		// need to check if user is editing the itemSpecialRequestTA
		// if NOT, then reset and update:
		if (!itemSpecialRequestTF.isEnabled()) {
			updateOrder();
		}

	}

	public void keyPressed(KeyEvent kev) {
		/**
		 * this is hooked up to the itemSpecialRequestTA
		 * need to see when enter or return is pressed
		 * to store the request
		 */
		if (kev.getSource() == this.itemSpecialRequestTF) {

			int i = orderTable.getSelectedRow();

			if (i > -1) {
				if (kev.getKeyCode() == KeyEvent.VK_ENTER ) {
					order.get(i).specialRequest = this.itemSpecialRequestTF.getText();
					itemSpecialRequestTF.setEnabled(false);

					updateOrder();

				}
			}

		} else if (kev.getModifiers() == 8) {
			this.removeItemBtn.setText("Remove ALL Items");
		}
	}

	public void keyReleased(KeyEvent kev) {
		this.removeItemBtn.setText("Remove Selected Item");
	}

	public void keyTyped(KeyEvent kev) {

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		/**
		 * this code handles the user making selections in the JList
		 */
		if (!e.getValueIsAdjusting()) {

			itemSpecialRequestTF.setEnabled(false);
			int s = orderTable.getSelectedRow();

			if (s > -1 && s < order.size()) {

				WebItem selected = order.get(s);
				if (!selected.specialRequest.equals("Item Special Request")) {
					itemSpecialRequestTF.setText(selected.specialRequest);
				} else {
					itemSpecialRequestTF.setText("Item Special Request");
				}

			} else {
				itemSpecialRequestTF.setText("Item Special Request");

			}

		}

	}

	/**
	 * itemStateChanged() handles all of the checkboxes and radio buttons
	 */
	public void itemStateChanged(ItemEvent iev) {

		if (iev.getSource() == extraSauceCB) {
			if (extraSauceCB.isSelected()) {
				//ahoy.play();
			} 

		} else if (iev.getSource() == this.kidsBookCB) {
			if (this.kidsBookCB.isSelected()) {
				//ahoy.play();
			}

		} else if (iev.getSource() == this.deliverRB) {
			addressTA.setEnabled(true);
			if (addressTA.getText().equals("Delivery Address")) {
				addressTA.setText("");
			}
			addressTA.requestFocusInWindow();

		} else if (iev.getSource() == this.dineInRB || iev.getSource() == this.takeAwayRB) {
			addressTA.setEnabled(false);

		} else if (iev.getSource() == this.orderInstructionsCB) {

			/**
			 * when the order special instructions cb is checked,
			 * the ta is enabled.
			 * if the ta says "Order Special Instructions", then that is erased
			 * and focus is requested
			 * 
			 * when the cb is unchecked, the ta is reset
			 */
			if (this.orderInstructionsCB.isSelected()) {
				//ahoy.play();
				this.orderInstructionsTA.setEnabled(true);
				if (this.orderInstructionsTA.getText().equals("Order Special Instructions")) {
					this.orderInstructionsTA.setText("");
				}
				this.orderInstructionsTA.requestFocusInWindow();

			} else {
				this.orderInstructionsTA.setText("Order Special Instructions");
				this.orderInstructionsTA.setEnabled(false);

			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		titleLBL.setIcon(title_overIMGICON);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		titleLBL.setIcon(titleIMGICON);

	}



}



