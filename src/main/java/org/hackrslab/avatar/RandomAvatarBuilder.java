package org.hackrslab.avatar;

import java.util.List;
import java.util.ArrayList;

public class RandomAvatarBuilder {
    private int squareSize;
    private int blockSize;
    private boolean asymmetry;
    private int padding;
    private List<Integer> colors;

    public RandomAvatarBuilder() {
        colors = new ArrayList<Integer>();
    }

    public RandomAvatarBuilder squareSize(int squareSize) {
        this.squareSize = squareSize;
        return this;
    }

    public RandomAvatarBuilder blockSize(int blockSize) {
        this.blockSize = blockSize;
        return this;
    }

    public RandomAvatarBuilder asymmetry(boolean asymmetry) {
        this.asymmetry = asymmetry;
        return this;
    }

    public RandomAvatarBuilder padding(int padding) {
        this.padding = padding;
        return this;
    }

    /**
     * @param r Red (0~255)
     * @param g Green (0~255)
     * @param b Blue (0~255)
     */
    public RandomAvatarBuilder addColor(int r, int g, int b) {
        return addColor(r, g, b, 255);
    }

    /**
     * @param r Red (0~255)
     * @param g Green (0~255)
     * @param b Blue (0~255)
     * @param a Alpha (0~255)
     */
    public RandomAvatarBuilder addColor(int r, int g, int b, int a) {
        return addColor(r*256*256 + g*256 + b, a);
    }

    /**
     * @param rgb RGB
     */
    public RandomAvatarBuilder addColor(int rgb) {
        return addColor(rgb, 255);
    }

    /**
     * @param rgb RGB
     * @param alpha Alpha (0~255)
     */
    public RandomAvatarBuilder addColor(int rgb, int alpha) {
        colors.add(alpha*256*256*256 + rgb);
        return this;
    }

    public RandomAvatar build() {
        int[] colors = new int[this.colors.size()];
        int i = 0;
        for (Integer color : this.colors) {
            colors[i++] = color.intValue();
        }
        RandomAvatar instance = new RandomAvatar();
        instance.setSquareSize(squareSize);
        instance.setBlockSize(blockSize);
        instance.setAsymmetry(asymmetry);
        instance.setPadding(padding);
        instance.setColors(colors);
        instance.setPadding(padding);
        return instance;
    }
}