import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Saving {

    // путь к папке с сохранениями берем из предыдущего задания
    public static final String PATH_TO_SAVES = Installation.getPathToSaves();
    public static List<String> savedGames = new ArrayList<>();
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {
        saveGame();
        zipGames();
        removeNotZippedSaves();
    }

    public static void saveGame() {
        List<GameProgress> games = getGames(); // получаем список GameProgress'ов
        for (int i = 0; i < getGames().size(); i++) {
            String fileName = PATH_TO_SAVES + "save" + (i + 1) + ".dat"; // генерация полного пути файла сохранения
            try (ObjectOutputStream serializer = new ObjectOutputStream(
                         new FileOutputStream(fileName))) {
                serializer.writeObject(games.get(i)); // записываем в файл состояние нашего объекта GameProgress
                savedGames.add(fileName); // заполняем список с файлами сохранений
                System.out.printf("%s - прогресс успешно сохранен в файл %s\n",
                        DATE_FORMAT.format(new Date()), fileName);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void zipGames() {
        String archiveName = "zippedSaves.zip";
        try (ZipOutputStream zipper = new ZipOutputStream(
                     new FileOutputStream(PATH_TO_SAVES + archiveName))) {
            for (int i = 0; i < getGames().size(); i++) {
                String fileName = "save" + (i + 1) + ".dat"; // имя файла
                FileInputStream save = new FileInputStream(PATH_TO_SAVES + fileName); // поток из нашего файла
                ZipEntry entry = new ZipEntry(fileName); // создаем новую запись для архива, даем ей имя (такое же как у файла)
                zipper.putNextEntry(entry); // добавлем эту запись в архив
                byte[] buffer = new byte[save.available()]; // создаем массив байтов для записи сюда файла
                save.read(buffer); // побайтово считываем наш файл и кладем эти байты в массив
                zipper.write(buffer); // записываем этот массив байтов в архив
                zipper.closeEntry(); // закрываем запись в архиве
                save.close(); // закрывает входной поток из файла
                System.out.printf("%s - файл %s успешно добавлен в архив %s\n",
                        DATE_FORMAT.format(new Date()), PATH_TO_SAVES + fileName, archiveName);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeNotZippedSaves() {
        savedGames.forEach(path -> {
            new File(path).delete();
            System.out.printf("%s - файл %s успешно удален\n",
                    DATE_FORMAT.format(new Date()), path);
        });
        savedGames.clear();
    }

    public static List<GameProgress> getGames() {
        return List.of(
                new GameProgress(90, 20, 5, 125.5),
                new GameProgress(70, 15, 10, 250.7),
                new GameProgress(30, 5, 20, 450.3)
        );
    }

    public static String getPathToZip() {
        return PATH_TO_SAVES + "zippedSaves.zip";
    }
}
