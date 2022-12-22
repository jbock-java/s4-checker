package uppu.parse;

import io.parmigiano.Permutation;

sealed interface Parsed permits Parsed.ParsedDot, Parsed.ParsedHome, Parsed.ParsedToken {

    ParsedDot PARSED_DOT = new ParsedDot();

    ParsedHome PARSED_HOME = new ParsedHome();

    boolean isDot();

    boolean isHome();

    final class ParsedDot implements Parsed {


        private ParsedDot() {
        }

        @Override
        public String toString() {
            return ".";
        }

        @Override
        public boolean isDot() {
            return true;
        }

        @Override
        public boolean isHome() {
            return false;
        }
    }

    final class ParsedHome implements Parsed {

        private ParsedHome() {
        }

        @Override
        public String toString() {
            return ".";
        }

        @Override
        public boolean isDot() {
            return false;
        }

        @Override
        public boolean isHome() {
            return true;
        }
    }

    record ParsedToken(Permutation permutation) implements Parsed {

        @Override
        public String toString() {
            return permutation.toString();
        }

        @Override
        public boolean isDot() {
            return false;
        }

        @Override
        public boolean isHome() {
            return false;
        }
    }
}
