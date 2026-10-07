/* CONTROLADOR
Esta clase conecta la vista con el modelo
Manejador de eventos como: botones, teclado, mouse, filtrado y exportación
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import modelo.persona;
import modelo.personaDAO;
import vista.ventana;

public class logica_ventana
        implements ActionListener, ListSelectionListener, ItemListener {

    private ventana delegado;

    private String nombres;
    private String email;
    private String telefono;
    private String categoria = "";

    private persona persona;

    private List<persona> contactos;
    private List<persona> contactosMostrados;

    private boolean favorito = false;

    // Evita guardar cambios mientras se están cargando datos
    private boolean cargandoContacto = false;

    // CONSTRUCTOR
    public logica_ventana(ventana delegado) {
        this.delegado = delegado;
        cargarContactosRegistrados();
        delegado.btn_add.addActionListener(this);
        delegado.btn_eliminar.addActionListener(this);
        delegado.btn_modificar.addActionListener(this);
        delegado.btn_exportar.addActionListener(this);
        delegado.lst_contactos.addListSelectionListener(this);
        delegado.cmb_categoria.addItemListener(this);
        delegado.chb_favorito.addItemListener(this);
        registrarEventosTeclado();
        registrarEventosMouse();
    }

    // CAMPOS    
    private void inicializacionCampos() {
        nombres = delegado.txt_nombres.getText();
        email = delegado.txt_email.getText();
        telefono = delegado.txt_telefono.getText();
    }

    // CARGAR CONTACTOS    
    private void cargarContactosRegistrados() {
        try {
            delegado.barra_progreso.setValue(20);
            contactos = new personaDAO(new persona()).leerArchivo();
            delegado.barra_progreso.setValue(70);
            actualizarLista(contactos);
            actualizarEstadisticas();
            delegado.barra_progreso.setValue(100);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
            contactos = new ArrayList<persona>();
            contactosMostrados = new ArrayList<persona>();
            delegado.barra_progreso.setValue(0);
        }
    }

    // ACTUALIZAR LISTA    
    private void actualizarLista(List<persona> listaContactos) {
        contactosMostrados = listaContactos;
        DefaultListModel modeloLista = new DefaultListModel();
        DefaultTableModel modeloTabla = (DefaultTableModel) delegado.tabla_contactos.getModel();
        modeloTabla.setRowCount(0);
        for (persona contacto : listaContactos) {
            modeloLista.addElement(contacto.formatoLista());
            modeloTabla.addRow(new Object[]{contacto.getNombre(), contacto.getTelefono(), contacto.getEmail(), contacto.getCategoria(), contacto.isFavorito() ? "Sí" : "No"});
        }
        delegado.lst_contactos.setModel(modeloLista);
    }

    // ESTADÍSTICAS    
    private void actualizarEstadisticas() {
        if (contactos == null) {
            return;
        }
        int totalContactos = contactos.size();
        int totalFavoritos = 0;
        for (persona contacto : contactos) {
            if (contacto.isFavorito()) {
                totalFavoritos++;
            }
        }
        delegado.lbl_total_contactos.setText("Total de contactos: " + totalContactos);
        delegado.lbl_contactos_favoritos.setText("Contactos favoritos: " + totalFavoritos);
    }

    // LIMPIAR CAMPOS
    private void limpiarCampos() {
        delegado.txt_nombres.setText("");
        delegado.txt_telefono.setText("");
        delegado.txt_email.setText("");
        delegado.txt_buscar.setText("");
        categoria = "";
        favorito = false;
        cargandoContacto = true;
        delegado.chb_favorito.setSelected(false);
        delegado.cmb_categoria.setSelectedIndex(0);
        cargandoContacto = false;
        inicializacionCampos();
        cargarContactosRegistrados();
    }

    // TECLADO    
    private void registrarEventosTeclado() {
        delegado.txt_buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarContactos();
            }
        }
        );
        delegado.txt_email.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    delegado.btn_add.doClick();
                }
            }
        }
        );

        delegado.txt_nombres.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    limpiarCampos();
                }
            }
        }
        );
    }

    // MOUSE    
    private void registrarEventosMouse() {
        JPopupMenu menuContextual = new JPopupMenu();
        JMenuItem itemCargar = new JMenuItem("Cargar contacto");
        JMenuItem itemLimpiar = new JMenuItem("Limpiar campos");
        menuContextual.add(itemCargar);
        menuContextual.add(itemLimpiar);
        itemCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = delegado.lst_contactos.getSelectedIndex();
                if (index != -1) {
                    cargarContacto(index);
                } else {
                    JOptionPane.showMessageDialog(delegado, "Seleccione un contacto de la lista");
                }
            }
        }
        );
        itemLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        }
        );

        delegado.lst_contactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenuContextual(e, menuContextual);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarMenuContextual(e, menuContextual);
            }
        }
        );
    }

    // MENÚ CONTEXTUAL
    private void mostrarMenuContextual(MouseEvent e, JPopupMenu menuContextual) {
        if (e.isPopupTrigger()) {
            int index = delegado.lst_contactos.locationToIndex(e.getPoint());
            if (index != -1) {
                delegado.lst_contactos.setSelectedIndex(index);
                menuContextual.show(delegado.lst_contactos, e.getX(), e.getY());
            }
        }
    }

    // BUSCAR    
    private void filtrarContactos() {
        String textoBusqueda = delegado.txt_buscar.getText().toLowerCase();
        List<persona> filtrados = new ArrayList<persona>();
        for (persona contacto : contactos) {
            if (contacto.getNombre().toLowerCase().contains(textoBusqueda)) {
                filtrados.add(contacto);
            }
        }
        actualizarLista(filtrados);
    }

    // EXPORTAR    
    private void exportarContactosCSV() {
        try {
            delegado.barra_progreso.setValue(25);
            FileWriter archivo = new FileWriter("c:/gestionContactos/" + "contactos_exportados.csv");
            archivo.write("NOMBRE;TELEFONO;EMAIL;" + "CATEGORIA;FAVORITO\n");
            delegado.barra_progreso.setValue(50);
            for (persona contacto : contactosMostrados) {
                archivo.write(contacto.datosContacto() + "\n");
            }

            archivo.close();
            delegado.barra_progreso.setValue(100);
            JOptionPane.showMessageDialog(delegado, "Contactos exportados correctamente en " + "c:/gestionContactos/" + "contactos_exportados.csv");
        } catch (IOException e) {
            delegado.barra_progreso.setValue(0);
            JOptionPane.showMessageDialog(delegado, "No se pudo exportar el archivo CSV");
        }
    }

    // BOTONES
    @Override
    public void actionPerformed(ActionEvent e) {
        inicializacionCampos();

        // AGREGAR
        if (e.getSource()== delegado.btn_add) {
            if (!nombres.equals("")&& !telefono.equals("")&& !email.equals("")) {
                if (!categoria.equals("Elija una Categoria") && !categoria.equals("")) {
                    persona = new persona(nombres,telefono,email,categoria,favorito);
                    new personaDAO(persona).escribirArchivo();
                    limpiarCampos();
                    JOptionPane.showMessageDialog(delegado,"Contacto Registrado!!!");
                } else {
                    JOptionPane.showMessageDialog(delegado,"Elija una Categoria!!!");
                }
            } else {
                JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
            }
        } 
        
        // EXPORTAR        
        else if (e.getSource()== delegado.btn_exportar) {
            exportarContactosCSV();
        } 
        
        // ELIMINAR        
        else if (e.getSource() == delegado.btn_eliminar) {
            int index = delegado.lst_contactos.getSelectedIndex();
            if (index != -1) {
                persona contactoEliminar = contactosMostrados.get(index);
                int respuesta = JOptionPane.showConfirmDialog(delegado,"¿Está seguro de eliminar " + "el contacto \""+ contactoEliminar.getNombre() + "\"?","Confirmar eliminación",JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        contactos.remove(contactoEliminar);
                        new personaDAO(new persona()).actualizarContactos(contactos);
                        actualizarLista(contactos);
                        actualizarEstadisticas();
                        cargandoContacto = true;
                        delegado.txt_nombres.setText("");
                        delegado.txt_telefono.setText("");
                        delegado.txt_email.setText("");
                        delegado.chb_favorito.setSelected(false);
                        delegado.cmb_categoria.setSelectedIndex(0);
                        cargandoContacto = false;
                        JOptionPane.showMessageDialog(delegado,"Contacto eliminado correctamente.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(delegado,"Error al eliminar el contacto:\n"+ ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(delegado, "Seleccione un contacto para eliminar.");
            }
        } 
        
        // MODIFICAR        
        else if (e.getSource() == delegado.btn_modificar) {
            JOptionPane.showMessageDialog(delegado,"La función modificar "+ "todavía no está implementada.");
        }
    }
    
    // SELECCIONAR CONTACTO    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int index = delegado.lst_contactos.getSelectedIndex();
        if (index != -1 && !e.getValueIsAdjusting()) {
            cargarContacto(index);
        }
    }
    
    // CARGAR CONTACTO    
    private void cargarContacto(int index) {
        if (index >= 0 && index < contactosMostrados.size()) {
            persona contacto = contactosMostrados.get(index);
            cargandoContacto = true;
            delegado.txt_nombres.setText(contacto.getNombre());
            delegado.txt_telefono.setText(contacto.getTelefono());
            delegado.txt_email.setText(contacto.getEmail());
            delegado.chb_favorito.setSelected(contacto.isFavorito());
            delegado.cmb_categoria.setSelectedItem(contacto.getCategoria());
            categoria = contacto.getCategoria();
            favorito = contacto.isFavorito();
            cargandoContacto = false;
        }
    }
    
    // FAVORITO Y CATEGORÍA    
    @Override
    public void itemStateChanged(ItemEvent e) {
        
        // CATEGORÍA        
        if (e.getSource() == delegado.cmb_categoria) {
            if (delegado.cmb_categoria.getSelectedItem() != null) {
                categoria = delegado.cmb_categoria.getSelectedItem().toString();
            }
        } 
        
        // FAVORITO        
        else if (e.getSource() == delegado.chb_favorito) {
            favorito = delegado.chb_favorito.isSelected();
            // Si estamos cargando un contacto,
            // no guardamos todavía.
            if (cargandoContacto) {
                return;
            }

            int index = delegado.lst_contactos.getSelectedIndex();
            // Si hay un contacto seleccionado,
            // actualizamos su favorito.
            if (index != -1&& index < contactosMostrados.size()) {
                persona contacto = contactosMostrados.get(index);
                contacto.setFavorito(favorito);
                try {
                    new personaDAO(new persona()).actualizarContactos(contactos);
                    actualizarLista(contactos);
                    actualizarEstadisticas();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(delegado,"No se pudo guardar " + "el cambio de favorito.");
                }
            }
        }
    }
}
