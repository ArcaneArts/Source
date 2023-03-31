package art.arcane.source.ui;

import art.arcane.multiburst.BurstExecutor;
import art.arcane.multiburst.MultiBurst;
import art.arcane.source.NoisePlane;
import art.arcane.source.util.NoiseCacheMap;
import art.arcane.source.util.PrecisionStopwatch;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Visualizer  extends JPanel implements MouseWheelListener{
    private NoisePlane inNoise;
    private NoisePlane noise;
    private NoiseCacheMap cache;
    double lx = Double.MAX_VALUE; //MouseX
    double lz = Double.MAX_VALUE; //MouseY
    double mx = 0;
    double mz = 0;
    double ox = 0; //Offset X
    double oz = 0; //Offset Y
    double scale = 1.01;
    double falft = 1;
    static double ascale = 10;
    static double oxp = 0;
    static double ozp = 0;
    static double mxx = 0;
    static double mzz = 0;
    double tz;
    double t;
    double deltaFrames = 0;
    double lft = 0;
    java.util.List<Double> alft = new ArrayList<>();
    PrecisionStopwatch pDelta = PrecisionStopwatch.start();
    boolean forceAccurate = false;
    boolean ultrafast = false;

    public Visualizer(NoisePlane noise) {
        this.inNoise = noise;
        this.cache = new NoiseCacheMap(noise, 32, 32, 8192);
        addMouseWheelListener(this);
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point cp = e.getPoint();
                lx = (cp.getX());
                lz = (cp.getY());
                mx = lx;
                mz = lz;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point cp = e.getPoint();
                ox += (lx - cp.getX());
                oz += (lz - cp.getY());
                lx = cp.getX();
                lz = cp.getY();
            }
        });
        updateNoisePlane();
    }

    BufferedImage img = null;
    int w = 1;
    int h = 1;
    double accuracy = 0.01;
    double laccuracy = 0;
    AtomicBoolean busy = new AtomicBoolean(false);
    double lsc = scale + 0.001;

    public void updateNoisePlane() {
        if(scale != lsc) {
            lsc = scale;
            noise = inNoise.fit(0, 1).scale(scale);
            cache.setNoise(noise);
        }
    }

    @Override
    public void paint(Graphics g) {
        PrecisionStopwatch p = PrecisionStopwatch.start();

        if (g instanceof Graphics2D gg) {
            deltaLerp();

            if(forceAccurate) {
                accuracy = 1;
            }

            if(laccuracy != accuracy)
            {
                laccuracy = accuracy;
                img = null;
            }

            if(w != getParent().getWidth() || h != getParent().getHeight()) {
                w = getParent().getWidth();
                h = getParent().getHeight();
                img = null;
            }

            if(img == null) {
                img = new BufferedImage((int) Math.max(10, w * accuracy), (int) Math.max(10, h * accuracy), BufferedImage.TYPE_INT_RGB);
            }

            if(!busy.get()) {
                busy.set(true);
                BurstExecutor e = MultiBurst.burst.burst();
                cache.precacheParallel((int)ox, (int)oz, (int)Math.ceil(ox + w), (int)Math.ceil(oz + h), e);

                if(ultrafast)
                {
                    MultiBurst.burst.lazy(() -> {
                        e.complete();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }

                        busy.set(false);
                    });
                }

                else
                {
                    e.complete();
                    busy.set(false);
                }
            }

            double f;
            double minF = Double.MAX_VALUE;
            double maxF = Double.MIN_VALUE;
            for(int i = 0; i < img.getWidth(); i++) {
                for(int j = 0; j < img.getHeight(); j++) {
                    f = cache.getValueOr((i/accuracy) + ox, (j/accuracy) + oz, 0);
                    img.setRGB(i, j, getColorFor(f));
                    minF = Math.min(minF, f);
                    maxF = Math.max(maxF, f);
                }
            }

            String dbg = "LFT: " + ((int)falft) + "ms ACC: " + (int)(accuracy * 100) + "%";
            dbg += " \nMAX: " + maxF + " \nMIN: " + minF;
            gg.drawImage(img, 0, 0, getParent().getWidth(), getParent().getHeight(), (img, infoflags, x, y, width, height) -> true);
            gg.setColor(ultrafast ? Color.cyan : Color.red);
            gg.drawString(dbg, 5, 10);
        }

        lft = p.getMilliseconds();
        alft.add(lft);

        if (!isVisible()) {
            return;
        }

        if (!getParent().isVisible()) {
            return;
        }

        if (!getParent().getParent().isVisible()) {
            return;
        }

        EventQueue.invokeLater(() ->
        {
            try {
                Thread.sleep((long) Math.max(1, 5 - lft));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repaint();
        });
    }

    public static void launch(NoisePlane noise) {
        EventQueue.invokeLater(() -> createAndShowGUI(noise));
    }

    private static void createAndShowGUI(NoisePlane noise) {
        JFrame frame = new JFrame("Noise Explorer");
        Visualizer nv = new Visualizer(noise);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JLayeredPane pane = new JLayeredPane();
        nv.setSize(new Dimension(1440, 820));
        pane.add(nv, 1, 0);
        frame.add(pane);
        frame.setSize(1440, 820);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

            }
        });
        frame.addKeyListener(new KeyListener() {
            double lacc = 0.5;
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    lacc = nv.accuracy;
                    nv.forceAccurate = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

                if(e.getKeyCode() == KeyEvent.VK_U)
                {
                    nv.ultrafast = !nv.ultrafast;
                }
                if(e.getKeyCode() == KeyEvent.VK_SHIFT)
                {
                    nv.forceAccurate = false;
                    nv.accuracy = lacc;
                }
            }
        });
    }

    public int getColorFor(double nValue) {
        if(nValue > 1)
        {
            return Color.RED.getRGB();
        }

        else if(nValue < 0) {
            return Color.BLUE.getRGB();
        }

        if(true) {
            return Color.getHSBColor(0, 0, (float)nValue).getRGB();
        }

        return Color.getHSBColor((float)nValue,
                (float)(Math.pow(nValue, 0.75)),
                (float)Math.pow(nValue, 0.475)).getRGB();
    }

    private void deltaLerp() {
        deltaFrames += lft/(1000D/60D);
        pDelta.reset();
        pDelta.begin();

        if(alft.size() > 1)
        {
            while(alft.size() > 8)
            {
                alft.remove(0);
            }

            falft = 0;

            for(double i : alft)
            {
                falft+= i;
            }

            falft /= alft.size();
            if(falft > 30) {
                accuracy-= 0.01;
            }

            else if(falft < 15) {
                accuracy+= 0.001;
            }

            accuracy = accuracy > 1 ? 1 : accuracy < 0.1 ? 0.1 : accuracy;
        }

        while(deltaFrames-- > 0) {
            lerp();
        }

        updateNoisePlane();
    }

    private void lerp() {
        if (scale < ascale) {
            ascale -= Math.abs(scale - ascale) * 0.16;
        }

        if (scale > ascale) {
            ascale += Math.abs(ascale - scale) * 0.16;
        }

        if (t < tz) {
            tz -= Math.abs(t - tz) * 0.29;
        }

        if (t > tz) {
            tz += Math.abs(tz - t) * 0.29;
        }

        if (ox < oxp) {
            oxp -= Math.abs(ox - oxp) * 0.16;
        }

        if (ox > oxp) {
            oxp += Math.abs(oxp - ox) * 0.16;
        }

        if (oz < ozp) {
            ozp -= Math.abs(oz - ozp) * 0.16;
        }

        if (oz > ozp) {
            ozp += Math.abs(ozp - oz) * 0.16;
        }

        if (mx < mxx) {
            mxx -= Math.abs(mx - mxx) * 0.16;
        }

        if (mx > mxx) {
            mxx += Math.abs(mxx - mx) * 0.16;
        }

        if (mz < mzz) {
            mzz -= Math.abs(mz - mzz) * 0.16;
        }

        if (mz > mzz) {
            mzz += Math.abs(mzz - mz) * 0.16;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (e.isControlDown()) {
            t = t + ((0.0025 * t) * notches);
            return;
        }

        scale = scale + ((0.044 * scale) * notches);
        scale = Math.max(scale, 0.00001);
    }
}
