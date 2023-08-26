package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField firstNameField;

    @FXML
    private Label firstNameError;

    @FXML
    private TextField lastNameField;

    @FXML
    private Label lastNameError;

    @FXML
    private TextField usernameField;

    @FXML
    private Label usernameError;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordError;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label confirmPasswordError;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    public void initialize() {
        registerButton.setDisable(true);
        firstNameError.setText("");
        lastNameError.setText("");
        usernameError.setText("");
        passwordError.setText("");
        confirmPasswordError.setText("");

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }

    private void validateFields() {
        if (!firstNameField.getText().isEmpty() && !lastNameField.getText().isEmpty() && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && !confirmPasswordField.getText().isEmpty()) {
            registerButton.setDisable(false);
        } else {
            registerButton.setDisable(true);
        }
    }

    public void handleRegisterButtonClick() {
        validateFirstName();
        validateLastName();
        validateUsername();
        validatePassword();
        validateConfirmPassword();
        try {
            FXRouter.goTo("index");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleLoginButtonClick(ActionEvent event) throws Exception {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateFirstName() {
        if (firstNameField.getText().isEmpty()) {
            firstNameError.setText("First Name is required.");
        } else {
            firstNameError.setText("");
        }
    }

    private void validateLastName() {
        if (lastNameField.getText().isEmpty()) {
            lastNameError.setText("Last Name is required.");
        } else {
            lastNameError.setText("");
        }
    }

    private void validateUsername() {
        if (usernameField.getText().isEmpty()) {
            usernameError.setText("Username is required.");
        } else {
            usernameError.setText("");
        }
    }

    private void validatePassword() {
        if (passwordField.getText().isEmpty()) {
            passwordError.setText("Password is required.");
        } else {
            passwordError.setText("");
        }
    }

    private void validateConfirmPassword() {
        if (!confirmPasswordField.getText().equals(passwordField.getText())) {
            confirmPasswordError.setText("Passwords do not match.");
        } else {
            confirmPasswordError.setText("");
        }
    }
}