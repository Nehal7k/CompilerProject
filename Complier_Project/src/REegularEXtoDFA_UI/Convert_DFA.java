package REegularEXtoDFA_UI;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import REegularEXtoDFA_UI.StateMatrix;

/*
* Regular expression to DFA conversion class
 */
public class Convert_DFA {

	/**
         * Convert_DFA regular expression to DFA
	 * @param text
	 * @return
	 */
	public StateMatrix toDFA(String text) throws EmptyStackException {
		String postfix = reToPostfix(text);
		Map<String, Object> map = getFirstposAndFollowpos(postfix);
		StateMatrix stateMatrix = createStateMatrix(map);
		return stateMatrix;
	}

	/**
         * Regular expressions are changed to postfix expressions	 * 
	 * @param text
	 * @return
	 */
	public String reToPostfix(String text) throws EmptyStackException {
		int i = 0;
		char c, pc;
		StringBuilder sb = new StringBuilder(text);
		sb.append('\0');

		pc = sb.charAt(i);
		c = sb.charAt(++i);
		while (sb.charAt(i) != '\0') // Add to&
		{
			if (((pc == ')' && c == '(')) || (!isOperator(pc) && !isOperator(c)) || (!isOperator(pc) && c == '(')
					|| (pc == ')' && !isOperator(c)) || (pc == '*' && !isOperator(c)) || (pc == '*' && c == '(')) {// Four cases need to be added&
				sb.insert(i, "&");
			}
			pc = sb.charAt(i++);
			c = sb.charAt(i);
		}
		sb.append("!");
		sb.replace(sb.indexOf("\0"), sb.indexOf("\0") + 1, "");

		// Convert_DFA to postfix expression
		String postfix = "";
		Stack<Character> stack = new Stack<Character>();

		stack.push('$');
		char tempc, tempc2;

		for (i = 0; i < sb.length(); i++) {
			tempc = sb.charAt(i);
			if (isOperator(tempc)) {
				switch (tempc) {
				case '(':
					stack.push(tempc);
					break;
				case ')':
					while (stack.peek() != '(') {
						tempc2 = stack.peek();
						stack.pop();
						postfix += tempc2;
					}
					stack.pop();
					break;
				default:
					tempc2 = stack.peek();
					while (tempc2 != '(' && Operator_Less_Than(tempc, tempc2)) {
						tempc2 = stack.peek();
						stack.pop();
						postfix += tempc2;
						tempc2 = stack.peek();
					}
					stack.push(tempc);
					break;
				}
			} else
				postfix += tempc;
		}
		postfix = postfix + "#&";

		return postfix;
	}

	/**
	 * Check if a character is an operator
	 * @param c
	 * @return
	 */
	private boolean isOperator(char c) {
		switch (c) {
		case '|':
		case '&':
		case '(':
		case ')':
		case '!':
		case '*':
			return true;
		default:
			return false;
		}
	}

	/**
	 * comparison operator number
	 * @param t1
	 * @param t2
	 * @return
	 */
	private boolean Operator_Less_Than(char t1, char t2) {
		int temp1 = getOperatorNumber(t1);
		int temp2 = getOperatorNumber(t2);
		if (temp1 <= temp2)
			return true;
		return false;
	}

	/**
	 * get operator number
	 * @param t1
	 * @return
	 */
	private int getOperatorNumber(char t1) {
		switch (t1) {
		case '$':
			return 0;
		case '!':
			return 1;
		case ')':
			return 2;
		case '|':
			return 3;
		case '&':
			return 4;
		case '*':
			return 5;
		case '(':
			return 6;
		default:
			return 7;
		}
	}

