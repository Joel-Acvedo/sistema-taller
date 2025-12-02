
# 🔧 Sistema de Gestión Inteligente para Taller Automotriz

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Status](https://img.shields.io/badge/Estado-En_Desarrollo-yellow?style=for-the-badge)

> Un sistema de cotización y gestión operativa diseñado específicamente para la lógica de negocio de un taller mecánico real. No es un simple punto de venta; es un administrador de servicios, márgenes y fidelización.

---

## 🚀 Características Principales

Este sistema resuelve problemas específicos que los POS genéricos no pueden manejar:

### 🧠 1. Matriz de Precios Inteligente
El precio de la mano de obra no se escribe manualmente. El sistema lo calcula cruzando variables:
* **Motor:** 4 cil / 6 cil / 8 cil / 4x4.
* **Combustible:** Gasolina / Diesel.
* **Estatus:** Cliente Regular vs. Cliente con Membresía.

### 💰 2. Calculadora de Márgenes en Refacciones
Protección automática de utilidades. El usuario ingresa el *Costo Proveedor* y el sistema calcula automáticamente el *Precio de Venta* asegurando el **15% de margen + IVA**.

### 🛡️ 3. Auditoría y Seguridad "Anti-Fraude"
Cada movimiento en la base de datos deja una huella digital.
* **Creación:** Sabemos quién registró el cliente o la venta.
* **Modificación:** Si un precio cambia mágicamente, el sistema registra *quién* lo hizo y *cuándo*.

### 🔔 4. "El Secretario" (CRM Proactivo)
El sistema aprende de los servicios realizados (ej. Afinación cada 6 meses) y programa recordatorios automáticos para avisar al dueño 30 días antes de la próxima visita sugerida.



---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java (OpenJDK) con Arquitectura Cliente-Servidor.
* **Interfaz Gráfica:** JavaFX + AtlantaFX (Diseño Moderno/Dark Mode).
* **Base de Datos:** PostgreSQL 16.
* **Infraestructura:** Docker (Contenedor `pos-db`).
* **Hardware Meta:** Mini PC (Windows), Impresora de Oficina, Cajón de dinero USB.

---

## 📖 Historias de Usuario (Casos de Uso Real)

El sistema está diseñado basándose en situaciones reales del taller:

| Escenario | Descripción | Resultado |
| :--- | :--- | :--- |
| **🏆 El Premio Automático** | Al realizar un **Servicio Premium** (ej. Afinación Mayor) a un cliente con membresía por vencer. | El sistema **renueva gratis** la membresía por 1 año automáticamente en el ticket. |
| **📈 Inflación de Piezas** | El mecánico cotiza una pieza a $800, pero el proveedor subió el precio a $850 al momento de comprar. | Al editar el costo, el sistema **recalcula el precio de venta** para mantener el margen y pregunta si desea actualizar el Inventario Maestro. |
| **📝 El Flotillero** | Una empresa trae 4 camionetas. Se generan 4 cotizaciones individuales pero se pagan juntas. | Se genera un **Reporte Fiscal Consolidado** para el contador con desglose de folios, uso de CFDI y totales. |
| **🕵️ El Dedo Chueco** | Un precio de venta aparece sospechosamente bajo ($500 en lugar de $5,000). | El módulo de **Auditoría** revela qué usuario editó el precio y a qué hora exacta ocurrió el error. |
| **📉 El Regateo** | Un cliente no alcanza a pagar la cotización completa de $4,500. | Se edita la cotización en estado **'Borrador'** eliminando partidas. Se regenera el PDF con el nuevo total sin desperdiciar folios. |

---

## 💾 Estructura de Base de Datos

El núcleo del sistema corre sobre **PostgreSQL**. A continuación se muestra la estructura simplificada de las tablas principales.

<details>
<summary><strong>Ver Esquema de Base de Datos (SQL)</strong></summary>

```sql
-- =================================================================================
-- SCRIPT MAESTRO V6.0: TALLER GESA (Con Triggers Automáticos de Fechas)
-- =================================================================================

-- ⚠️ LIMPIEZA TOTAL
DROP TABLE IF EXISTS detalles_cotizacion CASCADE;
DROP TABLE IF EXISTS cotizaciones CASCADE;
DROP TABLE IF EXISTS precios_mano_obra CASCADE;
DROP TABLE IF EXISTS refacciones CASCADE;
DROP TABLE IF EXISTS catalogo_servicios CASCADE;
DROP TABLE IF EXISTS autos CASCADE;
DROP TABLE IF EXISTS membresias CASCADE;
DROP TABLE IF EXISTS clientes CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- =================================================================================
-- 1. TABLA USUARIOS
-- =================================================================================
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =================================================================================
-- 2. TABLA CLIENTES
-- =================================================================================
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    rfc VARCHAR(13),
    razon_social_fiscal VARCHAR(255),
    regimen_fiscal VARCHAR(100),
    cp_fiscal VARCHAR(10),
    uso_cfdi VARCHAR(100),
    email_facturacion VARCHAR(100),
    creado_por_id INT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    eliminado BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT fk_audit_creador_cli FOREIGN KEY (creado_por_id) REFERENCES usuarios(id)
);

-- =================================================================================
-- 3. TABLA MEMBRESÍAS
-- =================================================================================
CREATE TABLE membresias (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL UNIQUE, 
    fecha_vencimiento DATE NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    creado_por_id INT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente_membresia FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- =================================================================================
-- 4. TABLA AUTOS (Con Último y Próximo Servicio)
-- =================================================================================
CREATE TABLE autos (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano INT NOT NULL,
    motor_categoria VARCHAR(20) NOT NULL,
    combustible VARCHAR(20) NOT NULL,
    placas VARCHAR(20),
    vin VARCHAR(50),
    
    -- 🔔 EL SECRETARIO AUTOMÁTICO MEJORADO
    fecha_ultimo_servicio DATE, -- Se llena solo con el Trigger
    fecha_proximo_servicio DATE, -- Se calcula solo con el Trigger
    
    creado_por_id INT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    eliminado BOOLEAN DEFAULT FALSE NOT NULL ,
    CONSTRAINT fk_cliente_auto FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- =================================================================================
-- 5. CATÁLOGO DE SERVICIOS
-- =================================================================================
CREATE TABLE catalogo_servicios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL, 
    descripcion TEXT,
    frecuencia_recomendada_meses INT DEFAULT 0, -- 0 significa que no es recurrente
    es_premium BOOLEAN DEFAULT FALSE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =================================================================================
-- 6. MATRIZ DE PRECIOS MANO DE OBRA
-- =================================================================================
CREATE TABLE precios_mano_obra (
    id SERIAL PRIMARY KEY,
    servicio_id INT NOT NULL,
    motor_categoria VARCHAR(20) NOT NULL,
    combustible VARCHAR(20) NOT NULL,
    precio_publico NUMERIC(10, 2) NOT NULL,
    precio_membresia NUMERIC(10, 2) NOT NULL,
    creado_por_id INT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_servicio_precio FOREIGN KEY (servicio_id) REFERENCES catalogo_servicios(id)
                               
);

-- =================================================================================
-- 7. TABLA REFACCIONES
-- =================================================================================
CREATE TABLE refacciones (
    id SERIAL PRIMARY KEY,
    codigo_interno VARCHAR(50) UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT, 
    compatibilidad TEXT,
    costo_proveedor NUMERIC(10, 2) NOT NULL,
    margen_ganancia_porcentaje INT DEFAULT 15,
    precio_venta_sugerido NUMERIC(10, 2) GENERATED ALWAYS AS (costo_proveedor * (1 + margen_ganancia_porcentaje/100.0)) STORED,
    stock_actual INT DEFAULT 0,
    stock_minimo INT DEFAULT 1,
    creado_por_id INT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    eliminado BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT fk_audit_creador_ref FOREIGN KEY (creado_por_id) REFERENCES usuarios(id)
);

-- =================================================================================
-- 8. COTIZACIONES / VENTAS
-- =================================================================================
CREATE TABLE cotizaciones (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    auto_id INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    url_pdf VARCHAR(255), 
    subtotal NUMERIC(10, 2),
    iva_total NUMERIC(10, 2),
    gran_total NUMERIC(10, 2),
    estado VARCHAR(20) DEFAULT 'Borrador', -- TRIGGER ACTIVA CUANDO ESTO SEA 'Pagada'
    requiere_factura BOOLEAN DEFAULT FALSE,
    estatus_facturacion VARCHAR(20) DEFAULT 'No Requerida',
    creado_por_id INT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente_cot FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_auto_cot FOREIGN KEY (auto_id) REFERENCES autos(id)
);

-- =================================================================================
-- 9. DETALLES DE COTIZACIÓN
-- =================================================================================
CREATE TABLE detalles_cotizacion (
    id SERIAL PRIMARY KEY,
    cotizacion_id INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    servicio_id INT, 
    refaccion_id INT,
    costo_proveedor NUMERIC(10, 2) DEFAULT 0,
    margen_ganancia_porcentaje INT DEFAULT 15,
    precio_unitario_sin_iva NUMERIC(10, 2) NOT NULL,
    cantidad INT DEFAULT 1,
    total_renglon NUMERIC(10, 2),
    CONSTRAINT fk_cotizacion_det FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE CASCADE,
    CONSTRAINT fk_servicio_det FOREIGN KEY (servicio_id) REFERENCES catalogo_servicios(id)
);
```
</details>

⚡ Instalación y Despliegue (Dev)

Prerrequisitos

    Java JDK 21+

    Docker Desktop / Engine

    Maven

1. Base de Datos (Docker)

Ejecutar el contenedor de PostgreSQL con las credenciales configuradas:

```
docker run -d \
  --name pos-db \
  -e POSTGRES_USER=puntoventa_user \
  -e POSTGRES_DB=puntoventa_db \
  -e POSTGRES_PASSWORD=Noviembre0511 \
  -p 5432:5432 \
  -v pos-db-data:/var/lib/postgresql/data \
  postgres:16  
```
Nota: Este proyecto es privado y propiedad intelectual de GESA Automotriz.
