package art.arcane.source.testing;

import art.arcane.source.api.NoisePlane;
import art.arcane.source.api.noise.Generator;
import art.arcane.source.api.noise.provider.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class NoiseRenderer {
    public static void main(String[] a)
    {
        Generator g = new Generator(new SimplexProvider(1337));
        g.scale = 0.1;
        NoisePlane p = new MirroredCacheProvider(g, 1024, 1024);
        p.benchmark("Simplex", 1000);
        Generator gg = new Generator(p);
        showNoise(gg);



























    }

    public static JFrame frame;

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
                    Thread.sleep(1000);

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
