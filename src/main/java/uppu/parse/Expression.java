package uppu.parse;

import io.jbock.util.Either;

import static io.jbock.util.Either.right;
import static uppu.parse.TokenParser.parseContent;

sealed interface Expression permits 
        Expression.DotExpression, 
        Expression.HomeExpression,
        Expression.ParenExpression {

    DotExpression DOT_EXPRESSION = new DotExpression();

    HomeExpression HOME_EXPRESSION = new HomeExpression();

    Either<String, Parsed> parse();

    final class DotExpression implements Expression {

        private DotExpression() {
        }

        @Override
        public String toString() {
            return ".";
        }

        @Override
        public Either<String, Parsed> parse() {
            return right(Parsed.PARSED_DOT);
        }
    }

    final class HomeExpression implements Expression {

        private HomeExpression() {
        }

        @Override
        public String toString() {
            return "~";
        }

        @Override
        public Either<String, Parsed> parse() {
            return right(Parsed.PARSED_HOME);
        }
    }

    record ParenExpression(String token) implements Expression {

        static ParenExpression parenExpression(String token) {
            return new ParenExpression(token);
        }

        @Override
        public String toString() {
            return token;
        }

        @Override
        public Either<String, Parsed> parse() {
            return parseContent(token).map(Parsed.ParsedToken::new);
        }
    }
}
