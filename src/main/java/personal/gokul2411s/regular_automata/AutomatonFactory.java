package personal.gokul2411s.regular_automata;

public final class AutomatonFactory {

    public static <Symbol> Automaton<Symbol> automatonAcceptingSingleSymbol(Symbol symbol) {
        return Automaton.<Symbol>builder()
                .withNumStates(2)
                .withInitialState(0)
                .withFinalState(1)
                .withTransition(0, symbol, 1)
                .build();
    }

    public static <Symbol> Automaton<Symbol> kleeneStarred(Automaton<Symbol> automaton) {
        return new KleeneStar<Symbol>().apply(automaton);
    }

    public static <Symbol> Automaton<Symbol> unioned(Automaton<Symbol> first, Automaton<Symbol> second) {
        return new Union<Symbol>().apply(first, second);
    }

    public static <Symbol> Automaton<Symbol> concatenated(Automaton<Symbol> first, Automaton<Symbol> second) {
        return new Concatenation<Symbol>().apply(first, second);
    }

    private AutomatonFactory() { }
}