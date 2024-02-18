package REegularEXtoDFA_UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class REtoDFA_GUI {

	private static JButton button = null; // Convert button
	private static JTextField text = null; // Input box
	private static JScrollPane tabPanel1 = null; // DFA State Transition Diagram Panel
	private static JScrollPane tabPanel2 = null; // DFA State Transition Matrix Panel
        
        public void RETODFA_GUI(String re){
            setUI(re);
	    setButtonAction();
        }

	/**
	 * Create UI graphical interface
	 */
	private static void setUI(String re) {
		JFrame frame = new JFrame("Regular Expression to DFA ");
		frame.setSize(710, 580); // Set the size of the component
		frame.setBackground(Color.WHITE); // set the background to white
		frame.setLocation(100, 80); // Set the display position of the component
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel hPanel = new JPanel(); // top panel
		hPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 3));
		hPanel.setPreferredSize(new Dimension(710, 80));
		JLabel blank = new JLabel(); // top margin
		blank.setPreferredSize(new Dimension(680, 20));
		JLabel label = new JLabel("  Regular Expression: "); 
		label.setFont(new Font("Time", Font.BOLD, 15));
		text = new JTextField(); 
		text.setFont(new Font("Time", Font.ROMAN_BASELINE, 15));
		text.setPreferredSize(new Dimension(430, 30));
                text.setText(re);
		button = new JButton("Convert"); 
		hPanel.add(blank);
		hPanel.add(label);
		hPanel.add(text);
		hPanel.add(button);

		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP); 
		tabPane.setFont(new Font("Time", Font.BOLD, 13));
		
		tabPanel1 = new JScrollPane();
		tabPanel2 = new JScrollPane();
		JLabel msg1 = new JLabel("Please enter a regular expression ");
		msg1.setFont(new Font("Time", Font.BOLD, 16));
		JLabel msg2 = new JLabel("Please enter a regular expression ");
		msg2.setFont(new Font("Time", Font.BOLD, 16));
		msg1.setPreferredSize(new Dimension(690, 380));
		msg1.setVerticalAlignment(SwingConstants.CENTER);
		msg1.setHorizontalAlignment(SwingConstants.CENTER);
		msg2.setPreferredSize(new Dimension(690, 380));
		msg2.setVerticalAlignment(SwingConstants.CENTER);
		msg2.setHorizontalAlignment(SwingConstants.CENTER);
		tabPanel1.setViewportView(msg1);
		tabPanel2.setViewportView(msg2);

		tabPane.addTab("DFA state transition diagram", tabPanel1);
		tabPane.addTab("DFA state transition matrix", tabPanel2);
		tabPane.setSelectedIndex(0);

		frame.setLayout(new BorderLayout(3, 3));
		frame.add(hPanel, BorderLayout.NORTH);
		frame.add(tabPane, BorderLayout.CENTER);

		frame.setResizable(false);
		frame.setVisible(true); 
	}

	/**
	 * Bind the action event of the transition button
	 */
	private static void setButtonAction() {
		AcListener acListener = new AcListener(button, text, tabPanel1, tabPanel2);
		button.addActionListener(acListener);  // Add convert button action
		
	}
}
