package org.hackrslab.avatar;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Random;

import javax.imageio.ImageIO;

public class RandomAvatar {
    public static class Extra {
        String seed;
        String initial;

        public Extra() {
        }

        public static Extra initial(String initial) {
            return initial(initial, 1);
        }

        public static Extra initial(String initial, int length) {
            length = Math.max(length, 0);
            if (length == 0) {
                length = initial.length();
            }
            if (initial != null && initial.length() > 0) {
                Extra extra = new Extra();
                extra.initial = initial.substring(0, Math.min(initial.length(), length)).toUpperCase();
                return extra;
            }
            return null;
        }

        public static Extra seed(String seed) {
            return seed(seed, 0);
        }

        public static Extra seed(String seed, String initial) {
            return seed(seed, initial, 1);
        }

        public static Extra seed(String seed, int length) {
            return seed(seed, null, length);
        }

        public static Extra seed(String seed, String initial, int length) {
            length = Math.max(length, 0);
            if (seed != null && seed.length() > 0) {
                Extra extra = new Extra();
                extra.seed = seed;
                if (initial != null && length > 0 && initial.length() >= length) {
                    extra.initial = initial.substring(0, length);
                } else if (length != 0) {
                    extra.initial = seed.substring(0, Math.min(seed.length(), length)).toUpperCase();
                }
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
    private AvatarCache cache;

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
        String cacheKey = generateCacheKey(extra);
        if (cacheKey != null) {
            byte[] bytes = cache.get(cacheKey);
            if (bytes != null) {
                try {
                    output.write(bytes);
                    return;
                } catch (Exception e) {
                    throw new RandomAvatarException(e);
                }
            }
        }
        setDefaultOptions();
        boolean[] blocks = null;
        while (!validate(blocks)) {
            blocks = generateRandomBlocks(extra == null ? null : extra.seed);
            if (extra != null) {
                break;
            }
        }
        BufferedImage avatar = new BufferedImage(squareSize + padding * 2, squareSize + padding * 2
            , BufferedImage.TYPE_INT_ARGB);
        generateAvatar(avatar, blocks, extra);
        try {
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ImageIO.write(avatar, "png", byteOutput);
            byte[] bytes = byteOutput.toByteArray();
            byteOutput.close();
            output.write(bytes);
            if (cacheKey != null) {
                cache.put(cacheKey, bytes);
            }
        } catch (Exception e) {
            throw new RandomAvatarException(e);
        }
    }

    String generateCacheKey(Extra extra) {
        if (cache == null || extra == null || extra.seed == null) {
            return null;
        }
        if (extra.initial == null) {
            return extra.seed;
        } else {
            return extra.seed + ":" + extra.initial;
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

    boolean[] generateRandomBlocks(String seed) {
        boolean[] blocks = new boolean[blockSize * blockSize];
        for (int y = 0; y < blockSize; y++) {
            for (int x = 0; x < blockSize; x++) {
                int index = y * blockSize + x;
                if (blockSize / 2 < x && !asymmetry) {
                    blocks[index] = blocks[index - x + blockSize - x - 1];
                } else {
                    blocks[index] = nextBoolean(seed, blockSize, y, x);
                }
            }
        }
        return blocks;
    }

    boolean nextBoolean(String seed, int blockSize, int y, int x) {
        if (seed == null) {
            return new Random().nextBoolean();
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] values = md.digest(seed.getBytes("UTF-8"));
                int offset = y * blockSize + x;
                int length = values.length;
                int b = 1 << (offset % 8);
                return ((values[(offset / 8) % length]) & (b)) == b;
            } catch (Exception e) {
                return new Random().nextBoolean();
            }
        }
    }

    int nextInt(int n, String seed, int offset) {
        if (seed == null) {
            return new Random().nextInt(colors.length);
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] values = md.digest((seed + ":" + offset).getBytes("UTF-8"));
                return Math.abs(ByteBuffer.wrap(values).getInt()) % n;
            } catch (Exception e) {
                return new Random().nextInt(n);
            }
        }
    }

    void generateAvatar(BufferedImage avatar, boolean[] blocks, Extra extra) {
        Graphics2D g = avatar.createGraphics();
        int size = squareSize / blockSize;
        int color = colors[nextInt(colors.length, extra == null ? null : extra.seed, 0)];
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
            Font f = new Font("Serif", Font.BOLD, (int)(holeBlockSizeY * size * 0.95));
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

    public void enableCache(boolean cache) {
        if (cache) {
            this.cache = new AvatarCache();
        }
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