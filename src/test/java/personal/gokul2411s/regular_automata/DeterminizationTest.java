package personal.gokul2411s.regular_automata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static personal.gokul2411s.regular_automata.AutomatonFactory.*;

public class DeterminizationTest {

    @Test
    public void determinization_shouldReturnAutomatonWithNoEpsilonTransitions() {
        Automaton<Character> automaton =
                Automaton.<Character>builder()
                        .withNumStates(3)
                        .withInitialState(0)
                        .withFinalState(2)
                        .withEpsilonTransition(0, 1)
                        .withTransition(1, 'a', 2)
                        .build();

        Automaton<Character> determinizedAutomaton = determinized(automaton);

        assertThat(determinizedAutomaton.getEpsilonTransitions().size(), is(0));
    }

    @Test
    public void determinization_shouldWorkCorrectlyOnUnionedConcatenatenatedAutomata() {
        Automaton<Character> automaton =
                unioned(
                        automatonAcceptingSingleSymbol('a'),
                        concatenated(
                                automatonAcceptingSingleSymbol('b'),
                                automatonAcceptingSingleSymbol('c')));

        Automaton<Character> determinizedAutomaton = determinized(automaton);

        Character[] input1 = { 'a' };
        assertThat(determinizedAutomaton.accepts(input1), is(true));

        Character[] input2 = { 'b', 'c' };
        assertThat(determinizedAutomaton.accepts(input2), is(true));

        Character[] input3 = { 'b' };
        assertThat(determinizedAutomaton.accepts(input3), is(false));

        Character[] input4 = { 'c' };
        assertThat(determinizedAutomaton.accepts(input4), is(false));

        Character[] input5 = { 'a', 'c' };
        assertThat(determinizedAutomaton.accepts(input5), is(false));
    }

    @Test
    public void determinization_shouldWorkCorrectlyOnConcatenatenatedUnionedAutomata() {
        Automaton<Character> automaton =
                concatenated(
                        automatonAcceptingSingleSymbol('a'),
                        unioned(automatonAcceptingSingleSymbol('b'),
                                automatonAcceptingSingleSymbol('c')));

        Automaton<Character> determinizedAutomaton = determinized(automaton);

        Character[] input1 = { 'a', 'b' };
        assertThat(determinizedAutomaton.accepts(input1), is(true));

        Character[] input2 = { 'a', 'c' };
        assertThat(determinizedAutomaton.accepts(input2), is(true));

        Character[] input3 = { 'a' };
        assertThat(determinizedAutomaton.accepts(input3), is(false));

        Character[] input4 = { 'b' };
        assertThat(determinizedAutomaton.accepts(input4), is(false));

        Character[] input5 = { 'c' };
        assertThat(determinizedAutomaton.accepts(input5), is(false));
    }
}
