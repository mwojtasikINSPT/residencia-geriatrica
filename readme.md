# 🏥 Sistema de Gestión de Residencia Geriátrica

Aplicación de consola desarrollada en **Java**, utilizando arquitectura **MVC**, Programación Orientada a Objetos (POO) y principios **SOLID**.

Permite administrar una residencia geriátrica mediante operaciones CRUD, gestión de habitaciones, reservas y asignaciones de enfermeros.

## 🚀 Funcionalidades

- **Residentes:** alta, listado, búsqueda, modificación y baja lógica.
- **Enfermeros:** gestión de datos personales y bajas lógicas.
- **Habitaciones:** administración y control de disponibilidad.
- **Reservas:** asignación y cancelación de habitaciones para residentes.
- **Asignaciones:** vinculación de enfermeros con habitaciones.
- **Consultas especiales:**
  - Habitación asignada a un residente.
  - Residentes atendidos por un enfermero.
  - Enfermero responsable de un residente.

## 🛠️ Tecnologías

- Java
- Apache Maven
- NetBeans
- Persistencia mediante archivos `.txt`

## 🏗️ Arquitectura

El proyecto utiliza **MVC con una capa Service** para separar responsabilidades.

```text
src/main/java/residenciageriatrica/
├── models/
├── dtos/
├── daos/
├── services/
├── controllers/
├── views/
├── utils/
├── exceptions/
└── Main.java
```

Se aplican conceptos de POO como **herencia, abstracción, encapsulamiento y polimorfismo**, junto con validaciones y excepciones personalizadas.

## 💾 Persistencia

Los datos se almacenan en archivos de texto:

- `residentes.txt`
- `enfermeros.txt`
- `habitaciones.txt`
- `reservas.txt`
- `asignaciones.txt`

Se utilizan identificadores únicos y **bajas lógicas** para conservar el historial de registros.

## ▶️ Ejecución

1. Clonar el repositorio.
2. Abrir el proyecto Maven en NetBeans.
3. Ejecutar `Main.java`.
4. Utilizar el menú interactivo para gestionar la residencia.

Los archivos `.txt` se crean automáticamente si no existen, en el directorio de ejecución.

---

**Proyecto académico — Programación II | Java, POO y MVC**