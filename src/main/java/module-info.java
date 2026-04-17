module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;

    opens drinkshop.ui;

    exports drinkshop.ui;

    opens drinkshop.domain to javafx.base;

    exports drinkshop.domain;

    opens drinkshop.service;
    opens drinkshop.repository;
}