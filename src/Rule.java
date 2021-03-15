public abstract class Rule {
    private int ruleNum;

    protected Rule(int ruleNum) {
        this.ruleNum = ruleNum;
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
    public abstract boolean[] getNeighborhood(int idx, Generation gen);


    /**
     * Evolves one index given the neighborhood instead of the entire generation.
     *
     * @param neighborhood The neighborhood that is going to be used to determine the next generation of the middle index.
     * @return Returns a boolean value which signifies the evolved state of the index.
     */

    //evolves a specific neighborhood
    public abstract boolean evolve(boolean[] neighborhood);

    /**
     * Gets the rule number.
     *
     * @return Returns the rule number.
     */

    //returns the rule number
    public int getRuleNum() {
        return ruleNum;
    }

    public abstract String getRuleTable(char falseSymbol, char trueSymbol);

}
