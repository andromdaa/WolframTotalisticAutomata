public class AutomatonDriver {
    public static void main(String[] args) {
        Automaton eca = new ElementaryAutomaton("rule22-61cells-input.txt");
        Automaton tca = new TotalisticAutomaton("rule22-61cells-input.txt");


        eca.evolve(15);
        tca.evolve(15);

        eca.saveEvolution("elementary22-61cells-15steps.txt");
        tca.saveEvolution("totalistic22-61cells-15steps.txt");


    }
}
