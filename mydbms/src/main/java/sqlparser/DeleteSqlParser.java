package sqlparser;

/** 
 *
 * 单句删除语句解析器
 */
//正确
public class DeleteSqlParser extends BaseSingleSqlParser
{


	public DeleteSqlParser(String originalSql) {
		super(originalSql);

	}

	//delete from table_name where id=2 AND ID = 8;

	@Override
	protected void initializeSegments() {
		//System.out.println("调用了DeleteSqlParser的initializeSegments方法");
		segments.add(new SqlSegment("(delete from)(.+?)( where | ENDOFSQL)","[,]"));
		segments.add(new SqlSegment("(where)(.+)( ENDOFSQL)","(and|or)"));
	}

}


