package esf;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

import esf.lucene.SearchFiles;
import esf.lucene.SearchResult;

public class DoSearch {
	public static void main(String[] args) throws ParseException, IOException {
		new DoSearch().doSearch();
	}
	public void doSearch() throws ParseException, IOException {
		SearchFiles searchFiles = new SearchFiles();
		List<SearchResult> searchResults = searchFiles.query("Star Trek");
		if ( searchResults.size() > 0 ) {
			System.out.println("Found results: " + searchResults.size());
			for ( SearchResult searchResult: searchResults ) {
				System.out.println(searchResult);
			}
			System.out.println("Best result\n"+searchResults.get(0).subject);
			System.out.println("Best result\n"+searchResults.get(0).preamble);
		}
		searchFiles.close();
	}

}
