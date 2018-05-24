package personal.gokul2411s.regular_automata;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a non-deterministic finite automaton.
 *
 * @param <Symbol> Any type that has equals and hashCode defined.
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Automaton<Symbol> {

    @NonNull
    private final int numStates;

    @NonNull
    private final Table<Integer, Symbol, Set<Integer>> transitions;

    @NonNull
    private final Map<Integer, Set<Integer>> epsilonTransitions;

    @NonNull
    private final int initialState;

    @NonNull
    private final Set<Integer> finalStates;

    /**
     * Accepts an array of symbols and returns true indicating acceptance and false rejection.
     */
    public boolean accepts(Symbol[] input) {
        return accepts(Arrays.asList(input));
    }

    /**
     * Accepts an iterable of symbols and returns true indicating acceptance and false rejection.
     */
    public boolean accepts(Iterable<Symbol> input) {

        Set<Integer> currentStates = new HashSet<>();
        currentStates.addAll(epsilonClosure(initialState));

        for (Symbol symbol : input) {
            if (currentStates.isEmpty()) {
                return false;
            }
            Set<Integer> nextStates =
                    currentStates.stream()
                            .map(s -> adjacentStates(s, symbol))
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet());
            currentStates = epsilonClosure(nextStates);
        }
        return reachedFinalState(currentStates);
    }

    private boolean reachedFinalState(Set<Integer> currentStates) {
        return !Sets.intersection(currentStates, finalStates).isEmpty();
    }

    /**
     * Returns the epsilon closure of a given state.
     */
    public Set<Integer> epsilonClosure(int state) {
        Set<Integer> states = new HashSet<>();
        states.add(state);
        return epsilonClosure(states);
    }

    /**
     * Returns the epsilon closure of a given set of states.
     */
    public Set<Integer> epsilonClosure(Set<Integer> states) {
        Queue<Integer> bfsStates = new LinkedList<>();
        for (int state : states) {
            bfsStates.add(state);
        }
        Set<Integer> visited = new HashSet<>();
        while (!bfsStates.isEmpty()) {
            int nextState = bfsStates.remove();
            if (visited.contains(nextState)) {
                continue;
            }
            visited.add(nextState);
            bfsStates.addAll(epsilonTransitions.getOrDefault(nextState, new HashSet<>()));
        }
        return visited;
    }

    private Set<Integer> adjacentStates(int s, Symbol sym) {
        Set<Integer> out = transitions.get(s, sym);
        if (out == null) {
            out = new HashSet<>();
        }
        return out;
    }

    public Map<Symbol, Set<Integer>> stateTransitions(int state) {
        return transitions.row(state);
    }

    public static <Symbol> AutomatonBuilder<Symbol> builder() {
        return new AutomatonBuilder<>();
    }

    public static class AutomatonBuilder<Symbol> {

        private int numStates;
        private Table<Integer, Symbol, Set<Integer>> transitions;
        private Map<Integer, Set<Integer>> epsilonTransitions;
        private int initialState;
        private Set<Integer> finalStates;

        private AutomatonBuilder() {
            transitions = HashBasedTable.create();
            epsilonTransitions = new HashMap<>();
            finalStates = new HashSet<>();
        }

        public AutomatonBuilder<Symbol> withNumStates(int numStates) {
            this.numStates = numStates;
            return this;
        }

        public AutomatonBuilder<Symbol> withInitialState(int s) {
            initialState = s;
            return this;
        }

        public AutomatonBuilder<Symbol> withFinalState(int s) {
            finalStates.add(s);
            return this;
        }

        public AutomatonBuilder<Symbol> withFinalStates(Set<Integer> finalStates) {
            Preconditions.checkNotNull(finalStates);
            this.finalStates = finalStates;
            return this;
        }

        public AutomatonBuilder<Symbol> withTransition(int from, Symbol s, int to) {
            Set<Integer> toStates = transitions.get(from, s);
            if (toStates == null) {
                toStates = new HashSet<>();
                transitions.put(from, s, toStates);
            }
            toStates.add(to);
            return this;
        }

        public AutomatonBuilder<Symbol> withTransitions(Table<Integer, Symbol, Set<Integer>> transitions) {
            Preconditions.checkNotNull(transitions);
            for (Table.Cell<Integer, Symbol, Set<Integer>> cell : transitions.cellSet()) {
                Preconditions.checkNotNull(cell.getValue());
            }
            this.transitions = transitions;
            return this;
        }

        public AutomatonBuilder<Symbol> withEpsilonTransition(int from, int to) {
            Set<Integer> toStates = epsilonTransitions.get(from);
            if (toStates == null) {
                toStates = new HashSet<>();
                epsilonTransitions.put(from, toStates);
            }
            toStates.add(to);
            return this;
        }

        public Automaton<Symbol> build() {
            validate();
            return new Automaton<>(numStates, transitions, epsilonTransitions, initialState, finalStates);
        }

        void validate() throws InvalidAutomatonException {
            validateStateInUniverse(initialState);

            for (int s : finalStates) {
                validateStateInUniverse(s);
            }

            for (Table.Cell<Integer, Symbol, Set<Integer>> cell : transitions.cellSet()) {
                validateStateInUniverse(cell.getRowKey());
                for (int s : cell.getValue()) {
                    validateStateInUniverse(s);
                }
            }

            for (Map.Entry<Integer, Set<Integer>> entry : epsilonTransitions.entrySet()) {
                validateStateInUniverse(entry.getKey());
                for (int s : entry.getValue()) {
                    validateStateInUniverse(s);
                }
            }
        }

        void validateStateInUniverse(int s) {
            if (s < 0 || s >= numStates) {
                throw new InvalidAutomatonException("State " + s + " not in universe");
            }
        }
    }
}
