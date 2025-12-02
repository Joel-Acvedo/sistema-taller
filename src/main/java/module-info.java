module com.gesa {
    // ==========================================
    // 1. MIS HERRAMIENTAS (REQUIRES)
    // ==========================================
    requires javafx.controls;       // Ventanas y botones
    requires javafx.graphics;       // Gráficos
    requires javafx.fxml;           // Leer archivos .fxml
    requires java.sql;              // Conexión a BD general
    requires org.postgresql.jdbc;// Driver de Postgres
    requires atlantafx.base;     // (Descomenta solo si ya instalaste AtlantaFX)

    // ==========================================
    // 2. PERMISOS DE ACCESO (OPENS)
    // ==========================================

    // ABRIR LA CARPETA RAÍZ
    // Permite que JavaFX arranque tu App.java
    opens com.gesa to javafx.fxml;


    // ABRIR LOS MODELOS (¡Vital para tablas!)
    // Permite que la Tabla de JavaFX lea "getNombre()", "getId()", etc.
    // Si borras esta línea, las tablas saldrán vacías o darán error.
   // opens com.gesa.models to javafx.base;

    // ABRIR LOS CONTROLADORES (¡Vital para botones!)
    // Permite que los @FXML funcionen.
    opens com.gesa.controllers to javafx.fxml;
    opens com.gesa.models to javafx.base;

    // ==========================================
    // 3. EXPORTAR LO PÚBLICO
    // ==========================================
    exports com.gesa;
}