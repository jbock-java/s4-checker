open module uppufx.test {

    requires javafx.controls;
    requires io.parmigiano;
    requires io.jbock.util;
    requires net.jbock;
    requires uppufx;

    requires org.mockito;
    requires org.junit.jupiter.api;

    exports uppu.test.model;
    exports uppu.test.engine;
}
