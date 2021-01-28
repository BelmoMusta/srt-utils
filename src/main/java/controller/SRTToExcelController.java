package controller;

import io.github.belmomusta.excel.importexport.ExcelExporter;
import io.github.belmomusta.excel.importexport.exception.ExcelExporterException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.SrtItem;
import service.ImportTask;

import java.io.File;
import java.net.URL;
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
			final Task<Set<SrtItem>> task = new ImportTask(srtFile);
			task.setOnSucceeded(ev -> {
				try {
					Set<SrtItem> srtItems = task.get();
					ExcelExporter.exportContent(srtItems)
							.toFile(excelFile)
							.withHeaders("Number", "Start", "Ends", "Content", "FR", "AR", "DA")
							.mapMethod("getOrder").toCell(0)
							.mapMethod("getStartsAt").toCell(1)
							.mapMethod("getEndsAt").toCell(2)
							.mapMethod("getText").toCell(3)
							.export();
				} catch (InterruptedException | ExecutionException  | ExcelExporterException e) {
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
