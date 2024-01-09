package sqlparser;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSingleSqlParser {

	//原始Sql语句
	protected String originalSql;

	//Sql语句片段
	protected List<SqlSegment> segments;


	/** *//**
	 　* 构造函数，传入原始Sql语句，进行劈分。
	 　* @param originalSql
	 　*/
	public BaseSingleSqlParser(String originalSql)
	{
		//System.out.println("调用了BaseSingleSqlParser的构造函数");
		this.originalSql=originalSql;
		segments=new ArrayList<SqlSegment>();
		initializeSegments();
		//splitSql2Segment();
	}

	/** *//**
	 　* 初始化segments，强制子类实现
	 　*
	 　*/
	protected abstract void initializeSegments();


	/**
	 　* 将originalSql劈分成一个个片段
	 　*
	 */
	public List<List<String>> splitSql2Segment()//分割成两个模块
	{
		List<List<String>> list=new ArrayList<List<String>>();

		//System.out.println("调用了BaseSingleSqlParser的splitSql2Segment方法，用于分割sql为不同的模块");
		for(SqlSegment sqlSegment:segments)    // int[] aaa = 1,2,3;   int a:aaa
		{
			sqlSegment.parse(originalSql);
			list.add(sqlSegment.getBodyPieces());
		}
		return list;
	}

	/**
	 　* 得到解析完毕的Sql语句
	 　* @return
	 　*/
	public String getParsedSql()
	{
		StringBuffer sb=new StringBuffer();
		for(SqlSegment sqlSegment:segments)
		{
			sb.append(sqlSegment.getParsedSqlSegment()+"n");
		}
		String retval=sb.toString().replaceAll("n+", "n");
		return retval;
	}

}
