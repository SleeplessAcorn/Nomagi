package info.sleeplessacorn.nomagi.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.*;

@SuppressWarnings("ALL")
@SideOnly(Side.CLIENT)
public class FontRendererSmall implements IResourceManagerReloadListener {

    private static final ResourceLocation[] UNICODE_PAGE_LOCS = new ResourceLocation[256];
    
    private static final String CHARS =
                    "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da" + 
                    "\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174" +
                    "\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
                    " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" +
                    "\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb" +
                    "\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6" +
                    "\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192" +
                    "\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac" +
                    "\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561" +
                    "\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514" +
                    "\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566" +
                    "\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553" +
                    "\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2" +
                    "\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e" +
                    "\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248" +
                    "\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";

    private final ResourceLocation locationFontTexture;

    private int FONT_HEIGHT = 9;

    private Random rand = new Random();
    
    private int[] charWidth = new int[256];
    private byte[] glyphWidth = new byte[65536];
    private int[] colorCode = new int[32];

    private TextureManager renderEngine;

    private float posX;
    private float posY;

    private boolean unicodeFlag;
    private boolean useBidirectionalAlgorithm;

    private float red;
    private float green;
    private float blue;
    private float alpha;

    private int textColor;

    private boolean hasObfuscation;
    private boolean hasBold;
    private boolean hasItalics;
    private boolean hasUnderline;
    private boolean hasStrikethrough;

    public FontRendererSmall(GameSettings cfg, ResourceLocation path, TextureManager texMgr) {
        locationFontTexture = path;
        renderEngine = texMgr;
        unicodeFlag = true;
        if (renderEngine != null) {
            renderEngine.bindTexture(path);
        } else {
            for (int i = 0; i < 32; ++i) {
                int j = (i >> 3 & 1) * 85;
                int k = (i >> 2 & 1) * 170 + j;
                int l = (i >> 1 & 1) * 170 + j;
                int i1 = (i >> 0 & 1) * 170 + j;

                if (i == 6) {
                    k += 85;
                }

                if (cfg.anaglyph) {
                    int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                    int k1 = (k * 30 + l * 70) / 100;
                    int l1 = (k * 30 + i1 * 70) / 100;
                    k = j1;
                    l = k1;
                    i1 = l1;
                }

                if (i >= 16) {
                    k /= 4;
                    l /= 4;
                    i1 /= 4;
                }

                colorCode[ i ] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
            }
        }

        readGlyphSizes();
    }

    public void onResourceManagerReload(IResourceManager par1ResourceManager) {
        readFontTexture();
    }

