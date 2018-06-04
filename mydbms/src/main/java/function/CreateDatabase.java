package function;

import java.io.File;

public class CreateDatabase {
    //create database 数据库名称
    public static void createDB(String databaseName){
        //创建数据库目录
        File db=new File("./mydatabase/"+databaseName+"");
        //判断数据库是否存在
        if(!db.exists()){
            db.mkdir();
            System.out.println("数据库"+databaseName+"创建成功");
        }
        else{
            System.out.println("数据库"+databaseName+"已经存在");
        }
    }


}
