package personal.gokul2411s.regular_automata;

import java.util.Set;
import java.util.function.Function;

import static personal.gokul2411s.regular_automata.AutomatonCopyUtils.addEpsilonTransitions;
import static personal.gokul2411s.regular_automata.AutomatonCopyUtils.copyEpsilonTransitions;
import static personal.gokul2411s.regular_automata.AutomatonCopyUtils.copyTransitions;

public class KleeneStar<Symbol> implements Function<Automaton<Symbol>, Automaton<Symbol>> {

    @Override
    public Automaton<Symbol> apply(Automaton<Symbol> input) {

        int oldInitialState = input.getInitialState();

        int newNumStates = input.getNumStates() + 2; // one extra input state and one final state.
        int newInitialState = newNumStates - 2;
        int newFinalState = newNumStates - 1;

        Automaton.AutomatonBuilder<Symbol> outputBuilder =
                Automaton.<Symbol>builder()
                        .withNumStates(newNumStates)
                        .withInitialState(newInitialState)
                        .withFinalState(newFinalState)
                        .withEpsilonTransition(newInitialState, newFinalState)
                        .withEpsilonTransition(newInitialState, oldInitialState);

        Set<Integer> oldFinalStates = input.getFinalStates();
        addEpsilonTransitions(outputBuilder, oldFinalStates, newFinalState);
        addEpsilonTransitions(outputBuilder, oldFinalStates, oldInitialState);
        copyTransitions(input, outputBuilder);
        copyEpsilonTransitions(input, outputBuilder);
        return outputBuilder.build();
    }
}
