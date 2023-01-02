package uppu.test;

import io.parmigiano.Permutation;
import uppu.model.Colour;
import uppu.util.Suppliers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static io.parmigiano.Permutation.cycle;
import static java.util.Objects.requireNonNull;
import static uppu.model.Colour.BLUE;
import static uppu.model.Colour.GREEN;
import static uppu.model.Colour.RED;
import static uppu.model.Colour.SILVER;

public enum Month {
    JANUARY(cycle(0, 1, 2), "01 Январь", List.of(BLUE, RED, GREEN, SILVER)),
    FEBRUARY(cycle(0, 2, 1), "02 Февраль", List.of(GREEN, BLUE, RED, SILVER)),
    MARCH(cycle(0, 2).compose(1, 3), "03 Март", List.of(BLUE, SILVER, RED, GREEN)),

    APRIL(cycle(0, 3, 1), "04 Апрель", List.of(GREEN, SILVER, BLUE, RED)),
    MAY(cycle(1, 2, 3), "05 Май", List.of(RED, SILVER, GREEN, BLUE)),
    JUNE(cycle(0, 1).compose(2, 3), "06 Июнь", List.of(GREEN, RED, SILVER, BLUE)),

    JULY(cycle(1, 3, 2), "07 Июль", List.of(RED, BLUE, SILVER, GREEN)),
    AUGUST(cycle(0, 3, 2), "08 Август", List.of(BLUE, GREEN, SILVER, RED)),
    SEPTEMBER(cycle(0, 3).compose(1, 2), "09 Сентябрь", List.of(SILVER, BLUE, GREEN, RED)),

    OCTOBER(cycle(0, 2, 3), "10 Октябрь", List.of(SILVER, GREEN, RED, BLUE)),
    NOVEMBER(cycle(0, 1, 3), "11 Ноябрь", List.of(SILVER, RED, BLUE, GREEN)),
    DECEMBER(Permutation.identity(), "12 Декабрь", List.of(RED, GREEN, BLUE, SILVER)),
    ;

    private final Permutation p;
    private final String title;
    private final List<Colour> state;

    static final Supplier<Map<Permutation, Month>> MAP = Suppliers.memoize(() -> {
        Map<Permutation, Month> monthMap = new HashMap<>();
        for (Month month : Month.values()) {
            monthMap.put(month.p, month);
        }
        return monthMap;
    });

    public static Month monthOf(Permutation p) {
        return requireNonNull(MAP.get().get(p));
    }

    public Permutation permutation() {
        return p;
    }

    public String title() {
        return title;
    }

    Month(Permutation p, String title, List<Colour> state) {
        this.p = p;
        this.state = state;
        this.title = title + " " + p.toString();
    }

    @Override
    public String toString() {
        return title;
    }
}
