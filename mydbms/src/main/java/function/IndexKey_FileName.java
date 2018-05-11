package function;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IndexKey_FileName {
    public static List<List<String>> indexKey_FileName(String dbName,String tbName,String index) throws DocumentException {
        //数据库是否合法
        if(UseDatabase.dbName==null){
            System.out.println("请先进入数据库（SQL语句：use database 数据库名称;）");
            return null;
        }

        //声明一个索引id的list
        List<List<String>> index_file=new ArrayList<List<String>>();
        //打开逻辑表的配置文件，获得所有文件名
        File config_file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+"-config.xml");
        SAXReader config_file_reader=new SAXReader();
        Document config_file_document=config_file_reader.read(config_file);
        Element file_name_element=(Element)config_file_document.getRootElement().selectSingleNode(tbName);
        //从最后一个表文件开始遍历
        for(int j=Integer.parseInt(file_name_element.getText());j>=0;j--) {
            String file_name=tbName+Integer.toString(j);

            //打开逻辑表的物理层文件，解析该文件，获取并列的所有记录的父节点
            File file = new File("./mydatabase/" + dbName + "/" + file_name + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            List<Node> nodes = document.getRootElement().selectNodes(tbName);

            //遍历所有节点，取出索引列的值和对应的文件名
            for (Node node : nodes) {
                Element element = (Element) node;
                List<Attribute> list=element.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                   Attribute attribute=(Attribute)i.next();
                    if (attribute.getName().equals(index)) {
                        index_file.get(j).set(0, attribute.getText());
                        index_file.get(j).set(1, file_name);
                    }
                }
            }
        }

        return index_file;
    }
}
