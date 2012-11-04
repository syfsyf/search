package org.syfsyf.search;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;

public class FileIndexer implements Indexer{

	private static final Logger LOGGER = Logger.getLogger(FileIndexer.class);
	
	public static final String FILE_NAME="file.name";
	public static final String FILE_PATH="file.path";
	public static final String FILE_SIZE="file.size";
	public static final String FILE_DATE="file.date";
	/**
	 * 2 values 'd' - directory 'f'- file
	 */
	public static final String FILE_TYPE="file.type";
	//public static final String FILE_EXT="file.ext";
	
	
	public void index(IndexerContext context) throws Exception {
		
		
		File file=context.getFile();
		LOGGER.debug("indexing file:"+file);
		Document document=context.getDocument();
		Fieldable field;
		field=new Field(FILE_NAME,file.getName(),Field.Store.NO,Field.Index.ANALYZED);
		document.add(field);
		
		/*
		String ext = FilenameUtils.getExtension(file.getName());
		field=new Field(FILE_EXT,ext,Field.Store.NO,Field.Index.ANALYZED);
		document.add(field);
		*/
		
		// context.getDir().getPath();

		File rootPath = new File(context.getDir().getPath());
		String rootAbsPath = rootPath.getAbsolutePath();
		
	
		
		String path=file.getAbsolutePath();
		LOGGER.debug("rootAbsPath:"+rootAbsPath+" path:"+path);
		path=path.substring(rootAbsPath.length()+1);
		path=FilenameUtils.getPath(path);
		//
		if(path.length()>0){
			path=path.substring(0,path.length()-1);
		}
		LOGGER.debug("relativePath:"+path);
		field=new Field(FILE_PATH,path,Field.Store.YES,Field.Index.ANALYZED);
		document.add(field);
		
		
		
		String type="d";
		
		if(file.isFile()){
			long fileSize = file.length();
			document.add(new NumericField(FILE_SIZE).setLongValue(fileSize));
			type="f";
		}
		field=new Field(FILE_TYPE,type,Field.Store.YES,Field.Index.ANALYZED);
		document.add(field);
		
		
		document.add(new NumericField(FILE_DATE).setLongValue(file.lastModified()));
		
		
	}

}
