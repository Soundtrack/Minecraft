package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class CanvasIsomPreview extends Canvas
    implements KeyListener, MouseListener, MouseMotionListener, Runnable
{
    private int currentRender;
    private int zoom;
    private boolean showHelp;
    private World level;
    private File workDir;
    private boolean running;
    private java.util.List zonesToRender;
    private IsoImageBuffer zoneMap[][];
    private int field_1785_i;
    private int field_1784_j;
    private int field_1783_k;
    private int field_1782_l;

    public File getWorkingDirectory()
    {
        if (workDir == null)
        {
            workDir = getWorkingDirectory("minecraft");
        }
        return workDir;
    }

    public File getWorkingDirectory(String s)
    {
        String s1 = System.getProperty("user.home", ".");
        File file;
        switch (OsMap.field_1193_a[getPlatform().ordinal()])
        {
            case 1:
            case 2:
                file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
                break;

            case 3:
                String s2 = System.getenv("APPDATA");
                if (s2 != null)
                {
                    file = new File(s2, (new StringBuilder()).append(".").append(s).append('/').toString());
                }
                else
                {
                    file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
                }
                break;

            case 4:
                file = new File(s1, (new StringBuilder()).append("Library/Application Support/").append(s).toString());
                break;

            default:
                file = new File(s1, (new StringBuilder()).append(s).append('/').toString());
                break;
        }
        if (!file.exists() && !file.mkdirs())
        {
            throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(file).toString());
        }
        else
        {
            return file;
        }
    }

    private static EnumOS1 getPlatform()
    {
        String s = System.getProperty("os.name").toLowerCase();
        if (s.contains("win"))
        {
            return EnumOS1.windows;
        }
        if (s.contains("mac"))
        {
            return EnumOS1.macos;
        }
        if (s.contains("solaris"))
        {
            return EnumOS1.solaris;
        }
        if (s.contains("sunos"))
        {
            return EnumOS1.solaris;
        }
        if (s.contains("linux"))
        {
            return EnumOS1.linux;
        }
        if (s.contains("unix"))
        {
            return EnumOS1.linux;
        }
        else
        {
            return EnumOS1.unknown;
        }
    }

    public CanvasIsomPreview()
    {
        currentRender = 0;
        zoom = 2;
        showHelp = true;
        running = true;
        zonesToRender = Collections.synchronizedList(new LinkedList());
        zoneMap = new IsoImageBuffer[64][64];
        workDir = getWorkingDirectory();
        for (int i = 0; i < 64; i++)
        {
            for (int j = 0; j < 64; j++)
            {
                zoneMap[i][j] = new IsoImageBuffer(null, i, j);
            }
        }

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        setBackground(Color.red);
    }

    public void loadLevel(String s)
    {
        field_1785_i = field_1784_j = 0;
        level = new World(new SaveHandler(new File(workDir, "saves"), s, false), s, new WorldSettings((new Random()).nextLong(), 0, true, false, EnumWorldType.DEFAULT));
        level.skylightSubtracted = 0;
        synchronized (zonesToRender)
        {
            zonesToRender.clear();
            for (int i = 0; i < 64; i++)
            {
                for (int j = 0; j < 64; j++)
                {
                    zoneMap[i][j].init(level, i, j);
                }
            }
        }
    }

    private void setBrightness(int i)
    {
        synchronized (zonesToRender)
        {
            level.skylightSubtracted = i;
            zonesToRender.clear();
            for (int j = 0; j < 64; j++)
            {
                for (int k = 0; k < 64; k++)
                {
                    zoneMap[j][k].init(level, j, k);
                }
            }
        }
    }

    public void start()
    {
        (new ThreadRunIsoClient(this)).start();
        for (int i = 0; i < 8; i++)
        {
            (new Thread(this)).start();
        }
    }

    public void stop()
    {
        running = false;
    }

    private IsoImageBuffer getZone(int i, int j)
    {
        int k = i & 0x3f;
        int l = j & 0x3f;
        IsoImageBuffer isoimagebuffer = zoneMap[k][l];
        if (isoimagebuffer.x == i && isoimagebuffer.y == j)
        {
            return isoimagebuffer;
        }
        synchronized (zonesToRender)
        {
            zonesToRender.remove(isoimagebuffer);
        }
        isoimagebuffer.init(i, j);
        return isoimagebuffer;
    }

    public void run()
    {
        TerrainTextureManager terraintexturemanager = new TerrainTextureManager();
        while (running)
        {
            IsoImageBuffer isoimagebuffer = null;
            synchronized (zonesToRender)
            {
                if (zonesToRender.size() > 0)
                {
                    isoimagebuffer = (IsoImageBuffer)zonesToRender.remove(0);
                }
            }
            if (isoimagebuffer != null)
            {
                if (currentRender - isoimagebuffer.lastVisible < 2)
                {
                    terraintexturemanager.render(isoimagebuffer);
                    repaint();
                }
                else
                {
                    isoimagebuffer.addedToRenderQueue = false;
                }
            }
            try
            {
                Thread.sleep(2L);
            }
            catch (InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
    }

    public void update(Graphics g)
    {
    }

    public void paint(Graphics g)
    {
    }

    public void render()
    {
        BufferStrategy bufferstrategy = getBufferStrategy();
        if (bufferstrategy == null)
        {
            createBufferStrategy(2);
            return;
        }
        else
        {
            render((Graphics2D)bufferstrategy.getDrawGraphics());
            bufferstrategy.show();
            return;
        }
    }

    public void render(Graphics2D graphics2d)
    {
        currentRender++;
        java.awt.geom.AffineTransform affinetransform = graphics2d.getTransform();
        graphics2d.setClip(0, 0, getWidth(), getHeight());
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2d.translate(getWidth() / 2, getHeight() / 2);
        graphics2d.scale(zoom, zoom);
        graphics2d.translate(field_1785_i, field_1784_j);
        if (level != null)
        {
            ChunkCoordinates chunkcoordinates = level.getSpawnPoint();
            graphics2d.translate(-(chunkcoordinates.posX + chunkcoordinates.posZ), -(-chunkcoordinates.posX + chunkcoordinates.posZ) + 64);
        }
        Rectangle rectangle = graphics2d.getClipBounds();
        graphics2d.setColor(new Color(0xff101020));
        graphics2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        byte byte0 = 16;
        byte byte1 = 3;
        int i = rectangle.x / byte0 / 2 - 2 - byte1;
        int j = (rectangle.x + rectangle.width) / byte0 / 2 + 1 + byte1;
        int k = rectangle.y / byte0 - 1 - byte1 * 2;
        int l = (rectangle.y + rectangle.height + 16 + level.worldHeight) / byte0 + 1 + byte1 * 2;
        for (int i1 = k; i1 <= l; i1++)
        {
            for (int k1 = i; k1 <= j; k1++)
            {
                int l1 = k1 - (i1 >> 1);
                int i2 = k1 + (i1 + 1 >> 1);
                IsoImageBuffer isoimagebuffer = getZone(l1, i2);
                isoimagebuffer.lastVisible = currentRender;
                if (!isoimagebuffer.rendered)
                {
                    if (!isoimagebuffer.addedToRenderQueue)
                    {
                        isoimagebuffer.addedToRenderQueue = true;
                        zonesToRender.add(isoimagebuffer);
                    }
                    continue;
                }
                isoimagebuffer.addedToRenderQueue = false;
                if (!isoimagebuffer.noContent)
                {
                    int j2 = k1 * byte0 * 2 + (i1 & 1) * byte0;
                    int k2 = i1 * byte0 - level.worldHeight - 16;
                    graphics2d.drawImage(isoimagebuffer.image, j2, k2, null);
                }
            }
        }

        if (showHelp)
        {
            graphics2d.setTransform(affinetransform);
            int j1 = getHeight() - 32 - 4;
            graphics2d.setColor(new Color(0x80000000, true));
            graphics2d.fillRect(4, getHeight() - 32 - 4, getWidth() - 8, 32);
            graphics2d.setColor(Color.WHITE);
            String s = "F1 - F5: load levels   |   0-9: Set time of day   |   Space: return to spawn   |   Double click: zoom   |   Escape: hide this text";
            graphics2d.drawString(s, getWidth() / 2 - graphics2d.getFontMetrics().stringWidth(s) / 2, j1 + 20);
        }
        graphics2d.dispose();
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        int i = mouseevent.getX() / zoom;
        int j = mouseevent.getY() / zoom;
        field_1785_i += i - field_1783_k;
        field_1784_j += j - field_1782_l;
        field_1783_k = i;
        field_1782_l = j;
        repaint();
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if (mouseevent.getClickCount() == 2)
        {
            zoom = 3 - zoom;
            repaint();
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        int i = mouseevent.getX() / zoom;
        int j = mouseevent.getY() / zoom;
        field_1783_k = i;
        field_1782_l = j;
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void keyPressed(KeyEvent keyevent)
    {
        if (keyevent.getKeyCode() == 48)
        {
            setBrightness(11);
        }
        if (keyevent.getKeyCode() == 49)
        {
            setBrightness(10);
        }
        if (keyevent.getKeyCode() == 50)
        {
            setBrightness(9);
        }
        if (keyevent.getKeyCode() == 51)
        {
            setBrightness(7);
        }
        if (keyevent.getKeyCode() == 52)
        {
            setBrightness(6);
        }
        if (keyevent.getKeyCode() == 53)
        {
            setBrightness(5);
        }
        if (keyevent.getKeyCode() == 54)
        {
            setBrightness(3);
        }
        if (keyevent.getKeyCode() == 55)
        {
            setBrightness(2);
        }
        if (keyevent.getKeyCode() == 56)
        {
            setBrightness(1);
        }
        if (keyevent.getKeyCode() == 57)
        {
            setBrightness(0);
        }
        if (keyevent.getKeyCode() == 112)
        {
            loadLevel("World1");
        }
        if (keyevent.getKeyCode() == 113)
        {
            loadLevel("World2");
        }
        if (keyevent.getKeyCode() == 114)
        {
            loadLevel("World3");
        }
        if (keyevent.getKeyCode() == 115)
        {
            loadLevel("World4");
        }
        if (keyevent.getKeyCode() == 116)
        {
            loadLevel("World5");
        }
        if (keyevent.getKeyCode() == 32)
        {
            field_1785_i = field_1784_j = 0;
        }
        if (keyevent.getKeyCode() == 27)
        {
            showHelp = !showHelp;
        }
        repaint();
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    static boolean isRunning(CanvasIsomPreview canvasisompreview)
    {
        return canvasisompreview.running;
    }
}
