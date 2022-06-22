package art.arcane.source.testing;

import art.arcane.source.api.accessor.ValueAccessor2D;
import art.arcane.source.api.accessor.ValueAccessor3D;
import art.arcane.source.api.interpolator.HermiteInterpolator;
import art.arcane.source.api.interpolator.Interpolator;
import art.arcane.source.api.interpolator.LinearInterpolator;
import art.arcane.source.api.interpolator.StarcastInterpolator;
import art.arcane.source.api.noise.CompositeGenerator;
import art.arcane.source.api.noise.Generator;
import art.arcane.source.api.noise.provider.*;

import javax.sound.sampled.Line;
import javax.sql.rowset.CachedRowSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class NoiseRenderer {
    public static void main(String[] a)
    {
        Generator g = new Generator( new SimplexProvider(1337));
        g.scale = 0.1 / 4;
        Generator w = new Generator(new PerlinHermiteNoiseProvider(23454));
        w.scale = 0.01 / 4;
        w.maxOutput = 9;
        w.minOutput = -9;
        g.warp = w;
        Generator ww = new Generator(new WhiteProvider(234154));
        ww.scale = 0.01 / 8;
        ww.maxOutput = 0.01;
        ww.minOutput = -0.01;
        w.warp = ww;
        ValueAccessor3D gg = new StarcastInterpolator(g, 64, 12);
        CompositeGenerator c = new CompositeGenerator(CompositeGenerator.CompositeMode.ADD);
        Generator gxc = new Generator(new SimplexProvider(234985));
        gxc.scale = 0.0001;
        c.add(gg);
        c.add(gxc);
        Generator gx = new Generator(c);
        gx.maxInput = 2;
        gx.minInput = 0;

        showNoise(gx);



























    }

    public static JFrame frame;

    public static void showNoise(ValueAccessor2D g)
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
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }frame.update(frame.getGraphics());
            }
        }).start();
    }

    public static class NoiseRenderFrame extends JFrame
    {
        private final ValueAccessor2D generator;
        private BufferedImage image;
        private int lw;
        private int lh;

        public NoiseRenderFrame(ValueAccessor2D generator)
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
                        double value = generator.noise(i, j);
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
