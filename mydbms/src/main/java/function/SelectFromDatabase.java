package function;

import java.io.File;

public class SelectFromDatabase {
    public static File selectFromDB(String baseDirName, String targetFileName) {
        //create a file object
        File baseDir = new File("./mydatabase/"+baseDirName);
        //if directory exist
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println("query failed: " + baseDirName + " is not a directory.");
        }
        String tempName = null;

        File tempFile;
        File[] files = baseDir.listFiles();
        //this directory is empty file
        if (files.length == 0) {
            System.out.println("is empty file");
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];
            tempName = tempFile.getName();
            //System.out.println("1");
            if (tempName.equals(targetFileName + ".xml")) {
                System.out.println("find successfully");
                return tempFile.getAbsoluteFile();
            } else {
                System.out.println("table " + targetFileName + " is not exist.");
            }
        }
        return null;
    }
}

