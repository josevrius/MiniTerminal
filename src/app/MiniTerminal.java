package app;

import entities.MiniFileManager;

public final class MiniTerminal {

    public void launchApp() {

        System.out.println();
        System.out.println("MINI TERMINAL");
        System.out.println("=============");
        MiniFileManager mfm = new MiniFileManager();
        mfm.enterCommand();
    }
}
