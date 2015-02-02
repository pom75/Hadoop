package ihm;
import main.*;
import hadoop.Julia;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainPanel extends JPanel {

	JLabel l1,l2,l3,l4,l5,l6,l7;
	JTextField f1,f2,f3,f4,f5;
	JButton b1;


	public MainPanel() {
		init();

	}

	private void init(){



		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


		JPanel mp1 = new JPanel();
		mp1.setLayout(new GridLayout(6,1));
		mp1.setBorder(BorderFactory.createTitledBorder("Params"));


		l1  = new JLabel("Dimensions :");
		mp1.add(l1);

		JPanel p1 = new JPanel();
		l2 = new JLabel("Width");
		f1  =  new JTextField(8);
		f1.setText("500");

		l6 = new JLabel("Length");
		f2 = new JTextField(8);
		f2.setText("500");
		p1.add(l2);
		p1.add(f1);
		p1.add(l6);
		p1.add(f2);
		mp1.add(p1);

		l3 = new JLabel("Complexe    :");
		mp1.add(l3);

		JPanel p2 = new JPanel();

		l4 = new JLabel("Real");
		f3 = new JTextField(8);
		f3.setText("-0.8");
		p2.add(l4);
		p2.add(f3);
		mp1.add(p2);


		l5 = new JLabel("Imaginary");
		f4 = new JTextField(8);
		f4.setText("-0.2");
		p2.add(l5);
		p2.add(f4);



		mp1.add(new JLabel("Coloring :"));

		JPanel p5 = new JPanel();
		l7 = new JLabel("Number of Colors          ");
		f5 = new JTextField(10);
		f5.setHorizontalAlignment(JTextField.CENTER);
		f5.setToolTipText("Value Between 2 and 256");
		f5.setText("256");

		p5.add(l7);
		p5.add(f5);

		mp1.add(p5);

		this.add(mp1);


		JPanel mp2 = new JPanel();
		b1 = new JButton("Start");
		b1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				execute();
			}

		});

		mp2.add(b1);
		this.add(mp2);

		this.show();
	}


	private void execute(){
		try{
			int width  = Integer.valueOf(f1.getText());
			int length = Integer.valueOf(f2.getText());
			float read = Float.valueOf(f3.getText());
			float img = Float.valueOf(f4.getText());
			int colors = Integer.valueOf(f5.getText());

			try {
				Julia.mainJulia(Main.in, Main.out, width, length, colors, read, img);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}catch(NumberFormatException  E){
			JOptionPane.showMessageDialog(null,"Please insert correct values", "Error",JOptionPane.ERROR_MESSAGE);
		}
	}

}
