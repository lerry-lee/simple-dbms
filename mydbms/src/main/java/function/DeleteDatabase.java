package function;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class DeleteDatabase {

    public static void deleteDB(String dbname) throws IOException {

            File file = new File("./mydatabase/" + dbname + "");
            if (!file.exists()) {
                System.out.println("database " + dbname + " is not exist");
                return;
            }
            //if database has tables
            if (file.listFiles().length > 0) {
                System.out.println("There are tables still in the database " + dbname + " ,are you sure that you want to delete it? (Y/N)");

                Scanner scanner=new Scanner(System.in);
                String answer=scanner.next();
                if (answer.toUpperCase().equals("Y")) {

                    File[] files = file.listFiles();
                    for (int i=0;i<files.length;i++) {
                        files[i].delete();
                    }
                    file.delete();
                    System.out.println("database " + dbname + " delete successfully");
                } else {
                    return;
                }
            }
            //if database is empty
            else {
                file.delete();
                System.out.println("database " + dbname + " delete successfully");
            }
        }



}
