package controller;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import service.ExcelImporterService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExcelToSRTController implements Initializable {
	
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
	@FXML
	public ChoiceBox<String> language;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpExcelSelectButton();
		setUpSRTSelectButton();
		setupLanguageChoice();
		setupConvertButton();
	}
	
	private void setupConvertButton() {
		convert.setOnAction(event -> {
			try {
				ExcelImporterService.createSRTFileFromExcel(excelPath.getText(), srtPath.getText(),
						language.getValue());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private void setUpSRTSelectButton() {
		selectSRTFile.setOnAction(event -> {
			FileChooser chooser = new FileChooser();
			chooser.setInitialFileName("content.srt");
			chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("SRT file", "srt"));
			File file = chooser.showSaveDialog(null);
			if (file != null) {
				srtPath.setText(file.getAbsolutePath());
			}
		});
	}
	
	private void setupLanguageChoice() {
		List<String> langs = new ArrayList<>();
		langs.add("EN");
		langs.add("FR");
		langs.add("AR");
		langs.add("DA");
		ObservableList<String> listOfLanguages = new ImmutableObservableList(langs.toArray());
		language.setItems(listOfLanguages);
		language.getSelectionModel().select(0);
	}
	
	private void setUpExcelSelectButton() {
		selectExcelFile.setOnAction(event -> {
			FileChooser chooser = new FileChooser();
			chooser.setInitialFileName("content.xlsx");
			chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Excel file", "xlsx"));
			File file = chooser.showOpenDialog(null);
			if (file != null) {
				excelPath.setText(file.getAbsolutePath());
			}
		});
	}
}
