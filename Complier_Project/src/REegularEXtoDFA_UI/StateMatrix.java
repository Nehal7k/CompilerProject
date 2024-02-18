package REegularEXtoDFA_UI;

/**
 * State transition matrix class *
 */
public class StateMatrix {
	
	private int[][] matrix = null;
	private char[] inCh = null;
	
	/**
	 * Get a two-dimensional array of state matrices from a StateMatrix object
	 */
	public int[][] getMatrix() {
		return matrix;
	}
	
	/**
         * Add a two-dimensional array of state matrices to the StateMatrix object
         * @param matrix
	 */
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	
	/**
	 * Get the character array containing the character set from the StateMatrix object
	 * @return
	 */
	public char[] getInCh() {
		return inCh;
	}
	
	/**
	 * Add the character set to the StateMatrix object in the form of 
         * a character array as the header of the state matrix table
	 * @param labelArray
	 */
	public void setInCh(char[] labelArray) {
		this.inCh = labelArray;
	}
	
	/**
	 * Get the total number of states in the first column of a two-dimensional integer array
	 * @return
	 */
	public int stateTotal() {
		int length = 0, state = -1;
		for(int i = 0; i < matrix.length; i++) {
			if(matrix[i][0] != -1 && matrix[i][0] > state) {
				length++;
				state = matrix[i][0];
			}
		}
		return length;
	}
	
}
