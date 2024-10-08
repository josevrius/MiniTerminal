package org.jvrb.java.app;

import org.jvrb.java.entities.MiniFileManager;

public class MiniTerminal {

    public static void main(String[] args) {

        System.out.println("MINI TERMINAL");
        System.out.println("=============");
        MiniFileManager mfm = new MiniFileManager();
        mfm.enterCommand();
    }
}