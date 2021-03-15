import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The purpose of this class is to hold the rule number and its respective values.
 * In order to evolve generations, we need to be able to apply the rule that is given,
 * that is what this class does. Using the hashmap, its true false values are correctly stored
 * in relation with its possible eight states. Ultimately, this class is able to evolve whole
 * generations, as well as just neighborhoods.
 *
 * @author Cole Hoffman
 * @version 0.1
 */

public class Rule {
    private final int ruleNum;
    private final boolean[] ruleStates = new boolean[8];
    private final HashMap<String, Boolean> binaryValues = new HashMap<>();
    private final ArrayList<Character> charList = new ArrayList<>();
    private static final int TRUE = 1;
    private static final int FALSE = 0;

    /*
    On the first generation, the middle index is set to 1 (positive),
    for the next generation, we compare the states of its two neighbors (left and right) in order from left to right.
    example: to get the next generation of the middle index, we would get 010. We then compare that to the corresponding
    eight possible states and if it had any numbers divided into it. In the case of rule 145, it would be set to 0 the next
    generation.
     */

    /**
     * Initializes the rule given a rule number.
     *
     * @param ruleNum The rule number that is going to be used for the class methods.
     */
    public Rule(int ruleNum) {
        if (ruleNum > 255) {
            ruleNum = 255;
        } else if (ruleNum < 0) {
            ruleNum = 0;
        }

        this.ruleNum = ruleNum;

        //pads the binary number so that it matches with the ruleStates boolean array
        padBinary();
        //fills in the boolean array with the correct values using the padded binary value
        createValueArray();
        // fills in the hashmap with the correct values corresponding to the eight possible states
        fillHashmap();

    }

    /**
     * Pads the binary value with zeros to ensure that the rule is correct represented within the
     * ruleStates boolean array. Without padding, the true or false values would be incorrect.
     */
    public void padBinary() {
        //Convert the rule number into a binary, and then make a character array of it
        char[] binaryCharArray = Integer.toBinaryString(ruleNum).toCharArray();

        //New character array with the same length as the binaryCharArray.
        Character[] charArray = new Character[binaryCharArray.length];

        //Get the required padding amount from the difference of 8 and the binary array.
        Character[] padAmount = new Character[8 - binaryCharArray.length];
        //Fill the padding array with zeros (false values)
        Arrays.fill(padAmount, '0');

        //Fill the charArray with its corresponding binary value
        for (int i = 0; i < binaryCharArray.length; i++) {
            charArray[i] = binaryCharArray[i];
        }
        //Add the padding first to ensure correct corresponding state to binary values.
        charList.addAll(Arrays.asList(padAmount));
        //Add the binary values
        charList.addAll(Arrays.asList(charArray));

    }

    /**
     * Creates the array that is used in the hashmap to assign the relational values
     * of the eight states. Replaces ones and zeros with true and false, respectively.
     */

    //fills in the true or false values in the ruleStates array
    private void createValueArray() {

        //loops over the charArray and assigns corresponding true/false value
        for (int i = 0; i < charList.size(); i++) {
            //if equal to one, assigns true, else false
            if (Integer.parseInt(String.valueOf(charList.get(i))) == 1) {
                ruleStates[i] = true;
            } else {
                ruleStates[i] = false;
            }
        }
    }

    /**
     * Fills the hashmap with the correct true or false values in respect to
     * the eight states and the binary value of the rule number.
     */
    //initializes the hashmap with its corresponding values from ruleStates
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


    /**
     * Evolves the entire generation onto the next using the given rule number corresponding values.
     *
     * @param gen The generation that is going to be evolved
     * @return Returns the new, evolved generation.
     */
    public Generation evolve(Generation gen) {
        // get the current states of passed generation in the parameter
        boolean[] generationBooleans = gen.getStates();

        //creates a new boolean array for the coming new generation of the length of the previous gen.
        boolean[] newGeneration = new boolean[generationBooleans.length];

        //loops over the indexes and evolves the indexes into their next generation state
        for (int i = 0; i < generationBooleans.length; i++) {
            newGeneration[i] = evolve(getNeighborhood(i, gen));
        }

        //returns a new generation created from the newGeneration array
        return new Generation(newGeneration);

    }

    /**
     * Gets the neighborhood of the passed index. The neighborhood includes the left and right values in relation
     * to the index that is given, as well as the index value itself.
     *
     * @param idx The index to get the neighborhood of.
     * @param gen The generation that is to be used for the neighborhood.
     * @return Returns a boolean array that contains the neighborhood that is found.
     */
    // gets the neighborhood given a specific index
    public static boolean[] getNeighborhood(int idx, Generation gen) {
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


    /**
     * Evolves one index given the neighborhood instead of the entire generation.
     *
     * @param neighborhood The neighborhood that is going to be used to determine the next generation of the middle index.
     * @return Returns a boolean value which signifies the evolved state of the index.
     */

    //evolves a specific neighborhood
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

    /**
     * Gets the rule number.
     *
     * @return Returns the rule number.
     */

    //returns the rule number
    public int getRuleNum() {
        return ruleNum;
    }

}
