package personal.gokul2411s.regular_automata;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static personal.gokul2411s.regular_automata.AutomatonFactory.automatonAcceptingSingleSymbol;
import static personal.gokul2411s.regular_automata.AutomatonFactory.concatenated;
import static personal.gokul2411s.regular_automata.AutomatonFactory.kleeneStarred;

public class KleeneStarTest {

    @Test
    public void kleeneStarredAutomaton_alwaysAcceptsEmptyInput() {
        Automaton<Character> originalAutomaton = automatonAcceptingSingleSymbol('a');

        Automaton<Character> kleeneStarredAutomaton = kleeneStarred(originalAutomaton);

        Character[] input = { };
        assertThat(kleeneStarredAutomaton.accepts(input), is(true));
    }

    @Test
    public void kleeneStarredAutomaton_acceptsRepeatsOfSymbolListsInOriginalLanguage() {
        Automaton<Character> originalAutomaton =
                concatenated(automatonAcceptingSingleSymbol('a'), automatonAcceptingSingleSymbol('b'));

        Automaton<Character> kleeneStarredAutomaton = kleeneStarred(originalAutomaton);

        List<Character> originalAcceptedInput = new ArrayList<>();
        originalAcceptedInput.add('a');
        originalAcceptedInput.add('b');
        for (int i = 1; i < 1000; i++) {
            List<Character> input =
                    Collections.nCopies(i, originalAcceptedInput).stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
            assertThat(kleeneStarredAutomaton.accepts(input), is(true));
        }
    }

}
