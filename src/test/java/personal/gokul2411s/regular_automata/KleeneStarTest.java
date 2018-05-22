package personal.gokul2411s.regular_automata;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KleeneStarTest {

    @Test
    public void kleeneStarredAutomaton_alwaysAcceptsEmptyInput() {
        Automaton<Character> originalAutomaton =
                Automaton.<Character>builder()
                        .withNumStates(1)
                        .withInitialState(0)
                        .withFinalState(0)
                        .withTransition(0, 'a', 0)
                        .build();

        Automaton<Character> kleeneStarredAutomaton = new KleeneStar<Character>().apply(originalAutomaton);

        Character[] input = { };
        assertThat(kleeneStarredAutomaton.accepts(input), is(true));
    }

    @Test
    public void kleeneStarredAutomaton_acceptsRepeatsOfSymbolListsInOriginalLanguage() {
        Automaton<Character> originalAutomaton =
                Automaton.<Character>builder()
                        .withNumStates(3)
                        .withInitialState(0)
                        .withFinalState(2)
                        .withTransition(0, 'a', 1)
                        .withTransition(1, 'b', 2)
                        .build();

        Automaton<Character> kleeneStarredAutomaton = new KleeneStar<Character>().apply(originalAutomaton);

        List<Character> originalAcceptedInput = new ArrayList<>();
        originalAcceptedInput.add('a');
        originalAcceptedInput.add('b');
        for (int i = 2; i < 1000; i++) {
            List<Character> input =
                    Collections.nCopies(i, originalAcceptedInput).stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
            assertThat(kleeneStarredAutomaton.accepts(input), is(true));
        }
    }

}
