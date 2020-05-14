package com.liceolapaz.des.dgm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;


public class Ventana extends JFrame {
	
	private Dialogo dialogo;
	private String usuario;
	private String password;
	private JPanel panelCampos;
	private JTextField txtCodigo;
	private JTextField txtDescripcion;
	private JComboBox tipo;
	private JTextField txtTipo;
	private JTextField txtMarca;
	private JTextField txtModelo;
	private JTextField txtNumeroSerie;
	private JTextField txtResponsable;
	private JTextField txtLocal;
	private JTextField txtFechaAlta;
	private JTextField txtFechaModificacion;
	private JTextField txtFechaBaja;
	private JTextField txtMotivoBaja;
	private JTextArea txtObservaciones;
	private static final String URL_BASE_DATOS = "jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid";
	
	public Ventana(Dialogo dialogo, String usuario, String password) {
		super();
		this.dialogo = dialogo;
		this.usuario = usuario;
		this.password = password;
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Inventario");
		setLayout(new BorderLayout());
		
		crearCampos();
		crearBotones();
		crearMenu();
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void crearMenu() {
		
		JMenuBar menuBar = new JMenuBar();
		// Creamos la pestaña de Opciones
		JMenu menuOpciones = new JMenu("Opciones");
				
		JMenuItem cambiarUsuario = new JMenuItem("Cambiar Usuario");
		cambiarUsuario.setMnemonic(KeyEvent.VK_U);
		cambiarUsuario.setAccelerator(KeyStroke.getKeyStroke("ctrl U"));
		cambiarUsuario.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
						
				setVisible(false);
				dialogo.setVisible(true);
						
			}
		});
		menuOpciones.add(cambiarUsuario);
				
		JMenuItem cargarDatos = new JMenuItem("Cargar Datos");
		cargarDatos.setMnemonic(KeyEvent.VK_D);
		cargarDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl D"));
		cargarDatos.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				//cargarDatos();
						
			}
		});
		menuOpciones.add(cargarDatos);
				
		JMenuItem limpiarDatos = new JMenuItem("Limpiar Datos");
		limpiarDatos.setMnemonic(KeyEvent.VK_L);
		limpiarDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
		limpiarDatos.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				//limpiar();
						
			}
		});
		menuOpciones.add(limpiarDatos);
		menuBar.add(menuOpciones);
		// Creamos la pesaña de Informes
		JMenu menuInformes = new JMenu("Informes");
		
		JMenuItem actuales = new JMenuItem("Actuales");
		actuales.setMnemonic(KeyEvent.VK_T);
		actuales.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
		actuales.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuInformes.add(actuales);
		
		JMenuItem actualesBajas = new JMenuItem("Actuales + Bajas");
		actualesBajas.setMnemonic(KeyEvent.VK_B);
		actualesBajas.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
		actualesBajas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuInformes.add(actualesBajas);
		
		JMenuItem historico = new JMenuItem("Histórico");
		historico.setMnemonic(KeyEvent.VK_I);
		historico.setAccelerator(KeyStroke.getKeyStroke("ctrl I"));
		historico.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuInformes.add(historico);
		
		JMenuItem responsable = new JMenuItem("Responsable/Aula");
		responsable.setMnemonic(KeyEvent.VK_R);
		responsable.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		responsable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuInformes.add(responsable);
			
		menuBar.add(menuInformes);
		
				
		setJMenuBar(menuBar);
		
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void crearBotones() {
		
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(1, 2, 30, 0));
		panelBotones.setBorder(new EmptyBorder(20,20,20,20));
		
		JButton bGrabar = new JButton("Grabar datos");
		bGrabar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		panelBotones.add(bGrabar);
		
		JButton bDarBaja = new JButton("Dar de baja");
		bDarBaja.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		panelBotones.add(bDarBaja);
		add(panelBotones, BorderLayout.SOUTH);
		
	}



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

		
	private void crearCampos() {
			
		panelCampos = new JPanel();
		panelCampos.setLayout(new GridLayout(13, 2, 20, 10));
		panelCampos.setBorder(new EmptyBorder(20,20,20,20));
		
		JLabel lbCodigo = new JLabel("Código");
		panelCampos.add(lbCodigo);
		txtCodigo = new JTextField();
		panelCampos.add(txtCodigo);
		
		JLabel lbDescripcion = new JLabel("Descripción");
		panelCampos.add(lbDescripcion);
		txtDescripcion = new JTextField();
		panelCampos.add(txtDescripcion);
		
		JLabel lbTipo = new JLabel("Tipo");
		panelCampos.add(lbTipo);
		txtTipo = new JTextField();
		// Creacion del JComboBox y añadir los items.
		tipo = new JComboBox();
		tipo.addItem("PC");
		tipo.addItem("Proyector");
		tipo.addItem("Pantalla");
		tipo.addItem("Pantalla interactiva");
		tipo.addItem("Tablet");
		tipo.addItem("HIFI");
		tipo.addItem("TV");
		tipo.addItem("DVD");
		tipo.addItem("Combo");
		// Accion a realizar cuando el JComboBox cambia de item seleccionado.
		tipo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtTipo.setText(tipo.getSelectedItem().toString());
			}
		});
		panelCampos.add(tipo);
		
		JLabel lbMarca = new JLabel("Marca");
		panelCampos.add(lbMarca);
		txtMarca = new JTextField();
		panelCampos.add(txtMarca);
		
		JLabel lbModelo = new JLabel("Modelo");
		panelCampos.add(lbModelo);
		txtModelo = new JTextField();
		panelCampos.add(txtModelo);
		
		JLabel lbNumeroSerie = new JLabel("Numero de serie");
		panelCampos.add(lbNumeroSerie);
		txtNumeroSerie = new JTextField();
		panelCampos.add(txtNumeroSerie);
		
		JLabel lbResponsable = new JLabel("Responsable");
		panelCampos.add(lbResponsable);
		txtResponsable = new  JTextField();
		panelCampos.add(txtResponsable);
		
		JLabel lbLocal = new JLabel("Local/Aula");
		panelCampos.add(lbLocal);
		txtLocal = new  JTextField();
		panelCampos.add(txtLocal);
		
		JLabel lbFechaAlta = new JLabel("Fecha de Alta");
		panelCampos.add(lbFechaAlta);
		txtFechaAlta = new  JTextField();
		panelCampos.add(txtFechaAlta);
		
		JLabel lbFechaModificacion = new JLabel("Fecha de Modificación");
		panelCampos.add(lbFechaModificacion);
		txtFechaModificacion = new  JTextField();
		panelCampos.add(txtFechaModificacion);
		
		JLabel lbFechaBaja = new JLabel("Fecha de Baja");
		panelCampos.add(lbFechaBaja);
		txtFechaBaja = new  JTextField();
		panelCampos.add(txtFechaBaja);
		
		JLabel lbMotivoBaja = new JLabel("Motivo de baja");
		panelCampos.add(lbMotivoBaja);
		txtMotivoBaja = new  JTextField();
		panelCampos.add(txtMotivoBaja);
		
		JLabel lbObservaciones = new JLabel("Observaciones");
		panelCampos.add(lbObservaciones);
		txtObservaciones = new JTextArea();
		panelCampos.add(txtObservaciones);
		
		add(panelCampos, BorderLayout.CENTER);
			
		}
		
	

}
