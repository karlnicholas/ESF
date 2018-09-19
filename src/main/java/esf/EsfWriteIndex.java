package esf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import esf.lucene.IndexFiles;
import esf.lucene.SearchResult;

public class EsfWriteIndex {
	private static final String baseUrl = "http://www.sf-encyclopedia.com/";
	private IndexFiles indexFiles;
	private Map<String, SearchResult> resultMap;

	public static void main(String[] args) throws Exception {
		new EsfWriteIndex().run();
	}
	
	@SuppressWarnings("deprecation")
	private void run() throws Exception {
		indexFiles = new IndexFiles(); 
		resultMap = new HashMap<>();
		final ZipFile file = new ZipFile( "c:/users/karln/eclipse-workspace/ESF-RIP/src/main/resources/encyclopedia-entries.zip" );
		try
		{
		    final Enumeration<? extends ZipEntry> entries = file.entries();
		    while ( entries.hasMoreElements() )
		    {
		        final ZipEntry entry = entries.nextElement();
		        
		        //use entry input stream:
		        String strEntry = readInputStream( file.getInputStream( entry ), entry.getSize() );
		        String descr = new BufferedReader( new StringReader(strEntry)).readLine();
		        Document doc = Jsoup.parse(strEntry, baseUrl);
		        Elements ps = doc.select("p");
		        boolean found = false;
		        Element foundEl = null;
		        for ( Element el: ps) {
		        	if ( found ) {
		        		foundEl = el;
		        		break;
		        	}
		        	String elClass = el.attr("class");
		        	if ( elClass.contains("entryCategories") ) {
		        		found = true;
		        	}
		        }
		        if ( found ) {
		        	String text = foundEl.text();
		        	String[] descrips = descr.split("\\|");
		        	String[] titles = descrips[3].split(",");
		        	StringBuilder title = new StringBuilder(titles[0]);
		        	if ( titles.length > 1 ) {
	        			title.insert(0, titles[1].trim() + ' ');
		        	}
		    		resultMap.put(title.toString(), new SearchResult( title.toString(), descrips[2], StringEscapeUtils.escapeXml10(text), 0.0f ));
		        }
		    }
		}
		finally
		{
		    file.close();
		}

		for ( SearchResult sr: resultMap.values()) {
			indexFiles.indexEntry(sr.subject, sr.url, sr.preamble);
		}
		indexFiles.close();
	}

	private String readInputStream( final InputStream is, long entrySize) throws IOException {
	    final byte[] buf = new byte[ (int)entrySize ];
	    int read = 0;
	    int cntRead;
	    while ( ( cntRead = is.read( buf, read, buf.length - read ) ) > 0  ) {
	        read += cntRead;
	    }
	    if ( read != entrySize ) {
	    	throw new IllegalStateException("Size Mismatch");
	    }
	    return new String(buf, StandardCharsets.UTF_8);
	}		

}
