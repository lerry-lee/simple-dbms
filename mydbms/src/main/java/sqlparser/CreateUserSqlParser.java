package sqlparser;

public class CreateUserSqlParser extends BaseSingleSqlParser{

    public CreateUserSqlParser(String originalSql) {
        super(originalSql);
    }


    @Override
    protected void initializeSegments() {

        segments.add(new SqlSegment("(create user)(.+)(ENDOFSQL)","[,]"));

    }

}
