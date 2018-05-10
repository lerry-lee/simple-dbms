package factory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import sqlparser.*;

/*
 * 工厂类:用于创建不同的BaseSingleSqlParser的实例，并对每句sql进行解析；
 */
public class SingleSqlParserFactory {
	public static List<List<String>> generateParser(String sql)
	{
		BaseSingleSqlParser tmp = null;

		if(contains(sql,"(insert into)(.+)(select)(.+)(from)(.+)"))
		{
			System.out.println("2)matching regular expressions: insert_select");
			tmp = new InsertSelectSqlParser(sql);

		}
		else if(contains(sql,"(select \\* from)(.+)"))
		{
			if(contains(sql,"(select \\* from)(.+)(where)(.+)")){
				System.out.println("2)matching regular expressions: select*where");
				tmp=new SelectAllWhereSqlParser(sql);
			}
			else {
				System.out.println("2)matching regular expressions: select*");
				tmp = new SelectAllSqlParser(sql);
			}

		}
		else if(contains(sql,"(select)(.+)(from)(.+)"))
		{

			System.out.println("2)matching regular expressions: select");
			tmp = new SelectSqlParser(sql);

		}

		else if(contains(sql,"(delete from)(.+)"))
		{
			System.out.println("2)matching regular expressions: delete from");
			tmp = new DeleteSqlParser(sql);

		}
        else if(contains(sql,"(delete database)(.+)"))
        {
            System.out.println("2)matching regular expressions: delete database");
            tmp = new DeleteDatabaseSqlParser(sql);

        }
        else if(contains(sql,"(delete table)(.+)"))
        {
            System.out.println("2)matching regular expressions: delete table");
            tmp = new DeleteTableSqlParser(sql);

        }
		else if(contains(sql,"(update)(.+)(set)(.+)"))
		{
			System.out.println("2)matching regular expressions: update");
			tmp = new UpdateSqlParser(sql);

		}

		else if(contains(sql,"(insert into)(.+)(values)(.+)"))
		{
			System.out.println("2)matching regular expressions: insert into");
			tmp = new InsertSqlParser(sql);

		}
		else if(contains(sql,"(create table)(.+)"))
		{
			System.out.println("2)matching regular expressions: create table");
			tmp = new CreateTableSqlParser(sql);

		}
		else if(contains(sql,"(create database)(.+)"))
		{
			System.out.println("2)matching regular expressions: create database");
			tmp = new CreateDatabaseSqlParser(sql);
		}
		else if(contains(sql,"(show databases)"))
		{
			System.out.println("2)matching regular expressions: show databases");
			tmp = new ShowDatabaseSqlParser(sql);
		}
		else if(contains(sql,"(show tables)"))
		{
			System.out.println("2)matching regular expressions: show tables");
			tmp = new ShowTablesSqlParser(sql);
		}
		else if(contains(sql,"(use database)(.+)"))
		{
			System.out.println("2)matching regular expressions: use database");
			tmp = new UseDatabaseSqlParser(sql);
		}
		else
		{
			System.out.println("Illegal sql command, please re-enter");
			return null;
		}
		//sql=sql.replaceAll("ENDSQL", "");
		//	throw new Exception(sql.replaceAll("ENDOFSQL", ""));
		//return null;

		return tmp.splitSql2Segment();
	}
	/** *//**
	 　* 看word是否在lineText中存在，支持正则表达式
	 　* @param sql:要解析的sql语句
	 　* @param regExp:正则表达式
	 　* @return
	 　*/
	private static boolean contains(String sql,String regExp)
	{
		Pattern pattern=Pattern.compile(regExp,Pattern.CASE_INSENSITIVE);
		Matcher matcher=pattern.matcher(sql);
		return matcher.find();
	}
}
