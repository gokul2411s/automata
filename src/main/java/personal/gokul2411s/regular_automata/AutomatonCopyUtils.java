package personal.gokul2411s.regular_automata;

import com.google.common.collect.Table;

import java.util.Map;
import java.util.Set;

public final class AutomatonCopyUtils {

    public static <Symbol> void addEpsilonTransitions(
            Automaton.AutomatonBuilder<Symbol> outputBuilder, Set<Integer> fromStates, int toState) {
        addEpsilonTransitions(outputBuilder, fromStates, toState, 0);
    }

    public static <Symbol> void addEpsilonTransitions(
            Automaton.AutomatonBuilder<Symbol> outputBuilder, Set<Integer> fromStates, int toState, int stateOffset) {
        for (int fromState : fromStates) {
            outputBuilder.withEpsilonTransition(fromState + stateOffset, toState);
        }
    }

    public static <Symbol> void copyTransitions(
            Automaton<Symbol> input, Automaton.AutomatonBuilder<Symbol> outputBuilder) {
        copyTransitions(input, outputBuilder, 0);
    }

    public static <Symbol> void copyTransitions(
            Automaton<Symbol> input, Automaton.AutomatonBuilder<Symbol> outputBuilder, int stateOffset) {
        for (Table.Cell<Integer, Symbol, Set<Integer>> cell : input.getTransitions().cellSet()) {
            int fromState = cell.getRowKey();
            Symbol s = cell.getColumnKey();
            for (int toState : cell.getValue()) {
                outputBuilder.withTransition(fromState + stateOffset, s, toState + stateOffset);
            }
        }
    }

    public static <Symbol> void copyEpsilonTransitions(
            Automaton<Symbol> input, Automaton.AutomatonBuilder<Symbol> outputBuilder) {
        copyEpsilonTransitions(input, outputBuilder, 0);
    }

    public static <Symbol> void copyEpsilonTransitions(
            Automaton<Symbol> input, Automaton.AutomatonBuilder<Symbol> outputBuilder, int stateOffset) {
        for (Map.Entry<Integer, Set<Integer>> entry : input.getEpsilonTransitions().entrySet()) {
            int fromState = entry.getKey();
            for (int toState : entry.getValue()) {
                outputBuilder.withEpsilonTransition(fromState + stateOffset, toState + stateOffset);
            }
        }
    }

    private AutomatonCopyUtils() { }
}
