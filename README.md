# Inventory Platform

Spring Boot + PostgreSQL Inventory Management System

This project is a small-business inventory management web application designed to demonstrate **systems integration** between a web application, relational database, and container infrastructure.

The system allows administrators and employees to manage inventory items, track stock levels, and generate reorder reports.

---

# Features

* Admin and Employee login (role-based access)
* Inventory item management
* Stock in / stock out tracking
* Automatic LOW stock alerts
* Surplus stock indicators
* Reorder report export (CSV)
* Optional email alerts

---

# System Architecture

The system integrates multiple components:

User Browser
↓
Spring Boot Web Application
↓
PostgreSQL Database
↓
Docker Container Environment

The Docker Compose configuration orchestrates the containers and enables communication between the application and the database.

---

# Requirements

To run this project you need:

* Docker Desktop
* Git

---

# Run the System (Recommended)

Clone the repository:

git clone https://github.com/FearReaperCell/inventory-platform-01.git

Navigate to the project directory:

cd inventory-platform-01

Start the system:

docker compose up --build

Once the containers start, open the application:

http://localhost:8080

---


# Demo Accounts

Admin
username: admin
password: admin123

Employee
username: employee
password: employee123

---

# Admin Features

Manage items
/admin/items

Download reorder report
/admin/reports/reorder.csv

---

# Technologies Used

* Java 17
* Spring Boot
* Spring Security
* PostgreSQL
* Docker
* Docker Compose
* Maven

---

# Purpose

This project demonstrates **systems integration** by combining an application layer, database subsystem, container infrastructure, and source code repository into a unified platform.
