package app.artistsPage;

import lombok.Getter;

public class Merch {
    @Getter
    private String name;
    @Getter
    private double price;
    @Getter
    private String description;
    public Merch(double price, String description, String name) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
