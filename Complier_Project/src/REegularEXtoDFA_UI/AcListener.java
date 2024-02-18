package REegularEXtoDFA_UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import REegularEXtoDFA_UI.Convert_DFA;

/**
* Button action event listener class
*
 */
public class AcListener implements ActionListener {

	private JButton button = null; // button
	private JTextField text = null; // Input box
	private JScrollPane tabPanel1 = null; // DFA State Transition Diagram Panel
	private JScrollPane tabPanel2 = null; // DFA State Transition Matrix Panel

	public AcListener(JButton button, JTextField text, JScrollPane tabPanel12, JScrollPane tabPanel22) {
		this.button = button;
		this.text = text;
		this.tabPanel1 = tabPanel12;
		this.tabPanel2 = tabPanel22;
	}

	/**
        * Execute the specific action event that the conversion button responds to
        */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if (obj == button) {
			if (text.getText().trim().equals("")) {  // When the text box is empty, display the input prompt
				JLabel msg1 = new JLabel("Please enter a regular expression");
				msg1.setFont(new Font("Time", Font.BOLD, 16));
				JLabel msg2 = new JLabel("Please enter a regular expression");
				msg2.setFont(new Font("Time", Font.BOLD, 16));
				msg1.setPreferredSize(new Dimension(690, 380));
				msg1.setVerticalAlignment(SwingConstants.CENTER);
				msg1.setHorizontalAlignment(SwingConstants.CENTER);
				msg2.setPreferredSize(new Dimension(690, 380));
				msg2.setVerticalAlignment(SwingConstants.CENTER);
				msg2.setHorizontalAlignment(SwingConstants.CENTER);
				tabPanel1.setViewportView(msg1);
				tabPanel2.setViewportView(msg2);
			} else {
				try {
					Convert_DFA convert = new Convert_DFA();
                                        // String input check, throw an exception if the input is wrong
					convert.createTree(convert.reToPostfix(text.getText().trim()));  
					
					Graph graph = new Graph(text.getText().trim());  
                                        // draw the state transition diagram
					tabPanel1.setViewportView(graph);
                                       // System.out.print(graph);
					
					Matrix matrix = new Matrix(text.getText().trim());  
                                        // draw the state transition matrix
					tabPanel2.setViewportView(matrix);
				} catch(EmptyStackException ex) {
					JLabel msg1 = new JLabel("There is an error in the input of the regular expression, please check and re-enter");
					msg1.setFont(new Font("Time", Font.BOLD, 16));
					JLabel msg2 = new JLabel("There is an error in the input of the regular expression, please check and re-enter");
					msg2.setFont(new Font("Time", Font.BOLD, 16));
					msg1.setPreferredSize(new Dimension(690, 380));
					msg1.setVerticalAlignment(SwingConstants.CENTER);
					msg1.setHorizontalAlignment(SwingConstants.CENTER);
					msg2.setPreferredSize(new Dimension(690, 380));
					msg2.setVerticalAlignment(SwingConstants.CENTER);
					msg2.setHorizontalAlignment(SwingConstants.CENTER);
					tabPanel1.setViewportView(msg1);
					tabPanel2.setViewportView(msg2);
				}
			}
		}
                
	}

}
