package RegularEXtoDFA;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

public class RegexToDfa {

    private static Set<Integer>[] followPos;
    private static Node root;
    private static Set<State> DStates;

    private static Set<String> input; //set of characters is used in input regex

    /**  
     * a number is assigned to each characters (even duplicate ones)
     *
     * @param symbNum is a hash map has a key which mentions the number and has
     * a value which mentions the corresponding character or sometimes a string
     * for characters is followed up by backslash like "\*"
     */
    private static HashMap<Integer, String> symbNum;

 
    public static String initialize() {
        String  result= "";
        String in =JOptionPane.showInputDialog("Enter a regular expression: ");
        result+="Regular Expression : "+ in +"\n";
        String regex = in+"#";

        // DStates is a Set of States which is used for creating the final dfa.
        DStates = new HashSet<>();
       
        //input is also a Set which holding the characters of the input regular expression taken from user.
        input = new HashSet<String>();
        
        //getSymbols(regex);  sets the input Set.
        getSymbols(regex);
        SyntaxTree st = new SyntaxTree(regex);
        root = st.getRoot(); //get the root of the syntax tree
        followPos = st.getFollowPos(); //the followpos of the syntax tree

        /**
         * creating the DFA using the syntax tree were created upside and
         * returning the start state of the resulted DFA
         */
        State q0 = createDFA();
        //makes a new DFA Traversal object for traversing the resulted DFA and
        //recognizing whether the DFA can accept a particular string or not.
        DfaTraversal dfat = new DfaTraversal(q0, input);
        
       
        // get the string to test using regulare expresion
        String test = JOptionPane.showInputDialog("Enter a String: ");;
        String str =test; //getStr(in);
        result+="String  : "+ str +"\n";

        
        boolean acc = false;
        for (char c : str.toCharArray()) {
            if (dfat.setCharacter(c)) {
                acc = dfat.traverse();
            } else {
                result +="The CHARACTER: "+c+" WRONG CHARACTER >> " +"\n";;
            }
        }
        if (acc) {
             result+=">>> This string is acceptable by the Regular Expression!";
        } else {
            result+=">>>  This string is not acceptable by the Regular Expression!";
        }
        return result;
    }



    private static void getSymbols(String regex) {
        /**
         * op is a set of characters have operational meaning for example '*'
         * could be a closure operator
         */
        Set<Character> op = new HashSet<>();
        Character[] ch = {'(', ')', '*', '|', '&', '.', '\\', '[', ']', '+'};
        op.addAll(Arrays.asList(ch));

        input = new HashSet<>();
        symbNum = new HashMap<>();
        int num = 1;
        for (int i = 0; i < regex.length(); i++) {
            char charAt = regex.charAt(i);

            /**
             * if a character which is also an operator, is followed up by   
             * backslash ('\'), then we should consider it as a normal character
             * and not an operator
             */
            if (op.contains(charAt)) {
                if (i - 1 >= 0 && regex.charAt(i - 1) == '\\') {
                    input.add("\\" + charAt);
                    symbNum.put(num++, "\\" + charAt);
                }
            } else {
                input.add("" + charAt);
                symbNum.put(num++, "" + charAt);
            }
        }
    }

    private static State createDFA() {
        int id = 0;
        Set<Integer> firstpos_n0 = root.getFirstPos();

        State q0 = new State(id++);
        q0.addAllToName(firstpos_n0);
        if (q0.getName().contains(followPos.length)) {
            q0.setAccept();
        }
        DStates.clear();
        DStates.add(q0);

        while (true) {
            boolean exit = true;
            State s = null;
            for (State state : DStates) {
                if (!state.getIsMarked()) {
                    exit = false;
                    s = state;
                }
            }
            if (exit) {
                break;
            }

            if (s.getIsMarked()) {
                continue;
            }
            s.setIsMarked(true); //mark the state
            Set<Integer> name = s.getName();
            for (String a : input) {
                Set<Integer> U = new HashSet<>();
                for (int p : name) {
                    if (symbNum.get(p).equals(a)) {
                        U.addAll(followPos[p - 1]);
                    }
                }
                boolean flag = false;
                State tmp = null;
                for (State state : DStates) {
                    if (state.getName().equals(U)) {
                        tmp = state;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    State q = new State(id++);
                    q.addAllToName(U);
                    if (U.contains(followPos.length)) {
                        q.setAccept();
                    }
                    DStates.add(q);
                    tmp = q;
                }
                s.addMove(a, tmp);
            }
        }

        return q0;
    }


}
