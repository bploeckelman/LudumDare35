package lando.systems.ld35.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld35.ActionResolver;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.utils.Config;

public class DesktopLauncher {
    public static void main (String[] args) {
        DesktopActionResolver actionResolver = new DesktopActionResolver();
        for (String arg : args) {
            try {
                if (arg.equalsIgnoreCase("help") || arg.equalsIgnoreCase("h")) {
                    showHelp();
                } else if (arg.equalsIgnoreCase("fps")){
                   actionResolver.showFPS = true;
                } else if (arg.equalsIgnoreCase("freeplay")){
                    actionResolver.freePlay = true;
                } else if (arg.contains("fullscreen")) {
                    String[] parts = arg.split("=");
                    if (parts.length >= 2) {
                        actionResolver.fullScreen = Boolean.parseBoolean(parts[1]);
                    }
                } else if (arg.contains("showmouse")) {
                    String[] parts = arg.split("=");
                    if (parts.length >= 2) {
                        actionResolver.showMouseCursor = Boolean.parseBoolean(parts[1]);
                    }
                } else if (arg.contains("lives")){
                    String[] parts = arg.split("=");
                    if (parts.length >= 2) {
                        actionResolver.livesPerCredit = Integer.parseInt(parts[1]);
                    }
                }
                else if (arg.contains("continues")){
                    String[] parts = arg.split("=");
                    if (parts.length >= 2) {
                        actionResolver.continuesPerCredit = Integer.parseInt(parts[1]);
                    }
                }
                else {
                    throw new Exception("Unknown Command...");
                }
            }catch(Exception ex){
                System.out.println(ex.getMessage());
                showHelp();
            }

        }

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Config.gameWidth;
        config.height = Config.gameHeight;
        config.resizable = false;
        config.fullscreen = actionResolver.fullScreen;
        new LwjglApplication(new LudumDare35(actionResolver), config);
    }

    public static void showHelp(){
        System.out.println("Available Arguments");
        System.out.println("help (h) - This messsage");
        System.out.println("freeplay - Don't use coins to add credits (Default: coin op)");
        System.out.println("fullscreen={true:false} - make the game full screen (Default: true)");
        System.out.println("showmouse={true:false} - Show the mouse cursor (Default: false)");
        System.out.println("lives=# - Number of lives per credit (Default: 5)");
        System.out.println("continues=# - Number of Continues (Default: 3)");
        System.out.println("Exiting");
        System.exit(0);
    }
}
