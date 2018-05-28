package personal.gokul2411s.regular_automata;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RegexTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void emptyRegex_shouldAcceptEmptyInput() {
        Regex regex = Regex.builder().withPattern("").build();
        assertThat(regex.matches(""), is(true));
    }

    @Test
    public void singleCharRegex_shouldAcceptThatChar() {
        Regex regex = Regex.builder().withPattern("a").build();
        assertThat(regex.matches("a"), is(true));
        assertThat(regex.matches("b"), is(false));
        assertThat(regex.matches(""), is(false));
    }

    @Test
    public void literalStringRegex_shouldAcceptThatLiteralString() {
        Regex regex = Regex.builder().withPattern("abc").build();
        assertThat(regex.matches("abc"), is(true));
        assertThat(regex.matches("bac"), is(false));
        assertThat(regex.matches("ab"), is(false));
        assertThat(regex.matches("a"), is(false));
        assertThat(regex.matches(""), is(false));
    }

    @Test
    public void unmatchedGroupInRegex_shouldThrowException() {
        thrown.expect(InvalidRegexException.class);
        thrown.expectMessage(is("No matching group close for index 1"));
        Regex.builder().withPattern("a(bc").build();
    }

    @Test
    public void strayClosingGroupInRegex_shouldThrowException() {
        thrown.expect(InvalidRegexException.class);
        thrown.expectMessage(is("Stray closing group at index 2"));
        Regex.builder().withPattern("ab)c").build();
    }

    @Test
    public void strayOpeningGroupInRegex_shouldThrowException() {
        thrown.expect(InvalidRegexException.class);
        thrown.expectMessage(is("Stray opening group at index 3"));
        Regex.builder().withPattern("a(b(c)").build();
    }

    @Test
    public void kleeneStarWithNothingOnTheLeft_shouldThrowException() {
        thrown.expect(InvalidRegexException.class);
        thrown.expectMessage(is("No expression preceeds Kleene star at index 0"));
        Regex.builder().withPattern("*").build();
    }

    @Test
    public void kleeneStarImmediatelyAfterUnion_shouldThrowException() {
        thrown.expect(InvalidRegexException.class);
        thrown.expectMessage(is("No expression preceeds Kleene star at index 2"));
        Regex.builder().withPattern("a|*").build();
    }

    @Test
    public void nonQuantifiableExpressionBeforeKleeneStar_shouldThrowException() {
        thrown.expect(InvalidRegexException.class);
        thrown.expectMessage(is("Non-quantifiable expression preceeding Kleene star at index 2"));
        Regex.builder().withPattern(".**").build();
    }

    @Test
    public void concatenationShouldHavePrecedenceOverUnion() {
        Regex regex = Regex.builder().withPattern("a|bc").build();
        assertThat(regex.matches("a"), is(true));
        assertThat(regex.matches("bc"), is(true));
        assertThat(regex.matches("ac"), is(false));
    }

    @Test
    public void kleeneStarShouldHavePrecedenceOverConcatenation() {
        Regex regex = Regex.builder().withPattern("ac*").build();
        assertThat(regex.matches("a"), is(true));
        assertThat(regex.matches("ac"), is(true));
        assertThat(regex.matches("acc"), is(true));
        assertThat(regex.matches("acac"), is(false));
        assertThat(regex.matches(""), is(false));
    }

    @Test
    public void kleeneStarOnPreceedingGroup_shouldWork() {
        Regex regex = Regex.builder().withPattern("(abc)*").build();
        assertThat(regex.matches(""), is(true));
        assertThat(regex.matches("abc"), is(true));
        assertThat(regex.matches("abcabc"), is(true));
        assertThat(regex.matches("ab"), is(false));
        assertThat(regex.matches("abcd"), is(false));
    }

    @Test
    public void unionBetweenTwoGroups_shouldWork() {
        Regex regex = Regex.builder().withPattern("(abc)|(def)").build();
        assertThat(regex.matches("abc"), is(true));
        assertThat(regex.matches("def"), is(true));
        assertThat(regex.matches("aef"), is(false));
        assertThat(regex.matches("abf"), is(false));
    }

    @Test
    public void multipleUnions_shouldWork() {
        Regex regex = Regex.builder().withPattern("a|b|c|d").build();
        assertThat(regex.matches("a"), is(true));
        assertThat(regex.matches("b"), is(true));
        assertThat(regex.matches("c"), is(true));
        assertThat(regex.matches("d"), is(true));
        assertThat(regex.matches("e"), is(false));
        assertThat(regex.matches(""), is(false));
    }

    @Test
    public void unionWithNothingOnOneSide_shouldAcceptEmptyInputAndOtherSide() {
        Regex regex1 = Regex.builder().withPattern("|b").build();
        assertThat(regex1.matches(""), is(true));
        assertThat(regex1.matches("b"), is(true));

        Regex regex2 = Regex.builder().withPattern("a|").build();
        assertThat(regex2.matches("a"), is(true));
        assertThat(regex2.matches(""), is(true));
    }

    @Test
    public void multipleKleeneStars_shouldWork() {
        Regex regex = Regex.builder().withPattern("a*b*c*").build();
        assertThat(regex.matches(""), is(true));
        assertThat(regex.matches("a"), is(true));
        assertThat(regex.matches("b"), is(true));
        assertThat(regex.matches("c"), is(true));
        assertThat(regex.matches("ab"), is(true));
        assertThat(regex.matches("ac"), is(true));
        assertThat(regex.matches("bc"), is(true));
        assertThat(regex.matches("abc"), is(true));
        assertThat(regex.matches("abb"), is(true));
        assertThat(regex.matches("acc"), is(true));
        assertThat(regex.matches("aab"), is(true));
        assertThat(regex.matches("aac"), is(true));
    }

    @Test
    public void dotOperator_shouldWork() {
        Regex regex = Regex.builder().withPattern(".*a*").build();
        assertThat(regex.matches(""), is(true));
        assertThat(regex.matches("a"), is(true));
        assertThat(regex.matches("aa"), is(true));
        for (int i = 0; i < 65536; i++) {
            String base = String.valueOf((char) i);
            assertThat(regex.matches(base), is(true));
            assertThat(regex.matches(base + "a"), is(true));
            assertThat(regex.matches(base + "aa"), is(true));
        }
    }
}
