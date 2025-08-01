# TailorSoft COCOLAND

This project is a minimal Java web application for managing tailoring orders. It demonstrates basic CRUD operations using servlets, simple DAO classes and JSP views. All JSP pages currently display labels in Vietnamese. The sample MySQL schema is provided in `db/cocoland_schema.sql`.

## Modules
- **Customers** – list, create and update customers
- **Materials** – list and create fabrics in inventory
- **Orders** – list orders and view order details
- **Measurements** – list and create measurement records
- **Product Types** – manage product categories and their measurement fields

## Building
Execute `ant compile` to build the project and `ant test` to run the default test target. Adjust the database connection in `src/java/dao/connect/DBConnect.java` for your environment.
