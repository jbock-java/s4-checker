package uppu.parse;

import io.jbock.util.Either;
import io.parmigiano.Permutation;

import java.util.Arrays;
import java.util.regex.Pattern;

import static io.jbock.util.Either.left;
import static io.jbock.util.Either.right;

public final class TokenParser {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Either<String, Permutation> IDENTITY = right(Permutation.identity());

    static Either<String, Permutation> parseContent(String content) {
        return parseTrimmed(content.trim());
    }

    private static Either<String, Permutation> parseTrimmed(String content) {
        if (content.isEmpty()) {
            return IDENTITY;
        }
        String[] tokens = WHITESPACE.split(content);
        int[] input = new int[tokens.length];
        if (tokens.length == 0) {
            return IDENTITY;
        }
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            switch (token) {
                case "0":
                    input[i] = 0;
                    break;
                case "1":
                    input[i] = 1;
                    break;
                case "2":
                    input[i] = 2;
                    break;
                case "3":
                    input[i] = 3;
                    break;
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    return left("Only numbers 0, 1, 2 and 3 are allowed! But found: " + token);
                default:
                    return left("Unknown token: " + token);
            }
        }
        if (input.length <= 1) {
            return left("Short cycle");
        }
        return right(Permutation.cycle(input[0], input[1], Arrays.copyOfRange(input, 2, input.length)));
    }

    private TokenParser() {
    }
}