package personal.gokul2411s.regular_automata;

public final class AutomatonFactory {

    public static Automaton<Character> automatonAcceptingAllChars() {
        Automaton.AutomatonBuilder<Character> outputBuilder =
                Automaton.<Character>builder()
                        .withNumStates(2)
                        .withInitialState(0)
                        .withFinalState(1);

        for (int i = 0; i < 65536; i++) {
            outputBuilder.withTransition(0, (char) i, 1);
        }
        return outputBuilder.build();
    }

    public static <Symbol> Automaton<Symbol> automatonAcceptingEmptyInput() {
        return Automaton.<Symbol>builder()
                .withNumStates(1)
                .withInitialState(0)
                .withFinalState(0)
                .withEpsilonTransition(0, 0)
                .build();
    }

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

    public static <Symbol> Automaton<Symbol> determinized(Automaton<Symbol> automaton) {
        return new Determinization<Symbol>().apply(automaton);
    }

    private AutomatonFactory() { }
}
