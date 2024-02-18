package REegularEXtoDFA_UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

import REegularEXtoDFA_UI.Convert_DFA;
import REegularEXtoDFA_UI.StateMatrix;

/**
 State transition matrix display class
 */
public class Matrix extends JPanel {
	private static final long serialVersionUID = 1L;

	private String text = null;
	
	public Matrix(String text) {
		this.setBackground(Color.white);
		this.text = text;
	}

	/**
	 * Draw the state transition matrix table
	 */
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("Time", Font.BOLD, 18));
		g.drawString("State", 60, 50);
		Convert_DFA convert = new Convert_DFA();
		StateMatrix stateMatrix = convert.toDFA(text);
		for(int i = 0; i < stateMatrix.getInCh().length; i++) {
			g.drawString(stateMatrix.getInCh()[i] + "", 10 + (i + 1) * 70, 50);
		}
		for(int i = 0; i < stateMatrix.stateTotal(); i++) {
			for(int j = 0; j < stateMatrix.getMatrix()[i].length; j++) {
				if(stateMatrix.getMatrix()[i][j] == -1) {
					g.drawString("-", 10 + (j + 1) * 70, 50 + (i + 1) * 30);
				} else {
					g.drawString(stateMatrix.getMatrix()[i][j] + "", 10 + (j + 1) * 70, 50 + (i + 1) * 30);
				}
			}
		}
		this.setPreferredSize(new Dimension(stateMatrix.getMatrix()[0].length * 80 +20, stateMatrix.stateTotal() * 35 + 30));
	}
}
