package sqlparser;
/**
 *
 * 单句使用数据库语句解析器
 * 2018-4-14
 */
//correct
public class UseDatabaseSqlParser extends BaseSingleSqlParser
{

	public UseDatabaseSqlParser(String originalSql) {
		super(originalSql);
	}

	//use databases_name;

	@Override
	protected void initializeSegments() {
		//System.out.println("调用了UserDatabaseSqlParser的initializeSegments方法");
		segments.add(new SqlSegment("(use database)(.+)( ENDOFSQL)","[,]"));
	}

}
