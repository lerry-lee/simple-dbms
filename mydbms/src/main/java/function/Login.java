package function;

import factory.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Login {
    public static void main(String[]args) throws IOException, DocumentException {


        System.out.println("welcome to my simple dbms.if you want to use it,please login first.");
        //set a to account login-failed times
        int a=3;
        while(a>0) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            //get user id
            System.out.println("input user id:");
            String name = bufferedReader.readLine();
            //get password
            System.out.println("input password:");
            String password = bufferedReader.readLine();

            //find user info from database
            Document document = (Document) new SAXReader().read("./mydatabase/user/user.xml");
            org.dom4j.Element node = (org.dom4j.Element) document.selectSingleNode("users/user[@name='" + name + "'and @password='" + password + "']");

            if (node != null) {
                System.out.println("login successfully.");
                a=0;
            }
            else {
                System.out.println("login failed.please try again.");
                a--;
                if(a==0) {
                System.out.println("you have input the wrong id/password three times,dbms will exit.");
                return;
                }
            }
        }

        System.out.println("please input sql statement:");
        UseDatabase.dbName=null;
        while(true)
        {
            @SuppressWarnings("resource")
            Scanner input = new Scanner(System.in);
            String sql = input.nextLine();
//            /*
//             * 预处理:获得语句;
//             * 处理前后空格;
//             * 变小写;
//             * 处理中间多余的空格回车和特殊字符;
//             * 在末尾加特殊符号;
//             * 处理掉最后的;
//             */
//            //处理分行输入的问题，就是读;号才停止;
            //sql parse
            while(sql.lastIndexOf(";")!=sql.length()-1){
                sql = sql+" "+input.nextLine();
            }

            sql = sql.trim();
            sql=sql.toLowerCase();
            sql=sql.replaceAll("\\s+", " ");

            sql = sql.substring(0, sql.lastIndexOf(";"));
            sql=""+sql+" ENDOFSQL";
            System.out.println("1)sql parsing result: "+sql);
//
//            /*
//             * 结束输入判断
//             */
////			Pattern pattern=Pattern.compile("(quit)");
////			Matcher matcher=pattern.matcher(sql);
////			System.out.println(matcher.find());
            List<List<String>> parameter_list=new ArrayList<List<String>>();

            if(sql.equals("quit ENDOFSQL"))
            {
                return;
            }
            else
            {
//                //System.out.println("准备执行SingleSqlParserFactory.generateParser函数");
//                /*
//                 * 分割出sql语句的关键部分，用list-parameter_list存储
//                 */
                //match regular expressions
                try{
                    parameter_list = SingleSqlParserFactory.generateParser(sql);
                }
                catch (Exception e){
                    e.printStackTrace();

                }

//
////----------------------------------------------------------------------------------------------------------------
//                //System.out.println("执行结束SingleSqlParserFactory.generateParser函数");
//                /*
//                 * 查看解析后的List<List<String>>是否正确
//                 */
//
////				for(int i = 0;i < parameter_list.size();i++)
////				{
////					System.out.println(parameter_list.get(i));
////				}
////
////				System.out.println();
//
////				List<String> test = new ArrayList<String>();
////				test = parameter_list.get(2);
////
////				for(int i = 0;i < test.size();i++)
////				{
////					String r = test.get(i);
////					System.out.println(r.trim());
////				}
////----------------------------------------------------------------------------------------------------------------
//                /*
//                 * 根据关键字判断调用底层哪个功能模块
//                 */
//                //System.out.println("PassingParametersFactory.dealParameters");
                //call function method
                try{
                    PassingParametersFactory.dealParameters(parameter_list);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }


        }
    }
}
