package Main;

import java.io.File;
import java.util.ArrayList;

public class FileNavigator {

    public void getPaths(String folderPath, ArrayList<String> paths) {
        File folder = new File(folderPath);
        File[] fileList = folder.listFiles();
        for (File file : fileList) {
            if (file.isFile()) {
                paths.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                getPaths(file.getAbsolutePath(), paths);
            }
        }
    }

}
