package personal.gokul2411s.regular_automata;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AutomatonTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void ifInvalidStartState_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Character>builder()
                .withNumStates(1)
                .withInitialState(1)
                .build();
    }

    @Test
    public void ifInvalidFinalState_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Character>builder()
                .withNumStates(1)
                .withFinalState(1)
                .build();
    }

    @Test
    public void ifInvalidFromStateInTransition_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Character>builder()
                .withNumStates(1)
                .withTransition(1, 'a', 0)
                .build();
    }

    @Test
    public void ifInvalidFromStateInEpsilonTransition_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Character>builder()
                .withNumStates(1)
                .withEpsilonTransition(1, 0)
                .build();
    }

    @Test
    public void ifInvalidToStateInTransition_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Character>builder()
                .withNumStates(1)
                .withTransition(0, 'a', 1)
                .build();
    }

    @Test
    public void ifInvalidToStateInEpsilonTransition_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Character>builder()
                .withNumStates(1)
                .withEpsilonTransition(0, 1)
                .build();
    }

    @Test
    public void singleNonFinalStateAutomaton_shouldAcceptNothing() {

        Automaton<Character> automaton =
                Automaton.<Character>builder()
                        .withNumStates(1)
                        .withTransition(0, 'a', 0)
                        .build();

        Character[] input = { 'a' };
        assertThat(automaton.accepts(input), is(false));
    }

    @Test
    public void singleFinalStateAutomaton_shouldAcceptInputIfMatchesTransitions() {

        Automaton<Character> automaton =
                Automaton.<Character>builder()
                        .withNumStates(1)
                        .withFinalState(0)
                        .withTransition(0, 'a', 0)
                        .withTransition(0, 'b', 0)
                        .build();

        Character[] input1 = { 'a' };
        assertThat(automaton.accepts(input1), is(true));

        Character[] input2 = { 'b' };
        assertThat(automaton.accepts(input2), is(true));

        Character[] input3 = { 'a', 'b' };
        assertThat(automaton.accepts(input3), is(true));

        Character[] input4 = { 'b', 'a' };
        assertThat(automaton.accepts(input4), is(true));
    }

    @Test
    public void automaton_shouldNotAcceptPartialInput() {

        Automaton<Character> automaton =
                Automaton.<Character>builder()
                        .withNumStates(3)
                        .withInitialState(0)
                        .withFinalState(2)
                        .withTransition(0, 'a', 1)
                        .withTransition(1, 'b', 2)
                        .build();

        Character[] input = { 'a' };
        assertThat(automaton.accepts(input), is(false));
    }

    @Test
    public void automationJumpsThroughEpsilonTransitions() {

        Automaton<Character> automaton =
                Automaton.<Character>builder()
                        .withNumStates(4)
                        .withFinalState(3)
                        .withEpsilonTransition(0, 1)
                        .withEpsilonTransition(1, 2)
                        .withTransition(2, 'b', 3)
                        .build();

        Character[] input2 = { 'b' };
        assertThat(automaton.accepts(input2), is(true));

        assertThat(automaton.accepts(Arrays.asList()), is(false));
    }
}
