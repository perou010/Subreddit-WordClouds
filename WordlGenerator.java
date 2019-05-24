import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WordlGenerator {
    private static final String DEFAULT_FONT = "default";

    private Random rand;
    private BufferedImage img;
    private Graphics2D gc;
    private String fontName;
    List<Shape> others;

    private int maxCount = 900;
    private int maxFont = 200;
    private int minCount = 100;
    private int minFont = 20;

    // bigger speed, faster render, but less well "packed" words;
    private double speedMult = 5;

    /**
     * Create a new Word cloud generator that generates to an image width pixels wide, and height pixels tall
     * @param width
     * @param height
     */
    public WordlGenerator(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // this looks pretty sketchym, but apparently, is standard operating procedure
        gc = (Graphics2D) img.getGraphics();
        gc.setColor(Color.black);
        gc.fillRect(0, 0, width, height);
        gc.setColor(Color.black);
        fontName = DEFAULT_FONT;
        others = new LinkedList<>();
        rand = new Random();

        // improve text rednering quality
        gc.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    }

    /**
     * This effects how agressively the algorithm searches for new spaces to put a word.
     * The smaller the value the more optimally compact you can expect the word cloud to be,
     * but the longer it will take to render.
     *
     * The larger the value the quicker, but clumsier, you can expect the word cloud to be.
     *
     * In my experiance 1 is too low, and 10 is getting a bit too high.
     * @param speedMult
     */
    public void setSpeedMult(double speedMult) {
        this.speedMult = speedMult;
    }

    /**
     * Set the minimum and maximum counts - this is used to make sure words are appropriately scaled and should be
     * called once before adding words to the image
     * @param maxCount
     * @param minCount
     */
    public void setCountRange(int maxCount, int minCount) {
        this.maxCount = maxCount;
        this.minCount = minCount;
    }

    /**
     * Sets the range of font sizes - the defaults are probably OK, but if you do want to change then do this once
     * before adding words to the image
     * @param minFont
     * @param maxFont
     */
    public void setFontRange(int minFont, int maxFont) {
        this.minFont = minFont;
        this.maxFont = maxFont;
    }

    /**
     * Change the color of the words that are drawn. Applies to every word drawn after the is called
     * You can call this once per word if you want. Not tested.
     * @param color
     */
    public void setColor(Color color) {
        gc.setColor(color);
    }

    /**
     * Changes the font for all following words. Not tested.
     * @param fontName
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * Draw the given word. The size the word will be drawn will be based on the count.
     * The word is garunteed not to overlap with any other word.
     * @param word
     */

    public void addWord(WordCount word) {
        //random color for each word
        float b = .8f + ((1f- .8f)*rand.nextFloat());
        setColor(Color.getHSBColor(rand.nextFloat(),rand.nextFloat(),b));
        //
        Font font = new Font(fontName, Font.PLAIN, fontSize(word.getCount()));
        gc.setFont(font);
        FontRenderContext frc = gc.getFontMetrics().getFontRenderContext();
        TextLayout layout = new TextLayout(word.getWord(), font, frc);

        Shape base = layout.getOutline(null);
        double speed = speedMult*Math.sqrt(Math.pow(base.getBounds().width,2)+Math.pow(base.getBounds().height,2));

        int t = -1;
        Shape shape = null;
        do {
            t++;
            double angle = Math.sqrt(2)*Math.sqrt(speed*t);
            double x = img.getWidth()/2.0 - base.getBounds().getCenterX() + angle * Math.cos(angle);
            double y = img.getHeight()/2.0 - base.getBounds().getCenterY() + angle * Math.sin(angle);
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);

            shape = layout.getOutline(at);

        } while (intersects(shape));
        gc.fill(shape);
        others.add(shape);
    }

    /**
     * Save the image to file in the ".png" (portable network graphics) image format.
     * This should probably be the last method called on this object.
     * @param fileName
     */
    public void save(String fileName) {
        try {
            ImageIO.write(img, "png", new File(fileName));
        } catch (IOException e) {
            // let's just wrap that in a runtime exception, shall we?
            throw new RuntimeException(e);
        }
    }

    private int fontSize(int count) {
        if (count > maxCount) {
            count = maxCount;
        } else if (count < minCount) {
            count = minCount;
        }
        return (int)((1.0*count - minCount)/(maxCount-minCount)*(maxFont-minFont)+minFont);
    }

    private boolean intersects(Shape shape) {
        Area area = new Area(shape);
        for(Shape other:others) {
            Area otherA = new Area(other);
            otherA.intersect(area);
            if(!otherA.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
