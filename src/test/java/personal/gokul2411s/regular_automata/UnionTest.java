package personal.gokul2411s.regular_automata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UnionTest {

    @Test
    public void unionedAutomaton_shouldAcceptUnionOfTheTwoInputLanguages() {
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

        Automaton<Character> unionedAutomaton = new Union<Character>().apply(firstAutomaton, secondAutomaton);

        Character[] input1 = { 'a' };
        assertThat(unionedAutomaton.accepts(input1), is(true));

        Character[] input2 = { 'b' };
        assertThat(unionedAutomaton.accepts(input2), is(true));
    }
}
