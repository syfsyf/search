package org.syfsyf.search;

import java.text.SimpleDateFormat;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 * CustomQueryParser for handling numbers and dates range
 * field name should ends with .l,.i,.d,.dt 
 * example:
 * 	file.time.d:[2012-01-01 TO 2012-01-10]
 * 
 */
public class CustomQueryParser extends QueryParser {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	protected CustomQueryParser(CharStream stream) {
		super(stream);
	}

	public CustomQueryParser(QueryParserTokenManager tm) {
		super(tm);
	}

	public CustomQueryParser(Version matchVersion, String f, Analyzer a) {
		super(matchVersion, f, a);
	}

	@Override
	protected org.apache.lucene.search.Query getRangeQuery(String field,
			String part1, String part2, boolean inclusive)
			throws ParseException {
		boolean minInclusive = inclusive;
		boolean maxInclusive = inclusive;
		if (field.endsWith(".l")) {
			Long min = Long.valueOf(part1);
			Long max = Long.valueOf(part2);
			
			NumericRangeQuery<Long> q = NumericRangeQuery.newLongRange(field,
					min, max, minInclusive, maxInclusive);
			return q;
		} else if (field.endsWith(".i")) {
			Integer min = Integer.valueOf(part1);
			Integer max = Integer.valueOf(part2);
			NumericRangeQuery<Integer> q = NumericRangeQuery.newIntRange(field,
					min, max, minInclusive, maxInclusive);
			return q;
		} else if (field.endsWith(".d")) {
			try {
				Long min = dateFormat.parse(part1).getTime();
				Long max = dateFormat.parse(part2).getTime();
				NumericRangeQuery<Long> q = NumericRangeQuery.newLongRange(
						field, min, max, minInclusive, maxInclusive);
				return q;
			} catch (Exception e) {
				new ParseException(e.getMessage());
			}
		} else if (field.endsWith(".dt")) {
			try {
				Long min = dateTimeFormat.parse(part1).getTime();
				Long max = dateTimeFormat.parse(part2).getTime();
				
				NumericRangeQuery<Long> q = NumericRangeQuery.newLongRange(
						field, min, max, minInclusive, maxInclusive);
				return q;
			} catch (Exception e) {
				new ParseException(e.getMessage());
			}
		}
		return super.getRangeQuery(field, part1, part2, inclusive);

	}

	public static void main(String[] args) throws ParseException,
			java.text.ParseException {
		Analyzer a = new StandardAnalyzer(ConfigManager.LUCENE_VERSION);
		// System.out.println("");
		CustomQueryParser customQueryParser = new CustomQueryParser(
				ConfigManager.LUCENE_VERSION, "", a);

		Query res = customQueryParser
				.parse("a:b AND (c.l:[-1 TO -2] OR d:[10 TO 20]) AND x.d:[2012-01-01 TO 2012-02-01]");

		
		System.out.println("res:"+res);
	}

}
