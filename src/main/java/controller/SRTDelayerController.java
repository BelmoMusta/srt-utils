package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.SrtItem;
import service.DelayTask;
import service.Writers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class SRTDelayerController implements Initializable {
	
	@FXML
	public Button selectOutputFile;
	@FXML
	public Button selectSRTFile;
	@FXML
	public TextField excelPath;
	private File srtFile;
	private File excelFile;
	
	@FXML
	public TextField srtPath;
	@FXML
	public Button delay;
	@FXML
	public TextField instant;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpExcelSelectButton();
		setUpSRTSelectButton();
		setupConvertButton();
	}
	
	private void setupConvertButton() {
		delay.setOnAction(event -> {
			String text = instant.getText();
			Dialog dialog = new Alert(Alert.AlertType.ERROR);
			if (text == null || !text.matches("[-+]?\\d{2}:\\d{2}:\\d{2},\\d{3}")) {
				dialog.setTitle("Error");
				dialog.setContentText("Duration is not valid.\nExample 01:02:03:234");
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						dialog.hide();
					}
				}, 5000);
				
				dialog.showAndWait();
				
				return;
			} if (srtFile == null || !srtFile.exists()) {
				dialog.setTitle("Error");
				dialog.setContentText("Please choose a valid input file");
				dialog.showAndWait();
				return;
			}
			if (excelFile == null || !excelFile.exists()) {
				dialog.setTitle("Error");
				dialog.setContentText("Please choose a valid oututFile file");
				dialog.showAndWait();
				return;
			}
			final Task<Set<SrtItem>> task = new DelayTask(srtFile, text, 0);
			task.setOnSucceeded(ev -> {
				try {
					Set<SrtItem> srtItems = task.get();
					Writers.writeCollectionToFile(srtItems, excelFile);
					Dialog successAlert  = new Alert(Alert.AlertType.CONFIRMATION);
					successAlert.setContentText("Srt delayed file exported at  " + excelFile.getAbsolutePath());
					successAlert.showAndWait();
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							successAlert.close();;
						}
					}, 5000);
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
		selectOutputFile.setOnAction(event -> {
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
