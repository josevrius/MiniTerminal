package app;

import entities.minifilemanager.MiniFileManager;

public final class MiniTerminal {

    public void launchApp() {

        System.out.println("MINI TERMINAL");
        System.out.println("=============");
        MiniFileManager mfm = new MiniFileManager();
        mfm.enterCommand();
    }
}
