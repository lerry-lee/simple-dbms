package function;

import java.io.File;

public class ShowTable {
    public static void showTable(String dbname){
        //if database illegal
        if(!IsDatabase.idDatabase()){
            return;
        }
        File dir=new File("./mydatabase/"+dbname+"");
        for(File file:dir.listFiles()){
            if(file.isFile()){
                System.out.println(file.getName());
            }
        }
    }
}
