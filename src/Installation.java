import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class Installation {

    public static final String PATH = "./Game/"; // путь к папке Game
    public static StringBuilder log = new StringBuilder();

    public static void main(String[] args) {
        getPaths().forEach((name, path) -> {
            if (name.startsWith("file")) {
                createFile(name, path);
            } else {
                createDir(name, path);
            }
        });
        try {
            File tempTxt = new File(PATH + "temp/Temp.txt");
            FileWriter writer = new FileWriter(tempTxt);
            writer.write(log.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Something goes wrong with writing the file Temp.txt");
        }
    }
    
    public static void createFile(String name, String path) {
        try {
            if (new File(path).createNewFile()) {
                log.append("Файл ")
                        .append(name.replace("file", ""))
                        .append(" успешно создан").append("\n");
            } else {
                log.append("Не удалось создать файл ")
                        .append(name.replace("file", ""))
                        .append("\n");
            }
        } catch (IOException e) {
            System.out.println("Something goes wrong with " +
                    name.replace("file", "") + " file creation");
        }
    }
    
    public static void createDir(String name, String path) {
        if (new File(path).mkdirs()) {
            log.append("Папка ").append(name).append(" успешно создана").append("\n");
        } else {
            log.append("Не удалось создать папку ").append(name).append("\n");
        }
    }

    // коллекция с необходимыми папками и файлами
    public static LinkedHashMap<String, String> getPaths() {
        LinkedHashMap<String, String> paths = new LinkedHashMap<>();
        paths.put("Game", PATH);
        paths.put("src", PATH + "src/");
        paths.put("res", PATH + "res/");
        paths.put("savegames", PATH + "savegames/");
        paths.put("temp", PATH + "temp/");
        paths.put("main", PATH + "src/main/");
        paths.put("test", PATH + "src/test/");
        paths.put("drawables", PATH + "res/drawables/");
        paths.put("vectors", PATH + "res/vectors/");
        paths.put("icons", PATH + "res/icons/");
        paths.put("fileMain", PATH + "src/main/Main.java");
        paths.put("fileUtils", PATH + "src/main/Utils.java");
        paths.put("fileTemp", PATH + "temp/Temp.txt");
        return paths;
    }

    public static String getPathToSaves() {
        return getPaths().get("savegames");
    }
}