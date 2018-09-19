package quote;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

import esf.lucene.SearchFiles;
import esf.lucene.SearchResult;

public class GetQuote {
	public static void main(String[] args) throws ParseException, IOException {
		SearchResult searchResult = new GetQuote().getRandomQuote();
		System.out.println( searchResult.subject + ":" +  searchResult.preamble );
	}
	public SearchResult getRandomQuote() throws IOException {
		SearchFiles searchFiles = new SearchFiles();
		SearchResult searchResult = searchFiles.randomResult();
		searchFiles.close();
		
		return searchResult;
	}

}
