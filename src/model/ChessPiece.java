package model;

import java.awt.*;

public enum ChessPiece {
    BLACK(Color.BLACK), WHITE(Color.WHITE),
    RED(new Color(255,0,0,0));

    private final Color color;

    ChessPiece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
