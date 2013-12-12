package org.hackrslab.avatar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class RandomAvatar {
    private int squareSize;
    private int blockSize;
    private boolean asymmetry;
    private int[] colors;
    private int padding;

    public RandomAvatar() {
    }

    public void generate(File file) throws RandomAvatarException {
        try {
            generate(new FileOutputStream(file));
        } catch (Exception e) {
            throw new RandomAvatarException(e);
        }
    }

    public void generate(OutputStream output) throws RandomAvatarException {
        setDefaultOptions();
        boolean[] blocks = null;
        while (!validate(blocks)) {
            blocks = generateRandomBlocks();
        }
        BufferedImage avatar = new BufferedImage(squareSize + padding * 2, squareSize + padding * 2
            , BufferedImage.TYPE_INT_ARGB);
        generateAvatar(avatar, blocks);
        try {
            ImageIO.write(avatar, "png", output);
        } catch (Exception e) {
            throw new RandomAvatarException(e);
        }
    }

    void setDefaultOptions() {
        squareSize = squareSize > 0 ? squareSize : 630;
        blockSize = blockSize > 0 ? blockSize : 5;
    }

    boolean validate(boolean[] blocks) {
        if (blocks == null) {
            return false;
        }
        int count = 0;
        for (boolean block : blocks) {
            count = block ? count + 1 : count;
        }
        if (count < 6) {
            return false;
        }
        if (count == blockSize * blockSize) {
            return false;
        }
        return true;
    }

    boolean[] generateRandomBlocks() {
        boolean[] blocks = new boolean[blockSize * blockSize];
        for (int y = 0; y < blockSize; y++) {
            for (int x = 0; x < blockSize; x++) {
                int index = y * blockSize + x;
                if (blockSize / 2 < x && !asymmetry) {
                    blocks[index] = blocks[index - x + blockSize - x - 1];
                } else {
                    blocks[index] = new Random().nextBoolean();
                }
            }
        }
        return blocks;
    }

    void generateAvatar(BufferedImage avatar, boolean[] blocks) {
        Graphics2D g = avatar.createGraphics();
        int size = squareSize / blockSize;
        int color = colors[new Random().nextInt(colors.length)];
        for (int y = 0; y < blockSize; y++) {
            for (int x = 0; x < blockSize; x++) {
                if (blocks[y * blockSize + x]) {
                    g.setColor(new Color(color));
                    g.fillRect(padding + x * size, padding + y * size, size, size);
                }
            }
        }
    }

    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setAsymmetry(boolean asymmetry) {
        this.asymmetry = asymmetry;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setPallet(int[] colors) {
        this.colors = colors;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}