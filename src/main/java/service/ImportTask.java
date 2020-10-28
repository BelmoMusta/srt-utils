package service;


import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import model.SrtItem;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class ImportTask extends Task<Set<SrtItem>> {
	
	private File file;
	private BooleanProperty visibleProperty;
	
	public ImportTask(File file, BooleanProperty visibleProperty) {
		this.file = file;
		this.visibleProperty = visibleProperty;
	}
	
	public ImportTask(File file) {
		this(file, null);
	}
	
	@Override
	protected Set<SrtItem> call() {
		if (visibleProperty != null) {
			visibleProperty.set(true);
		}
		final String content = loadFile(file);
		return SrtItemReader.createItemsFromContent(content);
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
