package function;

import java.io.File;
//select table 表名称 from 数据库名称
public class SelectFromDatabase {
    public static File selectFromDB(String dbName, String tbName) {

        File baseDir = new File("./mydatabase/"+dbName);
        //数据库是否存在
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println("数据库" + dbName + "不存在");
        }
        String tempName = null;

        File tempFile;
        File[] files = baseDir.listFiles();
        //数据库中没有表
        if (files.length == 0) {
            System.out.println("数据库"+dbName+"中没有任何表");
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];
            tempName = tempFile.getName();

            if (tempName.equals(tbName)) {
                System.out.println(tbName+"表存在，已找到");
                return tempFile.getAbsoluteFile();
            } else {
                System.out.println(tbName + "表不存在，未找到");
            }
        }
        return null;
    }
}

