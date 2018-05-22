package personal.gokul2411s.regular_automata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static personal.gokul2411s.regular_automata.AutomatonFactory.automatonAcceptingSingleSymbol;
import static personal.gokul2411s.regular_automata.AutomatonFactory.unioned;

public class UnionTest {

    @Test
    public void unionedAutomaton_shouldAcceptUnionOfTheTwoInputLanguages() {
        Automaton<Character> firstAutomaton = automatonAcceptingSingleSymbol('a');
        Automaton<Character> secondAutomaton = automatonAcceptingSingleSymbol('b');

        Automaton<Character> unionedAutomaton = unioned(firstAutomaton, secondAutomaton);

        Character[] input1 = { 'a' };
        assertThat(unionedAutomaton.accepts(input1), is(true));

        Character[] input2 = { 'b' };
        assertThat(unionedAutomaton.accepts(input2), is(true));
    }
}
