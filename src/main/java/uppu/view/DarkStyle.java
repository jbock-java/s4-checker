package uppu.view;

import uppu.util.Suppliers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Supplier;

class DarkStyle {

    private static final String CSS = """
.list-cell {
    -fx-font: 18px Monospace;
    -fx-font-weight: bold;
    -fx-background-radius: 0;
    -fx-border-width: 0;
    border-width: 0;
    -fx-text-fill: #FFFFFF;
    -fx-background-color: #404040;
    background-color: #404040;
    -fx-min-height: 24;
    min-height: 24;
    -fx-padding: 0 0 0 3;
    padding: 0 0 0 3;
}

.list-cell:hover {
    -fx-background-color: #3E3E40;
    background-color: #3E3E40;
    -fx-cursor: HAND;
    cursor: HAND;
}

.list-cell:pressed {
    -fx-background-color: #3E3E40;
    background-color: #3E3E40;
}

.list-cell:selected {
    -fx-background-color: #0045e2;
    background-color: #0045e2;
}

/* ListView */
.list-view {
    -fx-background-radius: 0;
    -fx-background-color: #404040;
    background-color: #404040;
    -fx-border-width: 1;
    border-width: 1;
    -fx-padding: 1;
    padding: 1;
}

.list-view .scroll-bar {
    -fx-background-insets: 0 -1 0 0;
}

.list-view .scroll-bar .thumb{
    -fx-background-insets: 0 3 0 2;
}

.list-view:hover {
    -fx-border-color: #3E3E40;
    border-color: #3E3E40;
    -fx-padding: 0;
    padding: 0;
    -fx-background-insets: 1 1 0 1;
}

.list-view:focused {
    -fx-background-color: #404040;
    background-color: #404040;
    -fx-border-color: #4e4e4e;
    border-color: #4e4e4e;
    -fx-padding: 0 0 1 0;
    padding: 0 0 1 0;
    -fx-background-insets: 1;
}

.text-area {
    -fx-font: 18px Monospace;
    -fx-font-weight: bold;
    -fx-text-fill: #FFFFFF;
    -fx-display-caret: true;
}

.text-area .content{
    -fx-background-color: #404040;
    background-color: #404040;
}
            """;

    private static final Supplier<String> CSS_SUPPLIER = Suppliers.memoize(() -> {
        byte[] cssBytes = CSS.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(cssBytes);
    });

    static String getBase64() {
        return CSS_SUPPLIER.get();
    }
}
