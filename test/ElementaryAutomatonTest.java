import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class ElementaryAutomatonTest {

	@Test
	void testInheritance() throws RuleNumException {
		ElementaryAutomaton elementaryAutomaton =
				new ElementaryAutomaton(0, new Generation());
		assertTrue(elementaryAutomaton instanceof Automaton);
	}

	@Test
	void testConstructor() throws RuleNumException {
		int ruleNum = 255;
		Generation initial = new Generation(false, true, false);
		Automaton automaton = new ElementaryAutomaton(ruleNum, initial);

		// Check the Rule.
		String table =
				"111 110 101 100 011 010 001 000" + System.lineSeparator() +
				" 1   1   1   1   1   1   1   1 ";
		assertEquals(ruleNum, automaton.getRuleNum());
		assertEquals(table, automaton.getRuleTable());

		// Check the Generations.
		assertEquals(0, automaton.getTotalSteps());
		assertArrayEquals(new boolean[] {false, true, false},
				automaton.getGeneration(0).getStates());
		assertEquals("010", automaton.toString());
	}

	@Test
	void testEvolve() throws RuleNumException {
		Automaton automaton = new ElementaryAutomaton(22,
				new Generation("000000010000000", '1'));
		automaton.evolve(1);
		assertEquals(1, automaton.getTotalSteps());
		String evolution =
				"000000010000000" + System.lineSeparator() +
				"000000111000000";
		assertEquals(evolution, automaton.toString());

		automaton.evolve(2);
		assertEquals(3, automaton.getTotalSteps());
		automaton.falseSymbol = '_';
		automaton.trueSymbol = '%';
		evolution =
				"_______%_______" + System.lineSeparator() +
				"______%%%______" + System.lineSeparator() +
				"_____%___%_____" + System.lineSeparator() +
				"____%%%_%%%____";
		assertEquals(evolution, automaton.toString());

		Generation gen = automaton.getGeneration(7);
		assertEquals("OOO.OOO.OOO.OOO", gen.getStates('.', 'O'));
		assertEquals(7, automaton.getTotalSteps());
		automaton.falseSymbol = '.';
		automaton.trueSymbol = 'O';
		evolution =
				".......O......." + System.lineSeparator() +
				"......OOO......" + System.lineSeparator() +
				".....O...O....." + System.lineSeparator() +
				"....OOO.OOO...." + System.lineSeparator() +
				"...O.......O..." + System.lineSeparator() +
				"..OOO.....OOO.." + System.lineSeparator() +
				".O...O...O...O." + System.lineSeparator() +
				"OOO.OOO.OOO.OOO";
		assertEquals(evolution, automaton.toString());
	}


	@Test
	void customTests() throws RuleNumException {
		Automaton eca = new ElementaryAutomaton(110, new Generation("00000000000000000000100000000000000000000", '1'));
		eca.trueSymbol = '#';
		eca.falseSymbol = ' ';
		eca.evolve(20);
		assertEquals(20, eca.getTotalSteps());
		eca.saveEvolution("rule110.txt");
		String expectedToString =
				"                    #                    " + System.lineSeparator() +
						"                   ##                    " + System.lineSeparator() +
						"                  ###                    " + System.lineSeparator() +
						"                 ## #                    " + System.lineSeparator() +
						"                #####                    " + System.lineSeparator() +
						"               ##   #                    " + System.lineSeparator() +
						"              ###  ##                    " + System.lineSeparator() +
						"             ## # ###                    " + System.lineSeparator() +
						"            ####### #                    " + System.lineSeparator() +
						"           ##     ###                    " + System.lineSeparator() +
						"          ###    ## #                    " + System.lineSeparator() +
						"         ## #   #####                    " + System.lineSeparator() +
						"        #####  ##   #                    " + System.lineSeparator() +
						"       ##   # ###  ##                    " + System.lineSeparator() +
						"      ###  #### # ###                    " + System.lineSeparator() +
						"     ## # ##  ##### #                    " + System.lineSeparator() +
						"    ######## ##   ###                    " + System.lineSeparator() +
						"   ##      ####  ## #                    " + System.lineSeparator() +
						"  ###     ##  # #####                    " + System.lineSeparator() +
						" ## #    ### ####   #                    " + System.lineSeparator() +
						"#####   ## ###  #  ##                    ";
		assertEquals(expectedToString, eca.toString());
		assertEquals(getFromFile("rule110.txt"), expectedToString);


		Generation step50 = eca.getGeneration(50);
		assertEquals("### #   ##### ##### ###  #  ##      #####", step50.getStates(' ', '#'));
		assertEquals(50, eca.getTotalSteps());
		eca.trueSymbol = 'X';
		eca.falseSymbol = '_';
		eca.saveEvolution("rule110.txt");
		String evolution =
				"____________________X____________________" + System.lineSeparator() +
						"___________________XX____________________" + System.lineSeparator() +
						"__________________XXX____________________" + System.lineSeparator() +
						"_________________XX_X____________________" + System.lineSeparator() +
						"________________XXXXX____________________" + System.lineSeparator() +
						"_______________XX___X____________________" + System.lineSeparator() +
						"______________XXX__XX____________________" + System.lineSeparator() +
						"_____________XX_X_XXX____________________" + System.lineSeparator() +
						"____________XXXXXXX_X____________________" + System.lineSeparator() +
						"___________XX_____XXX____________________" + System.lineSeparator() +
						"__________XXX____XX_X____________________" + System.lineSeparator() +
						"_________XX_X___XXXXX____________________" + System.lineSeparator() +
						"________XXXXX__XX___X____________________" + System.lineSeparator() +
						"_______XX___X_XXX__XX____________________" + System.lineSeparator() +
						"______XXX__XXXX_X_XXX____________________" + System.lineSeparator() +
						"_____XX_X_XX__XXXXX_X____________________" + System.lineSeparator() +
						"____XXXXXXXX_XX___XXX____________________" + System.lineSeparator() +
						"___XX______XXXX__XX_X____________________" + System.lineSeparator() +
						"__XXX_____XX__X_XXXXX____________________" + System.lineSeparator() +
						"_XX_X____XXX_XXXX___X____________________" + System.lineSeparator() +
						"XXXXX___XX_XXX__X__XX____________________" + System.lineSeparator() +
						"X___X__XXXXX_X_XX_XXX___________________X" + System.lineSeparator() +
						"X__XX_XX___XXXXXXXX_X__________________XX" + System.lineSeparator() +
						"X_XXXXXX__XX______XXX_________________XX_" + System.lineSeparator() +
						"XXX____X_XXX_____XX_X________________XXXX" + System.lineSeparator() +
						"__X___XXXX_X____XXXXX_______________XX___" + System.lineSeparator() +
						"_XX__XX__XXX___XX___X______________XXX___" + System.lineSeparator() +
						"XXX_XXX_XX_X__XXX__XX_____________XX_X___" + System.lineSeparator() +
						"X_XXX_XXXXXX_XX_X_XXX____________XXXXX__X" + System.lineSeparator() +
						"XXX_XXX____XXXXXXXX_X___________XX___X_XX" + System.lineSeparator() +
						"__XXX_X___XX______XXX__________XXX__XXXX_" + System.lineSeparator() +
						"_XX_XXX__XXX_____XX_X_________XX_X_XX__X_" + System.lineSeparator() +
						"XXXXX_X_XX_X____XXXXX________XXXXXXXX_XX_" + System.lineSeparator() +
						"X___XXXXXXXX___XX___X_______XX______XXXXX" + System.lineSeparator() +
						"X__XX______X__XXX__XX______XXX_____XX____" + System.lineSeparator() +
						"X_XXX_____XX_XX_X_XXX_____XX_X____XXX___X" + System.lineSeparator() +
						"XXX_X____XXXXXXXXXX_X____XXXXX___XX_X__XX" + System.lineSeparator() +
						"__XXX___XX________XXX___XX___X__XXXXX_XX_" + System.lineSeparator() +
						"_XX_X__XXX_______XX_X__XXX__XX_XX___XXXX_" + System.lineSeparator() +
						"XXXXX_XX_X______XXXXX_XX_X_XXXXXX__XX__X_" + System.lineSeparator() +
						"X___XXXXXX_____XX___XXXXXXXX____X_XXX_XXX" + System.lineSeparator() +
						"X__XX____X____XXX__XX______X___XXXX_XXX__" + System.lineSeparator() +
						"X_XXX___XX___XX_X_XXX_____XX__XX__XXX_X_X" + System.lineSeparator() +
						"XXX_X__XXX__XXXXXXX_X____XXX_XXX_XX_XXXXX" + System.lineSeparator() +
						"__XXX_XX_X_XX_____XXX___XX_XXX_XXXXXX____" + System.lineSeparator() +
						"_XX_XXXXXXXXX____XX_X__XXXXX_XXX____X____" + System.lineSeparator() +
						"XXXXX_______X___XXXXX_XX___XXX_X___XX____" + System.lineSeparator() +
						"X___X______XX__XX___XXXX__XX_XXX__XXX___X" + System.lineSeparator() +
						"X__XX_____XXX_XXX__XX__X_XXXXX_X_XX_X__XX" + System.lineSeparator() +
						"X_XXX____XX_XXX_X_XXX_XXXX___XXXXXXXX_XX_" + System.lineSeparator() +
						"XXX_X___XXXXX_XXXXX_XXX__X__XX______XXXXX";

		assertEquals(evolution, eca.toString());
		assertEquals(getFromFile("rule110.txt"), evolution);


		Automaton eca2 = new ElementaryAutomaton(1, new Generation("000000000000010000000000000", '1'));
		eca2.trueSymbol = 'X';
		eca2.falseSymbol = '_';
		eca2.evolve(25);
		assertEquals(25, eca2.getTotalSteps());
		eca2.saveEvolution("rule1.txt");
		String expectedToStringRule1 =
				"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX" + System.lineSeparator() +
						"_____________X_____________" + System.lineSeparator() +
						"XXXXXXXXXXXX___XXXXXXXXXXXX";
		assertEquals(expectedToStringRule1, eca2.toString());
		assertEquals(getFromFile("rule1.txt"), expectedToStringRule1);

		Automaton eca3 = new ElementaryAutomaton(15, new Generation("011111100110001", '1'));
		eca3.trueSymbol = 'T';
		eca3.falseSymbol = '?';
		eca3.evolve(10);
		assertEquals(10, eca3.getTotalSteps());
		eca3.saveEvolution("rule15.txt");
		String expectedToStringRule15 =
				"?TTTTTT??TT???T" + System.lineSeparator() +
						"?T??????TT??TTT" + System.lineSeparator() +
						"?T?TTTTTT??TT??" + System.lineSeparator() +
						"TT?T??????TT??T" + System.lineSeparator() +
						"???T?TTTTTT??TT" + System.lineSeparator() +
						"?TTT?T??????TT?" + System.lineSeparator() +
						"TT???T?TTTTTT??" + System.lineSeparator() +
						"T??TTT?T??????T" + System.lineSeparator() +
						"??TT???T?TTTTTT" + System.lineSeparator() +
						"?TT??TTT?T?????" + System.lineSeparator() +
						"TT??TT???T?TTTT";
		assertEquals(expectedToStringRule15, eca3.toString());
		assertEquals(getFromFile("rule15.txt"), expectedToStringRule15);


	}

	private String getFromFile(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			StringBuilder builder = new StringBuilder();
			boolean first = true;

			String str = reader.readLine();

			while(str != null) {
				if (first) {
					first = false;
				} else {
					builder.append(System.lineSeparator());
				}
				builder.append(str);
				str = reader.readLine();
			}
			reader.close();

			return builder.toString();

		} catch (IOException ignored) {}
		return null;
	}
}
