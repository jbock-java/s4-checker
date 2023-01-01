package uppu.test;

import io.parmigiano.Permutation;
import uppu.util.Suppliers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static io.parmigiano.Permutation.cycle;
import static java.util.Objects.requireNonNull;

public enum Month {
    JANUARY(cycle(0, 1, 2), "01 Январь"),
    FEBRUARY(cycle(0, 2, 1), "02 Февраль"),
    MARCH(cycle(0, 2).compose(1, 3), "03 Март"),

    APRIL(cycle(0, 3, 1), "04 Апрель"),
    MAY(cycle(1, 2, 3), "05 Май"),
    JUNE(cycle(0, 1).compose(2, 3), "06 Июнь"),

    JULY(cycle(1, 3, 2), "07 Июль"),
    AUGUST(cycle(0, 3, 2), "08 Август"),
    SEPTEMBER(cycle(0, 3).compose(1, 2), "09 Сентябрь"),

    OCTOBER(cycle(0, 2, 3), "10 Октябрь"),
    NOVEMBER(cycle(0, 1, 3), "11 Ноябрь"),
    DECEMBER(Permutation.identity(), "12 Декабрь"),
    ;

    private final Permutation p;
    private final String title;

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

    Month(Permutation p, String title) {
        this.p = p;
        this.title = title + " " + p.toString();
    }

    @Override
    public String toString() {
        return title;
    }
}
