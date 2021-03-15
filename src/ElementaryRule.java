import java.util.Arrays;
import java.util.HashMap;

public class ElementaryRule extends Rule {
    private char[] binaryChar;
    private final boolean[] ruleStates = new boolean[8];
    private final HashMap<String, Boolean> binaryValues = new HashMap<>();
    private static final int TRUE = 1;
    private static final int FALSE = 0;


    protected ElementaryRule(int ruleNum) throws RuleNumException {
        super(ruleNum);
        if(getRuleNum() > 255 || getRuleNum() < 0) throw new RuleNumException(0, 255);
        else {
            fillRuleStates();
            fillHashmap();
        }
    }

    private void fillRuleStates() {
        //padding code courtesy of Tina Nguyen.
        binaryChar = String.format("%08d", Integer.parseInt(Integer.toBinaryString(getRuleNum()))).toCharArray();

        for (int i = 0; i < binaryChar.length; i++) {
            if (binaryChar[i] == '1') ruleStates[i] = true;
            else ruleStates[i] = false;
        }
    }

    private void fillHashmap() {
        binaryValues.put("111", ruleStates[0]);
        binaryValues.put("110", ruleStates[1]);
        binaryValues.put("101", ruleStates[2]);
        binaryValues.put("100", ruleStates[3]);
        binaryValues.put("011", ruleStates[4]);
        binaryValues.put("010", ruleStates[5]);
        binaryValues.put("001", ruleStates[6]);
        binaryValues.put("000", ruleStates[7]);
    }


    @Override
    public boolean evolve(boolean[] neighborhood) {
        //create a new stringBuilder object which we will use with the hashmap to get the correct next value
        StringBuilder intNeighborhood = new StringBuilder();

        //enhanced for loop checks if true, then adds 1 to the string, if false, then adds a 0
        for (boolean b : neighborhood) {
            if (b) {
                intNeighborhood.append(TRUE);
            } else {
                intNeighborhood.append(FALSE);
            }
        }

        //uses the value of intNeighborhood to get the corresponding true/false value from the hashmap
        return binaryValues.get(intNeighborhood.toString());
    }

    @Override
    public boolean[] getNeighborhood(int idx, Generation gen) {
        //gets the boolean values of the given generation to check against the index
        boolean[] generationBooleans = gen.getStates();

        //creates a new boolean array which will store the neighbors of the index and the index itself
        boolean[] neighbors = new boolean[3];

        if (generationBooleans.length == 1) {
            //if the array only has one value, the only neighbors it has are itself.
            Arrays.fill(neighbors, generationBooleans[0]);
        } else if (idx == 0) {
            //if the index is 0, the left neighbor is the last index
            neighbors[0] = generationBooleans[generationBooleans.length - 1];
            neighbors[1] = generationBooleans[idx];
            neighbors[2] = generationBooleans[idx + 1];
        } else if (idx == generationBooleans.length - 1) {
            //if the index is the last index, the right neighbor is the first index
            neighbors[0] = generationBooleans[idx - 1];
            neighbors[1] = generationBooleans[idx];
            neighbors[2] = generationBooleans[0];
        } else {
            //fill in the neighborhood properly with its left neighbor, the index, then the right neighbor values
            neighbors[0] = generationBooleans[idx - 1];
            neighbors[1] = generationBooleans[idx];
            neighbors[2] = generationBooleans[idx + 1];
        }

        return neighbors;
    }

    @Override
    public String getRuleTable(char falseSymbol, char trueSymbol) {
        char[] TFVals = new char[8];

        String possibleStates = "111 110 101 100 011 010 001 000";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < possibleStates.length(); i++) {
            if(possibleStates.charAt(i) == '1') sb.append(trueSymbol);
            else if(possibleStates.charAt(i) == ' ') sb.append(' ');
            else sb.append(falseSymbol);
        }
        for (int i = 0; i < ruleStates.length; i++) {
            if(ruleStates[i]) TFVals[i] = trueSymbol;
            else TFVals[i] = falseSymbol;
        }


        return String.format(sb.toString() + System.lineSeparator() +
                      " %c   %c   %c   %c   %c   %c   %c   %c ", TFVals[0], TFVals[1], TFVals[2], TFVals[3], TFVals[4], TFVals[5], TFVals[6], TFVals[7]);
    }
}
