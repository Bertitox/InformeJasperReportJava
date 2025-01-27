package org.example.practicainforme;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class HelloController {
    String opcion;
    String comparador;

    @FXML
    private ComboBox ComboBox;

    @FXML
    private TextField field1;

    @FXML
    private ComboBox ComboBoxComparador;

    @FXML
    private Button generarPDFboton;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    //MÉTODO QUE CAMBIA EL VALOR DE LA VARIABLE opcion AL SELECCIONADO
    @FXML
    void cambiarCombo(ActionEvent event) {
        opcion = ComboBox.getSelectionModel().getSelectedItem().toString();
        System.out.println(opcion);
        if (opcion.equalsIgnoreCase("Salario")) {
            label2.setVisible(true);
            label1.setVisible(true);
            label1.setText("Salario");
            label2.setText("Comparador");
            field1.setVisible(true);
            field1.setText("");
            ComboBoxComparador.setVisible(true);

        } else if (opcion.equalsIgnoreCase("Departamento")) {
            field1.setText("");
            label2.setVisible(false);
            label1.setVisible(true);
            label1.setText("Nombre del departamento");
            field1.setVisible(true);
            ComboBoxComparador.setVisible(false);
        } else {
            label1.setVisible(false);
            label2.setVisible(false);
            field1.setVisible(false);
            ComboBoxComparador.setVisible(false);
        }
    }

    //MÉTODO QUE CAMBIA EL VALOR DE LA VARIABLE comparador AL SELECCIONADO
    @FXML
    void cambiarComboComparador(ActionEvent event) {
        comparador = ComboBoxComparador.getSelectionModel().getSelectedItem().toString();
        System.out.println(comparador);
    }

    //MÉTODO QUE INICIALIZA LOS COMBOBOX
    @FXML
    private void initialize() {
        ObservableList<String> estados = FXCollections.observableArrayList("Nada", "Salario", "Departamento");
        ComboBox.setItems(estados);

        ObservableList<String> estadosComp = FXCollections.observableArrayList("=", "<", ">");
        ComboBoxComparador.setItems(estadosComp);
        ComboBoxComparador.setVisible(false);
        ComboBox.setDisable(false);
        label1.setVisible(false);
        label2.setVisible(false);
        field1.setVisible(false);
    }

    //MÉTODO QUE GENERA EL INFORME DEPENDIENDO DE LOS VALORES DE opcion.
    @FXML
    void generarPDF(ActionEvent event) {
        System.out.println("Creando PDF...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/empleadosDB", "root", "210205");
            Map parametros = new HashMap();
            JasperPrint print;
            if (opcion.equalsIgnoreCase("nada")) {
                //SE INDICA EL FICHERO .JASPER EN EL PROYECTO (EN ESTE CASO EL GENERICO)
                print = JasperFillManager.fillReport("/Users/alber/IdeaProjects/PracticaInforme/src/main/java/org/example/practicainforme/Leaf_GreenGenerico.jasper", null, conexion);
            } else if (opcion.equalsIgnoreCase("salario")) {
                //SE BORRA EL MAPA PARA EVITAR ERRORES
                parametros.clear();
                //SE INTRODUCEN LOS DATOS EN EL MAPA DE VALORES
                parametros.put("ParametroSalario", field1.getText().toString());
                parametros.put("ComparadorSalario", comparador);
                //SE INDICA EL FICHERO .JASPER EN EL PROYECTO
                print = JasperFillManager.fillReport("/Users/alber/IdeaProjects/PracticaInforme/src/main/java/org/example/practicainforme/Leaf_GreenSalario.jasper", parametros, conexion);
            } else {
                //SE BORRA EL MAPA PARA EVITAR ERRORES
                parametros.clear();
                parametros.put("ParametroDepartamento", field1.getText().toString());
                //SE INDICA EL FICHERO .JASPER EN EL PROYECTO
                print = JasperFillManager.fillReport("/Users/alber/IdeaProjects/PracticaInforme/src/main/java/org/example/practicainforme/Leaf_GreenDepartamento.jasper", parametros, conexion);
            }
            //SE INDICA DONDE SE GUARDA EL PDF
            JasperExportManager.exportReportToPdfFile(print, "/Users/alber/Desktop/JasperInformes/informe.pdf");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }


}