package personal.gokul2411s.regular_automata;

import com.google.common.collect.*;
import lombok.*;

import java.util.*;

/**
 * Represents a non-deterministic finite automaton.
 *
 * @param <State> Any type representin Symbol sg a state that has equals and hashCode defined.
 * @param <Symbol> Any type that has equals and hashCode defined.
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Automaton<State, Symbol> {

    @NonNull
    private final Set<State> states;

    @NonNull
    private final Table<State, Symbol, Set<State>> transitions;

    @NonNull
    private final Map<State, Set<State>> epsilonTransitions;

    @NonNull
    private final State initialState;

    @NonNull
    private final Set<State> finalStates;

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

        Set<State> currentStates = new HashSet<>();
        currentStates.addAll(epsilonClosure(initialState));

        for (Symbol symbol : input) {
            Set<State> nextStates = new HashSet<>();
            for (State currentState : currentStates) {
                for (State adjacentState : adjacentStates(currentState, symbol)) {
                    Set<State> adjacentStateEpsilonClosure = epsilonClosure(adjacentState);
                    nextStates.addAll(adjacentStateEpsilonClosure);
                    if (!Sets.intersection(adjacentStateEpsilonClosure, finalStates).isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private Set<State> epsilonClosure(State s) {
        Set<State> states = epsilonTransitions.getOrDefault(s, new HashSet<>());
        states.add(s);
        return states;
    }

    private Set<State> adjacentStates(State s, Symbol sym) {
        Set<State> out = transitions.get(s, sym);
        if (out == null) {
            out = new HashSet<>();
        }
        return out;
    }

    public static <State, Symbol> AutomatonBuilder<State, Symbol> builder() {
        return new AutomatonBuilder<>();
    }

    public static class AutomatonBuilder<State, Symbol> {

        private Set<State> states;
        private Table<State, Symbol, Set<State>> transitions;
        private Map<State, Set<State>> epsilonTransitions;
        private State initialState;
        private Set<State> finalStates;

        private AutomatonBuilder() {
            states = new HashSet<>();
            transitions = HashBasedTable.create();
            epsilonTransitions = new HashMap<>();
            finalStates = new HashSet<>();
            initialState = null;
        }

        public AutomatonBuilder<State, Symbol> withState(State s) {
            states.add(s);
            return this;
        }

        public AutomatonBuilder<State, Symbol> withInitialState(State s) {
            initialState = s;
            return this;
        }

        public AutomatonBuilder<State, Symbol> withFinalState(State s) {
            finalStates.add(s);
            return this;
        }

        public AutomatonBuilder<State, Symbol> withTransition(State from, Symbol s, State to) {
            Set<State> toStates = transitions.get(from, s);
            if (toStates == null) {
                toStates = new HashSet<>();
                transitions.put(from, s, toStates);
            }
            toStates.add(to);
            return this;
        }

        public AutomatonBuilder<State, Symbol> withEpsilonTransition(State from, State to) {
            Set<State> toStates = epsilonTransitions.get(from);
            if (toStates == null) {
                toStates = new HashSet<>();
                epsilonTransitions.put(from, toStates);
            }
            toStates.add(to);
            return this;
        }

        public Automaton<State, Symbol> build() {
            validate();
            return new Automaton<>(states, transitions, epsilonTransitions, initialState, finalStates);
        }

        void validate() throws InvalidAutomatonException {
            if (initialState == null) {
                throw new InvalidAutomatonException("Initial state not specified");
            }
            validateStateInUniverse(initialState);

            for (State s : finalStates) {
                validateStateInUniverse(s);
            }

            for (Table.Cell<State, Symbol, Set<State>> cell : transitions.cellSet()) {
                validateStateInUniverse(cell.getRowKey());
                for (State s : cell.getValue()) {
                    validateStateInUniverse(s);
                }
            }

            for (Map.Entry<State, Set<State>> entry : epsilonTransitions.entrySet()) {
                validateStateInUniverse(entry.getKey());
                for (State s : entry.getValue()) {
                    validateStateInUniverse(s);
                }
            }
        }

        void validateStateInUniverse(State s) {
            if (!states.contains(s)) {
                throw new InvalidAutomatonException("State " + s + " not in universe");
            }
        }
    }
}
