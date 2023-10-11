package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.User;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.JoinEventCollection;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableCell;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Date;

public class EventHistoryController {
    @FXML
    private AnchorPane footer;

    @FXML
    private AnchorPane navbar;

    @FXML
    private TableView<Event> eventHistoryTable;

    @FXML
    private TableColumn<Event, String> joinTimeColumn;
    @FXML
    private TableColumn<Event, ?> eventColumn;
    @FXML
    private TableColumn<Event, Integer> orderColumn;
    @FXML
    private TableColumn<Event, String> statusColumn;
    @FXML
    private TableColumn<Event, Void> toolColumn;

    private UUID userId;
    private HashMap<String, Object> data;
    private Datasource<UserCollection> userListFileDatasource;
    private Datasource<EventCollection> eventListFileDatasource;
    private Datasource<JoinEventCollection> joinEventCollectionDatasource;

    @FXML
    private void initialize() {
        userListFileDatasource = new UserListFileDatasource("data", "user.csv");
        eventListFileDatasource = new EventListFileDatasource("data/event", "event.csv");
        joinEventCollectionDatasource = new JoinEventListFileDatasource("data/event", "joinEvent.csv");

        data = FXRouter.getData();
        userId = UUID.fromString((String) data.get("userId"));

        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/cs211/project/views/navbar.fxml"));
        FXMLLoader footerComponentLoader = new FXMLLoader(getClass().getResource("/cs211/project/views/footer.fxml"));
        try {
            AnchorPane navbarComponent = navbarComponentLoader.load();
            NavbarController navbarController = navbarComponentLoader.getController();
            navbarController.setData(data);
            navbar.getChildren().add(navbarComponent);

            AnchorPane footerComponent = footerComponentLoader.load();
            footer.getChildren().add(footerComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = userListFileDatasource.query("id = " + userId).getAllUsers().get(0);
        EventCollection eventCollection = eventListFileDatasource.readData();
        JoinEventCollection joinEventCollection = joinEventCollectionDatasource.readData();

        loadEventsIntoTable();
    }

    private void loadEventsIntoTable() {
        List<String> userEventIds = joinEventCollectionDatasource.readData().getEventIdsByUserId(userId);
        List<Event> events = eventListFileDatasource.readData().getEvents();
        //List<JoinEvent> joinEvents = joinEventCollectionDatasource.readData().getJoinEvents();

        List<Event> userEvents = events.stream()
                .filter(event -> userEventIds.contains(event.getId()))
                .collect(Collectors.toList());

        /*List<String> joinTimes = joinEvents.stream()
                .map(JoinEvent::getJoinTime)
                .collect(Collectors.toList());
        List<JoinEvent> userJoinEvents = joinEvents.stream()
                .filter(joinEvent -> userEventIds.contains(joinEvent.getEventId()))
                .collect(Collectors.toList());*/

        orderColumn.setCellValueFactory(column -> {
            int rowIndex = eventHistoryTable.getItems().indexOf(column.getValue()) + 1;
            return new SimpleObjectProperty<>(rowIndex);
        });

        eventColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //joinTimeColumn.setCellValueFactory(new PropertyValueFactory<>("joinTime"));
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));

        eventHistoryTable.setItems(FXCollections.observableArrayList(userEvents));
    }

}
