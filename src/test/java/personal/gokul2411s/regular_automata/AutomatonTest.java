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
    public void ifNoStartState_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Integer, Character>builder().build();
    }

    @Test
    public void ifInvalidStartState_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Integer, Character>builder()
                .withState(1)
                .withInitialState(2)
                .build();
    }

    @Test
    public void ifInvalidFinalState_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Integer, Character>builder()
                .withState(1)
                .withInitialState(1)
                .withFinalState(2)
                .build();
    }

    @Test
    public void ifInvalidFromStateInTransition_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Integer, Character>builder()
                .withState(1)
                .withInitialState(1)
                .withFinalState(1)
                .withTransition(2, 'a', 1)
                .build();
    }

    @Test
    public void ifInvalidFromStateInEpsilonTransition_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Integer, Character>builder()
                .withState(1)
                .withInitialState(1)
                .withFinalState(1)
                .withEpsilonTransition(2, 1)
                .build();
    }


    @Test
    public void ifInvalidToStateInTransition_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Integer, Character>builder()
                .withState(1)
                .withInitialState(1)
                .withFinalState(1)
                .withTransition(1, 'a', 2)
                .build();
    }

    @Test
    public void ifInvalidToStateInEpsilonTransition_builder_shouldThrowException() {

        thrown.expect(InvalidAutomatonException.class);
        Automaton.<Integer, Character>builder()
                .withState(1)
                .withInitialState(1)
                .withFinalState(1)
                .withEpsilonTransition(1, 2)
                .build();
    }

    @Test
    public void singleNonFinalStateAutomaton_shouldAcceptNothing() {

        Automaton<Integer, Character> automaton =
                Automaton.<Integer, Character>builder()
                        .withState(1)
                        .withInitialState(1)
                        .withTransition(1, 'a', 1)
                        .build();

        Character[] input = { 'a' };
        assertThat(automaton.accepts(input), is(false));
    }


    @Test
    public void singleFinalStateAutomaton_shouldAcceptInputIfMatchesTransitions() {

        Automaton<Integer, Character> automaton =
                Automaton.<Integer, Character>builder()
                        .withState(1)
                        .withInitialState(1)
                        .withFinalState(1)
                        .withTransition(1, 'a', 1)
                        .withTransition(1, 'b', 1)
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
    public void automationJumpsThroughEpsilonTransitions() {

        Automaton<Integer, Character> automaton =
                Automaton.<Integer, Character>builder()
                        .withState(1)
                        .withState(2)
                        .withState(3)
                        .withState(4)
                        .withInitialState(1)
                        .withFinalState(4)
                        .withEpsilonTransition(1, 2)
                        .withEpsilonTransition(1, 3)
                        .withTransition(2, 'a', 3)
                        .withTransition(3, 'b', 4)
                        .build();

        Character[] input1 = { 'a', 'b' };
        assertThat(automaton.accepts(input1), is(true));


        Character[] input2 = { 'b' };
        assertThat(automaton.accepts(input2), is(true));


        Character[] input3 = { 'a' };
        assertThat(automaton.accepts(input3), is(false));

        assertThat(automaton.accepts(Arrays.asList()), is(false));
    }
}
