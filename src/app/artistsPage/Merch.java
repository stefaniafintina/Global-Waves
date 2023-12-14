package app.artistsPage;

import lombok.Getter;

public final class Merch {
    @Getter
    private String name;
    @Getter
    private double price;
    @Getter
    private String description;
    public Merch(final double price, final String description, final String name) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
