package function;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CreateUser {
    public static void createUser() throws DocumentException, IOException {
        //打开user表,获得所有user节点
        File file=new File("./mydatabase/user/user.xml");
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        List<Node> list=  document.getRootElement().selectNodes("user");

        Scanner scanner=new Scanner(System.in);
        String username,password;
        while(true) {
            System.out.println("请输入要创建的用户名：");
            username = scanner.nextLine();
            //判断用户名是否存在
            boolean islegal=true;
            for (Node node : list) {
                Element element = (Element) node;
                Attribute name = element.attribute("name");
                if (name.getText().equals(username)) {
                    System.out.println("用户名已存在，请重新输入");
                    islegal=false;
                    break;
                }
            }
            if(islegal){
                break;
            }
        }
        System.out.println("请输入密码：");
        password=scanner.nextLine();

        document.getRootElement().addElement("user").addAttribute("id",list.size()+1+"").addAttribute("name",username).addAttribute("password",password);
        CreateTable.writeIO(file,document);
        System.out.println("新用户"+username+"创建成功");
    }
}
