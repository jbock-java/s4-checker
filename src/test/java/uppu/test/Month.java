package uppu.test;

import io.parmigiano.Permutation;
import io.parmigiano.Taking;
import uppu.model.Colour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static uppu.model.Colour.BLUE;
import static uppu.model.Colour.GREEN;
import static uppu.model.Colour.RED;
import static uppu.model.Colour.SILVER;
import static uppu.util.Suppliers.memoize;

public enum Month {
    JANUARY("01 Январь", List.of(BLUE, RED, GREEN, SILVER)),
    FEBRUARY("02 Февраль", List.of(GREEN, BLUE, RED, SILVER)),
    MARCH("03 Март", List.of(BLUE, SILVER, RED, GREEN)),

    APRIL("04 Апрель", List.of(GREEN, SILVER, BLUE, RED)),
    MAY("05 Май", List.of(RED, SILVER, GREEN, BLUE)),
    JUNE("06 Июнь", List.of(GREEN, RED, SILVER, BLUE)),

    JULY("07 Июль", List.of(RED, BLUE, SILVER, GREEN)),
    AUGUST("08 Август", List.of(BLUE, GREEN, SILVER, RED)),
    SEPTEMBER("09 Сентябрь", List.of(SILVER, BLUE, GREEN, RED)),

    OCTOBER("10 Октябрь", List.of(SILVER, GREEN, RED, BLUE)),
    NOVEMBER("11 Ноябрь", List.of(SILVER, RED, BLUE, GREEN)),
    DECEMBER("12 Декабрь", Colour.getValues()),
    ;

    private final Supplier<String> title = memoize(() -> {
        int maxLabel = 0;
        for (Month m : values()) {
            maxLabel = Math.max(maxLabel, m.label.length());
        }
        String padding = " ".repeat(maxLabel + 1 - label().length());
        return label() + padding + state().stream()
                .map(c -> c.name().substring(0, 1))
                .collect(Collectors.joining(" "));
    });

    private final List<Colour> state;
    private final String label;

    static final Supplier<Map<Permutation, Month>> MAP = memoize(() -> {
        Map<Permutation, Month> monthMap = new HashMap<>();
        for (Month month : Month.values()) {
            monthMap.put(month.permutation(), month);
        }
        return monthMap;
    });

    static final Supplier<Map<List<Colour>, Month>> STATE_MAP = memoize(() -> {
        Map<List<Colour>, Month> monthMap = new HashMap<>();
        for (Month month : Month.values()) {
            monthMap.put(month.state, month);
        }
        return monthMap;
    });

    public static Month monthOf(Permutation p) {
        return requireNonNull(MAP.get().get(p));
    }

    public static Month monthOf(List<Colour> state) {
        return requireNonNull(STATE_MAP.get().get(state));
    }

    public Permutation permutation() {
        return Taking.from(Colour.getValues()).to(state);
    }

    public String title() {
        return title.get();
    }

    private String label() {
        return label;
    }

    public List<Colour> state() {
        return state;
    }

    Month(String title, List<Colour> state) {
        this.state = state;
        this.label = title;
    }

    @Override
    public String toString() {
        return title();
    }
}
