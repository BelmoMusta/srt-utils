package controller;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.SrtItem;
import service.ExcelExporterService;
import service.ExcelImporterService;
import service.Extractor;
import service.ImportTask;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class SRTToExcelController implements Initializable {
	
	@FXML
	public Button selectExcelFile;
	@FXML
	public Button selectSRTFile;
	@FXML
	public TextField excelPath;
	@FXML
	public TextField srtPath;
	@FXML
	public Button convert;
	private File srtFile;
	private File excelFile;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpExcelSelectButton();
		setUpSRTSelectButton();
		setupConvertButton();
	}
	
	private void setupConvertButton() {
		convert.setOnAction(event -> {
			ExcelExporterService<SrtItem> excelExporterService = new ExcelExporterService<>();
			
			final Task<Set<SrtItem>> task = new ImportTask(srtFile);
			task.setOnSucceeded(ev -> {
				try {
					Set<SrtItem> srtItems = task.get();
					final Map<String, Extractor<SrtItem, String>> extractors = new LinkedHashMap<>();
					extractors.put("Number", item -> String.valueOf(item.getOrder()));
					extractors.put("Start", SrtItem::getStartsAt);
					extractors.put("Ends", SrtItem::getEndsAt);
					extractors.put("Content", SrtItem::getText);
					extractors.put("FR", s -> "");
					extractors.put("AR", s -> "");
					extractors.put("DA", s -> "");
					excelExporterService.export(srtItems, extractors, excelFile);
					
				} catch (InterruptedException | ExecutionException | IOException e) {
					System.exit(0);
					throw new IllegalStateException(e);
				}
			});
			
			Platform.runLater(task);
		});
	}
	
	private void setUpSRTSelectButton() {
		selectSRTFile.setOnAction(event -> {
			FileChooser chooser = new FileChooser();
			chooser.setInitialFileName("content.srt");
			chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("SRT file", "srt"));
			File file = chooser.showOpenDialog(null);
			if (file != null) {
				srtPath.setText(file.getAbsolutePath());
				this.srtFile = file;
			}
		});
	}
	
	
	private void setUpExcelSelectButton() {
		selectExcelFile.setOnAction(event -> {
			FileChooser chooser = new FileChooser();
			chooser.setInitialFileName("content.xlsx");
			chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Excel file", "xlsx"));
			File file = chooser.showSaveDialog(null);
			if (file != null) {
				excelPath.setText(file.getAbsolutePath());
				this.excelFile = file;
			}
		});
	}
}
