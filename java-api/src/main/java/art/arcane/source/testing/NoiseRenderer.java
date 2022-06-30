package art.arcane.source.testing;

import art.arcane.source.api.NoisePlane;
import art.arcane.source.api.noise.provider.ExponentProvider;
import art.arcane.source.api.noise.provider.ExpressionProvider;
import art.arcane.source.api.noise.provider.NoisePlaneProvider;
import art.arcane.source.api.noise.provider.PerlinProvider;
import art.arcane.source.api.noise.provider.SimplexProvider;
import art.arcane.source.api.script.NoisePlaneConstructor;
import art.arcane.source.api.util.NoisePreset;
import com.dfsek.paralithic.eval.tokenizer.ParseException;

import javax.script.ScriptException;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NoiseRenderer {
    public static JFrame frame;

    public static void main(String[] a) throws ScriptException, ParseException {
        showNoise(new ExpressionProvider("sin(x + y)"));
    }

    public static void showNoise(NoisePlane g)
    {
        frame = new NoiseRenderFrame(g);
        frame.setSize(700, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.update(frame.getGraphics());

        new Thread(()->{
            while(frame.isVisible())
            {
                try {
                    Thread.sleep(250);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }frame.update(frame.getGraphics());
            }
        }).start();
    }

    public static class NoiseRenderFrame extends JFrame
    {
        private final NoisePlane generator;
        private BufferedImage image;
        private int lw;
        private int lh;

        public NoiseRenderFrame(NoisePlane generator)
        {
            this.generator = generator;
            lw = getWidth();
            lh = getHeight();
        }

        @Override
        public void update(Graphics g) {
            super.update(g);

            if(g instanceof Graphics2D g2)
            {
                g2.drawString("Rendering...", getWidth() / 2, getHeight() / 2);
                g2.drawImage(render(), 0, 0, getWidth(), getHeight(), new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return true;
                    }
                });
            }
        }

        public BufferedImage render()
        {
           if(image == null || lw != getWidth() || lh != getHeight())
           {
               long ms = System.currentTimeMillis();
               lw = getWidth();
               lh = getHeight();
               image = new BufferedImage(lw, lh, BufferedImage.TYPE_INT_RGB);

               for(int i = 0; i < lw; i++)
               {
                   for(int j = 0; j < lh; j++)
                   {
                       double value = generator.noise(i - (lw/2), j - (lh/2));
                       image.setRGB(i, j, Color.getHSBColor((float)value, 1f, 1f).getRGB());
                   }
               }
               long dur = System.currentTimeMillis() - ms;
               System.out.println("Render took " + dur + " ms");
           }

            return image;
        }
    }
}
