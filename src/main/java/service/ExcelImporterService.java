package service;

import model.SrtItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class ExcelImporterService<T> {
	public static void main(String[] args) throws Exception {
		String path = "C:\\Users\\mbelmokhtar\\Desktop\\content.xlsx";
		String language = "AR";
		String outputFile = "output.srt";
		createSRTFileFromExcel(path, outputFile, language);
	}
	
	public static void createSRTFileFromExcel(String path, String outputFile, String language) throws IOException {
		final Set<SrtItem> items = createSrtItemsFromExcelFile(path, language);
		Writers.writeCollectionToFile(items, new File(outputFile));
	}
	
	private static Set<SrtItem> createSrtItemsFromExcelFile(String excelFilePath, String language) throws IOException {
		final Workbook workbook = new XSSFWorkbook(new FileInputStream(new File(excelFilePath)));
		final Sheet sheet = workbook.getSheetAt(0);
		final Iterator<Row> rowIterator = sheet.rowIterator();
		rowIterator.next();
		final Set<SrtItem> items = new TreeSet<>();
		while (rowIterator.hasNext()) {
			
			SrtItem srtItem = convertRowToSrtItem(rowIterator.next());
			srtItem.setLanguage(language);
			items.add(srtItem);
		}
		return items;
	}
	
	private static SrtItem convertRowToSrtItem(Row row) {
		final SrtItem srtItem = new SrtItem();
		srtItem.setOrder(Integer.parseInt(getStringCellValue(row, 0)));
		srtItem.startsAt(getStringCellValue(row, 1));
		srtItem.endsAt(getStringCellValue(row, 2));
		srtItem.setText(getStringCellValue(row, 3));
		srtItem.setText("FR", getStringCellValue(row, 4));
		srtItem.setText("AR", getStringCellValue(row, 5));
		srtItem.setText("DA", getStringCellValue(row, 6)); // short for darija
		
		return srtItem;
	}
	
	private static String getStringCellValue(Row row, int i) {
		return Optional.ofNullable(row.getCell(i))
				.map(Cell::getStringCellValue)
				.orElse("");
	}
}
