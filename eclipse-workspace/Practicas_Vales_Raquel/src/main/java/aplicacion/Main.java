package aplicacion;

import util.InitDB;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando aplicación...");

        // Ejecutar script de inicialización de la base de datos
        InitDB.runScript();

        System.out.println("Base de datos inicializada correctamente.");
        System.out.println("Aplicación lista para ejecutar operaciones.");
    }
}