	/**
	 * Obtain the result set of the firstpos function of the root node
         * and the result set of the followpos function of each node 
         * according to the suffix expression
	 * 
	 * @param postfix
	 * @return
	 */
	private Map<String, Object> getFirstposAndFollowpos(String postfix) {
		Map<String, Object> map = new HashMap<String, Object>();
		int length = postfix.length();
		boolean[] nullable = new boolean[length];
		int[] position = new int[length];// Identify character position
		int[][] firstpos = new int[length][length];
		int[][] lastpos = new int[length][length];
		int[][] followpos = new int[length][length];
		int pos = 1;

		int[][] tree = createTree(postfix); // Create a syntax tree from a suffix expression

		for (int i = 0; i < length; i++) {  // Initialize the collection array
			for (int j = 0; j < length; j++) {
				firstpos[i][j] = lastpos[i][j] = followpos[i][j] = 0;
			}
		}
		for (int i = 0; i < length; i++) { // find out each node nullable(n)、firstpos(n)、lastpos(n)、followpos(n)
			if (Isalpha(postfix.charAt(i)) || postfix.charAt(i) == '#') {  // Letter or terminal symbol (leaf node of syntax tree)
				position[i] = pos;  // Number letters or terminators
				pos++;
				nullable[i] = false;
				firstpos[i][0] = position[i];
				lastpos[i][0] = position[i];
			} else {  // operator (parent node of syntax tree)
				position[i] = 0;// The operator's position is set to 0
				if (postfix.charAt(i) == '|') {
					// nullable(i) = nullable(c1) or nullable(c2)
					nullable[i] = (nullable[tree[i][0]] || nullable[tree[i][1]]);

					// firstpos(i) = firstpos(c1) U firstpos(c2)
					// lastpos(i) = lastpos (c1) U lastpos (c2)
					int j = 0;
					while (firstpos[tree[i][1]][j] != 0) {
						firstpos[i][j] = firstpos[tree[i][1]][j];
						lastpos[i][j] = lastpos[tree[i][1]][j];
						j++;
					}
					int k = 0;
					int m;
					while (firstpos[tree[i][0]][k] != 0) {
						for (m = 0; m < j; m++) {
							if (firstpos[i][m] == firstpos[tree[i][0]][k])
								break;
						}
						if (m == j) {
							firstpos[i][j] = firstpos[tree[i][0]][k];
							lastpos[i][j] = lastpos[tree[i][0]][k];
							j++;
						}
						k++;
					}

				} else if (postfix.charAt(i) == '*') {
					nullable[i] = true;

					// firstpos(i) = firstpos(c1)
					// lastpos(i) = lastpos (c1)
					int j = 0;
					while (firstpos[tree[i][0]][j] != 0) {
						firstpos[i][j] = firstpos[tree[i][0]][j];
						lastpos[i][j] = lastpos[tree[i][0]][j];
						j++;
					}
				} else { // Joiner&
					nullable[i] = (nullable[tree[i][0]] && nullable[tree[i][1]]);

					if (nullable[tree[i][0]] == true) { // firstpos(i) = firstpos(c1) U firstpos(c2)
						int j = 0;
						int m;
						while (firstpos[tree[i][1]][j] != 0) {
							firstpos[i][j] = firstpos[tree[i][1]][j];
							j++;
						}
						int k = 0;
						while (firstpos[tree[i][0]][k] != 0) {
							for (m = 0; m < j; m++) {
								if (firstpos[i][m] == firstpos[tree[i][0]][k])
									break;
							}
							if (m == j) {
								firstpos[i][j] = firstpos[tree[i][0]][k];
								j++;
							}
							k++;
						}
					} else { // firstpos(i) = firstpos(c1)
						int j = 0;
						while (firstpos[tree[i][0]][j] != 0) {
							firstpos[i][j] = firstpos[tree[i][0]][j];
							j++;
						}
					}

					if (nullable[tree[i][1]]) { // lastpos(i) = lastpos (c1) U lastpos (c2)
						int j = 0;
						while (lastpos[tree[i][1]][j] != 0) {
							lastpos[i][j] = lastpos[tree[i][1]][j];
							j++;
						}
						int k = 0;
						int m;
						while (lastpos[tree[i][0]][k] != 0) {
							for (m = 0; m < j; m++) {
								if (lastpos[i][m] == lastpos[tree[i][0]][k])
									break;
							}
							if (m == j) {
								lastpos[i][j] = lastpos[tree[i][0]][k];
								j++;
							}
							k++;
						}
					} else { // lastpos(i) = lastpos (c2)
						int j = 0;
						while (lastpos[tree[i][1]][j] != 0) {
							lastpos[i][j] = lastpos[tree[i][1]][j];
							j++;
						}
					}

				}
			}
		}

		Set<Integer> firstposSet = new HashSet<Integer>();
		int j = 0;
		while (firstpos[length - 1][j] != 0) // Add the firstpos collection of the suffixed root node
		{
			firstposSet.add(firstpos[length - 1][j]);
			j++;
		}
		map.put("firstpos", firstposSet);

		for (int i = 0; i < length; i++) { // Find the followpos(n) of each node
			if (postfix.charAt(i) == '&') {
				int m = 0;
				while (lastpos[tree[i][0]][m] != 0) {
					int k;
					for (k = 0; k < length; k++) {  //Find a free position to insert the result set element of firstpos(c2)
						if (followpos[lastpos[tree[i][0]][m]][k] == 0)
							break;
					}
					int n = 0;
					while (firstpos[tree[i][1]][n] != 0) { // For all positions i' in lastpos(c1), followpos(i') = firstpos(c2)
						followpos[lastpos[tree[i][0]][m]][k] = firstpos[tree[i][1]][n];
						n++;
						k++;
					}
					m++;
				}
			}
			if (postfix.charAt(i) == '*') {
				int m = 0;
				while (lastpos[i][m] != 0) {
					int n;
					for (n = 0; n < length; n++) {  // Find an empty position to insert the result set element of firstpos(n)
						if (followpos[lastpos[i][m]][n] == 0)
							break;
					}
					int k = 0;
					while (firstpos[i][k] != 0) { // For all positions i' in lastpos(n), followpos(i') = firstpos(n)
						followpos[lastpos[i][m]][n] = firstpos[i][k];
						k++;
						n++;
					}
					m++;
				}
			}
		}

		Map<Integer, Character> labelMap = new HashMap<Integer, Character>();
		for (int i = 0; i < length; i++) { // Get number table of characters
			if (position[i] != 0) {
				labelMap.put(position[i], postfix.charAt(i));
			}
		}
		map.put("label", labelMap);

		Map<Integer, Set<Integer>> followposMap = new HashMap<Integer, Set<Integer>>();
		for (int i = 0; i < length; i++) {
			if (position[i] != 0) {
				int k = 0;
				Set<Integer> followposSet = new HashSet<Integer>();
				while (followpos[position[i]][k] != 0) {
					followposSet.add(followpos[position[i]][k]);
					k++;
				}
				followposMap.put(position[i], followposSet);
			}
		}
		map.put("followpos", followposMap);

		return map;
	}

