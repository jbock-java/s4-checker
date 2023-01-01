package uppu.view;

import uppu.util.Suppliers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Supplier;

class DarkStyle {

    private static final String CSS = """
.list-cell {
    -fx-font: 20px Monospace;
    -fx-font-weight: bold;
    -fx-background-radius: 0;
    -fx-border-width: 0;
    border-width: 0;
    -fx-text-fill: #FFFFFF;
    -fx-background-color: #404040;
    background-color: #404040;
    -fx-min-height: 30;
    min-height: 30;
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

.list-view {
    -fx-background-radius: 0;
    -fx-background-color: #404040;
    background-color: #404040;
    -fx-border-width: 1;
    border-width: 1;
    -fx-padding: 1;
    padding: 1;
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
    -fx-font: 20px Monospace;
    -fx-font-weight: bold;
    -fx-text-fill: #FFFFFF;
    -fx-display-caret: true;
}

.text-area .content{
    -fx-background-color: #404040;
    background-color: #404040;
}

.scroll-bar:horizontal .track,
.scroll-bar:vertical .track{
    -fx-background-color :transparent;
    -fx-border-color :transparent;
    -fx-background-radius : 0.0em;
    -fx-border-radius :2.0em;
}


.scroll-bar:horizontal .increment-button ,
.scroll-bar:horizontal .decrement-button {
    -fx-background-color :transparent;
    -fx-background-radius : 0.0em;
    -fx-padding :0.0 0.0 10.0 0.0;

}

.scroll-bar:vertical .increment-button ,
.scroll-bar:vertical .decrement-button {
    -fx-background-color :transparent;
    -fx-background-radius : 0.0em;
    -fx-padding :0.0 10.0 0.0 0.0;

}

.scroll-bar .increment-arrow,
.scroll-bar .decrement-arrow{
    -fx-shape : " ";
    -fx-padding :0.15em 0.0;
}

.scroll-bar:vertical .increment-arrow,
.scroll-bar:vertical .decrement-arrow{
    -fx-shape : " ";
    -fx-padding :0.0 0.15em;
}

.scroll-bar:horizontal .thumb,
.scroll-bar:vertical .thumb {
    -fx-background-color :derive(black,90.0%);
    -fx-background-insets : 2.0, 0.0, 0.0;
    -fx-background-radius : 2.0em;
}

.scroll-bar:horizontal .thumb:hover,
.scroll-bar:vertical .thumb:hover {
    -fx-background-color :derive(#4D4C4F,10.0%);
    -fx-background-insets : 2.0, 0.0, 0.0;
    -fx-background-radius : 2.0em;
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
