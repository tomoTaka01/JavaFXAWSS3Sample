/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxawss3sample;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * AWS S3 bucket simple sample.
 * 
 * @author tomo
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private TabPane tabPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AmazonS3 s3 = new AmazonS3Client();
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon S3");
        System.out.println("===========================================");
        s3.listBuckets().forEach(bucket -> {
            // creating tab for each bucket
            Tab tab = new Tab();
            String bucketNm = bucket.getName();
            tab.setText(bucketNm);
            tabPane.getTabs().add(tab);
            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(10));
            vbox.setStyle("-fx-background-color: #ffc9ae");
            // Owner
            HBox hbox1 = new HBox(10);
            Label ownerLabel = new Label("Owner:");
            String owner = bucket.getOwner().getDisplayName();
            Text ownerText = new Text(owner);
            hbox1.getChildren().addAll(ownerLabel, ownerText);
            // Create Date
            HBox hbox2 = new HBox(10);
            Label dateLabel = new Label("Create Date:");
            String createDate = bucket.getCreationDate().toString();
            Text dateText = new Text(createDate);
            hbox2.getChildren().addAll(dateLabel, dateText);
            tab.setContent(vbox);
            tab.setStyle("-fx-background-color: #ffc9ae;");
            // ListView for file path
            ObservableList<String> items = FXCollections.observableArrayList("file path");
            ListView view = new ListView(items);
            ObjectListing objList = s3.listObjects(new ListObjectsRequest()
                    .withBucketName(bucketNm)
                    .withPrefix(""))
                    ;
            objList.getObjectSummaries().forEach(obj -> {
                String filePath = obj.getKey();
                items.add(filePath);
            });
            vbox.getChildren().addAll(hbox1, hbox2, view);
        });
    }    
    
}