	/**
	 * Create a syntax tree for postfix expressions
	 * 
	 * @param postfix
	 * @return
	 */
	public int[][] createTree(String postfix) throws EmptyStackException {
		Stack<Integer> st = new Stack<Integer>();
		int[][] tree = new int[postfix.length()][2];
		for (int i = 0; i < postfix.length(); i++) { // Initialize the syntax tree
			for (int j = 0; j < 2; j++) {
				tree[i][j] = -1;
			}
		}
		for (int i = 0; i < postfix.length(); i++) {
			switch (postfix.charAt(i)) {
			case '*':
				tree[i][0] = st.pop();
				st.push(i);
				break;
			case '&':
				tree[i][1] = st.pop();
				tree[i][0] = st.pop();
				st.push(i);
				break;
			case '|':
				tree[i][1] = st.pop();
				tree[i][0] = st.pop();
				st.push(i);
				break;
			default:
				if (Isalpha(postfix.charAt(i)) || postfix.charAt(i) == '#') {
					st.push(i);
				}
			}
		}
		return tree;
	}

	/**
	 * Check if a character is a letter
	 * @param c
	 * @return
	 */
	private boolean Isalpha(char c) {
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			return true;
		else
			return false;
	}

	/**
	 * Construct the state matrix
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public StateMatrix createStateMatrix(Map<String, Object> map) {
		Map<Integer, Character> labelMap = (Map<Integer, Character>) map.get("label");
		Set<Character> set = new HashSet<Character>();
		for (Character ch : labelMap.values()) { // Get unique character set
			if (ch != '#') {
				set.add(ch);
			}
		}
		List<Set<Integer>> stateList = new ArrayList<Set<Integer>>();
		List<List<Object>> tran = new ArrayList<List<Object>>();
		Map<Integer, Set<Integer>> followposMap = (Map<Integer, Set<Integer>>) map.get("followpos");
		Stack<Set<Integer>> stateStack = new Stack<Set<Integer>>();
		stateStack.push((Set<Integer>) map.get("firstpos")); // Push the result set of firstpos(root) as the initial state
		while (!stateStack.isEmpty()) {
			Set<Integer> state = stateStack.pop();
			stateList.add(state); // Pop the top state state from the stack and add it to the state set
			for (Character ch : set) {
				Set<Integer> tmpSet = new HashSet<Integer>();
				for (int i : state) { //Find the union tmpSet of the followpos(n) of all positions n corresponding to ch in state
					if (labelMap.get(i) == ch) {
						tmpSet.addAll(followposMap.get(i));
					}
				}
				if (!tmpSet.isEmpty()) {
					int eq = 0;
					for (Set<Integer> set1 : stateList) {
						if (isSetEqual(set1, tmpSet)) {
							eq = 1;
							break;
						}
					}
					if (eq == 0) { // If tmpSet is not in the state set, push it into the stack
						stateStack.push(tmpSet);
					}
					List<Object> rel = new ArrayList<Object>();
					rel.add(state);
					rel.add(ch);
					rel.add(tmpSet);
					tran.add(rel); // Add transition relation (state, ch)->tmpSet in state transition set
				}
			}
		}
		List<Character> labelList = new ArrayList<Character>();
		labelList.addAll(set);
		int[][] intMatrix = new int[stateList.size()][set.size() + 1];
		for (int i = 0; i < stateList.size(); i++) { // Initialize a two-dimensional integer array
			for (int j = 0; j < set.size() + 1; j++) {
				intMatrix[i][j] = -1;
			}
		}
		Set<Integer> state = new HashSet<Integer>();
		int i = -1, j = 0;
		for (List<Object> rela : tran) {  // Add the transition relationship in the state transition set to a two-dimensional integer array after processing
			if (!isSetEqual(state, (Set<Integer>) rela.get(0))) {
				j = 0;
				i++;
				intMatrix[i][j] = stateList.indexOf(rela.get(0));
			}
			j = 1 + labelList.indexOf(rela.get(1));
			intMatrix[i][j] = stateList.indexOf(rela.get(2));
			state = (Set<Integer>) rela.get(0);
		}
		
		StateMatrix stateMatrix = new StateMatrix();
		stateMatrix.setMatrix(intMatrix);
		char[] labelArray = new char[labelList.size() + 1];
		labelArray[0] = ' ';
		int i2 = 1;
		for (char ch : labelList) { // Store the character set in a character array
			labelArray[i2] = ch;
			i2++;
		}
		stateMatrix.setInCh(labelArray); // Add character set to stateMatrix object
		return stateMatrix;
                
                
	}

	/**
	 * Check if two sets are equal
	 * @param set1
	 * @param set2
	 * @return
	 */
	private boolean isSetEqual(@SuppressWarnings("rawtypes") Set set1, @SuppressWarnings("rawtypes") Set set2) {

		if (set1 == null && set2 == null) {
			return true; // Both are null
		}

		if (set1 == null || set2 == null || set1.size() != set2.size() || set1.size() == 0 || set2.size() == 0) {
			return false;
		}

		@SuppressWarnings("rawtypes")
		Iterator ite2 = set2.iterator();

		boolean isFullEqual = true;

		while (ite2.hasNext()) {
			if (!set1.contains(ite2.next())) {
				isFullEqual = false;
			}
		}

		return isFullEqual;
	}
}
