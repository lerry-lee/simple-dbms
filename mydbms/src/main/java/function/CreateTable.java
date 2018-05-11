package function;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

public class CreateTable extends UseDatabase {

    //create table 表名(列名称1 数据类型1，列名称2 数据类型2)
    public static void createTb(String dbName, String tbName, List<String> tmp) throws IOException {

        //判断数据库是否合法
        if(IsLegal.isDatabaseEmpty()){
            return;
        }
        //创建表名文件夹，逻辑层表示一张表
        File tableFile=new File("./mydatabase/"+dbName+"/"+tbName+"");
        if(!tableFile.exists()){
            tableFile.mkdir();
        }
        else{
            System.out.println(tbName+"表已经存在");
            return;
        }
        //一张表在物理层表示为多个XML文件，创建表配置文件，存储表的列名称、数据类型、文件名
        File table=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"-config.xml");
        Document document = DocumentHelper.createDocument();
        Element rootElem = document.addElement(tbName+"s");
        //表配置文件的根节点的属性值为列名称=数据类型
        for (int i = 0; i < tmp.size(); i++) {
                String[] list=tmp.get(i).split(" ");
                rootElem.addAttribute(list[0],list[1]);
        }
        rootElem.addElement(tbName).setText("000");

        //记录文件名信息并创建第一个文件
        File first_file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"000.xml");
        Document first_document=DocumentHelper.createDocument();
        first_document.addElement(tbName+"s");
        //确定文件输出位置
        FileOutputStream outputStream=new FileOutputStream(first_file);
        OutputFormat outputFormat=OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        //创建写入对象，写入document对象
        XMLWriter xmlWriter=new XMLWriter(outputStream,outputFormat);
        xmlWriter.write(first_document);
        //关闭流
        xmlWriter.close();


        //创建配置文件
        //确定文件输出位置
        FileOutputStream outputStream1=new FileOutputStream(table);
        OutputFormat outputFormat1=OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        //创建写入对象，写入document对象
        XMLWriter xmlWriter1=new XMLWriter(outputStream1,outputFormat1);
        xmlWriter1.write(document);
        //关闭流
        xmlWriter1.close();
        System.out.println(tbName+"表创建成功");

    }
}
