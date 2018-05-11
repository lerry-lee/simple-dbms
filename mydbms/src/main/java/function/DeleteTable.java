package function;

import java.io.File;

public class DeleteTable {
    //delete table 表名
    public static void deleteTable(String databaseName,String tableName){
        //判断数据库是否合法
        if(IsLegal.isDatabaseEmpty()){
            return;
        }
        //获得表的文件对象，判断表是否存在
        File file=new File("./mydatabase/"+databaseName+"/"+tableName+"");
        if(!file.exists()){
            System.out.println(tableName+"表不存在，请删除已有的表（SQL语句：delete table 表名）");
            return;
        }
        else{
            File[] files=file.listFiles();
            for(int i=0;i<files.length;i++){
                files[i].delete();
            }
            file.delete();
            System.out.println(tableName+"表删除成功");
        }
    }
}
