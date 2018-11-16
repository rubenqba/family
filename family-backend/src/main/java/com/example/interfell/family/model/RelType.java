package com.example.interfell.family.model;

public enum RelType {
    MARRIED(true), PARENT(false), SIBLING(true);

    private boolean bidirectional;

    RelType(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    public boolean isBidirectional() {
        return bidirectional;
    }
}
