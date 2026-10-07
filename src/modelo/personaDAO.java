/* MODELO
Esta clase representa los datos de los contactos y el acceso al archivo CSV
Gestionador de los atributos: lectura y escritura de informaicón
 */
package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class personaDAO {

    private File archivo;
    private persona persona;

    // Constructor
    public personaDAO(persona persona) {
        this.persona = persona;
        archivo = new File("c:/gestionContactos");
        prepararArchivo();
    }

    // Preparar carpeta y archivo
    private void prepararArchivo() {
        // Crear carpeta si no existe
        if (!archivo.exists()) {
            archivo.mkdir();
        }

        // Crear archivo CSV
        archivo = new File(archivo.getAbsolutePath(),"datosContactos.csv");
        // Crear archivo si no existe
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                String encabezado = "NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO";
                escribir(encabezado);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Escribir una línea en el archivo
    private void escribir(String texto) {
        try {
            FileWriter escribir = new FileWriter(archivo.getAbsolutePath(),true);
            escribir.write(texto + "\n");
            escribir.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Agregar contacto
    public boolean escribirArchivo() {
        escribir(persona.datosContacto());
        return true;
    }

    // Leer todos los contactos
    public List<persona> leerArchivo()throws IOException {
        List<persona> personas = new ArrayList<persona>();
        BufferedReader leer = new BufferedReader(new FileReader(archivo));
        String linea;while ((linea = leer.readLine()) != null) {
            
            // Ignorar líneas vacías
            if (linea.trim().isEmpty()) {
                continue;
            }

            // Ignorar encabezado
            if (linea.startsWith("NOMBRE;")) {
                continue;
            }
            String[] datos = linea.split(";");
            
            // Verificar que tenga los 5 datos
            if (datos.length < 5) {
                continue;
            }

            persona p = new persona();
            p.setNombre(datos[0]);
            p.setTelefono(datos[1]);
            p.setEmail(datos[2]);
            p.setCategoria(datos[3]);
            p.setFavorito(Boolean.parseBoolean(datos[4]));
            personas.add(p);
        }
        leer.close();
        return personas;
    }

    // Actualizar todos los contactos
    // Se utiliza para eliminar y modificar
    // favoritos
    public void actualizarContactos(List<persona> personas)throws IOException {
        FileWriter escribir = new FileWriter(archivo.getAbsolutePath(),false);

        // Encabezado
        escribir.write("NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO\n");

        // Escribir todos los contactos actuales
        for (persona p : personas) {
            escribir.write(p.datosContacto() + "\n");
        }

        escribir.close();
    }
}
