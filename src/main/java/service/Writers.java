package service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

public class Writers {
	private static <T> void writeToFile(T objectToBeWritten, File outputFile) throws IOException {
		writeToWriter(objectToBeWritten, new FileWriter(outputFile));
	}
	
	public static <T> void writeToWriter(T objectToBeWritten, Writer writer) throws IOException {
		writer.write(objectToBeWritten.toString());
		writer.close();
	}
	
	public static <T> void writeCollectionToWriter(Collection<T> collection, Writer writer) throws IOException {
		String s = collection.stream()
				.map(Objects::toString)
				.reduce("", (a, b) -> a + '\n' + b);
		writeToWriter(s, writer);
	}
	
	public static <T> void writeCollectionToFile(Collection<T> collection, File outputFile) throws IOException {
		writeCollectionToWriter(collection, new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8));
	}
}
