package sqlparser;
/** *//**
 *
 * 单句显示数据库语句解析器
 * 2018-4-14
 */
//correct
public class ShowDatabaseSqlParser extends BaseSingleSqlParser
{

	public ShowDatabaseSqlParser(String originalSql)
	{
		super(originalSql);
		// TODO Auto-generated constructor stub
	}
	//show databases;

	@Override
	protected void initializeSegments()
	{

		//System.out.println("调用了ShowDatabaseSqlParser的initializeSegments方法");
		segments.add(new SqlSegment("(show databases)(.+)(ENDOFSQL)","[,]"));
	}

}
