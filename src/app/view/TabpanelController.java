package app.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import app.Main;
import app.model.UserInfo;
import app.util.FileUtil;
import app.util.PBEncryption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TabpanelController {

	private Main mainApp;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab userInfoTab;
	@FXML
	private Tab symTab;
	@FXML
	private Tab asymTab;
	@FXML
	private Tab checksumTab;
	@FXML
	private UserInfoTabController userInfoTabController;
	@FXML
	private SymTabController symTabController;
	@FXML
	private AsymTabController asymTabController;
	@FXML
	private CheckSumTabController checkSumTabController;

	public TabpanelController() {
	}

	@FXML
	private void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/userInfoTab.fxml"));
			userInfoTab.setContent(loader.load());
			userInfoTabController = loader.getController();

			loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SymTab.fxml"));
			symTab.setContent(loader.load());
			symTabController = loader.getController();

			loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/AsymTab.fxml"));
			asymTab.setContent(loader.load());
			asymTabController = loader.getController();

			loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/CheckSumTab.fxml"));
			checksumTab.setContent(loader.load());
			checkSumTabController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
		userInfoTabController.setMainApp(mainApp);
		symTabController.setMainApp(mainApp);
		asymTabController.setMainApp(mainApp);
		checkSumTabController.setMainApp(mainApp);
	}

}