package function;

import function.CreateIndex;
import function.CreateTable;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class DropIndex {
    public static void dropIndex(String dbName,String tbName) throws DocumentException, IOException {
        //数据库不为空
        if(IsLegal.isDatabaseEmpty()){
            return;
        }
        //表存在且索引存在
        File file=IsLegal.isTable(dbName,tbName);
        if(!IsLegal.hasIndex(dbName,tbName)){
            System.out.println(tbName+"表没有建立主键索引");
            return;
        }
        //clear 该表对应的B+树
        for(int i=0;i< CreateIndex.myTree_list.size();i++){
            if(CreateIndex.myTree_list.get(i).containsKey(tbName)){
                CreateIndex.myTree_list.get(i).clear();
                break;
            }
        }
        //设置该表的配置文件的索引情况为0
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        Element element= (Element) document.getRootElement().selectSingleNode("index");
        element.setText("0");
        CreateTable.writeIO(file,document);
        //删除该表对应的索引文件
        File index_file=new File("./mydatabase/index.xml");
        SAXReader saxReader1=new SAXReader();
        Document index_document=saxReader1.read(index_file);
        Element root=index_document.getRootElement();
        for(Iterator i=root.elementIterator();i.hasNext();){
            Element element1=(Element)i.next();
            if(element1.getName().equals(tbName)){
                root.remove(element1);
            }
        }
        CreateTable.writeIO(index_file,index_document);
        System.out.println("索引删除成功");
    }
}
