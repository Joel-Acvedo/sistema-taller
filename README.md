Prompt para la ia de lo que llevo:
Actúa como el Arquitecto de Software Senior y Desarrollador Líder del proyecto "Sistema de Gestión de Servicios y Cotizaciones para Taller Automotriz".

A continuación te doy el CONTEXTO COMPLETO y el estado actual del proyecto para que puedas ayudarme a continuar el desarrollo, realizar cambios o escribir el código Java sin perder la lógica de negocio ya definida.

--- 1. TECNOLOGÍAS Y ENTORNO ---
* Lenguaje: Java (Visual Studio Code).
* Base de Datos: PostgreSQL 16 corriendo en Docker (Contenedor: pos-db).
* Gestión BD: DBeaver.
* Hardware Meta: Mini PC (Windows), Impresora de Oficina (Láser/Inyección), Cajón de dinero USB.
* Arquitectura: Aplicación de Escritorio escalable (Cliente-Servidor).

--- 2. LÓGICA DE NEGOCIO (CORE) ---
El sistema no es un punto de venta tradicional, es un cotizador de servicios mecánicos:
1. Matriz de Precios Automática (Mano de Obra): El precio NO se escribe manual. Se determina cruzando: [Categoría Motor del Auto (4cil/6cil)] + [Combustible] + [Si el cliente tiene Membresía].
2. Calculadora de Refacciones: El usuario ingresa el "Costo Proveedor" y el sistema calcula automáticamente: (Costo + 15% Margen) + IVA.
3. Flujo de Cotización:
   - Estado 'Borrador': Editable (Carga, limpia detalles, reescribe). No afecta stock.
   - Estado 'Aprobada': No editable. Reserva stock.
   - Estado 'Pagada/Facturada': Venta finalizada. Baja stock. Genera reporte para contador.
4. Archivos: Los PDFs se generan externamente y solo se guarda la ruta en la BD.
   - Naming Convention: [TIPO]_[FOLIO]_[CLIENTE]_[AUTO]_[PLACAS].pdf

