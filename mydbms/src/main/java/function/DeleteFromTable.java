package function;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

public class DeleteFromTable {
    //delete from 表名 where 列名称=列值
    public static void deleteFromTable(String dbName,String tbName,List<String> tmp) throws DocumentException, IOException {
        //数据库是否合法,表是否合法
        if(IsLegal.isDatabaseEmpty()){
            return;
        }
        String write_file=IsLegal.isTable(tbName);
//        if(write_file==null){
//            return;
//        }
        //find标记是否找到记录
        Boolean find = false;
        //j表示表的文件名中的数字
        for(int j=Integer.parseInt(write_file);j>=0;j--) {
            //设置变量traverse_file用来遍历表的所有文件
            String traverse_file=""+j;
            //创建写入对象，创建sax解析器，document对象，获得root节点
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + traverse_file + ".xml");
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element root = document.getRootElement();

            String[] tmp1 = new String[2];
            tmp1 = tmp.get(0).split("=");

            //设置一个节点用来遍历
            Element element;
            List<Node> nodes = root.selectNodes(tbName);
            for (Node node : nodes) {
                Element currentNode=(Element)node;
                List<Attribute> lists=currentNode.attributes();
                for (Iterator i = lists.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    if (attribute.getName().equals(tmp1[0]) && attribute.getText().equals(tmp1[1])) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    root.remove(currentNode);

                    //IO操作
                    OutputFormat format = OutputFormat.createPrettyPrint();
                    format.setEncoding("UTF-8");
                    XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+traverse_file+".xml")), format);
                    writer.write(document);
                    writer.close();

                    //更新配置文件
                    File file1=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"-config.xml");
                    SAXReader saxReader=new SAXReader();
                    Document document1=saxReader.read(file1);
                    Element element1= (Element) document1.getRootElement().selectSingleNode(tbName);
                    element1.addElement("insertable").setText(traverse_file);
                    //IO
                    OutputFormat format1 = OutputFormat.createPrettyPrint();
                    format.setEncoding("UTF-8");
                    XMLWriter writer1 = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"-config.xml")), format1);
                    writer1.write(document1);
                    writer1.close();
                    System.out.println("删除记录成功");
                    return;
                }

            }
        }
    if(!find){
            System.out.println("没有找到要删除的记录");
            return;
        }
    }
}

