package uppu.parse;

import io.parmigiano.Permutation;

import java.util.List;
import java.util.stream.Collectors;

public sealed interface Row permits Row.HomeRow, Row.ExplicitRow {

    HomeRow HOME_ROW = new HomeRow();

    List<Permutation> permutations(Permutation current);

    String toString(Permutation current);

    record ExplicitRow(List<Permutation> permutations) implements Row {

        public static ExplicitRow explicitRow(List<Permutation> permutations) {
            return new ExplicitRow(permutations);
        }

        @Override
        public List<Permutation> permutations(Permutation current) {
            return permutations;
        }

        @Override
        public String toString(Permutation current) {
            return permutations.stream().map(Permutation::toString).collect(Collectors.joining(" . "));
        }
    }

    final class HomeRow implements Row {

        private HomeRow() {
        }

        @Override
        public List<Permutation> permutations(Permutation current) {
            return List.of(current.invert());
        }

        @Override
        public String toString(Permutation current) {
            return "~";
        }
    }
}