    private void readFontTexture() {
        BufferedImage bufferedimage;

        try {
            bufferedimage = ImageIO.read(Minecraft.getMinecraft().getResourceManager()
                    .getResource(locationFontTexture).getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        int k = j / 16;
        int l = i / 16;
        byte b0 = 1;
        float f = 8.0F / (float) l;
        int i1 = 0;

        while (i1 < 256) {
            int j1 = i1 % 16;
            int k1 = i1 / 16;

            if (i1 == 32) {
                charWidth[i1] = 3 + b0;
            }

            int l1 = l - 1;

            while (true) {
                if (l1 >= 0) {
                    int i2 = j1 * l + l1;
                    boolean flag = true;

                    for (int j2 = 0; j2 < k && flag; ++j2) {
                        int k2 = (k1 * l + j2) * i;

                        if ((aint[i2 + k2] >> 24 & 255) != 0) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        --l1;
                        continue;
                    }
                }

                ++l1;
                charWidth[i1] = (int) (0.5D + (double) ((float) l1 * f)) + b0;
                ++i1;
                break;
            }
        }
    }

    private void readGlyphSizes() {
        try {
            InputStream inputstream = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
            inputstream.read(glyphWidth);
        } catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
    }

    /**
     * Render a single chr with the default.png font at current (posX,posY) location...
     */
    private float renderDefaultChar(int par1, boolean par2) {
        float f = (float) (par1 % 16 * 8);
        float f1 = (float) (par1 / 16 * 8);
        float f2 = par2 ? 1.0F : 0.0F;
        renderEngine.bindTexture(locationFontTexture);
        float f3 = (float) charWidth[par1] - 0.01F;
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(f / 128.0F, f1 / 128.0F);
        GL11.glVertex3f(posX + f2, posY, 0.0F);
        GL11.glTexCoord2f(f / 128.0F, (f1 + 7.99F) / 128.0F);
        GL11.glVertex3f(posX - f2, posY + 7.99F, 0.0F);
        GL11.glTexCoord2f((f + f3 - 1.0F) / 128.0F, f1 / 128.0F);
        GL11.glVertex3f(posX + f3 - 1.0F + f2, posY, 0.0F);
        GL11.glTexCoord2f((f + f3 - 1.0F) / 128.0F, (f1 + 7.99F) / 128.0F);
        GL11.glVertex3f(posX + f3 - 1.0F - f2, posY + 7.99F, 0.0F);
        GL11.glEnd();
        return (float) charWidth[par1];
    }

    private ResourceLocation getUnicodePageLocation(int glyphIndex) {
        if (UNICODE_PAGE_LOCS[glyphIndex] == null) {
            UNICODE_PAGE_LOCS[glyphIndex] = new ResourceLocation("minecraft",
                    String.format("textures/font/unicode_page_%02x.png", glyphIndex));
        }
        return UNICODE_PAGE_LOCS[glyphIndex];
    }

    private void loadGlyphTexture(int glyphIndex) {
        renderEngine.bindTexture(getUnicodePageLocation(glyphIndex));
    }

    private float renderUnicodeChar(char chr, boolean horizontalOffset) {
        if (glyphWidth[chr] == 0) {
            return 0.0F;
        } else {
            int i = chr / 256;
            loadGlyphTexture(i);
            int j = glyphWidth[chr] >>> 4;
            int k = glyphWidth[chr] & 15;
            float f1 = k + 1;
            float f2 = chr % 16 * 16 + j;
            float f3 = ((chr & 255) >> 4) * 16;
            float f4 = f1 - j - 0.02F;
            float f5 = horizontalOffset ? 1.0F : 0.0F;
            GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);
            GlStateManager.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
            GlStateManager.glVertex3f(posX + f5, posY, 0.0F);
            GlStateManager.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
            GlStateManager.glVertex3f(posX - f5, posY + 7.99F, 0.0F);
            GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
            GlStateManager.glVertex3f(posX + f4 / 2.0F + f5, posY, 0.0F);
            GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
            GlStateManager.glVertex3f(posX + f4 / 2.0F - f5, posY + 7.99F, 0.0F);
            GlStateManager.glEnd();
            return (f1 - j) / 2.0F + 1.0F;
        }
    }
    
    public int drawStringWithShadow(String str, int x, int y, int color) {
        return drawString(str, x, y, color, true);
    }
    
    public int drawString(String str, int x, int y, int color) {
        return drawString(str, x, y, color, false);
    }
    
    public int drawString(String str, int x, int y, int color, boolean hasDropShadow) {
        resetStyles();

        if (useBidirectionalAlgorithm) {
            str = applyBidirectionalReordering(str);
        }

        int l = renderString(str, x, y, color, false);

        if (hasDropShadow) {
            l = renderString(str, x + 1, y + 1, color, true);
            l = Math.max(l, renderString(str, x, y, color, false));
        }

        return l;
    }

    private String applyBidirectionalReordering(String str) {
        if (str != null && Bidi.requiresBidi(str.toCharArray(), 0, str.length())) {
            Bidi bidi = new Bidi(str, -2);
            byte[] abyte = new byte[bidi.getRunCount()];
            String[] astring = new String[abyte.length];
            int i;

            for (int j = 0; j < abyte.length; ++j) {
                int k = bidi.getRunStart(j);
                i = bidi.getRunLimit(j);
                int l = bidi.getRunLevel(j);
                String s1 = str.substring(k, i);
                abyte[j] = (byte) l;
                astring[j] = s1;
            }

            String[] astring1 = astring.clone();
            Bidi.reorderVisually(abyte, 0, astring, 0, abyte.length);
            StringBuilder stringbuilder = new StringBuilder();
            i = 0;

            while (i < astring.length) {
                byte b0 = abyte[i];
                int i1 = 0;

                while (true) {
                    if (i1 < astring1.length) {
                        if (!astring1[i1].equals(astring[i])) {
                            ++i1;
                            continue;
                        }

                        b0 = abyte[i1];
                    }

                    if ((b0 & 1) == 0) {
                        stringbuilder.append(astring[i]);
                    } else {
                        for (i1 = astring[i].length() - 1; i1 >= 0; --i1) {
                            char c0 = astring[i].charAt(i1);

                            if (c0 == 40) {
                                c0 = 41;
                            } else if (c0 == 41) {
                                c0 = 40;
                            }

                            stringbuilder.append(c0);
                        }
                    }

                    ++i;
                    break;
                }
            }

            return stringbuilder.toString();
        } else {
            return str;
        }
    }

    private void resetStyles() {
        hasObfuscation = false;
        hasBold = false;
        hasItalics = false;
        hasUnderline = false;
        hasStrikethrough = false;
    }

    private void renderStringAtPos(String str, boolean hasDropShadow) {
        for (int i = 0; i < str.length(); ++i) {
            char c0 = str.charAt(i);

            if (c0 == 167 && i + 1 < str.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(String.valueOf(str.charAt(i + 1))
                        .toLowerCase(Locale.ROOT).charAt(0));

                if (i1 < 16) {
                    resetStyles();

                    if (i1 < 0 || i1 > 15) {
                        i1 = 15;
                    }

                    if (hasDropShadow) {
                        i1 += 16;
                    }

                    int j1 = colorCode[i1];
                    textColor = j1;
                    setColor((j1 >> 16) / 255.0F, (j1 >> 8 & 255) / 255.0F, (j1 & 255) / 255.0F, alpha);
                } else if (i1 == 16) {
                    hasObfuscation = true;
                } else if (i1 == 17) {
                    hasBold = true;
                } else if (i1 == 18) {
                    hasStrikethrough = true;
                } else if (i1 == 19) {
                    hasUnderline = true;
                } else if (i1 == 20) {
                    hasItalics = true;
                } else {
                    resetStyles();
                    setColor(red, blue, green, alpha);
                }

                ++i;
            } else {                
                int j = CHARS.indexOf(c0);

                if (hasObfuscation && j != -1) {
                    int k = getCharWidth(c0);
                    char c1;
                    while (true) {
                        j = rand.nextInt(CHARS.length());
                        c1 = CHARS.charAt(j);

                        if (k == getCharWidth(c1)) {
                            break;
                        }
                    }
                    c0 = c1;
                }

                float f1 = j == -1 || unicodeFlag ? 0.5f : 1f;
                boolean flag = (c0 == 0 || j == -1 || unicodeFlag) && hasDropShadow;

                if (flag) {
                    posX -= f1;
                    posY -= f1;
                }

                float f = renderChar(c0, hasItalics);

                if (flag) {
                    posX += f1;
                    posY += f1;
                }

                if (hasBold) {
                    posX += f1;

                    if (flag) {
                        posX -= f1;
                        posY -= f1;
                    }

                    renderChar(c0, hasItalics);
                    posX -= f1;

                    if (flag) {
                        posX += f1;
                        posY += f1;
                    }

                    ++f;
                }
                doDraw(f);
            }
        }
    }

    private float renderChar(char ch, boolean italic) {
        if (ch == 160) return 4.0F; // forge: display nbsp as space. MC-2595
        if (ch == 32) {
            return 4.0F;
        } else {
            int i = CHARS.indexOf(ch);
            return i != -1 && !unicodeFlag ? renderDefaultChar(i, italic) : renderUnicodeChar(ch, italic);
        }
    }

    protected void doDraw(float f) {
        if (hasStrikethrough) {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            GlStateManager.disableTexture2D();
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
            vertexbuffer.pos(posX, posY + FONT_HEIGHT / 2, 0.0D).endVertex();
            vertexbuffer.pos(posX + f, posY + FONT_HEIGHT / 2, 0.0D).endVertex();
            vertexbuffer.pos(posX + f, posY + FONT_HEIGHT / 2 - 1.0F, 0.0D).endVertex();
            vertexbuffer.pos(posX, posY + FONT_HEIGHT / 2 - 1.0F, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }

        if (hasUnderline) {
            Tessellator tessellator1 = Tessellator.getInstance();
            VertexBuffer vertexbuffer1 = tessellator1.getBuffer();
            GlStateManager.disableTexture2D();
            vertexbuffer1.begin(7, DefaultVertexFormats.POSITION);
            int l = hasUnderline ? -1 : 0;
            vertexbuffer1.pos(posX + l, posY + FONT_HEIGHT, 0.0D).endVertex();
            vertexbuffer1.pos(posX + f, posY + FONT_HEIGHT, 0.0D).endVertex();
            vertexbuffer1.pos(posX + f, posY + FONT_HEIGHT - 1.0F, 0.0D).endVertex();
            vertexbuffer1.pos(posX + l, posY + FONT_HEIGHT - 1.0F, 0.0D).endVertex();
            tessellator1.draw();
            GlStateManager.enableTexture2D();
        }

        posX += (float) (int) f;
    }

    protected void setColor(float r, float g, float b, float a) {
        GlStateManager.color(r, g, b, a);
    }

    /**
     * Render string either left or right aligned depending on useBidirectionalAlgorithm
     */
    private int renderStringAligned(String par1Str, int par2, int par3, int par4, int par5, boolean par6) {
        if (useBidirectionalAlgorithm) {
            par1Str = applyBidirectionalReordering(par1Str);
            int i1 = getStringWidth(par1Str);
            par2 = par2 + par4 - i1;
        }

        return renderString(par1Str, par2, par3, par5, par6);
    }

    /**
     * Render single line string by setting GL color, current (posX,posY), and calling renderStringAtPos()
     */
    private int renderString(String str, int x, int y, int color, boolean hasDropShadow) {
        if (str == null) {
            return 0;
        } else {
            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (hasDropShadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            red = (color >> 16 & 255) / 255.0F;
            blue = (color >> 8 & 255) / 255.0F;
            green = (color & 255) / 255.0F;
            alpha = (color >> 24 & 255) / 255.0F;
            GlStateManager.color(red, blue, green, alpha);
            posX = x;
            posY = y;
            renderStringAtPos(str, hasDropShadow);
            return (int) posX;
        }
    }
    
    public int getStringWidth(String str) {
        if (str == null) {
            return 0;
        } else {
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < str.length(); ++j) {
                char c0 = str.charAt(j);
                int k = getCharWidth(c0);

                if (k < 0 && j < str.length() - 1) {
                    ++j;
                    c0 = str.charAt(j);

                    if (c0 != 108 && c0 != 76) {
                        if (c0 == 114 || c0 == 82) {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }

                    k = 0;
                }

                i += k;

                if (flag) {
                    ++i;
                }
            }

            return i;
        }
    }
    
    public int getCharWidth(char chr) {
        if (chr == 167) {
            return -1;
        } else if (chr == 32) {
            return 4;
        } else {
            int i;
            if (ChatAllowedCharacters.isAllowedCharacter(chr))
                i = chr;
            else {
                i = 69;
            }

            if (i >= 0 && !unicodeFlag) {
                return charWidth[i + 32];
            } else if (glyphWidth[chr] != 0) {
                int j = glyphWidth[chr] >>> 4;
                int k = glyphWidth[chr] & 15;

                if (k > 7) {
                    k = 15;
                    j = 0;
                }

                ++k;
                return (k - j) / 2 + 1;
            } else {
                return 0;
            }
        }
    }

    public String trimStringToWidth(String str, int width) {
        return trimStringToWidth(str, width, false);
    }

    public String trimStringToWidth(String str, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        int j = 0;
        int k = reverse ? str.length() - 1 : 0;
        int l = reverse ? -1 : 1;
        boolean flag1 = false;
        boolean flag2 = false;

        for (int i1 = k; i1 >= 0 && i1 < str.length() && j < width; i1 += l) {
            char c0 = str.charAt(i1);
            int j1 = getCharWidth(c0);

            if (flag1) {
                flag1 = false;

                if (c0 != 108 && c0 != 76) {
                    if (c0 == 114 || c0 == 82) {
                        flag2 = false;
                    }
                } else {
                    flag2 = true;
                }
            } else if (j1 < 0) {
                flag1 = true;
            } else {
                j += j1;

                if (flag2) {
                    ++j;
                }
            }

            if (j > width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }

    private String trimStringNewline(String str) {
        while (str != null && str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    /**
     * Splits and draws a String with wordwrap (maximum length is parameter k)
     */
    public void drawSplitString(String str, int par2, int par3, int splitIndex, int par5) {
        resetStyles();
        textColor = par5;
        str = trimStringNewline(str);
        renderSplitString(str, par2, par3, splitIndex, false);
    }

    /**
     * Perform actual work of rendering a multi-line string with wordwrap and with darker drop shadow color if flag is
     * set
     */
    private void renderSplitString(String par1Str, int par2, int par3, int par4, boolean hasDropShadow) {
        List<String> list = listFormattedStringToWidth(par1Str, par4);

        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); par3 += FONT_HEIGHT) {
            String s1 = iterator.next();
            renderStringAligned(s1, par2, par3, par4, textColor, hasDropShadow);
        }
    }

    /**
     * Returns the width of the wordwrapped String (maximum length is parameter k)
     */
    public int splitStringWidth(String par1Str, int par2) {
        return FONT_HEIGHT * listFormattedStringToWidth(par1Str, par2).size();
    }

    /**
     * Get unicodeFlag controlling whether strings should be rendered with Unicode fonts instead of the default.png
     * font.
     */
    public boolean getUnicodeFlag() {
        return unicodeFlag;
    }

    /**
     * Set unicodeFlag controlling whether strings should be rendered with Unicode fonts instead of the default.png
     * font.
     */
    public void setUnicodeFlag(boolean par1) {
        unicodeFlag = par1;
    }

    /**
     * Breaks a string into a list of pieces that will fit a specified width.
     */
    public List<String> listFormattedStringToWidth(String par1Str, int par2) {
        return Arrays.asList(wrapFormattedStringToWidth(par1Str, par2).split("\n"));
    }

    /**
     * Inserts newline and formatting into a string to wrap it within the specified width.
     */
    String wrapFormattedStringToWidth(String par1Str, int par2) {
        int j = sizeStringToWidth(par1Str, par2);

        if (par1Str.length() <= j) {
            return par1Str;
        } else {
            String s1 = par1Str.substring(0, j);
            char c0 = par1Str.charAt(j);
            boolean flag = c0 == 32 || c0 == 10;
            String s2 = getFormatFromString(s1) + par1Str.substring(j + (flag ? 1 : 0));
            return s1 + "\n" + wrapFormattedStringToWidth(s2, par2);
        }
    }

    /**
     * Determines how many chrs from the string will fit into the specified width.
     */
    @SuppressWarnings("fallthrough")
    private int sizeStringToWidth(String par1Str, int par2) {
        int j = par1Str.length();
        int k = 0;
        int l = 0;
        int i1 = -1;

        for (boolean flag = false; l < j; ++l) {
            char c0 = par1Str.charAt(l);

            switch (c0) {
                case 10:
                    --l;
                    break;
                case 167:
                    if (l < j - 1) {
                        ++l;
                        char c1 = par1Str.charAt(l);

                        if (c1 != 108 && c1 != 76) {
                            if (c1 == 114 || c1 == 82 || isFormatColor(c1)) {
                                flag = false;
                            }
                        } else {
                            flag = true;
                        }
                    }

                    break;
                case 32:
                    i1 = l;
                default:
                    k += getCharWidth(c0);

                    if (flag) {
                        ++k;
                    }
            }

            if (c0 == 10) {
                ++l;
                i1 = l;
                break;
            }

            if (k > par2) {
                break;
            }
        }

        return l != j && i1 != -1 && i1 < l ? i1 : l;
    }

    /**
     * Get useBidirectionalAlgorithm that controls if the Unicode Bidirectional Algorithm should be run before rendering any string
     */
    public boolean getUseBidirectionalAlgorithm() {
        return useBidirectionalAlgorithm;
    }

    /**
     * Set useBidirectionalAlgorithm to control if the Unicode Bidirectional Algorithm should be run before rendering any string.
     */
    public void setUseBidirectionalAlgorithm(boolean par1) {
        useBidirectionalAlgorithm = par1;
    }

    /**
     * Checks if the char code is a hexadecimal chr, used to set colour.
     */
    private static boolean isFormatColor(char par0) {
        return par0 >= 48 && par0 <= 57 || par0 >= 97 && par0 <= 102 || par0 >= 65 && par0 <= 70;
    }

    /**
     * Checks if the char code is O-K...lLrRk-o... used to set special formatting.
     */
    private static boolean isFormatSpecial(char par0) {
        return par0 >= 107 && par0 <= 111 || par0 >= 75 && par0 <= 79 || par0 == 114 || par0 == 82;
    }

    /**
     * Digests a string for nonprinting formatting chrs then returns a string containing only that formatting.
     */
    private static String getFormatFromString(String par0Str) {
        String s1 = "";
        int i = -1;
        int j = par0Str.length();

        while ((i = par0Str.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = par0Str.charAt(i + 1);

                if (isFormatColor(c0)) {
                    s1 = "\u00a7" + c0;
                } else if (isFormatSpecial(c0)) {
                    s1 = s1 + "\u00a7" + c0;
                }
            }
        }

        return s1;
    }

}