package sqlparser;
/** *//**
 *
 * 单句使用数据库语句解析器
 * 2018-4-14
 */
public class CreateTableSqlParser extends BaseSingleSqlParser
{

	public CreateTableSqlParser(String originalSql) {
		super(originalSql);

	}

	//create table table_name(id int not null primary key,name varchar(8) not null );
	@Override
	protected void initializeSegments()
	{
		//System.out.println("调用了CreateTableSqlParser的initializeSegments方法");
		segments.add(new SqlSegment("(create table)(.+?)([(])","[,]"));
		segments.add(new SqlSegment("([(])(.+?)([)] ENDOFSQL)","[,]"));

	}

}
