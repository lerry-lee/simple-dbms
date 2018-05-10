package sqlparser;
/** *//**
 *
 * 单句插入语句解析器
 */
//correct-test
public class InsertSqlParser extends BaseSingleSqlParser{

	public InsertSqlParser(String originalSql) {
		super(originalSql);

	}
	//insert into table_name (name,age,sex) values ("小明","28","女");
	@Override
	protected void initializeSegments()
	{
		//System.out.println("调用了InsertSqlParser的initializeSegments方法");
		segments.add(new SqlSegment("(insert into)(.+?)([(])","[,]"));
		segments.add(new SqlSegment("([(])(.+?)([)] values[(])","[,]"));
		segments.add(new SqlSegment("([)] values[(])(.+)([)] ENDOFSQL)","[,]"));

	}

	public String getParsedSql()
	{
		String retval=super.getParsedSql();
		retval=retval+")";
		return retval;
	}

}
