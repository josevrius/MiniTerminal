package entities.minifilemanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Scanner;

public final class MiniFileManager {

    private File getCurrentPath() {
        File currentPath = new File(System.getProperty("user.dir"));

        MiniFileManagerUtils.showPath(currentPath);
        return currentPath;
    }

    private File getNewPath(File currentPath, String path) {
        File newPath;

        if (path.equals("..")) {
            newPath = new File(currentPath.getParent());
        } else {
            newPath = new File("\\" + path);
        }

        if (!path.isEmpty() && !path.isBlank() && newPath.exists()) {
            MiniFileManagerUtils.showPath(newPath);
        } else {
            newPath = currentPath;
            System.out.println("ERROR: El directorio no existe");
            MiniFileManagerUtils.showPath(currentPath);
        }
        return newPath;
    }

    private void list(File currentPath, String command) {
        long size;
        Date date;
        boolean longListOK = command.equals("ll");

        System.out.println();
        for (File file : currentPath.listFiles()) {
            if (file.isDirectory()) {
                size = file.length();
                date = new Date(file.lastModified());
                System.out.println(file.getName() + (longListOK ? " | " + size + " | " + date : ""));
            }
        }
        for (File file : currentPath.listFiles()) {
            if (file.isFile()) {
                size = file.length();
                date = new Date(file.lastModified());
                System.out.println(file.getName() + (longListOK ? " | " + size + " | " + date : ""));
            }
        }
        System.out.println();
        MiniFileManagerUtils.showPath(currentPath);
    }

    private void createDir(File currentPath, String dir) {
        File newPath = new File(currentPath, dir);

        if (newPath.exists()) {
            System.out.println("ERROR: El directorio ya existe");
        } else {
            newPath.mkdir();
        }
        MiniFileManagerUtils.showPath(currentPath);
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
        MiniFileManagerUtils.showPath(currentPath);
    }

    private void writeDocument(File currentPath, String text, String file) {
        File newPath = new File(currentPath, file);

        if (newPath.isFile() && newPath.canWrite()) {
            if ((text.startsWith("\"") && text.endsWith("\""))) {
                text = MiniFileManagerUtils.debugText(text);
                try {
                    Files.write(newPath.toPath(), text.getBytes(), StandardOpenOption.APPEND);
                    Files.write(newPath.toPath(), "\n".getBytes(), StandardOpenOption.APPEND);
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
        MiniFileManagerUtils.showPath(currentPath);
    }

    private void delete(File currentPath, String path) {
        File newPath = new File(currentPath, path);

        if (newPath.exists()) {
            newPath.delete();
        } else {
            System.out.println("ERROR: La ruta no existe");
        }
        MiniFileManagerUtils.showPath(currentPath);
    }

    private void rename(File currentPath, String ActualName, String newName) {
        File newPath = new File(currentPath, ActualName);

        if (newPath.exists()) {
            newPath.renameTo(new File(currentPath, newName));
        } else {
            System.out.println("ERROR: El fichero no existe");
        }
        MiniFileManagerUtils.showPath(currentPath);
    }

    private void showHelp(File currentPath) {
        System.out.println();
        System.out.println("Lista de Comandos");
        System.out.println("=================");
        System.out.println("pwd ......................: Muestra la ruta actual");
        System.out.println("cd <DIR> .................: Cambia de directorio");
        System.out.println("cd <..> ..................: Cambia al directorio anterior");
        System.out.println("ls .......................: Muestra el contenido de la carpeta actual");
        System.out.println("ll .......................: Muestra el contenido de la carpeta actual con el tamaño y la fecha");
        System.out.println("mkdir <DIR> ..............: Crea un directorio en la carpeta actual");
        System.out.println("touch <FILE> .............: Crea un archivo en la carpeta actual");
        System.out.println("echo \"<TEXT>\" / <FILE> ...: Escribe una línea de texto en el archivo indicado");
        System.out.println("echo rm / <FILE> .........: Borra el contenido del archivo de texto indicado");
        System.out.println("rm <FILE> ................: Borra un directorio o archivo en la carpeta actual");
        System.out.println("mv <FILE1> / <FILE2> .....: Cambia de nombre una carpeta o archivo en la carpeta actual");
        System.out.println("help .....................: Muestra la lista de comandos");
        System.out.println("exit .....................: Termina el programa");
        System.out.println();
        MiniFileManagerUtils.showPath(currentPath);
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
            input = MiniFileManagerUtils.debugInput(new Scanner(System.in).nextLine());
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
                        MiniFileManagerUtils.showPath(currentPath);
                    }
                }
            } catch (Exception e) {
                MiniFileManagerUtils.showPath(currentPath);
            }
        } while (!exit);
    }
}
