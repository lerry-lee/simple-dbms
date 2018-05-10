package function;

import java.io.File;

public class CreateDatabase {
    public static void createDB(String databaseName){
        //create new directory
        File db=new File("./mydatabase/"+databaseName+"");
        //determine whether directory exists
        if(!db.exists()){
            db.mkdir();
            System.out.println("create database "+databaseName+" successfully.");
        }
        else{
            System.out.println("database "+databaseName+" has already exist.");
        }
    }


}
