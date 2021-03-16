public class TotalisticAutomaton extends Automaton {


    protected TotalisticAutomaton(int ruleNum, Generation initial) throws RuleNumException {
        super(ruleNum, initial);
    }

    protected TotalisticAutomaton(String fileName) {
        super(fileName);
    }

    @Override
    protected Rule createRule(int ruleNum) throws RuleNumException{
        return new TotalisticRule(ruleNum);
    }

    @Override
    public int getRuleNum() {
        return super.getRuleNum();
    }
}
