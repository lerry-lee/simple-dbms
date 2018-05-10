package function;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

public class CreateTable extends UseDatabase {


    public static void createTb(String dbName, String tbName, List<String> tmp) throws IOException {

        //if database illegal
        if(!IsDatabase.idDatabase()){
            return;
        }
        //create table
        File table=new File("./mydatabase/"+dbName+"/"+tbName+".xml");
        if(!table.exists()){
            table.createNewFile();
            System.out.println("table "+tbName+" create successfully");
        }
        else{
            System.out.println("table "+tbName+" is already exist");
        }
        //create document object
        Document document = DocumentHelper.createDocument();
        //add labels
        Element rootElem = document.addElement(tbName+"s");
        Element column=rootElem.addElement(tbName);


        if(tmp!=null) {

            for (int i = 0; i < tmp.size(); i++) {

                String[] list=tmp.get(i).split(" ");
                Element leafElem = column.addElement(list[0]);
                for(int j=1;j<list.length;j++) {
                    leafElem.addAttribute("type",list[j]);
                }
            }
//        /*
////        Element stuElem=rootElem.addElement("student");
////        stuElem.addElement("name").addText("Lerry");
////        stuElem.addElement("sex").addText("male");
////        //add attribute
////        stuElem.addAttribute("id","001");
////        //stuElem.addAttribute("sex","male");
////        */
        }
        //specify file output location
        FileOutputStream outputStream=new FileOutputStream(table);
        OutputFormat outputFormat=OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        //create write object
        XMLWriter xmlWriter=new XMLWriter(outputStream,outputFormat);
        //write document object
        xmlWriter.write(document);
        //close the stream
        xmlWriter.close();

    }
}
