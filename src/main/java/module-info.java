module com.gesa {
    requires javafx.controls;
    requires javafx.fxml;

    // --- AGREGA ESTAS LÍNEAS AQUÍ ---
    requires java.sql;             // <--- ESTA arregla tus errores rojos actuales
    requires org.postgresql.jdbc;  // Para que el driver de la BD funcione
    requires atlantafx.base;       // Para el diseño bonito (lo usaremos pronto)
    // --------------------------------

    opens com.gesa to javafx.fxml;
    exports com.gesa;
}