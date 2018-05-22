package personal.gokul2411s.regular_automata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcatenationTest {

    @Test
    public void concatenatedAutomaton_shouldAcceptConcatenationOfTheTwoInputLanguages() {
        Automaton<Character> firstAutomaton =
                Automaton.<Character>builder()
                        .withNumStates(2)
                        .withInitialState(0)
                        .withFinalState(1)
                        .withTransition(0, 'a', 1)
                        .build();

        Automaton<Character> secondAutomaton =
                Automaton.<Character>builder()
                        .withNumStates(2)
                        .withInitialState(0)
                        .withFinalState(1)
                        .withTransition(0, 'b', 1)
                        .build();

        Automaton<Character> concatenatedAutomaton =
                new Concatenation<Character>().apply(firstAutomaton, secondAutomaton);

        Character[] input1 = { 'a' };
        assertThat(concatenatedAutomaton.accepts(input1), is(false));

        Character[] input2 = { 'b' };
        assertThat(concatenatedAutomaton.accepts(input2), is(false));

        Character[] input3 = { 'a', 'b' };
        assertThat(concatenatedAutomaton.accepts(input3), is(true));
    }
}
