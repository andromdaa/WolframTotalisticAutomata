/**
 * The purpose of this class is to represent Generations and their respective functions.
 * Houses methods that are used to get information from the generation.
 *
 * @author Cole Hoffman
 * @version 0.1
 */
public class Generation {
    //represents the states of a row at a fixed time
    private final boolean[] cellStates;

    /**
     * Initializes a generation.
     *
     * @param cellStates An array of an arbitrary amount that contains the true or false values of the generation to be initialized.
     */
    public Generation(boolean... cellStates) {
        //null checks
        if (cellStates == null || cellStates.length == 0) {
            cellStates = new boolean[]{false};
        }

        //immutability, clone the cellStates array to the global cellStates variable
        this.cellStates = cellStates.clone();
    }

    /**
     * Initializes a generation
     *
     * @param states     A string of states that is representative of the true or false values used to initialize a generation.
     * @param trueSymbol The symbol that is used to signify an element from the string object is true.
     */
    public Generation(String states, char trueSymbol) {
        //null checks
        if (states == null || states.isEmpty()) {
            cellStates = new boolean[]{false};
            return;
        }

        //create new boolean array the size of the char array
        cellStates = new boolean[states.length()];

        //loop over the char array and assign its correct corresponding value
        for (int i = 0; i < states.length(); i++) {
            if (states.charAt(i) == trueSymbol) {
                cellStates[i] = true;
            } else {
                cellStates[i] = false;
            }
        }
    }

    /**
     * Gets the state of an index.
     *
     * @param idx The index to get the state of.
     * @return Returns the state of the given index.
     */
    //return the cellState of the given index
    public boolean getState(int idx) {
        //returns clone for immutability
        return cellStates.clone()[idx];
    }

    /**
     * Returns all the states of a generation.
     *
     * @return Returns a clone of the cellStates array.
     */
    public boolean[] getStates() {
        //returns clone of states, immutability
        return cellStates.clone();
    }

    /**
     * Gets the states of a generation given true and false symbols.
     *
     * @param falseSymbol The symbol that represents an element as false.
     * @param trueSymbol  The symbol that represents an element as true.
     * @return Returns the states as a string which has its true or false values replaced with its respective symbol.
     */
    public String getStates(char falseSymbol, char trueSymbol) {
        //stringbuilder to create the string
        StringBuilder stringBuilder = new StringBuilder();

        //if true, append the trueSymbol, else append falseSymbol to the stringbuilder
        for (boolean s : getStates()) {
            if (s) {
                stringBuilder.append(trueSymbol);
            } else {
                stringBuilder.append(falseSymbol);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Returns the size of the cellStates.
     *
     * @return Returns an integer represented by the cellStates cloned length.
     */
    public int size() {
        //return cellStates size clone length, clone not necessary, just for extra peace of mind with immutability
        return cellStates.clone().length;
    }
}
