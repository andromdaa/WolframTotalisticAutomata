public class ElementaryAutomaton extends Automaton {

    ElementaryAutomaton(int ruleNum, Generation initial) throws RuleNumException {
        super(ruleNum, initial);
    }

    ElementaryAutomaton(String fileName) {
        super(fileName);
    }

    @Override
    protected Rule createRule(int ruleNum) throws RuleNumException {
        return new ElementaryRule(ruleNum);
    }
}
