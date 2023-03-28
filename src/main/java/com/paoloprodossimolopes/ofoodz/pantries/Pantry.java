package com.paoloprodossimolopes.ofoodz.pantries;

import java.util.Optional;

public class Pantry {
    private final Optional<Long> id;
    private final String title;

    public Pantry(Optional<Long> id, String title) {
        this.id = id;
        this.title = title;
    }

    public Optional<Long> getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