--- 3. ESTRUCTURA DE BASE DE DATOS (FINAL) ---
El script SQL de PostgreSQL ya definido y aprobado es el siguiente:

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    rfc VARCHAR(13),
    razon_social_fiscal VARCHAR(255),
    regimen_fiscal VARCHAR(100),
    cp_fiscal VARCHAR(10),
    uso_cfdi VARCHAR(100),
    email_facturacion VARCHAR(100),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE membresias (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL UNIQUE, 
    fecha_vencimiento DATE NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente_membresia FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE autos (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano INT NOT NULL,
    motor_categoria VARCHAR(20) NOT NULL, -- '4 cil', '6 cil', '8 cil', '4x4'
    combustible VARCHAR(20) NOT NULL, -- 'Gasolina', 'Diesel'
    placas VARCHAR(20),
    vin VARCHAR(50),
    CONSTRAINT fk_cliente_auto FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE catalogo_servicios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    frecuencia_recomendada_meses INT
);

CREATE TABLE precios_mano_obra (
    id SERIAL PRIMARY KEY,
    servicio_id INT NOT NULL,
    motor_categoria VARCHAR(20) NOT NULL,
    combustible VARCHAR(20) NOT NULL,
    precio_publico NUMERIC(10, 2) NOT NULL,
    precio_membresia NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_servicio_precio FOREIGN KEY (servicio_id) REFERENCES catalogo_servicios(id)
);

CREATE TABLE cotizaciones (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    auto_id INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    url_pdf VARCHAR(255), 
    subtotal NUMERIC(10, 2),
    iva_total NUMERIC(10, 2),
    gran_total NUMERIC(10, 2),
    estado VARCHAR(20) DEFAULT 'Borrador', 
    requiere_factura BOOLEAN DEFAULT FALSE,
    estatus_facturacion VARCHAR(20) DEFAULT 'No Requerida',
    CONSTRAINT fk_cliente_cotizacion FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_auto_cotizacion FOREIGN KEY (auto_id) REFERENCES autos(id)
);

CREATE TABLE detalles_cotizacion (
    id SERIAL PRIMARY KEY,
    cotizacion_id INT NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- 'ManoObra' o 'Refaccion'
    descripcion VARCHAR(255) NOT NULL,
    servicio_id INT, 
    costo_proveedor NUMERIC(10, 2) DEFAULT 0,
    margen_ganancia_porcentaje INT DEFAULT 15,
    precio_unitario_sin_iva NUMERIC(10, 2) NOT NULL,
    cantidad INT DEFAULT 1,
    total_renglon NUMERIC(10, 2),
    CONSTRAINT fk_cotizacion_detalle FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE CASCADE,
    CONSTRAINT fk_servicio_detalle FOREIGN KEY (servicio_id) REFERENCES catalogo_servicios(id)
);

--- 4. INSTRUCCIÓN ACTUAL ---
Ya tengo la base de datos montada en Docker y las tablas creadas. Ahora necesito ayuda para [AQUÍ ESCRIBIRÁS TU SIGUIENTE PASO, EJEMPLO: "Empezar a conectar Java con esta base de datos" o "Crear la pantalla de Login"].

Historia de usuario
🎬 Escena 1: La Llegada y el Diagnóstico
El Cliente: Juan Pérez llega con su camioneta Nissan NP300 (4 cilindros, Gasolina). Se escucha un ruido feo al frenar. El Mecánico (Tú/Tu empleado): Lo recibe, revisa la camioneta y diagnostica: "Son las balatas delanteras y, de paso, le hace falta la afinación".
Juan dice: "Oye, ¿y en cuánto me sale el chistecito? Necesito factura"
🎬 Escena 2: El Sistema Entra en Acción (Java + BD)
El mecánico camina a la computadora y abre tu programa.
1. Búsqueda Rápida: Teclea "Juan Pérez". El sistema le muestra:
·	Juan Pérez - Nissan NP300 - Placas AC-99-XX.
·	El sistema ve en la base de datos que NO tiene Membresía activa.
2. Creando la Cotización (Aquí ocurre la magia de tu Matriz): El mecánico da clic en "Nueva Cotización" y selecciona:
·	Servicio 1: "Cambio de Balatas".
o	El Sistema piensa: "El auto es 4 cilindros y Gasolina. No tiene membresía. Busco en la tabla precios_mano_obra... ¡El precio es $350!".
o	En pantalla: Aparece automáticamente $350.00 en mano de obra. El mecánico no tuvo que recordar el precio.
·	Servicio 2: "Afinación Mayor".
o	El Sistema piensa: "4 cil, Gasolina. Precio regular... $1,200".
o	En pantalla: Se suma la afinación.
3. Agregando las Refacciones (La Calculadora): El mecánico llama a la refaccionaria: "¿Cuánto el kit de balatas para la NP300? ¿$600? Ok".
·	En el sistema, el mecánico selecciona "Agregar Refacción".
·	Escribe: "Kit Balatas Cerámicas".
·	Costo Proveedor: Le pone $600.
·	El Sistema calcula: $600 + 15% ganancia = $690.
·	En pantalla: El cliente ve $690 (más IVA). ¡Tu ganancia ya está asegurada!
🎬 Escena 3: El Cierre y el PDF
El mecánico le dice a Juan: "A ver, permíteme un segundo, te imprimo el presupuesto formal".
Da clic en "Generar PDF".
·	El sistema guarda el archivo Cotizacion_JuanPerez_001.pdf.
·	Se imprime una hoja bonita con tu logo.
·	Juan lee:
o	Mano de Obra: $1,550.00
o	Refacciones: $690.00
o	IVA: $358.40
o	TOTAL: $2,598.40
Juan ve el documento profesional, ve que desglosas todo claro y dice: "Va, me parece justo. Háganlo".
🎬 Escena 4: Ejecución y Facturación
El mecánico cambia el estado en el sistema de "Borrador" a "Aprobada".
Pasan las horas, el carro queda listo. Juan regresa a pagar.
1.	El Semáforo Fiscal: Juan recuerda: "Ah, te dije que quería factura, ¿verdad?".
2.	En el Sistema: El mecánico marca la casilla ☑ Requiere Factura.
3.	Datos: El sistema le recuerda: "Oye, ya tenemos sus datos fiscales guardados (Transportes del Norte SA de CV...)". El mecánico confirma que sigan vigentes.
4.	Cobro: Juan paga. El mecánico cambia el estado a "Pagada".
5.	El Ticket: Se imprime la "Nota de Venta" final.
🎬 Escena 5: El Final del Día (El Contador)
Llega el fin de semana. Tú (el dueño) le das clic al botón "Reporte para Contador".
El sistema busca en la base de datos todas las ventas que tienen la casilla ☑ Requiere Factura y genera un archivo limpio. Se lo mandas a tu contador por WhatsApp y él, feliz de la vida, hace las facturas fiscales en 10 minutos porque tu sistema le dio todos los datos (RFC, Montos, Uso de CFDI) peladitos y en la boca.
Resultado:
·	Juan se fue feliz por la transparencia.
·	El mecánico no tuvo que calcular precios ni porcentajes con calculadora.
·	Tú aseguraste tu margen del 15% en piezas y tienes todo registrado.
·	El contador no te odia.
 

