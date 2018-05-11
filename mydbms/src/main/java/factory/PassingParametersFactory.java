package factory;

import function.*;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.*;


/*
 * 工厂类：对解析后的sql语句进行判断，调用相应功能的模块函数
 */
public class PassingParametersFactory {

	public static void dealParameters(List<List<String>> list) throws IOException, DocumentException {
		List<String> ls = new ArrayList<String>();
		ls = list.get(0);     //等效：ls.addAll(list.get(0));
//--------------------------------------------------------------------------------------------------------------------------------------
//		for(int i = 0 ; i < list.size();i++)
//		{
//			System.out.println("list.get("+i+"): "+list.get(i));
//		}
		String key = ls.get(0);

		if(key.equals("create table"))
		{
			System.out.println("3)调用方法：创建表");

			List<String> tmp = new ArrayList<String>();//传递参数
			List<String> tmp1 = new ArrayList<String>();
			tmp1 = list.get(1);
			for(int i = 1 ; i<tmp1.size();i++)
			{
				tmp.add(tmp1.get(i));
			}
            CreateTable.createTb(UseDatabase.dbName,ls.get(1),tmp);

//			/*
//			 *  function.CreateTable(String s,List<String> tmp);
//			 *  s = table_name;
//			 *  tmp = 出入列的名称以及说明;
//			 */
//			//function.CreateTable(ls.get(1),tmp);
//
////---------------------------------------------------------------------------------------------------------------------------------	-------------------
//			//test：判断传参是否正确
////
////				System.out.println(ls.get(1));
////
////				for(int i = 0 ; i < tmp.size();i++)
////				{
////					System.out.println(tmp.get(i));
////				}
//////---------------------------------------------------------------------------------------------------------------------------------	-------------------
		}
		else if(key.equals("show databases"))
		{
			System.out.println("3)调用方法：列出所有数据库");
            ShowDatabases.showDatabase();
		}
		else if(key.equals("show tables"))
		{
			System.out.println("3)调用方法：列出所有表");
			ShowTables.showTable(UseDatabase.dbName);
		}
		else if(key.equals("use database"))
		{
			System.out.println("3)调用方法：进入数据库");
			UseDatabase.dbName=ls.get(1);
			//if database illegal
			if(!IsLegal.isDatabase()){
				UseDatabase.dbName=null;
				return;
			}
		}
		else if(key.equals("create database"))
		{
			System.out.println("3)调用方法：创建数据库");
            CreateDatabase.createDB(ls.get(1));
		}
		else if(key.equals("insert into"))
		{
			System.out.println("3)调用方法：插入数据到表");

			List<String> tmp2 = new ArrayList<String>();
			List<String> tmp1 = new ArrayList<String>();
			tmp1 = list.get(1);
			tmp2 = list.get(2);
			InsertIntoTable.insertIntoTable(UseDatabase.dbName,ls.get(1),tmp1,tmp2);
			//InsertIntoTable.inertIntoTable(UseDatabase.dbName,ls.get(1),tmp2);

//			for(int i = 1 ; i < tmp1.size();i++)
//			{
//				System.out.println(tmp1.get(i)+","+tmp2.get(i));
//				tmp.put(tmp1.get(i), tmp2.get(i));
//			}
////
//
//			//传递table_name:String,使用map传递name=values（map中规定键必须唯一，正好和我们列的名字必须唯一吻合）
//			/*
//			 *  InsertData(String s,map tmp);
//			 *  s = table_name;
//			 *  tmp = 要插入的键值对;
//			 */
			//InsertData(ls.get(1),tmp);

//---------------------------------------------------------------------------------------------------------------------------------	-------------------
			//test：判断传参是否正确
//
//			  Set<String> set = tmp.keySet();
//			  for (String s:set)
//			  {
//			   System.out.println(s+","+tmp.get(s));
//			  }

//---------------------------------------------------------------------------------------------------------------------------------	-------------------

		}
		else if(key.equals("select * from")){
			String arg="where";
			if(list.size()>1) {
				if (list.get(1).get(0).equals(arg)) {
					System.out.println("3)调用方法：查询指定记录");
					String tableName = list.get(0).get(1);
					List<String> tmp1 = new ArrayList<String>();
					tmp1 = list.get(1);
					SelectFromTable.selectAllFromTb(UseDatabase.dbName, tableName, tmp1);

				}
			}
			else {
				System.out.println("3)调用方法：查询所有记录");
				String tableName = list.get(0).get(1);
				SelectFromTable.selectFromTb(UseDatabase.dbName, tableName);
			}

		}
		else if(key.equals("select table")){
			System.out.println("3)调用方法：从数据库中查询表");
            String dbName=list.get(1).get(1);
            String tbName=list.get(0).get(1);
            SelectFromDatabase.selectFromDB(dbName,tbName);
		}
		else if(key.equals("select"))
		{
			System.out.println("3)调用方法：查询记录中的某些列");
			List<String> tmp1=new ArrayList<String>();
			List<String> tmp2=new ArrayList<String>();
			tmp1=list.get(0);
			String tableName=list.get(1).get(1);
			tmp2=list.get(2);

			if(tmp2.isEmpty()){
				SelectFromTable.selectFromTb(UseDatabase.dbName,tableName,tmp1);
			}
			else {
				SelectFromTable.selectFromTb(UseDatabase.dbName, tableName, tmp1, tmp2);
			}

//			List<List<String>> tmp = new ArrayList<List<String>>(); //传递的有效参数
//			tmp = getPararmeterList(list);
//			List<String> tmp1 = new ArrayList<String>();

//           这是个问题！！
//			如果非要一个字段一个去判断，组合的情况，太多，最少3,4层if！
//			if(？where)
//			{
//				if(?group)
//				{
//					if(?having)
//					{
//						if(?order by)
//						{
//							where+group+having+order by
//						}
//						else if(?limit)
//						{
//							where+group+having+limit
//						}
//						else
//						{
//							where+group+having
//						}
//					}
//					else if(?order by)
//					{
//						if(?limit)
//						{
//							where+group by + order by + limit
//						}
//						else
//						{
//							where+group by + order by
//						}
//					}
//					else if(?limit))
//					{
//						where+group by +limit
//					}
//					else
//					{
//						……
//					}
//				}
//			}
//
//
//			/*
//			 *  SelectData(String s,List<List<String>> tmp);
//			 *  s = table_name;
//			 *  tmp = 所有条件的集合;
//			 */
			//SelectData(ls.get(1),tmp);



//---------------------------------------------------------------------------------------------------------------------------------	-------------------
			//test：判断传参是否正确
//
//			System.out.println(ls.get(1));
//
//			for(int i = 0 ; i < tmp.size();i++)
//			{
//				System.out.println(tmp.get(i));
//			}
//---------------------------------------------------------------------------------------------------------------------------------	-------------------
		}

		else if(key.equals("update"))
		{
			System.out.println("3)调用方法：更新指定记录");

			List<List<String>> tmp = new ArrayList<List<String>>(); //传递参数
			tmp = getPararmeterList(list);
			UpdateTable.updateTable(UseDatabase.dbName,list.get(0).get(1),tmp);
//			/*
//			 *  UpdateData(String s,List<List<String>> ls);
//			 *  s = table_name;
//			 *  ls = set,where的集合;
//			 */
			//UpdateData(ls.get(1),tmp);
//---------------------------------------------------------------------------------------------------------------------------------	-------------------
			//test：判断传参是否正确
//			System.out.println(ls.get(1));
//
//			for(int i = 0 ; i < tmp.size();i++)
//			{
//				System.out.println(tmp.get(i));
//			}
//---------------------------------------------------------------------------------------------------------------------------------	-------------------
		}
		else if(key.equals("delete database")){
		    System.out.println("3)调用方法：删除数据库");
		    DeleteDatabase.deleteDB((ls.get(1)));
        }
        else if(key.equals("delete table")){
            System.out.println("3)调用方法：删除表");
            DeleteTable.deleteTable(UseDatabase.dbName,ls.get(1));
        }
		else if(key.equals("delete from"))
		{
			System.out.println("3)调用方法：删除指定记录");
			//取出每个list中的start部分，只传递后面的参数部分；
			List<String> tmp1 = new ArrayList<String>();
			List<String> tmp2 = new ArrayList<String>();
			tmp1 = list.get(1);
			for(int i = 1 ; i<tmp1.size();i++)
			{
				String r = tmp1.get(i);
				tmp2.add(r);
			}
			DeleteFromTable.deleteFromTable(UseDatabase.dbName,ls.get(1),tmp2);
//			/*
//			 * DeleteData(String s,List<String> ls);
//			 * s = table_name;
//			 * ls = tmp2; tmp包含的是整个的where的条件
//			 * 注意：传过去的where中，是name = value的形式，会存在前后，或者等号之间带空格的情况。接受用String， .trim（）先出现下，再用split（"="）分割就好了
//			 */

			//DeleteData(ls.get(1),tmp2);
//---------------------------------------------------------------------------------------------------------------------------------	-------------------
//			test
//			System.out.println(ls.get(1));
//
//			for(int i = 0 ; i < tmp2.size();i++)
//			{
//				System.out.println(tmp2.get(i));
//			}
////------------------------------------------------------------------------------------------------------------------------------------------------------



		}


	}
	/*
	 * 当list参数为多行时，用于提取多行有效的参数。
	 */
	protected static List<List<String>> getPararmeterList(List<List<String>> list)
	{
		List<List<String>> tmp1 = new ArrayList<List<String>>(); //传递参数
		List<String> tmp2;
		List<String> tmp3;

		//System.out.println(list.size());

		for(int i = 1 ; i <list.size();i++)
		{
			tmp2 = new ArrayList<String>();
			tmp3 = new ArrayList<String>();

			tmp2 = list.get(i);
			for(int j = 1 ; j < tmp2.size();j++)
			{
				String r = tmp2.get(j);
				tmp3.add(r);
			}
			tmp1.add(tmp3);
		}

//		for(int i = 0 ; i < tmp1.size();i++)
//		{
//			System.out.println(tmp1.get(i));
//		}

		return tmp1;
	}
}