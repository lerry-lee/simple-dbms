package function;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

public class IsLegal {
    public static boolean isDatabaseEmpty(){
        //获得数据库名称
        String dbName=UseDatabase.dbName;
        //如果数据库为空，表示未进入数据库
        if(dbName==null){
            System.out.println("数据库名为空，请先进入数据库");
            return true;
        }

        return false;
    }

    public static boolean isDatabase(){
        //判断数据库是否合法
        String dbName=UseDatabase.dbName;
        File file=new File("./mydatabase");
        File[] files=file.listFiles();
        for(int i=0;i<files.length;i++){
            if(files[i].getName().equals(dbName)){
                return true;
            }
        }
        System.out.println("数据库"+dbName+"不存在");
        return false;
    }

    public static String isTable(String tbName) throws DocumentException {
        String dbName=UseDatabase.dbName;
        //表是否存在
        File file = new File("./mydatabase/" + dbName + "/"+tbName+"");

        if(!file.exists()){
            System.out.println(tbName+"表不存在");
            return null;
        }

        //找到写入对象
        File write_file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"-config.xml");
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(write_file);
        Element element= (Element) document.getRootElement().selectSingleNode(tbName);
        String write_file_name=element.getText();
        //返回写入对象名
        return write_file_name;
    }
}
