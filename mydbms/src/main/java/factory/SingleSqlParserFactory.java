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

		if(contains(sql,"(create database)([^\\n]+)")) {// 判断字符串sql是否包含"(create database)"以及之后的任意字符
		System.out.println("2)匹配正则表达式：create database");
		tmp = new CreateDatabaseSqlParser(sql);
		}
		else if(contains(sql,"(drop database)(.+)"))
		{
			System.out.println("2)匹配正则表达式：drop database");
			tmp = new DropDatabaseSqlParser(sql);

		}
		else if(contains(sql,"(show databases)"))
		{
			System.out.println("2)匹配正则表达式：show databases");
			tmp = new ShowDatabaseSqlParser(sql);
		}
		else if(contains(sql,"(show tables)"))
		{
			System.out.println("2)匹配正则表达式：show tables");
			tmp = new ShowTablesSqlParser(sql);
		}
		else if(contains(sql,"(use database)(.+)"))
		{
			System.out.println("2)匹配正则表达式：use database");
			tmp = new UseDatabaseSqlParser(sql);
		}
		else if(contains(sql,"(create table)(.+)"))
		{
			System.out.println("2)匹配正则表达式：create table");
			tmp = new CreateTableSqlParser(sql);

		}
		else if(contains(sql,"(insert into)(.+)(values)(.+)"))
		{
			System.out.println("2)匹配正则表达式：insert into");
			tmp = new InsertSqlParser(sql);

		}
		else if(contains(sql,"(insert into)(.+)(valuess)(.+)()"))
		{
			System.out.println("2)匹配正则表达式：insert into where");
			tmp = new InsertSelectSqlParser(sql);

		}
		else if(contains(sql,"(select \\* from)(.+)"))
		{
			if(contains(sql,"(select \\* from)(.+)(where)(.+)")){
				System.out.println("2)匹配正则表达式：select * from where");
				tmp=new SelectAllWhereSqlParser(sql);
			}
			else {
				System.out.println("2)匹配正则表达式：select * from");
				tmp = new SelectAllSqlParser(sql);
			}

		}
		else if(contains(sql,"(select)(.+)(from)(.+)"))
		{

			System.out.println("2)匹配正则表达式：select from");
			tmp = new SelectSqlParser(sql);

		}

		else if(contains(sql,"(delete from)(.+)"))
		{
			System.out.println("2)匹配正则表达式：delete from");
			tmp = new DeleteSqlParser(sql);

		}

        else if(contains(sql,"(drop table)(.+)"))
        {
            System.out.println("2)匹配正则表达式：drop table");
            tmp = new DropTableSqlParser(sql);

        }
		else if(contains(sql,"(update)(.+)(set)(.+)"))
		{
			System.out.println("2)匹配正则表达式：update set");
			tmp = new UpdateSqlParser(sql);

		}
		else if(contains(sql,"(create index on)(.+)")){
			System.out.println("2)匹配正则表达式：create index on");
			tmp=new CreateIndexSqlParser(sql);
		}
		else if(contains(sql,"(drop index on)(.+)")){
			System.out.println("2)匹配正则表达式：drop index on");
			tmp=new DropIndexSqlParser(sql);
		}
		else if(contains(sql,"(create user)")){
			System.out.println("2)匹配正则表达式：create user");
			tmp=new CreateUserSqlParser(sql);
		}

		else
		{
			System.out.println("SQL语句有误，请重新输入");
			return null;
		}
		return tmp.splitSql2Segment();//注意此处是个方法，返回的是一个List<List<String>>

	}
	/** //**
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
