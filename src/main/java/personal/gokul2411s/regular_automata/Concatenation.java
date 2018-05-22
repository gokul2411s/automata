package personal.gokul2411s.regular_automata;

import java.util.function.BiFunction;

import static personal.gokul2411s.regular_automata.AutomatonCopyUtils.*;

public class Concatenation<Symbol> implements BiFunction<Automaton<Symbol>, Automaton<Symbol>, Automaton<Symbol>> {

    @Override
    public Automaton<Symbol> apply(Automaton<Symbol> firstAutomaton, Automaton<Symbol> secondAutomaton) {

        int numStatesInFirst = firstAutomaton.getNumStates();
        int newNumStates = numStatesInFirst + secondAutomaton.getNumStates() + 2;
        int newInitialState = newNumStates - 2;
        int newFinalState = newNumStates - 1;

        Automaton.AutomatonBuilder<Symbol> outputBuilder =
                Automaton.<Symbol>builder()
                        .withNumStates(newNumStates)
                        .withInitialState(newInitialState)
                        .withFinalState(newFinalState)
                        .withEpsilonTransition(newInitialState, firstAutomaton.getInitialState());

        addEpsilonTransitions(
                outputBuilder,
                firstAutomaton.getFinalStates(),
                secondAutomaton.getInitialState() + numStatesInFirst);
        copyTransitions(firstAutomaton, outputBuilder);
        copyEpsilonTransitions(firstAutomaton, outputBuilder);

        addEpsilonTransitions(outputBuilder, secondAutomaton.getFinalStates(), newFinalState, numStatesInFirst);
        copyTransitions(secondAutomaton, outputBuilder, numStatesInFirst);
        copyEpsilonTransitions(secondAutomaton, outputBuilder, numStatesInFirst);

        return outputBuilder.build();
    }
}
