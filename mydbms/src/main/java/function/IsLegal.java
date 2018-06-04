package function;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

public class IsLegal {
    //判断数据库是否为空
    public static boolean isDatabaseEmpty(){
        //获得数据库名称
        String dbName=UseDatabase.dbName;
        //如果数据库为空，表示未进入数据库
        if(dbName.equals("")){
            System.out.println("数据库名为空，请先进入数据库");
            return true;
        }

        return false;
    }
    //判断数据库是否存在
    public static boolean isDatabase(){
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
    //判断表是否存在,存在则返回该表的配置文件
    public  static File isTable(String dbName,String tbName){
        File file=new File("./mydatabase/"+dbName+"/"+tbName+"");
        if(!file.exists()){
            System.out.println(tbName+"表不存在");
            return null;
        }
        else{
            file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"-config.xml");
            return file;
        }
    }
    //判断表是否存在，存在则返回一个表的物理层最后一张子表的下标
    public  static String lastFileName(String dbName,String tbName) throws DocumentException {

        //找到写入对象
        File write_file=isTable(dbName,tbName);
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(write_file);
        Element element= (Element) document.getRootElement().selectSingleNode(tbName);
        String last_file_num=element.getText();
        //返回写入对象名
        return last_file_num;
    }
    //判断一个表是否建立了主键索引
    public static boolean hasIndex(String dbName,String tbName) throws DocumentException {
        File file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"-config.xml");
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        Element element= (Element) document.getRootElement().selectSingleNode("index");

        if(element.getText().equals("1")){
            return true;
        }

        return false;

    }
    //判断是不是通过主键查询
    public static boolean isIndex(File file,String key) throws DocumentException {
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        Element element= (Element) document.getRootElement().selectSingleNode("index_name");
        if(element.getText().equals(key)){
            return true;
        }
        return false;
    }
    //判断是否需要load index
    public static boolean need_loadIndex() throws DocumentException {
        File file=new File("./mydatabase/index.xml");
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        Element root=document.getRootElement();
        if(root.elementIterator().hasNext()){
            return true;
        }
        return false;
    }
}
