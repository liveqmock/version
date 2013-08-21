package com.win.tools.easy.other.formatter;

import java.util.StringTokenizer;

import org.springframework.util.StringUtils;



/**
 * copy from org.hibernate.engine.jdbc.internal.DDLFormatterImpl
 * @author 袁晓冬
 *
 */
public class DDLFormatter {
	/**
	 * Format an SQL statement using simple rules<ul>
	 * <li>Insert newline after each comma</li>
	 * <li>Indent three spaces after each inserted newline</li>
	 * </ul>
	 * If the statement contains single/double quotes return unchanged,
	 * it is too complex and could be broken by simple formatting.
	 * 
	 * @param sql The statement to be fornmatted.
	 */
	public String format(String sql) {
        if ( !StringUtils.hasLength(sql ) ) return sql;
		if ( sql.toLowerCase().startsWith( "create table" ) ) {
			return formatCreateTable( sql );
		}
		else if ( sql.toLowerCase().startsWith( "alter table" ) ) {
			return formatAlterTable( sql );
		}
		else if ( sql.toLowerCase().startsWith( "comment on" ) ) {
			return formatCommentOn( sql );
		}
		else {
			return "\n    " + sql;
		}
	}

	private String formatCommentOn(String sql) {
		StringBuilder result = new StringBuilder( 60 ).append( "\n    " );
//		StringBuilder result = new StringBuilder( 60 ).append( "" );
		StringTokenizer tokens = new StringTokenizer( sql, " '[]\"", true );

		boolean quoted = false;
		while ( tokens.hasMoreTokens() ) {
			String token = tokens.nextToken();
			result.append( token );
			if ( isQuote( token ) ) {
				quoted = !quoted;
			}
			else if ( !quoted ) {
				if ( "is".equals( token ) ) {
					result.append( "\n       " );
				}
			}
		}

		return result.toString();
	}

	private String formatAlterTable(String sql) {
		StringBuilder result = new StringBuilder( 60 ).append( "\n    " );
//		StringBuilder result = new StringBuilder( 60 ).append( "" );
		StringTokenizer tokens = new StringTokenizer( sql, " (,)'[]\"", true );

		boolean quoted = false;
		while ( tokens.hasMoreTokens() ) {
			String token = tokens.nextToken();
			if ( isQuote( token ) ) {
				quoted = !quoted;
			}
			else if ( !quoted ) {
				if ( isBreak( token ) ) {
					result.append( "\n        " );
				}
			}
			result.append( token );
		}

		return result.toString();
	}

	private String formatCreateTable(String sql) {
		StringBuilder result = new StringBuilder( 60 ).append( "\n    " );
//		StringBuilder result = new StringBuilder( 60 ).append( "" );
		StringTokenizer tokens = new StringTokenizer( sql, "(,)'[]\"", true );

		int depth = 0;
		boolean quoted = false;
		while ( tokens.hasMoreTokens() ) {
			String token = tokens.nextToken();
			if ( isQuote( token ) ) {
				quoted = !quoted;
				result.append( token );
			}
			else if ( quoted ) {
				result.append( token );
			}
			else {
				if ( ")".equals( token ) ) {
					depth--;
					if ( depth == 0 ) {
						result.append( "\n    " );
					}
				}
				result.append( token );
				if ( ",".equals( token ) && depth == 1 ) {
					result.append( "\n       " );
				}
				if ( "(".equals( token ) ) {
					depth++;
					if ( depth == 1 ) {
						result.append( "\n        " );
					}
				}
			}
		}

		return result.toString();
	}

	private static boolean isBreak(String token) {
		return "drop".equals( token ) ||
				"add".equals( token ) ||
				"references".equals( token ) ||
				"foreign".equals( token ) ||
				"on".equals( token );
	}

	private static boolean isQuote(String tok) {
		return "\"".equals( tok ) ||
				"`".equals( tok ) ||
				"]".equals( tok ) ||
				"[".equals( tok ) ||
				"'".equals( tok );
	}

}