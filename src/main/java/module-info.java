module com.gesa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires atlantafx.base;

    // --- PERMISOS DE ACCESO (OPENS) ---
    
    // 1. Permiso para la raíz (App.java)
    opens com.gesa to javafx.fxml;
    
    // 2. Permiso para los Controladores (Para que funcionen los botones)
    opens com.gesa.controllers to javafx.fxml;
    
    // 3. Permiso para los Modelos (Para que la Tabla pueda leer los datos)
    opens com.gesa.models to javafx.base;

    exports com.gesa;
}