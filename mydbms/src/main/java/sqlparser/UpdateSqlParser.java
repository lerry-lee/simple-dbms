package sqlparser;

import com.sun.org.glassfish.gmbal.Description;

/**
 * 单句更新语句sql解析
 */
 @Description("更新语句解析")
public class UpdateSqlParser extends BaseSingleSqlParser{

	public UpdateSqlParser(String originalSql) {
		super(originalSql);

	}

	//update(table_name) set (key = value) where()；
	//update table_name set age=3,name="xiaoming" where id=2 and id=6;

	@Override
	protected void initializeSegments()
	{
		//System.out.println("调用了UpdateSqlParser的initializeSegments方法");
		segments.add(new SqlSegment("(update)(.+)(set)","[,]"));
		segments.add(new SqlSegment("(set)(.+?)( where | ENDOFSQL)","[,]"));
		segments.add(new SqlSegment("(where)(.+)(ENDOFSQL)","(and|or)"));
	}

}
