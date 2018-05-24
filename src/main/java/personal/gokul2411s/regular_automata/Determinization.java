package personal.gokul2411s.regular_automata;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Determinization<Symbol> implements Function<Automaton<Symbol>, Automaton<Symbol>> {

    @Override
    public Automaton<Symbol> apply(Automaton<Symbol> automaton) {

        Table<Integer, Symbol, Set<Integer>> compositeTransitions = HashBasedTable.create();

        Set<Integer> compositeStartState = automaton.epsilonClosure(automaton.getInitialState());

        Comparator<Set<Integer>> comparator = new SetComparator();
        Map<Set<Integer>, Integer> compositeStates = new TreeMap<>(comparator);
        compositeStates.put(compositeStartState, 0);

        // Start BFS on composite states.
        Queue<Set<Integer>> bfsCompositeStates = new LinkedList<>();
        bfsCompositeStates.add(compositeStartState);
        Set<Set<Integer>> visitedCompositeStates = new TreeSet<>(comparator);

        while (!bfsCompositeStates.isEmpty()) {
            Set<Integer> compositeState = bfsCompositeStates.remove();
            if (visitedCompositeStates.contains(compositeState)) {
                continue;
            }
            visitedCompositeStates.add(compositeState);

            Map<Symbol, Set<Integer>> compositeStateTransitions = new HashMap<>();
            for (int state : compositeState) {
                Map<Symbol, Set<Integer>> stateTransitions = automaton.stateTransitions(state);
                for (Map.Entry<Symbol, Set<Integer>> entry : stateTransitions.entrySet()) {
                    Symbol sym = entry.getKey();
                    Set<Integer> compositeStateTransitionsForSymbol = compositeStateTransitions.get(sym);
                    if (compositeStateTransitionsForSymbol == null) {
                        compositeStateTransitionsForSymbol = new HashSet<>();
                        compositeStateTransitions.put(sym, compositeStateTransitionsForSymbol);
                    }
                    compositeStateTransitionsForSymbol.addAll(entry.getValue());
                }
            }

            int compositeStateId = compositeStates.get(compositeState);
            for (Map.Entry<Symbol, Set<Integer>> entry : compositeStateTransitions.entrySet()) {
                Set<Integer> destinationCompositeState = automaton.epsilonClosure(entry.getValue());
                Integer destinationCompositeStateId = compositeStates.get(destinationCompositeState);
                if (destinationCompositeStateId == null) {
                    destinationCompositeStateId = compositeStates.size();
                    compositeStates.put(destinationCompositeState, destinationCompositeStateId);
                }
                compositeTransitions.put(
                        compositeStateId, entry.getKey(), Sets.newHashSet(destinationCompositeStateId));
                bfsCompositeStates.add(destinationCompositeState);
            }
        }

        Set<Integer> finalStates = automaton.getFinalStates();
        Set<Integer> compositeFinalStateIds =
                compositeStates.entrySet().stream()
                        .filter(e -> !Sets.intersection(e.getKey(), finalStates).isEmpty())
                        .map(e -> e.getValue())
                        .collect(Collectors.toSet());

        return Automaton.<Symbol>builder()
                .withNumStates(compositeStates.size())
                .withInitialState(0)
                .withFinalStates(compositeFinalStateIds)
                .withTransitions(compositeTransitions)
                .build();
    }

    private static class SetComparator implements Comparator<Set<Integer>> {

        @Override
        public int compare(Set<Integer> s1, Set<Integer> s2) {

            Iterator<Integer> it1 = s1.iterator();
            Iterator<Integer> it2 = s2.iterator();

            while (it1.hasNext() && it2.hasNext()) {
                int next1 = it1.next();
                int next2 = it2.next();

                if (next1 < next2) {
                    return -1;
                } else if (next1 > next2) {
                    return 1;
                }
            }

            if (!it1.hasNext() && it2.hasNext()) {
                return -1;
            } else if (it1.hasNext() && !it2.hasNext()) {
                return 1;
            }
            return 0;
        }
    }
}
