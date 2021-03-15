public class RuleNumException extends Exception {
    private final long serialVersionUID = 69L;

    RuleNumException(int min, int max) {
        super("ruleNum is outside the range [" + min + ", " + max + "].");
    }



}
