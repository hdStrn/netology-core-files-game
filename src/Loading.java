import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Loading {

    public static final String PATH_TO_SAVES = Installation.getPathToSaves();
    public static final String PATH_TO_ZIP = Saving.getPathToZip();
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {
        openZip();
        openProgress();
    }

    public static void openZip() {
        // создаем входной поток из zip файла и оборачиваем его в ZipInputStream
        try (ZipInputStream unzipper = new ZipInputStream(
                new FileInputStream(PATH_TO_ZIP))) {
            ZipEntry entry;
            String entryName;
            String fileName;

            // вытаскиваем записи из zip архива пока они не кончатся
            while ((entry = unzipper.getNextEntry()) != null) {
                entryName = entry.getName(); // получаем имя записи
                fileName = PATH_TO_SAVES + entryName;
                // создаем поток вывода для разархивирования в указанный файл
                FileOutputStream unzippedSave = new FileOutputStream(fileName);

                // считываем побайтово в наш файл пока байты не закончатся
                for (int b = unzipper.read(); b != -1; b = unzipper.read()) {
                    unzippedSave.write(b);
                }
                unzippedSave.flush(); // очищаем буфер
                unzipper.closeEntry(); // закрываем запись
                unzippedSave.close(); // закрываем поток вывода
                System.out.printf("%s - файл %s успешно разархивирован в папку %s\n",
                        DATE_FORMAT.format(new Date()), entryName, PATH_TO_SAVES);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openProgress() {
        File[] savegames = new File(PATH_TO_SAVES).listFiles();
        Arrays.stream(Objects.requireNonNull(savegames))
                .filter(file -> file.getName().contains(".dat"))
                .forEach(file -> {
                    GameProgress gameProgress;

                    // создаем входной поток из разархивированного файла и оборачиваем его в десериализатор
                    try (ObjectInputStream deserializer = new ObjectInputStream(
                            new FileInputStream(file))) {

                        // читаем полученный файл как объект и кастим его в тип GameProgress
                        gameProgress = (GameProgress) deserializer.readObject();
                        System.out.println(gameProgress);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
    }
}
