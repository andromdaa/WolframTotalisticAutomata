import java.util.Arrays;
import java.util.HashMap;

public class TotalisticRule extends Rule {
    private final boolean[] ruleStates = new boolean[6];
    private final HashMap<String, Boolean> binaryValues = new HashMap<>();
    private static final int TRUE = 1;
    private static final int FALSE = 0;

    protected TotalisticRule(int ruleNum) throws RuleNumException {
        super(ruleNum);
        if (getRuleNum() > 63 || getRuleNum() < 0) throw new RuleNumException(0, 63);
        else {
            fillRuleStates();
            fillHashmap();
        }
    }


    private void fillRuleStates() {
        //padding code courtesy of Tina Nguyen.
        char[] charList = String.format("%06d", Integer.parseInt(Integer.toBinaryString(getRuleNum()))).toCharArray();

        for (int i = 0; i < charList.length; i++) {
            if (charList[i] == '1') ruleStates[i] = true;
            else ruleStates[i] = false;
        }
    }

    private void fillHashmap() {
        binaryValues.put("5", ruleStates[0]);
        binaryValues.put("4", ruleStates[1]);
        binaryValues.put("3", ruleStates[2]);
        binaryValues.put("2", ruleStates[3]);
        binaryValues.put("1", ruleStates[4]);
        binaryValues.put("0", ruleStates[5]);
    }


    @Override
    public boolean evolve(boolean[] neighborhood) {
        int trueAmount = 0;
        for (boolean b : neighborhood) {
            if (b) trueAmount += 1;
        }
        return (ruleStates[5 - trueAmount]);
    }

    @Override
    public boolean[] getNeighborhood(int idx, Generation gen) {
        boolean[] neighborHood = new boolean[5];
        boolean[] genStates = gen.getStates();
        int length = gen.size();

        if(gen.size() == 1) {
            Arrays.fill(neighborHood, genStates[0]);
        } else {
            setLeft(idx, genStates, neighborHood, length);
            neighborHood[2] = genStates[idx];
            setRight(idx, genStates, neighborHood, length);
        }

        return neighborHood;
    }

    private void setLeft(int idx, boolean[] genStates, boolean[] neighborHood, int length) {
        try {
            neighborHood[0] = genStates[idx - 2];
            neighborHood[1] = genStates[idx - 1];
        } catch (IndexOutOfBoundsException e) {
            if(idx - 1 < 0) {
                neighborHood[0] = genStates[length - 2];
                neighborHood[1] = genStates[length - 1];
            } else {
                neighborHood[0] = genStates[length - 1];
                neighborHood[1] = genStates[idx - 1];
            }
        }
    }

    private void setRight(int idx, boolean[] genStates, boolean[] neighborHood, int length) {
        try {
            neighborHood[3] = genStates[idx + 1];
            neighborHood[4] = genStates[idx + 2];
        } catch (IndexOutOfBoundsException e) {
            if(idx + 1 > length - 1) {
                neighborHood[3] = genStates[0];
                neighborHood[4] = genStates[1];
            } else {
                neighborHood[3] = genStates[length - 1];
                neighborHood[4] = genStates[0];
            }
        }
    }

    @Override
    public String getRuleTable(char falseSymbol, char trueSymbol) {
        char[] TFVals = new char[6];

        for (int i = 0; i < ruleStates.length; i++) {
            if(ruleStates[i]) TFVals[i] = trueSymbol;
            else TFVals[i] = falseSymbol;
        }

        return String.format("5 4 3 2 1 0" + System.lineSeparator() +
                "%c %c %c %c %c %c", TFVals[0], TFVals[1], TFVals[2], TFVals[3], TFVals[4], TFVals[5]);
    }
}
