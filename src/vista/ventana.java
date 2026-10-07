/* VISTA 
Esta clase contiene únicamente los componentes gráficos de la aplicación 
Aqui se definieron: notones, campos de texto, tablas, pestañas y barras de progreso
 */
package vista;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;

import controlador.logica_ventana;

public class ventana extends JFrame {

    public JPanel contentPane;

    public JTextField txt_nombres;
    public JTextField txt_telefono;
    public JTextField txt_email;
    public JTextField txt_buscar;

    public JCheckBox chb_favorito;
    public JComboBox cmb_categoria;

    public JButton btn_add;
    public JButton btn_modificar;
    public JButton btn_eliminar;
    public JButton btn_exportar;

    public JList lst_contactos;
    public JScrollPane scrLista;

    public JTable tabla_contactos;
    public JScrollPane scrTabla;

    public JProgressBar barra_progreso;

    //ESTADÍSTICAS
    public JLabel lbl_total_contactos;
    public JLabel lbl_contactos_favoritos;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ventana frame = new ventana();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ventana() {

        setTitle("GESTION DE CONTACTOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 1026, 748);

        contentPane = new JPanel();
        contentPane.setBorder(
                new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        //PESTAÑAS
        JTabbedPane tabbedPane
                = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(5, 5, 1000, 700);
        contentPane.add(tabbedPane);

        //PANEL DE CONTACTOS
        JPanel panelContactos
                = new JPanel();
        panelContactos.setLayout(null);
        tabbedPane.addTab("Contactos",null,panelContactos,null);

        //PANEL DE ESTADÍSTICAS
        JPanel panelEstadisticas = new JPanel();
        panelEstadisticas.setLayout(null);
        tabbedPane.addTab("Estadísticas",null,panelEstadisticas,null);

        // Título
        JLabel lbl_titulo_estadisticas
                = new JLabel("ESTADÍSTICAS DE CONTACTOS");
        lbl_titulo_estadisticas.setFont(new Font("Tahoma",Font.BOLD,20));
        lbl_titulo_estadisticas.setBounds(30, 40, 350, 30);
        panelEstadisticas.add(lbl_titulo_estadisticas);

        // TOTAL DE CONTACTOS
        lbl_total_contactos = new JLabel("Total de contactos: 0");
        lbl_total_contactos.setFont(new Font("Tahoma",Font.PLAIN,16));
        lbl_total_contactos.setBounds(30, 100, 300, 30);
        panelEstadisticas.add(lbl_total_contactos);

        // CONTACTOS FAVORITOS
        lbl_contactos_favoritos = new JLabel( "Contactos favoritos: 0");
        lbl_contactos_favoritos.setFont(new Font("Tahoma",Font.PLAIN,16));
        lbl_contactos_favoritos.setBounds(30, 140, 300, 30);
        panelEstadisticas.add(lbl_contactos_favoritos);

        // ETIQUETAS
        JLabel lbl_etiqueta1= new JLabel("NOMBRES:");
        lbl_etiqueta1.setBounds(25, 41, 89, 13);
        lbl_etiqueta1.setFont(new Font("Tahoma",Font.BOLD,15));
        panelContactos.add(lbl_etiqueta1);
        JLabel lbl_etiqueta2= new JLabel("TELEFONO:");
        lbl_etiqueta2.setBounds(25, 80, 89, 13);
        lbl_etiqueta2.setFont(new Font("Tahoma",Font.BOLD,15));
        panelContactos.add(lbl_etiqueta2);
        JLabel lbl_etiqueta3= new JLabel("EMAIL:");
        lbl_etiqueta3.setBounds(25, 122, 89, 13);
        lbl_etiqueta3.setFont(new Font("Tahoma",Font.BOLD,15));
        panelContactos.add(lbl_etiqueta3);
        JLabel lbl_etiqueta4= new JLabel("BUSCAR POR NOMBRE:");
        lbl_etiqueta4.setFont(new Font( "Tahoma",Font.BOLD,15));
        lbl_etiqueta4.setBounds(25, 661, 192, 13);
		panelContactos.add(lbl_etiqueta4);

        // CAMPO NOMBRES
        txt_nombres = new JTextField();
        txt_nombres.setBounds(124, 28, 427, 31);
        txt_nombres.setFont(new Font("Tahoma",Font.PLAIN,15));
        txt_nombres.setColumns(10);
        panelContactos.add(txt_nombres);

        // CAMPO TELÉFONO
        txt_telefono= new JTextField();
        txt_telefono.setBounds(124, 69, 427, 31);
        txt_telefono.setFont(new Font("Tahoma",Font.PLAIN,15));
        txt_telefono.setColumns(10);
        panelContactos.add(txt_telefono);

        
        // CAMPO EMAIL
       txt_email = new JTextField();
        txt_email.setBounds(124, 110, 427, 31);
        txt_email.setFont(new Font("Tahoma",Font.PLAIN,15));
        txt_email.setColumns(10);
        panelContactos.add(txt_email);

        // BUSCAR
        txt_buscar = new JTextField();
        txt_buscar.setFont(new Font("Tahoma",Font.PLAIN,15));
        txt_buscar.setColumns(10);
        txt_buscar.setBounds(212, 650, 784, 31);
        panelContactos.add(txt_buscar);

       //FAVORITO
        chb_favorito = new JCheckBox("CONTACTO FAVORITO");
        chb_favorito.setBounds(24, 170, 193, 21);
        chb_favorito.setFont(new Font("Tahoma",Font.PLAIN,15));
        panelContactos.add(chb_favorito);

        // CATEGORÍA
        cmb_categoria = new JComboBox();
        cmb_categoria.setBounds(300, 167, 251, 31);
        panelContactos.add(cmb_categoria);
        String[] categorias = {
            "Elija una Categoria","Familia","Amigos","Trabajo"};

        for (String categoria: categorias) {
            cmb_categoria.addItem(categoria);
        }

        // BOTÓN AGREGAR
        btn_add = new JButton("AGREGAR");btn_add.setFont(new Font("Tahoma",Font.PLAIN,15));
        btn_add.setBounds(601, 70, 125, 65);
        panelContactos.add(btn_add);

        // BOTÓN MODIFICAR
        btn_modificar = new JButton("MODIFICAR");
        btn_modificar.setFont(new Font("Tahoma",Font.PLAIN,15));
        btn_modificar.setBounds(736, 70, 125, 65);
        panelContactos.add(btn_modificar);

        // BOTÓN ELIMINAR
        btn_eliminar = new JButton("ELIMINAR");
        btn_eliminar.setFont(new Font("Tahoma",Font.PLAIN,15));
        btn_eliminar.setBounds(871, 69, 125, 65);
        panelContactos.add(btn_eliminar);
        
        // TABLA        
        tabla_contactos = new JTable();
        tabla_contactos.setFont(new Font("Tahoma",Font.PLAIN,14));
        tabla_contactos.setModel(new DefaultTableModel(new Object[][]{},new String[]{
                            "Nombre","Teléfono","Email","Categoría","Favorito"}));
        tabla_contactos.setAutoCreateRowSorter(true);
        scrTabla = new JScrollPane(tabla_contactos);
        scrTabla.setBounds(25, 242, 971, 330);
        panelContactos.add(scrTabla);
        
        // LISTA DE CONTACTOS        
        lst_contactos = new JList();
        lst_contactos.setFont(new Font("Tahoma",Font.PLAIN,15));
        lst_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrLista = new JScrollPane(lst_contactos);
        scrLista.setBounds(25, 585, 971, 55);
        panelContactos.add(scrLista);
        
        // EXPORTAR CSV        
        btn_exportar = new JButton("EXPORTAR CSV");
        btn_exportar.setFont(new Font("Tahoma",Font.PLAIN,15));
        btn_exportar.setBounds(601, 150, 180, 40);
        panelContactos.add(btn_exportar);
        
        // BARRA DE PROGRESO        
        barra_progreso = new JProgressBar();
        barra_progreso.setBounds(796, 150, 200, 40);
        barra_progreso.setStringPainted(true);
        barra_progreso.setValue(0);
        panelContactos.add(barra_progreso);
        
        // CONTROLADOR        
        logica_ventana lv = new logica_ventana(this);
    }
}
