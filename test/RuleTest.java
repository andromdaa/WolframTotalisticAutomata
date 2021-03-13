import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class RuleTest {

	@Test
	void testAbstractDeclarations() {
		assertTrue(Modifier.isAbstract(Rule.class.getModifiers()));

		try {
			Method method = Rule.class.getDeclaredMethod("evolve",
					boolean[].class);
			int modifiers = method.getModifiers();
			assertTrue(Modifier.isAbstract(modifiers));
		} catch (NoSuchMethodException e) {
			fail();
		}

		try {
			Method method = Rule.class.getDeclaredMethod("getNeighborhood",
					int.class, Generation.class);
			int modifiers = method.getModifiers();
			assertTrue(Modifier.isAbstract(modifiers));
		} catch (NoSuchMethodException e) {
			fail();
		}

		try {
			Method method = Rule.class.getDeclaredMethod("getRuleTable",
					char.class, char.class);
			int modifiers = method.getModifiers();
			assertTrue(Modifier.isAbstract(modifiers));
		} catch (NoSuchMethodException e) {
			fail();
		}
	}

	@Test
	void testConstructor() {
		Rule rule = new MockRule();
		assertEquals(2334, rule.getRuleNum());
	}

	@Test
	void testEvolveGeneration() {
		Generation current = new Generation("RLRLRRLRLRLL", 'R');

		Rule rule = new MockRule();
		Generation next = rule.evolve(current);
		assertEquals("LLLLLLLLLLLL", next.getStates('L', 'R'));
	}

	// Extend Rule with a concrete class to test the non-abstract methods.
	private static class MockRule extends Rule {

		public MockRule() {
			super(2334);
		}

		@Override
		public boolean evolve(boolean[] neighborhood) {
			return false;
		}

		@Override
		public boolean[] getNeighborhood(int idx, Generation gen) {
			return null;
		}

		@Override
		public String getRuleTable(char falseSymbol, char trueSymbol) {
			return null;
		}
	}
}
