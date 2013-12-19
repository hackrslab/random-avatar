package org.hackrslab.avatar;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class RandomAvatar {
    public static class Extra {
        String initial;

        public Extra() {
        }

        public static Extra initial(String initial) {
            return initial(initial, 1);
        }

        public static Extra initial(String initial, int length) {
            if (initial != null && initial.length() > 0) {
                Extra extra = new Extra();
                extra.initial = initial.substring(0, Math.min(initial.length(), length)).toUpperCase();
                return extra;
            }
            return null;
        }
    }

    private int squareSize;
    private int blockSize;
    private boolean asymmetry;
    private int[] colors;
    private int backgroundColor; // -1 is transparent
    private int fontColor;
    private int padding;

    public RandomAvatar() {
    }

    public void generate(File file) throws RandomAvatarException {
        generate(file, null);
    }

    public void generate(OutputStream output) throws RandomAvatarException {
        generate(output, null);
    }

    public void generate(File file, Extra extra) throws RandomAvatarException {
        try {
            generate(new FileOutputStream(file), extra);
        } catch (Exception e) {
            throw new RandomAvatarException(e);
        }
    }

    public void generate(OutputStream output, Extra extra) throws RandomAvatarException {
        setDefaultOptions();
        boolean[] blocks = null;
        while (!validate(blocks)) {
            blocks = generateRandomBlocks();
        }
        BufferedImage avatar = new BufferedImage(squareSize + padding * 2, squareSize + padding * 2
            , BufferedImage.TYPE_INT_ARGB);
        generateAvatar(avatar, blocks, extra);
        try {
            ImageIO.write(avatar, "png", output);
        } catch (Exception e) {
            throw new RandomAvatarException(e);
        }
    }

    void setDefaultOptions() {
        squareSize = squareSize > 0 ? squareSize : 630;
        blockSize = blockSize >= 3 ? blockSize : 5;
    }

    boolean validate(boolean[] blocks) {
        if (blocks == null) {
            return false;
        }
        int count = 0;
        for (boolean block : blocks) {
            count = block ? count + 1 : count;
        }
        if (2 < blockSize && count < 6) {
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

    void generateAvatar(BufferedImage avatar, boolean[] blocks, Extra extra) {
        Graphics2D g = avatar.createGraphics();
        int size = squareSize / blockSize;
        int color = colors[new Random().nextInt(colors.length)];
        int holeBlockSizeX = 0;
        int holeBlockSizeY = 0;

        // background
        if (backgroundColor >= 0) {
            g.setColor(new Color(backgroundColor));
            g.fillRect(0, 0, squareSize + padding * 2, squareSize + padding * 2);
        }

        // initial
        if (extra != null && extra.initial != null) {
            holeBlockSizeY = blockSize / 2;
            Font f = new Font("Serif", Font.BOLD, (int)(holeBlockSizeY * size * 0.8));
            FontMetrics fm = g.getFontMetrics(f);
            java.awt.geom.Rectangle2D rect = fm.getStringBounds(extra.initial, g);
            int textHeight = (int)(rect.getHeight()); 
            int textWidth  = (int)(rect.getWidth());
            holeBlockSizeX = Math.max((int)((textWidth + size - 1) / size), holeBlockSizeY);
            int panelHeight = holeBlockSizeY * size;
            int panelWidth = holeBlockSizeX * size;
            int x = padding + (panelWidth - textWidth) / 2;
            int y = padding + (panelHeight + 1 - padding) / 2 - ((fm.getAscent() + fm.getDescent()) / 2) + fm.getAscent() + fm.getDescent() / 2;

            g.setColor(new Color(color));
            g.fillRect(padding, padding, panelWidth, panelHeight);
            g.setFont(f);
            g.setColor(new Color(fontColor));
            g.drawString(extra.initial, x, y);
        }

        // blocks
        for (int y = 0; y < blockSize; y++) {
            for (int x = 0; x < blockSize; x++) {
                if (y < holeBlockSizeY && x < holeBlockSizeX) {
                    continue;
                }
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

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}