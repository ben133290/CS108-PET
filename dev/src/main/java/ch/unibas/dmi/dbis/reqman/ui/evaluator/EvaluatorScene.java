package ch.unibas.dmi.dbis.reqman.ui.evaluator;

import ch.unibas.dmi.dbis.reqman.core.Group;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ConcurrentModificationException;
import java.util.TreeMap;

/**
 * The class {@link EvaluatorScene} is the class which is directly the scene of the assessment mode.
 *
 * It initializes the components and lets its root lay them out.
 *
 * @author loris.sauter
 */
public class EvaluatorScene extends Scene{

    private BorderPane root;

    private SplitPane horizontalSplitter = new SplitPane();
    private SplitPane verticalSplitter = new SplitPane();

    private HBox leftContent = new HBox();
    private VBox rightContent = new VBox();
    private TabPane tabPane = new TabPane();

    private String title = "Evaluator";

    private EvaluatorController controller;

    private GroupView groupView;
    private CatalogueInfoView catInfoView;

    public EvaluatorScene(String title, int width, int height){
        this(width, height);
        this.title = title;

    }

    public EvaluatorScene(int width, int height){
        super(new BorderPane(), width, height);
        root = (BorderPane) getRoot();
        controller = new EvaluatorController(this);
        groupView = new GroupView(controller);
        catInfoView = new CatalogueInfoView();
        initComponents();
        disableAll();
    }

    public CatalogueInfoView getCatalogueInfoView(){
        return catInfoView;
    }


    private void initComponents(){
        horizontalSplitter.prefWidthProperty().bind(widthProperty() );
        horizontalSplitter.prefHeightProperty().bind(heightProperty() );

        verticalSplitter.setOrientation(Orientation.VERTICAL);

        verticalSplitter.prefWidthProperty().bind(widthProperty());
        verticalSplitter.prefHeightProperty().bind(heightProperty() );



        VBox upper = new VBox();
        upper.getChildren().add(catInfoView);

        VBox lower = new VBox();
        lower.getChildren().add(groupView);

        verticalSplitter.getItems().addAll(upper, lower);

        verticalSplitter.setDividerPositions(0.3);

        leftContent.getChildren().add(verticalSplitter);


        rightContent.setStyle("-fx-padding: 10px;-fx-spacing: 10px;");

        tabPane.setStyle("-fx-pading: 10px; -fx-spacing: 10px;");
        rightContent.getChildren().addAll(tabPane);// TODO Iff no catalogue loaded / all tabs closed: Plachoolder with info

        horizontalSplitter.getItems().addAll(leftContent, rightContent);


        root.setTop(createMenuBar() );

        VBox box = new VBox();
        box.getChildren().add(horizontalSplitter);
        root.setCenter(box);

        horizontalSplitter.setDividerPositions(0.3);
    }

    private MenuBar createMenuBar(){
        MenuBar bar = new MenuBar();

        Menu menuFile = new Menu("File");

        MenuItem itemLoad = new MenuItem("Load Catalogue");
        itemLoad.setOnAction(controller::handleLoadCatalogue);
        itemLoad.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        MenuItem itemNew = new MenuItem("New Group");
        itemNew.setOnAction(controller::handleAddGroup);
        itemNew.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));
        MenuItem itemOpen = new MenuItem("Open Group");
        itemOpen.setOnAction(controller::handleOpenGroup);
        itemOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        MenuItem itemSave = new MenuItem("Save Group");
        itemSave.setOnAction(controller::handleSaveGroup);
        MenuItem itemSaveAs = new MenuItem("Save Group As");
        itemSaveAs.setOnAction(controller::handleSaveAsGroup);
        MenuItem itemExportGroup = new MenuItem("Export Group(s)");
        itemExportGroup.setOnAction(controller::handleExportGroup);
        MenuItem itemExit = new MenuItem("Quit");


        menuFile.getItems().addAll(itemLoad, itemNew, itemOpen, itemSave, itemSaveAs, new SeparatorMenuItem(), itemExportGroup, new SeparatorMenuItem(), itemExit);

        Menu menuEdit = new Menu("Edit");
        MenuItem itemModify = new MenuItem("Modify Group");
        itemModify.setOnAction(controller::handleModifyGroup);

        menuEdit.getItems().addAll(itemModify);

        Menu menuView = new Menu("View");

        Menu menuHelp = new Menu("Help");
        MenuItem itemDebug = new MenuItem("DEBUG"); // DEBUG TODO Remove
        itemDebug.setOnAction(this::handleDebug);
        itemDebug.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));



        bar.getMenus().addAll(menuFile, menuEdit, menuView, menuHelp);
        return bar;
    }

    private void handleDebug(ActionEvent event) {
        Group active = getActiveGroup();
        Tab tab = groupTabMap.get(active);
        System.out.println(tab.getStyleClass());
    }

    public void addGroupTab(AssessmentView av){
        Tab tab = new Tab();
        tab.setText(av.getActiveGroup().getName() );
        av.bindToParentSize(rightContent);
        tab.setContent(av);
        tabPane.getTabs().add(tab);
        groupTabMap.put(av.getActiveGroup(), tab);
    }

    private TreeMap<Group, Tab> groupTabMap = new TreeMap<>();

    public boolean isGroupTabbed(Group group){
        for(Tab t : tabPane.getTabs() ){
            if(t.getText().equals(group.getName() )){
                return true;
            }
        }
        return false;
    }


    public Group getActiveGroup(){
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        Node content = selected.getContent();
        if(content instanceof AssessmentView ){
            AssessmentView av = (AssessmentView)content;
            return av.getActiveGroup();
        }else{
            throw new RuntimeException("This should definitively not happen (Non-AV-tab?)");
        }
    }

    public void removeTab(Group group){
        try{
            for(Tab tab : tabPane.getTabs() ){
                Node content = tab.getContent();
                if(content instanceof AssessmentView){
                    AssessmentView av = (AssessmentView)content;
                    if(group.equals(av.getActiveGroup() )){
                        tabPane.getTabs().remove(tab);
                        groupTabMap.remove(av.getActiveGroup());
                    }
                }
            }
        }catch(ConcurrentModificationException ex){
            // Silently catching this exception, since it occurs by design.
        }

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void disableAll(){
        groupView.setDisable(true);
        catInfoView.setDisable(true);
    }

    public void enableAll(){
        groupView.setDisable(false);
        catInfoView.setDisable(false);
    }

    public void markDirty(Group modified) {
        Tab tab = groupTabMap.get(modified);
        tab.getStyleClass().add("modified");
        if(tab.getText().indexOf("*")<0){
            tab.setText(tab.getText()+"*");
        }

    }

    public void unmarkDirty(Group modified){
        Tab tab = groupTabMap.get(modified);
        tab.getStyleClass().remove("modified");
        String text = tab.getText().substring(0, tab.getText().indexOf("*"));
        tab.setText(text);
    }
}
