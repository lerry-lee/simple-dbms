package function;

import java.io.File;

public class ShowTables {
    //show tables
    public static void showTable(String dbname){
        //数据库是否合法
        if(!IsLegal.isDatabaseEmpty()){
            return;
        }
        File dir=new File("./mydatabase/"+dbname+"");
        for(File file:dir.listFiles()){
            if(file.exists()){
                System.out.println(file.getName());
            }
        }
    }
}
