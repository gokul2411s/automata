package personal.gokul2411s.regular_automata;

import com.google.common.collect.Table;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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

        for (int oldFinalState : input.getFinalStates()) {
            outputBuilder
                    .withEpsilonTransition(oldFinalState, newFinalState)
                    .withEpsilonTransition(oldFinalState, oldInitialState);
        }

        copyTransitions(input, outputBuilder);
        copyEpsilonTransitions(input, outputBuilder);
        return outputBuilder.build();
    }

    private void copyTransitions(Automaton<Symbol> input, Automaton.AutomatonBuilder<Symbol> outputBuilder) {
        for (Table.Cell<Integer, Symbol, Set<Integer>> cell : input.getTransitions().cellSet()) {
            int fromState = cell.getRowKey();
            Symbol s = cell.getColumnKey();
            for (int toState : cell.getValue()) {
                outputBuilder.withTransition(fromState, s, toState);
            }
        }
    }

    private void copyEpsilonTransitions(Automaton<Symbol> input, Automaton.AutomatonBuilder<Symbol> outputBuilder) {
        for (Map.Entry<Integer, Set<Integer>> entry : input.getEpsilonTransitions().entrySet()) {
            int fromState = entry.getKey();
            for (int toState : entry.getValue()) {
                outputBuilder.withEpsilonTransition(fromState, toState);
            }
        }
    }
}
