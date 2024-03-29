package REegularEXtoDFA_UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import REegularEXtoDFA_UI.Convert_DFA;
import REegularEXtoDFA_UI.StateMatrix;

/**
 * State transition diagram display class
 */
public class Graph extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String text = null;
	private List<Point> labebCoordList = new ArrayList<Point>();
	private List<List<Integer>> loopCoordList = new ArrayList<List<Integer>>();

	public Graph(String text) {
		// TODO Auto-generated constructor stub
		this.setBackground(Color.white);
		this.text = text;
	}

	public void paint(Graphics g) {
		super.paint(g);
		
		labebCoordList.clear();
		loopCoordList.clear();
		List<List<Integer>> coordList = paintTotalStates(g);
		paintTotalLinkedArrows(g, coordList);
	}

	/**
	 * draw state node
	 * 
	 * @param g
	 * @param num
	 * @param x
	 * @param y
	 */
	private void paintState(Graphics g, String num, int x, int y) {
		g.drawOval(x, y, 36, 36);
		g.setFont(new Font("Time", Font.BOLD, 18));
		g.drawString(num, x + 13, y + 25);
	}
	
	/**
	 * draw connection arrows
	 * @param g
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param label
	 */
	private void paintArrow(Graphics g, int x1, int y1, int x2, int y2, String label) {
		Graphics2D g2d = (Graphics2D) g;
		int angle1 = (int) (Math.atan(((double)(y1 - y2)) / ((double)(x2 - x1))) * 180 / Math.PI);
		int angle2 = angle1, angle = angle1;
		
		if(Math.abs(angle) == 90) {
			angle2 += 180;
			angle1 -= 28;
			angle2 += 28;
		} else  if(Math.abs(angle) == 0 || Math.abs(angle) == 180) {
			if(x1 < x2) {
				angle2 += 180;
				angle1 -= 26;
				angle2 += 26;
			} else {
				angle1 += 180;
				angle1 -= 26;
				angle2 += 26;
			}
		} else {
			if (x2 >= x1) {
				angle2 += 180;
				angle1 += 28;
				angle2 -= 28;
			} else {
				angle1 += 180;
				angle1 += 28;
				angle2 -= 28;
			}
		}
		
		int xl1 = (int)(x1 + 18 + Math.cos(((double)angle1) / 180 * Math.PI) * 18);
		int yl1 = (int)(y1 + 18 - Math.sin(((double)angle1) / 180 * Math.PI) * 18);
		int xl2 = (int)(x2 + 18 + Math.cos(((double)angle2) / 180 * Math.PI) * 18);
		int yl2 = (int)(y2 + 18 - Math.sin(((double)angle2) / 180 * Math.PI) * 18);
		int ux1 = 0, uy1 = 0, ux2 = 0, uy2 = 0;
		
		if(Math.abs(angle) == 90) {
			if(y1 < y2) {
				ux1 = -16;
				uy1 = -Math.abs(yl2 - yl1) / 10;
				ux2 = -16;
				uy2 = Math.abs(yl2 - yl1) / 10;
				Point p = new Point(xl1 - 23, (yl1 + yl2) / 2 - 6);
				int eq = 0;
				for(Point pi : labebCoordList) {
					if(pi.equals(p)) {
						eq = 1;
						break;
					}
				}
				while (eq == 1) {
					eq = 0;
					p.setLocation(xl1 - 23, p.getY() + 11);
					for(Point pi : labebCoordList) {
						if(pi.equals(p)) {
							eq = 1;
							break;
						}
					}
				}
				labebCoordList.add(p);
				g2d.drawString(label, (int) p.getX(), (int) p.getY());  // show label
			} else {
				ux1 = 16;
				uy1 = -Math.abs(yl2 - yl1) / 10;
				ux2 = 16;
				uy2 = Math.abs(yl2 - yl1) / 10;
				Point p = new Point(xl1 + 14, (yl1 + yl2) / 2 - 6);
				int eq = 0;
				for(Point pi : labebCoordList) {
					if(pi.equals(p)) {
						eq = 1;
						break;
					}
				}
				while (eq == 1) {
					eq = 0;
					p.setLocation(xl1 + 14, p.getY() + 11);
					for(Point pi : labebCoordList) {
						if(pi.equals(p)) {
							eq = 1;
							break;
						}
					}
				}
				labebCoordList.add(p);
				g2d.drawString(label, (int) p.getX(), (int) p.getY());  // show label
			}
		} else if(Math.abs(angle) == 0 || Math.abs(angle) == 180) {
			if(x1 > x2) {
				ux1 = -Math.abs(xl2 - xl1) / 10;
				uy1 = -(Math.abs(xl2 - xl1) / 8 + 5);
				ux2 = Math.abs(xl2 - xl1) / 10;
				uy2 = -(Math.abs(xl2 - xl1) / 8 + 5);
				Point p = new Point((xl1 + xl2) / 2 - 9, yl1 - (Math.abs(xl2 - xl1) / 8));
				int eq = 0;
				for(Point pi : labebCoordList) {
					if(pi.equals(p)) {
						eq = 1;
						break;
					}
				}
				while (eq == 1) {
					eq = 0;
					p.setLocation(p.getX() + 11, yl1 - (Math.abs(xl2 - xl1) / 8));
					for(Point pi : labebCoordList) {
						if(pi.equals(p)) {
							eq = 1;
							break;
						}
					}
				}
				labebCoordList.add(p);
				g2d.drawString(label, (int) p.getX(), (int) p.getY());  // show label
			} else {
				ux1 = -Math.abs(xl2 - xl1) / 5;
				uy1 = (Math.abs(xl2 - xl1) / 8 + 5);
				ux2 = Math.abs(xl2 - xl1) / 5;
				uy2 = (Math.abs(xl2 - xl1) / 8 + 5);
				Point p = new Point((xl1 + xl2) / 2 - 9, yl1 + (Math.abs(xl2 - xl1) / 8 + 16));
				int eq = 0;
				for(Point pi : labebCoordList) {
					if(pi.equals(p)) {
						eq = 1;
						break;
					}
				}
				while (eq == 1) {
					eq = 0;
					p.setLocation(p.getX() + 11, yl1 - (Math.abs(xl2 - xl1) / 8 + 16));
					for(Point pi : labebCoordList) {
						if(pi.equals(p)) {
							eq = 1;
							break;
						}
					}
				}
				labebCoordList.add(p);
				g2d.drawString(label, (int) p.getX(), (int) p.getY());  // show label
			}
		} else {
			if(x1 < x2) {
				uy1 = -(Math.abs(xl2 - xl1) / 8 + 5);
				uy2 = -(Math.abs(xl2 - xl1) / 8 + 5);
			} else {
				uy1 = (Math.abs(xl2 - xl1) / 8 + 5);
				uy2 = (Math.abs(xl2 - xl1) / 8 + 5);
			}
			if(y1 > y2) {
				ux1 = -(Math.abs(yl2 - yl1) / 8 + 5);
				ux2 = -(Math.abs(yl2 - yl1) / 8 + 5);
			} else {
				ux1 = (Math.abs(yl2 - yl1) / 8 + 5);
				ux2 = (Math.abs(yl2 - yl1) / 8 + 5);
			}
			Point p = new Point((xl1 + xl2) / 2 + ux1, (yl1 + yl2) / 2 + uy1);
			int eq = 0;
			for(Point pi : labebCoordList) {
				if(pi.equals(p)) {
					eq = 1;
					break;
				}
			}
			while (eq == 1) {
				eq = 0;
				p.setLocation((xl1 + xl2) / 2 + ux1, p.getY() - 11);
				for(Point pi : labebCoordList) {
					if(pi.equals(p)) {
						eq = 1;
						break;
					}
				}
			}
			labebCoordList.add(p);
			g2d.drawString(label, (int) p.getX(), (int) p.getY());  // show label
		}
		
		GeneralPath path = new GeneralPath();
		path.moveTo(xl1, yl1);
		path.curveTo((xl1 + xl2) / 2 + ux1, (yl1 + yl2) / 2 + uy1, (xl1 + xl2) / 2 + ux2, (yl1 + yl2) / 2 + uy2, xl2, yl2);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.draw(path);
		
		g2d.translate(xl2, yl2); // Origin position
		g2d.rotate((90 - angle2) * Math.PI / 180);
		int[] xs = { 0, -5, 5 };
		int[] ys = { 0, -12, -12 };
		g2d.fillPolygon(xs, ys, 3); // solid arrow
		g2d.rotate((270 + angle2) * Math.PI / 180); // Restore rotation angle and origin position
		g2d.translate(-xl2, -yl2);
	}

	/**
	 * draw ring arrow
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param angle
	 * @param label
	 */
	private void paintLoop(Graphics g, int x, int y, int angle, String label) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(x + 18, y + 18); // Origin position

		List<Integer> p = new ArrayList<Integer>();
		p.add(x);
		p.add(y);
		p.add(1);
		int eq = 0;
		int i = 0;
		for(List<Integer> pi : loopCoordList) {
			if(pi.get(0).equals(p.get(0)) && pi.get(1).equals(p.get(1))) {
				eq = pi.get(2);
				break;
			}
			i++;
		}
		if(eq == 0) {
			loopCoordList.add(p);
		} else {
			loopCoordList.get(i).set(2, loopCoordList.get(i).get(2) + 1);
		}
		int x2 = (int) (Math.sin(angle * Math.PI / 180) * 60 - 9);
		while(eq > 0) {
			x2 = x2 + 11;
			eq--;
		}
		g2d.drawString(label, x2, (int) (-Math.cos(angle * Math.PI / 180) * 60));  // show label

		g2d.rotate(angle * Math.PI / 180);
		g2d.drawArc(-11, -46, 20, 35, -45, 180 + 90); // arc

		int[] xs = { 4, 15, 6 };
		int[] ys = { -29, -26, -15 };
		g2d.fillPolygon(xs, ys, 3); // solid arrow

		g2d.rotate((360 - angle) * Math.PI / 180); // Restore rotation angle and origin position
		g2d.translate(-(x + 18), -(y + 18));
	}

	/**
	 * draw all state nodes
	 * @param g
	 * @return
	 */
	private List<List<Integer>> paintTotalStates(Graphics g) {
		List<List<Integer>> layerList = new ArrayList<List<Integer>>();
		Convert_DFA convert = new Convert_DFA();
		StateMatrix stateMatrix = convert.toDFA(text);  // Get DFA state matrix by transformation

		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(0);
		layerList.add(stateList);
		int[] hArray = new int[100]; // Duplicate detection array
		for (int i = 0; i < 100; i++) {
			hArray[i] = 0;
		}
		int count = 0, state = 0, layer = 0; //The number of states currently added, the state number of the previous layer, the number of the previous layer
		int count2 = -1;
		while (count != count2) {  // Generate a hierarchical arrangement of state nodes
			count2 = count;
			stateList = new ArrayList<Integer>();
			for (int i = 0; i < layerList.get(layer).size(); i++) {
				state = layerList.get(layer).get(i);
				for (int m = 1; m < stateMatrix.getMatrix()[state].length; m++) {
					if ((stateMatrix.getMatrix()[state][m] > state) && (hArray[stateMatrix.getMatrix()[state][m]] == 0)) {
						stateList.add(stateMatrix.getMatrix()[state][m]);
						hArray[stateMatrix.getMatrix()[state][m]] = 1;
						count++;
					}
				}
			}
			layerList.add(stateList);
			layer++;
		}
		
		int max = 0;
		for(List<Integer> st: layerList) { // Get the maximum value of the number of states at each level
			if(st.size() > max) {
				max = st.size();
			}
		}
		
		this.setPreferredSize(new Dimension(layerList.size() * 110, max * 110));
		
		List<List<Integer>> coordList = new ArrayList<List<Integer>>(); // list of coordinates
		paintState(g, "0", 60, max / 2 * 110 + 100);  // Draw state node 0
		List<Integer> coord = new ArrayList<Integer>();
		coord.add(0);
		coord.add(60);
		coord.add(max / 2 * 110 + 100);
		coordList.add(coord);
		int layerNum = 1;
		for(List<Integer> st: layerList) {  // Draw each state node
			int n = 0;
			for(int stateNum: st) {
				if(stateNum == 0) {
					continue;
				}
				paintState(g, String.valueOf(stateNum), 110 * layerNum - 40, n * 110 + 100);
				coord = new ArrayList<Integer>();
				coord.add(stateNum);
				coord.add(110 * layerNum - 40);
				coord.add(n * 110 + 100);
				coordList.add(coord);
				n++;
			}
			layerNum++;
		}
		return coordList;
	}
	
	/**
	 * draw all connecting arrows
	 * @param g
	 * @param coordList
	 */
	private void paintTotalLinkedArrows(Graphics g, List<List<Integer>> coordList) {
		Convert_DFA convert = new Convert_DFA();
		StateMatrix stateMatrix = convert.toDFA(text);  // Get DFA state matrix by transformation
		
		int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		for(int state = 0; state < stateMatrix.stateTotal(); state++) {
			for(int p = 1; p < stateMatrix.getMatrix()[0].length; p++) {
				if(stateMatrix.getMatrix()[state][0] != stateMatrix.getMatrix()[state][p] && stateMatrix.getMatrix()[state][p] != -1) {
					int i;
					for(i = 0; i < coordList.size(); i++) {
						if(coordList.get(i).get(0) == stateMatrix.getMatrix()[state][0]) {
							break;
						}
					}
					x1 = coordList.get(i).get(1);
					y1 = coordList.get(i).get(2);
					for(i = 0; i < coordList.size(); i++) {
						if(coordList.get(i).get(0) == stateMatrix.getMatrix()[state][p]) {
							break;
						}
					}
					x2 = coordList.get(i).get(1);
					y2 = coordList.get(i).get(2);
					paintArrow(g, x1, y1, x2, y2, stateMatrix.getInCh()[p] + "");  // 绘制连接箭头
				} else {
					if(stateMatrix.getMatrix()[state][p] != -1) {
						int i;
						for(i = 0; i < coordList.size(); i++) {
							if(coordList.get(i).get(0) == stateMatrix.getMatrix()[state][0]) {
								break;
							}
						}
						x1 = coordList.get(i).get(1);
						y1 = coordList.get(i).get(2);
						paintLoop(g, x1, y1, 180, stateMatrix.getInCh()[p] + "");  // 绘制环箭头
					}
				}
			}
		}
	}
}
