/* Copyright (c) restSQL Project Contributors. Licensed under MIT. */
package org.restsql.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * Builds SQL for an operation on a SQL Resource.
 * 
 * @author Mark Sawers
 */
public interface SqlBuilder {

	/** Creates select SQL. */
	public SqlStruct buildSelectSql(final SqlResourceMetaData metaData, final String mainSql,
			final Request request) throws InvalidRequestException;

	/** Creates update, insert or delete SQL. */
	public Map<String, SqlStruct> buildWriteSql(final SqlResourceMetaData metaData, final Request request,
			final boolean doParent) throws InvalidRequestException;

	/**
	 * Helper struct for building SQL.
	 * 
	 * @author Mark Sawers
	 */
	public static class SqlStruct {
		private final StringBuilder clause, main, preparedClause, preparedStatement, statement, limit;
		private StringBuilder preparedMain;
		private final List<Object> preparedValues;
                private final List<Object> placeHolderValues;

		public SqlStruct(final int mainSize, final int clauseSize) {
			main = new StringBuilder(mainSize);
			clause = new StringBuilder(clauseSize);
			preparedClause = new StringBuilder(clauseSize);
			preparedValues = new ArrayList<>(clauseSize);
                        placeHolderValues = new ArrayList<>(clauseSize);
			statement = new StringBuilder(mainSize + clauseSize);
			preparedStatement = new StringBuilder(mainSize + clauseSize);
                        limit = new StringBuilder(clauseSize);
		}

		public SqlStruct(final int mainSize, final int clauseSize, final boolean usePreparedMain) {
			this(mainSize, clauseSize);
			preparedMain = new StringBuilder(mainSize);
		}

		public void appendToBothClauses(final String string) {
			clause.append(string);
			preparedClause.append(string);
		}

		public void appendToBothMains(final String string) {
			main.append(string);
			preparedMain.append(string);
		}

                public void appendToLimit(final String string) {
                    if(StringUtils.isNotEmpty(string)){
                        limit.append(string);
                    }
                }
                
                
                
		/**
		 * Appends clause to the main for the complete statement, and prepared clause to the main for the complete
		 * prepared statement.
		 */
		public void compileStatements() {
                    
                        String mainPart1 = main.toString();
                        String mainPart2 = "";
                        
                        // On va gérer le cas où on a un order by dans la requête
                        
                        int idx1 = StringUtils.lastIndexOf(mainPart1,')');
                        if(idx1 < 0 ){
                            idx1 = 0;
                        }
                        int idx2 = StringUtils.indexOf(mainPart1.toUpperCase(), "ORDER BY", idx1);
                        if(idx2 > 0){
                            mainPart2 = mainPart1.substring(idx2);
                            mainPart1 = mainPart1.substring(0,idx2);
                        }
                        
			statement.append(mainPart1).append(" ");
			statement.append(clause).append(" ");
                        statement.append(mainPart2).append(" ");
                        statement.append(limit);
                        
			preparedStatement.append(preparedMain == null ? mainPart1 : preparedMain).append(" ");
			preparedStatement.append(preparedClause).append(" ");
                        preparedStatement.append(preparedMain == null ? mainPart2 : "").append(" ");
                        preparedStatement.append(limit);
		}

		public StringBuilder getClause() {
			return clause;
		}

		public StringBuilder getMain() {
			return main;
		}

		public StringBuilder getPreparedClause() {
			return preparedClause;
		}

		public StringBuilder getPreparedMain() {
			return preparedMain;
		}

		public String getPreparedStatement() {
			return preparedStatement.toString();
		}

		public List<Object> getPreparedValues() {
			return preparedValues;
		}

                public List<Object> getPlaceHolderValues() {
                    return placeHolderValues;
                }

		public String getStatement() {
			return statement.toString();
		}

		public boolean isClauseEmpty() {
			return clause.length() == 0;
		}

                public StringBuilder getLimit() {
                    return limit;
                }
                
                
	}

}