package function;

import java.io.File;

public class DeleteTable {
    public static void deleteTable(String databaseName,String tableName){
        //if database illegal
        if(!IsDatabase.idDatabase()){
            return;
        }
        //get table object
        File file=new File("./mydatabase/"+databaseName+"/"+tableName+".xml");
        //if table does not exist
        if(!file.exists()){
            System.out.println(tableName+" does not exist");
            return;
        }
        else{
            file.delete();
            System.out.println("table "+tableName+" delete successfully");
        }
    }
}
