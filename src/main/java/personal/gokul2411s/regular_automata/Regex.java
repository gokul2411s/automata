package personal.gokul2411s.regular_automata;

import com.google.common.base.Preconditions;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static personal.gokul2411s.regular_automata.AutomatonFactory.*;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Regex {

    @NonNull
    private final String pattern;

    @NonNull
    private final Automaton<Character> compiledAutomaton;

    public boolean matches(String input) {
        return compiledAutomaton.accepts(input.chars().mapToObj(c -> (char)c).toArray(Character[]::new));
    }

    public static RegexBuilder builder() {
        return new RegexBuilder();
    }

    public static class RegexBuilder {

        private String pattern;

        public RegexBuilder withPattern(String pattern) {
            Preconditions.checkNotNull(pattern);
            this.pattern = pattern;
            return this;
        }

        public Regex build() {
            return new Regex(pattern, determinized(automaton(0, pattern.length())));
        }

        private Automaton<Character> automaton(
                int startIndex /* inclusive */,
                int endIndex /* exclusive */) {
            int index = startIndex;
            List<Automaton<Character>> concatenatedAutomata = new ArrayList<>();
            List<Automaton<Character>> currentAutomata = new ArrayList<>();
            boolean lastCharacterUnionOp = false;
            while (index < endIndex) {
                char charAtIndex = pattern.charAt(index);
                lastCharacterUnionOp = false;
                if (isGroupEnding(charAtIndex)) {
                    throw new InvalidRegexException("Stray closing group at index " + index);
                } else if (isGroupBeginning(charAtIndex)) {
                    Pair<Automaton<Character>, Integer> out = automatonForGroup(index);
                    currentAutomata.add(out.getFirst());
                    index = out.getSecond();
                } else if (isUnionOperator(charAtIndex)) {
                    if (currentAutomata.isEmpty()) {
                        throw new InvalidRegexException("No expression preceeds union at index " + index);
                    }
                    concatenatedAutomata.add(concatenated(currentAutomata));
                    currentAutomata.clear();
                    index++;
                    lastCharacterUnionOp = true;
                } else if (isKleeneStarOperator(charAtIndex)) {
                    if (currentAutomata.isEmpty()) {
                        throw new InvalidRegexException("No expression preceeds Kleene star at index " + index);
                    }
                    currentAutomata.add(kleeneStarred(currentAutomata.remove(currentAutomata.size() - 1)));
                    index++;
                } else {
                    currentAutomata.add(automatonAcceptingSingleSymbol(charAtIndex));
                    index++;
                }
            }

            if (lastCharacterUnionOp) {
                throw new InvalidRegexException("No expression follows union at index " + (index - 1));
            }

            if (!currentAutomata.isEmpty()) {
                concatenatedAutomata.add(concatenated(currentAutomata));
                currentAutomata.clear();
            }

            if (concatenatedAutomata.isEmpty()) {
                return automatonAcceptingEmptyInput();
            } else {
                return unioned(concatenatedAutomata);
            }
        }

        private static Automaton<Character> concatenated(List<Automaton<Character>> automata) {
            Automaton<Character> out = automata.get(0);
            boolean first = true;
            for (Automaton<Character> automaton : automata) {
                if (first) {
                    first = false;
                } else {
                    out = AutomatonFactory.concatenated(out, automaton);
                }
            }
            return out;
        }

        private static Automaton<Character> unioned(List<Automaton<Character>> automata) {
            Automaton<Character> out = automata.get(0);
            boolean first = true;
            for (Automaton<Character> automaton : automata) {
                if (first) {
                    first = false;
                } else {
                    out = AutomatonFactory.unioned(out, automaton);
                }
            }
            return out;
        }

        private Pair<Automaton<Character>, Integer> automatonForGroup(int groupStartIndex) {
            int index = groupStartIndex + 1;
            boolean foundClose = false;
            while (index < pattern.length()) {
                char charAtIndex = pattern.charAt(index);
                if (isGroupBeginning(charAtIndex)) {
                    throw new InvalidRegexException("Stray opening group at index " + index);
                }
                index++;
                if (isGroupEnding(charAtIndex)) {
                    foundClose = true;
                    break;
                }
            }
            if (!foundClose) {
                throw new InvalidRegexException("No matching group close for index " + groupStartIndex);
            }
            return new Pair<>(automaton(groupStartIndex + 1, index - 1), index);
        }

        private static boolean isSimpleChar(char c) {
            return true
                    && !isUnionOperator(c)
                    && !isKleeneStarOperator(c)
                    && !isUnionOperator(c)
                    && !isGroupBeginning(c)
                    && !isGroupEnding(c);
        }

        private static boolean isUnionOperator(char c) {
            return c == '|';
        }

        private static boolean isKleeneStarOperator(char c) {
            return c == '*';
        }

        private static boolean isGroupBeginning(char c) {
            return c == '(';
        }

        private static boolean isGroupEnding(char c) {
            return c == ')';
        }

        @Value
        private static class Pair<A, B> {

            @NonNull
            private final A first;

            @NonNull
            private final B second;
        }
    }



}
