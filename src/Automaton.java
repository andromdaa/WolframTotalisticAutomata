import java.io.*;
import java.util.ArrayList;

public abstract class Automaton {
    private Rule rule;
    private ArrayList<Generation> generations;
    char falseSymbol;
    char trueSymbol;

    protected Automaton(int ruleNum, Generation initial) throws RuleNumException {
        this.rule = createRule(ruleNum);
        generations = new ArrayList<>();
        generations.add(initial);
        trueSymbol = '1';
        falseSymbol = '0';
    }

    protected Automaton(String fileName) {
        //create empty string which will be used to pass the cell states
        String initialCellStates = "";
        try {
            //create buffered reader to read the file
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            //initialize rule global variable with the next line
            rule = createRule(Integer.parseInt(reader.readLine().trim()));
            //get the line value for the true/false symbols
            String valueSymbols = reader.readLine();
            //split the valueSymbols string, strip it of any spaces, and then get its character value
            String[] splitStrings = valueSymbols.split(" ");
            falseSymbol = splitStrings[0].charAt(0);
            trueSymbol = splitStrings[1].trim().charAt(0);
            //get the cellStates from the next line
            initialCellStates = reader.readLine();
        } catch (IOException | RuleNumException ignored) {
        }

        //initialize new generation
        Generation initialGeneration = new Generation(initialCellStates, trueSymbol);

        //add the initial generation
        generations = new ArrayList<>();
        generations.add(initialGeneration);
    }

    protected abstract Rule createRule(int ruleNum) throws RuleNumException;

    public int getRuleNum() {
        return rule.getRuleNum();
    }

    /**
     * Evolves the current generation the amount of steps specified further.
     *
     * @param numSteps The amount of steps to evolve the generation.
     */
    //evolves the latest generation numSteps times
    public void evolve(int numSteps) {
        //null checks
        if (numSteps <= 0) {
            return;
        }

        //get the last generation, evolve it, add it to the arrayList of generations. For loop for how many times to do this, given numSteps
        for (int i = 0; i < numSteps; i++) {
            Generation lastGeneration = generations.get(generations.size() - 1);
            generations.add(rule.evolve(lastGeneration));
        }

    }

    /**
     * Gets the generation of the specific step number, if it does not exist, then evolve the generation until it is reached.
     *
     * @param stepNum The step number that should be used to get that generation.
     * @return Returns the generation from the given step.
     */
    //gets the generation of the specific stepNum
    public Generation getGeneration(int stepNum) {
        //checks if the generation has reached stepNum value generations, if not, evolve to that number
        if (stepNum > generations.size() - 1) {
            evolve(stepNum - getTotalSteps());
        }

        //return the generation that was requested
        return generations.get(stepNum);
    }

    /**
     * Get the total amount of times a generation has evolved.
     *
     * @return Returns the number of times a generation has been evolved.
     */
    //get the total amount of times the eca has evolved
    public int getTotalSteps() {
        return generations.size() - 1;
    }

    /**
     * Converts the elementary cellular automaton into a visual representation using true and false symbols.
     *
     * @return A string that is representative of the elementary cellular automaton.
     */
    //convert the eca into a string representation
    public String toString() {

        //create string builder
        StringBuilder stringBuilder = new StringBuilder();
        //used to check if the for loop is on the first line
        boolean first = true;

        //enhanced for loop which loops through each generation
        for (Generation generation : generations) {
            //checks if first is true, if not, do not add a lineSeparator, if false, add one
            if (first) {
                first = false;
            } else {
                stringBuilder.append(System.lineSeparator());
            }
            //for each generation, get the states, append it to the string with its respective singular true/false value
            for (boolean b : generation.getStates()) {
                if (b) {
                    stringBuilder.append(trueSymbol);
                } else {
                    stringBuilder.append(falseSymbol);
                }
            }
        }

        //return the string builder as a string
        return stringBuilder.toString();

    }

    /**
     * Saves the elementary cellular automaton to a file.
     *
     * @param filename The filename that the file is saved as.
     */


    //save the evolution to a file
    public void saveEvolution(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(toString());
            writer.close();

        } catch (IOException ignored) {

        }

    }

    public String getRuleTable() {
        return rule.getRuleTable(falseSymbol, trueSymbol);
    }


}
