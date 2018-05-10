package function;

import java.io.File;

public class IsDatabase {
    public static boolean idDatabase(){
        //get database name
        String dbName=UseDatabase.dbName;
        //if not use database
        if(dbName==null){
            System.out.println("please use database first");
            return false;
        }
        //if use database,check
        File file=new File("./mydatabase");
        File[] files=file.listFiles();
        for(int i=0;i<files.length;i++){
            //System.out.println(files[i].getName());
            if(files[i].getName().equals(dbName)){
                return true;
            }
        }
        System.out.println("database "+dbName+" does not exist");
        return false;
    }
}
