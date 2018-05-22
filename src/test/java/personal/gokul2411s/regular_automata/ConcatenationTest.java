package personal.gokul2411s.regular_automata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static personal.gokul2411s.regular_automata.AutomatonFactory.automatonAcceptingSingleSymbol;
import static personal.gokul2411s.regular_automata.AutomatonFactory.concatenated;

public class ConcatenationTest {

    @Test
    public void concatenatedAutomaton_shouldAcceptConcatenationOfTheTwoInputLanguages() {
        Automaton<Character> firstAutomaton = automatonAcceptingSingleSymbol('a');
        Automaton<Character> secondAutomaton = automatonAcceptingSingleSymbol('b');

        Automaton<Character> concatenatedAutomaton = concatenated(firstAutomaton, secondAutomaton);

        Character[] input1 = { 'a' };
        assertThat(concatenatedAutomaton.accepts(input1), is(false));

        Character[] input2 = { 'b' };
        assertThat(concatenatedAutomaton.accepts(input2), is(false));

        Character[] input3 = { 'a', 'b' };
        assertThat(concatenatedAutomaton.accepts(input3), is(true));
    }
}
