package org.jvrb.java.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public final class MiniFileManager {

    private File getNewPath(File currentPath, String path) {
        File newPath;

        if (path.equals("..")) {
            newPath = new File(currentPath.getParent());
        } else {
            newPath = new File("\\" + path);
        }

        if (!path.isEmpty() && !path.isBlank() && newPath.exists()) {
            showPath(newPath);
        } else {
            System.out.println("ERROR: El directorio no existe");
            newPath = currentPath;
            showPath(currentPath);
        }
        return newPath;
    }

    private void list(File currentPath, String command) {
        boolean longListOK = command.equals("ll");

        for (File file : currentPath.listFiles()) {
            if (file.isDirectory()) {
                showInfo(file, longListOK);
            }
        }

        for (File file : currentPath.listFiles()) {
            if (file.isFile()) {
                showInfo(file, longListOK);
            }
        }
        showPath(currentPath);
    }

    private void createDir(File currentPath, String dir) {
        File newPath = new File(currentPath, dir);

        if (newPath.exists()) {
            System.out.println("ERROR: El directorio ya existe");
        } else {
            newPath.mkdir();
        }
        showPath(currentPath);
    }

    private void createFile(File currentPath, String file) {
        File newPath = new File(currentPath, file);

        if (newPath.exists()) {
            System.out.println("ERROR: El archivo ya existe");
        } else {
            try {
                newPath.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        showPath(currentPath);
    }

    private void writeDocument(File currentPath, String text, String file) {
        File newPath = new File(currentPath, file);

        if (!newPath.exists()) {
            try {
                newPath.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        if (newPath.isFile() && newPath.canWrite()) {
            if ((text.startsWith("\"") && text.endsWith("\""))) {
                text = debugText(text);
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(newPath, true))) {
                    bw.write(text + System.lineSeparator());
                } catch (Exception e) {
                    System.out.println("ERROR: no se puede escribir en el documento");
                }
            } else if (text.equals("rm")) {
                try (FileWriter fw = new FileWriter(newPath)) {
                    fw.write("");
                } catch (Exception e) {
                    System.out.println("ERROR: no se puede escribir en el documento");
                }
            }
        }
        showPath(currentPath);
    }

    private void delete(File currentPath, String path) {
        File newPath = new File(currentPath, path);

        if (newPath.exists()) {
            newPath.delete();
        } else {
            System.out.println("ERROR: La ruta no existe");
        }
        showPath(currentPath);
    }

    private void rename(File currentPath, String ActualName, String newName) {
        File newPath = new File(currentPath, ActualName);

        if (newPath.exists()) {
            newPath.renameTo(new File(currentPath, newName));
        } else {
            System.out.println("ERROR: El fichero no existe");
        }
        showPath(currentPath);
    }

    private void showHelp(File currentPath) {
        System.out.println();
        System.out.println("Lista de Comandos");
        System.out.println("=================");
        System.out.println("pwd ......................: Muestra la ruta actual");
        System.out.println("cd <DIR> .................: Cambia de directorio");
        System.out.println("cd <..> ..................: Cambia al directorio anterior");
        System.out.println("ls .......................: Muestra el contenido de la carpeta actual");
        System.out.println("ll .......................: Muestra el contenido extendido de la carpeta actual");
        System.out.println("mkdir <DIR> ..............: Crea un carpeta en el directorio actual");
        System.out.println("touch <FILE> .............: Crea un archivo en el directorio actual");
        System.out.println("echo \"<TEXT>\" / <FILE> ...: Escribe una línea de texto en el archivo indicado");
        System.out.println("echo rm / <FILE> .........: Borra todo el texto en el archivo indicado");
        System.out.println("rm <FILE> ................: Borra una carpeta o archivo en el directorio actual");
        System.out.println("mv <FILE1> / <FILE2> .....: Cambia de nombre una carpeta o archivo en el directorio actual");
        System.out.println("help .....................: Muestra la lista de comandos");
        System.out.println("exit .....................: Termina el programa");
        System.out.println();
        showPath(currentPath);
    }

    public void enterCommand() {
        File currentPath = getCurrentPath();
        String[] input;
        String command;
        String argument = "";
        String argument1 = "";
        String argument2 = "";
        boolean exit = false;

        do {
            input = debugInput(new Scanner(System.in).nextLine());
            command = input[0];
            if (input.length > 1) {
                argument = input[1];
                if (argument.contains(" / ")) {
                    argument1 = argument.split(" / ")[0];
                    argument2 = argument.split(" / ")[1];
                }
            }
            try {
                switch (command) {
                    case "pwd" -> currentPath = getCurrentPath();
                    case "cd" -> currentPath = getNewPath(currentPath, argument);
                    case "ls", "ll" -> list(currentPath, command);
                    case "mkdir" -> createDir(currentPath, argument);
                    case "touch" -> createFile(currentPath, argument);
                    case "echo" -> writeDocument(currentPath, argument1, argument2);
                    case "rm" -> delete(currentPath, argument);
                    case "mv" -> rename(currentPath, argument1, argument2);
                    case "help" -> showHelp(currentPath);
                    case "exit" -> exit = true;
                    default -> {
                        System.out.println("ERROR: Comando incorrecto");
                        showPath(currentPath);
                    }
                }
            } catch (Exception e) {
                showPath(currentPath);
            }
        } while (!exit);
    }

    private File getCurrentPath() {
        File currentPath = new File(System.getProperty("user.dir"));

        showPath(currentPath);
        return currentPath;
    }

    private String[] debugInput(String input) {
        return input.split(" ", 2);
    }

    private String debugText(String text) {
        return text.substring(1, text.length() - 1);
    }

    private void showPath(File path) {
        System.out.print(path.getAbsolutePath() + " > ");
    }

    private void showInfo(File file, boolean longListOK) {
        long size = file.length();
        Date date = new Date(file.lastModified());
        System.out.println(file.getName() + (longListOK ? " | " + size + " | " + date : ""));
    }
}
