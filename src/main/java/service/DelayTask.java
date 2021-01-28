package service;


import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import model.SrtItem;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class DelayTask extends Task<Set<SrtItem>> {
	
	private  String instant;
	private File file;
	private BooleanProperty visibleProperty;
	
	public DelayTask(File file, BooleanProperty visibleProperty) {
		this.file = file;
		this.visibleProperty = visibleProperty;
	}
	
	public DelayTask(File file) {
		this(file, null);
	}
	
	public DelayTask(File srtFile, String instant, int y) {
		this(srtFile, null);
		this.instant = instant;
		
	}
	
	@Override
	protected Set<SrtItem> call() {
		if (visibleProperty != null) {
			visibleProperty.set(true);
		}
		final String content = loadFile(file);
		return SrtItemReader.createItemsFromContent(content, instant);
	}
	
	private String loadFile(File file) {
		String fileRead = "";
		if (file != null) {
			try {
				fileRead = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileRead;
	}
	
}
