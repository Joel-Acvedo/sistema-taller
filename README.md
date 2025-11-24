Markdown

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
-- TABLAS PRINCIPALES DEL SISTEMA TALLER GESA

-- 1. USUARIOS (Seguridad y Auditoría)
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    rol VARCHAR(20) NOT NULL -- 'ADMIN', 'VENDEDOR', 'MECANICO'
);

-- 2. AUTOS (El corazón del filtro de precios)
CREATE TABLE autos (
    id SERIAL PRIMARY KEY,
    motor_categoria VARCHAR(20) NOT NULL, -- '4 cil', '6 cil'...
    combustible VARCHAR(20) NOT NULL, -- 'Gasolina', 'Diesel'
    fecha_proximo_servicio DATE -- Recordatorio automático
);

-- 3. MATRIZ DE PRECIOS (Mano de Obra)
CREATE TABLE precios_mano_obra (
    id SERIAL PRIMARY KEY,
    servicio_id INT NOT NULL,
    precio_publico NUMERIC(10, 2) NOT NULL,
    precio_membresia NUMERIC(10, 2) NOT NULL
);

-- 4. REFACCIONES (Inventario Inteligente)
CREATE TABLE refacciones (
    id SERIAL PRIMARY KEY,
    costo_proveedor NUMERIC(10, 2) NOT NULL,
    margen_ganancia_porcentaje INT DEFAULT 15,
    -- El precio se calcula solo:
    precio_venta_sugerido NUMERIC(10, 2) GENERATED ALWAYS AS ... STORED
);

-- 5. COTIZACIONES (Flujo de Venta)
CREATE TABLE cotizaciones (
    id SERIAL PRIMARY KEY,
    estado VARCHAR(20) DEFAULT 'Borrador', -- Borrador -> Aprobada -> Pagada
    requiere_factura BOOLEAN DEFAULT FALSE, -- Semáforo para contador
    url_pdf VARCHAR(255) -- Gestión de archivos externa
);

</details>

⚡ Instalación y Despliegue (Dev)

Prerrequisitos

    Java JDK 21+

    Docker Desktop / Engine

    Maven

1. Base de Datos (Docker)

Ejecutar el contenedor de PostgreSQL con las credenciales configuradas:
Bash

docker run -d \
  --name pos-db \
  -e POSTGRES_USER=puntoventa_user \
  -e POSTGRES_DB=puntoventa_db \
  -e POSTGRES_PASSWORD=Noviembre0511 \
  -p 5432:5432 \
  -v pos-db-data:/var/lib/postgresql/data \
  postgres:16

2. Ejecución del Proyecto

Bash

git clone [https://github.com/TU_USUARIO/sistema-taller.git](https://github.com/TU_USUARIO/sistema-taller.git)
cd sistema-taller
mvn clean javafx:run

    Nota: Este proyecto es privado y propiedad intelectual de GESA Automotriz.


***

### ¿Cómo actualizarlo en GitHub?

1.  Copia el código de arriba.
2.  Ve a tu **Visual Studio Code**.
3.  Abre el archivo `README.md` (debe estar en la raíz, junto al `pom.xml`). Si no existe, créalo.
4.  Pega el contenido y guarda.
5.  Sube los cambios a la nube:
    ```bash
    git add README.md
    git commit -m "Docs: Actualizado README con lógica de negocio y diagrama"
    git push
    ```

¡Cuando entres a tu página de GitHub, se verá increíble! 😎
