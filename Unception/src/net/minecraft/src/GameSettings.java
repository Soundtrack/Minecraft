package net.minecraft.src;

import java.io.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GameSettings
{
    private static final String RENDER_DISTANCES[] =
    {
        "options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"
    };
    private static final String DIFFICULTIES[] =
    {
        "options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"
    };
    private static final String GUISCALES[] =
    {
        "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"
    };
    private static final String PARTICLES[] =
    {
        "options.particles.all", "options.particles.decreased", "options.particles.minimal"
    };
    private static final String LIMIT_FRAMERATES[] =
    {
        "performance.max", "performance.balanced", "performance.powersaver"
    };
    public float musicVolume;
    public float soundVolume;
    public float mouseSensitivity;
    public boolean invertMouse;
    public int renderDistance;
    public boolean viewBobbing;
    public boolean anaglyph;
    public boolean advancedOpengl;
    public int limitFramerate;
    public boolean fancyGraphics;
    public boolean ambientOcclusion;
    public boolean clouds;
    public String skin;
    public KeyBinding keyBindForward;
    public KeyBinding keyBindLeft;
    public KeyBinding keyBindBack;
    public KeyBinding keyBindRight;
    public KeyBinding keyBindJump;
    public KeyBinding keyBindInventory;
    public KeyBinding keyBindDrop;
    public KeyBinding keyBindChat;
    public KeyBinding keyBindSneak;
    public KeyBinding keyBindAttack;
    public KeyBinding keyBindUseItem;
    public KeyBinding keyBindPlayerList;
    public KeyBinding keyBindPickBlock;
    public KeyBinding keyBindings[];
    protected Minecraft mc;
    private File optionsFile;
    public int difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public String lastServer;
    public boolean noclip;
    public boolean smoothCamera;
    public boolean debugCamEnable;
    public float noclipRate;
    public float debugCamRate;
    public float fovSetting;
    public float gammaSetting;
    public int guiScale;
    public int particles;
    public String field_44018_Q;

    public GameSettings(Minecraft minecraft, File file)
    {
        musicVolume = 1.0F;
        soundVolume = 1.0F;
        mouseSensitivity = 0.5F;
        invertMouse = false;
        renderDistance = 0;
        viewBobbing = true;
        anaglyph = false;
        advancedOpengl = false;
        limitFramerate = 1;
        fancyGraphics = true;
        ambientOcclusion = true;
        clouds = true;
        skin = "Default";
        keyBindForward = new KeyBinding("key.forward", 17);
        keyBindLeft = new KeyBinding("key.left", 30);
        keyBindBack = new KeyBinding("key.back", 31);
        keyBindRight = new KeyBinding("key.right", 32);
        keyBindJump = new KeyBinding("key.jump", 57);
        keyBindInventory = new KeyBinding("key.inventory", 18);
        keyBindDrop = new KeyBinding("key.drop", 16);
        keyBindChat = new KeyBinding("key.chat", 20);
        keyBindSneak = new KeyBinding("key.sneak", 42);
        keyBindAttack = new KeyBinding("key.attack", -100);
        keyBindUseItem = new KeyBinding("key.use", -99);
        keyBindPlayerList = new KeyBinding("key.playerlist", 15);
        keyBindPickBlock = new KeyBinding("key.pickItem", -98);
        keyBindings = (new KeyBinding[]
                {
                    keyBindAttack, keyBindUseItem, keyBindForward, keyBindLeft, keyBindBack, keyBindRight, keyBindJump, keyBindSneak, keyBindDrop, keyBindInventory,
                    keyBindChat, keyBindPlayerList, keyBindPickBlock
                });
        difficulty = 2;
        hideGUI = false;
        thirdPersonView = 0;
        showDebugInfo = false;
        lastServer = "";
        noclip = false;
        smoothCamera = false;
        debugCamEnable = false;
        noclipRate = 1.0F;
        debugCamRate = 1.0F;
        fovSetting = 0.0F;
        gammaSetting = 0.0F;
        guiScale = 0;
        particles = 0;
        field_44018_Q = "en_US";
        mc = minecraft;
        optionsFile = new File(file, "options.txt");
        loadOptions();
    }

    public GameSettings()
    {
        musicVolume = 1.0F;
        soundVolume = 1.0F;
        mouseSensitivity = 0.5F;
        invertMouse = false;
        renderDistance = 0;
        viewBobbing = true;
        anaglyph = false;
        advancedOpengl = false;
        limitFramerate = 1;
        fancyGraphics = true;
        ambientOcclusion = true;
        clouds = true;
        skin = "Default";
        keyBindForward = new KeyBinding("key.forward", 17);
        keyBindLeft = new KeyBinding("key.left", 30);
        keyBindBack = new KeyBinding("key.back", 31);
        keyBindRight = new KeyBinding("key.right", 32);
        keyBindJump = new KeyBinding("key.jump", 57);
        keyBindInventory = new KeyBinding("key.inventory", 18);
        keyBindDrop = new KeyBinding("key.drop", 16);
        keyBindChat = new KeyBinding("key.chat", 20);
        keyBindSneak = new KeyBinding("key.sneak", 42);
        keyBindAttack = new KeyBinding("key.attack", -100);
        keyBindUseItem = new KeyBinding("key.use", -99);
        keyBindPlayerList = new KeyBinding("key.playerlist", 15);
        keyBindPickBlock = new KeyBinding("key.pickItem", -98);
        keyBindings = (new KeyBinding[]
                {
                    keyBindAttack, keyBindUseItem, keyBindForward, keyBindLeft, keyBindBack, keyBindRight, keyBindJump, keyBindSneak, keyBindDrop, keyBindInventory,
                    keyBindChat, keyBindPlayerList, keyBindPickBlock
                });
        difficulty = 2;
        hideGUI = false;
        thirdPersonView = 0;
        showDebugInfo = false;
        lastServer = "";
        noclip = false;
        smoothCamera = false;
        debugCamEnable = false;
        noclipRate = 1.0F;
        debugCamRate = 1.0F;
        fovSetting = 0.0F;
        gammaSetting = 0.0F;
        guiScale = 0;
        particles = 0;
        field_44018_Q = "en_US";
    }

    public String getKeyBindingDescription(int i)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        return stringtranslate.translateKey(keyBindings[i].keyDescription);
    }

    public String getOptionDisplayString(int i)
    {
        int j = keyBindings[i].keyCode;
        return getKeyDisplayString(j);
    }

    public static String getKeyDisplayString(int i)
    {
        if (i < 0)
        {
            return StatCollector.translateToLocalFormatted("key.mouseButton", new Object[]
                    {
                        Integer.valueOf(i + 101)
                    });
        }
        else
        {
            return Keyboard.getKeyName(i);
        }
    }

    public void setKeyBinding(int i, int j)
    {
        keyBindings[i].keyCode = j;
        saveOptions();
    }

    public void setOptionFloatValue(EnumOptions enumoptions, float f)
    {
        if (enumoptions == EnumOptions.MUSIC)
        {
            musicVolume = f;
            mc.sndManager.onSoundOptionsChanged();
        }
        if (enumoptions == EnumOptions.SOUND)
        {
            soundVolume = f;
            mc.sndManager.onSoundOptionsChanged();
        }
        if (enumoptions == EnumOptions.SENSITIVITY)
        {
            mouseSensitivity = f;
        }
        if (enumoptions == EnumOptions.FOV)
        {
            fovSetting = f;
        }
        if (enumoptions == EnumOptions.GAMMA)
        {
            gammaSetting = f;
        }
    }

    public void setOptionValue(EnumOptions enumoptions, int i)
    {
        if (enumoptions == EnumOptions.INVERT_MOUSE)
        {
            invertMouse = !invertMouse;
        }
        if (enumoptions == EnumOptions.RENDER_DISTANCE)
        {
            renderDistance = renderDistance + i & 3;
        }
        if (enumoptions == EnumOptions.GUI_SCALE)
        {
            guiScale = guiScale + i & 3;
        }
        if (enumoptions == EnumOptions.PARTICLES)
        {
            particles = (particles + i) % 3;
        }
        if (enumoptions == EnumOptions.VIEW_BOBBING)
        {
            viewBobbing = !viewBobbing;
        }
        if (enumoptions == EnumOptions.RENDER_CLOUDS)
        {
            clouds = !clouds;
        }
        if (enumoptions == EnumOptions.ADVANCED_OPENGL)
        {
            advancedOpengl = !advancedOpengl;
            mc.renderGlobal.loadRenderers();
        }
        if (enumoptions == EnumOptions.ANAGLYPH)
        {
            anaglyph = !anaglyph;
            mc.renderEngine.refreshTextures();
        }
        if (enumoptions == EnumOptions.FRAMERATE_LIMIT)
        {
            limitFramerate = (limitFramerate + i + 3) % 3;
        }
        if (enumoptions == EnumOptions.DIFFICULTY)
        {
            difficulty = difficulty + i & 3;
        }
        if (enumoptions == EnumOptions.GRAPHICS)
        {
            fancyGraphics = !fancyGraphics;
            mc.renderGlobal.loadRenderers();
        }
        if (enumoptions == EnumOptions.AMBIENT_OCCLUSION)
        {
            ambientOcclusion = !ambientOcclusion;
            mc.renderGlobal.loadRenderers();
        }
        saveOptions();
    }

    public float getOptionFloatValue(EnumOptions enumoptions)
    {
        if (enumoptions == EnumOptions.FOV)
        {
            return fovSetting;
        }
        if (enumoptions == EnumOptions.GAMMA)
        {
            return gammaSetting;
        }
        if (enumoptions == EnumOptions.MUSIC)
        {
            return musicVolume;
        }
        if (enumoptions == EnumOptions.SOUND)
        {
            return soundVolume;
        }
        if (enumoptions == EnumOptions.SENSITIVITY)
        {
            return mouseSensitivity;
        }
        else
        {
            return 0.0F;
        }
    }

    public boolean getOptionOrdinalValue(EnumOptions enumoptions)
    {
        switch (EnumOptionsMappingHelper.enumOptionsMappingHelperArray[enumoptions.ordinal()])
        {
            case 1:
                return invertMouse;

            case 2:
                return viewBobbing;

            case 3:
                return anaglyph;

            case 4:
                return advancedOpengl;

            case 5:
                return ambientOcclusion;

            case 6:
                return clouds;
        }
        return false;
    }

    public String getKeyBinding(EnumOptions enumoptions)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        String s = (new StringBuilder()).append(stringtranslate.translateKey(enumoptions.getEnumString())).append(": ").toString();
        if (enumoptions.getEnumFloat())
        {
            float f = getOptionFloatValue(enumoptions);
            if (enumoptions == EnumOptions.SENSITIVITY)
            {
                if (f == 0.0F)
                {
                    return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.sensitivity.min")).toString();
                }
                if (f == 1.0F)
                {
                    return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.sensitivity.max")).toString();
                }
                else
                {
                    return (new StringBuilder()).append(s).append((int)(f * 200F)).append("%").toString();
                }
            }
            if (enumoptions == EnumOptions.FOV)
            {
                if (f == 0.0F)
                {
                    return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.fov.min")).toString();
                }
                if (f == 1.0F)
                {
                    return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.fov.max")).toString();
                }
                else
                {
                    return (new StringBuilder()).append(s).append((int)(70F + f * 40F)).toString();
                }
            }
            if (enumoptions == EnumOptions.GAMMA)
            {
                if (f == 0.0F)
                {
                    return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.gamma.min")).toString();
                }
                if (f == 1.0F)
                {
                    return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.gamma.max")).toString();
                }
                else
                {
                    return (new StringBuilder()).append(s).append("+").append((int)(f * 100F)).append("%").toString();
                }
            }
            if (f == 0.0F)
            {
                return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.off")).toString();
            }
            else
            {
                return (new StringBuilder()).append(s).append((int)(f * 100F)).append("%").toString();
            }
        }
        if (enumoptions.getEnumBoolean())
        {
            boolean flag = getOptionOrdinalValue(enumoptions);
            if (flag)
            {
                return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.on")).toString();
            }
            else
            {
                return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.off")).toString();
            }
        }
        if (enumoptions == EnumOptions.RENDER_DISTANCE)
        {
            return (new StringBuilder()).append(s).append(stringtranslate.translateKey(RENDER_DISTANCES[renderDistance])).toString();
        }
        if (enumoptions == EnumOptions.DIFFICULTY)
        {
            return (new StringBuilder()).append(s).append(stringtranslate.translateKey(DIFFICULTIES[difficulty])).toString();
        }
        if (enumoptions == EnumOptions.GUI_SCALE)
        {
            return (new StringBuilder()).append(s).append(stringtranslate.translateKey(GUISCALES[guiScale])).toString();
        }
        if (enumoptions == EnumOptions.PARTICLES)
        {
            return (new StringBuilder()).append(s).append(stringtranslate.translateKey(PARTICLES[particles])).toString();
        }
        if (enumoptions == EnumOptions.FRAMERATE_LIMIT)
        {
            return (new StringBuilder()).append(s).append(StatCollector.translateToLocal(LIMIT_FRAMERATES[limitFramerate])).toString();
        }
        if (enumoptions == EnumOptions.GRAPHICS)
        {
            if (fancyGraphics)
            {
                return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.graphics.fancy")).toString();
            }
            else
            {
                return (new StringBuilder()).append(s).append(stringtranslate.translateKey("options.graphics.fast")).toString();
            }
        }
        else
        {
            return s;
        }
    }

    public void loadOptions()
    {
        try
        {
            if (!optionsFile.exists())
            {
                return;
            }
            BufferedReader bufferedreader = new BufferedReader(new FileReader(optionsFile));
            for (String s = ""; (s = bufferedreader.readLine()) != null;)
            {
                try
                {
                    String as[] = s.split(":");
                    if (as[0].equals("music"))
                    {
                        musicVolume = parseFloat(as[1]);
                    }
                    if (as[0].equals("sound"))
                    {
                        soundVolume = parseFloat(as[1]);
                    }
                    if (as[0].equals("mouseSensitivity"))
                    {
                        mouseSensitivity = parseFloat(as[1]);
                    }
                    if (as[0].equals("fov"))
                    {
                        fovSetting = parseFloat(as[1]);
                    }
                    if (as[0].equals("gamma"))
                    {
                        gammaSetting = parseFloat(as[1]);
                    }
                    if (as[0].equals("invertYMouse"))
                    {
                        invertMouse = as[1].equals("true");
                    }
                    if (as[0].equals("viewDistance"))
                    {
                        renderDistance = Integer.parseInt(as[1]);
                    }
                    if (as[0].equals("guiScale"))
                    {
                        guiScale = Integer.parseInt(as[1]);
                    }
                    if (as[0].equals("particles"))
                    {
                        particles = Integer.parseInt(as[1]);
                    }
                    if (as[0].equals("bobView"))
                    {
                        viewBobbing = as[1].equals("true");
                    }
                    if (as[0].equals("anaglyph3d"))
                    {
                        anaglyph = as[1].equals("true");
                    }
                    if (as[0].equals("advancedOpengl"))
                    {
                        advancedOpengl = as[1].equals("true");
                    }
                    if (as[0].equals("fpsLimit"))
                    {
                        limitFramerate = Integer.parseInt(as[1]);
                    }
                    if (as[0].equals("difficulty"))
                    {
                        difficulty = Integer.parseInt(as[1]);
                    }
                    if (as[0].equals("fancyGraphics"))
                    {
                        fancyGraphics = as[1].equals("true");
                    }
                    if (as[0].equals("ao"))
                    {
                        ambientOcclusion = as[1].equals("true");
                    }
                    if (as[0].equals("clouds"))
                    {
                        clouds = as[1].equals("true");
                    }
                    if (as[0].equals("skin"))
                    {
                        skin = as[1];
                    }
                    if (as[0].equals("lastServer") && as.length >= 2)
                    {
                        lastServer = as[1];
                    }
                    if (as[0].equals("lang") && as.length >= 2)
                    {
                        field_44018_Q = as[1];
                    }
                    int i = 0;
                    while (i < keyBindings.length)
                    {
                        if (as[0].equals((new StringBuilder()).append("key_").append(keyBindings[i].keyDescription).toString()))
                        {
                            keyBindings[i].keyCode = Integer.parseInt(as[1]);
                        }
                        i++;
                    }
                }
                catch (Exception exception1)
                {
                    System.out.println((new StringBuilder()).append("Skipping bad option: ").append(s).toString());
                }
            }

            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception exception)
        {
            System.out.println("Failed to load options");
            exception.printStackTrace();
        }
    }

    private float parseFloat(String s)
    {
        if (s.equals("true"))
        {
            return 1.0F;
        }
        if (s.equals("false"))
        {
            return 0.0F;
        }
        else
        {
            return Float.parseFloat(s);
        }
    }

    public void saveOptions()
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new FileWriter(optionsFile));
            printwriter.println((new StringBuilder()).append("music:").append(musicVolume).toString());
            printwriter.println((new StringBuilder()).append("sound:").append(soundVolume).toString());
            printwriter.println((new StringBuilder()).append("invertYMouse:").append(invertMouse).toString());
            printwriter.println((new StringBuilder()).append("mouseSensitivity:").append(mouseSensitivity).toString());
            printwriter.println((new StringBuilder()).append("fov:").append(fovSetting).toString());
            printwriter.println((new StringBuilder()).append("gamma:").append(gammaSetting).toString());
            printwriter.println((new StringBuilder()).append("viewDistance:").append(renderDistance).toString());
            printwriter.println((new StringBuilder()).append("guiScale:").append(guiScale).toString());
            printwriter.println((new StringBuilder()).append("particles:").append(particles).toString());
            printwriter.println((new StringBuilder()).append("bobView:").append(viewBobbing).toString());
            printwriter.println((new StringBuilder()).append("anaglyph3d:").append(anaglyph).toString());
            printwriter.println((new StringBuilder()).append("advancedOpengl:").append(advancedOpengl).toString());
            printwriter.println((new StringBuilder()).append("fpsLimit:").append(limitFramerate).toString());
            printwriter.println((new StringBuilder()).append("difficulty:").append(difficulty).toString());
            printwriter.println((new StringBuilder()).append("fancyGraphics:").append(fancyGraphics).toString());
            printwriter.println((new StringBuilder()).append("ao:").append(ambientOcclusion).toString());
            printwriter.println((new StringBuilder()).append("clouds:").append(clouds).toString());
            printwriter.println((new StringBuilder()).append("skin:").append(skin).toString());
            printwriter.println((new StringBuilder()).append("lastServer:").append(lastServer).toString());
            printwriter.println((new StringBuilder()).append("lang:").append(field_44018_Q).toString());
            for (int i = 0; i < keyBindings.length; i++)
            {
                printwriter.println((new StringBuilder()).append("key_").append(keyBindings[i].keyDescription).append(":").append(keyBindings[i].keyCode).toString());
            }

            printwriter.close();
        }
        catch (Exception exception)
        {
            System.out.println("Failed to save options");
            exception.printStackTrace();
        }
    }

    public boolean shouldRenderClouds()
    {
        return renderDistance < 2 && clouds;
    }
}
