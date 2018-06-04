package function;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

public class CreateTable{
    static public int entry_num=10;
    //create table 表名(列名称1 数据类型1，列名称2 数据类型2)
    public static void createTb(String dbName, String tbName, List<String> tmp) throws IOException {

        //判断数据库是否为空
        if(IsLegal.isDatabaseEmpty()){
            return;
        }
        //创建一张表的文件夹，逻辑上表示一张表
        File tableFile=new File("./mydatabase/"+dbName+"/"+tbName+"");
        if(!tableFile.exists()){
            tableFile.mkdir();
        }
        else{
            System.out.println(tbName+"表已经存在");
            return;
        }
        //创建配置文件并设置根节点
        File table=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"-config.xml");
        Document document = DocumentHelper.createDocument();
        Element rootElem = document.addElement(tbName+"s");
        //根节点的属性值为列名称=数据类型
        for (int i = 0; i < tmp.size(); i++) {
                String[] list=tmp.get(i).split(" ");
                rootElem.addAttribute(list[0],list[1]);
        }
        //物理层第一张子表的下标为0
        rootElem.addElement(tbName).setText("0");
        //存储物理层可插入子表的下标
        rootElem.addElement("insertables");
        //记录是否建有索引
        rootElem.addElement("index");
        //记录主键名称
        rootElem.addElement("index_name");
        //写入操作
        writeIO(table,document);

        //创建表的物理层第一张子表
        File first_file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"0.xml");
        Document first_document=DocumentHelper.createDocument();
        first_document.addElement(tbName+"s");
        //写入操作
        writeIO(first_file,first_document);

        System.out.println(tbName+"表创建成功");

    }
    //更新document树，写入外存
    public static void writeIO(File write_file,Document write_document) throws IOException {
        //确定文件输出位置
        FileOutputStream outputStream=new FileOutputStream(write_file);
        OutputFormat outputFormat=OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        //创建写入对象，写入document对象
        XMLWriter xmlWriter=new XMLWriter(outputStream,outputFormat);
        xmlWriter.write(write_document);
        //关闭流
        xmlWriter.close();
    }
}
