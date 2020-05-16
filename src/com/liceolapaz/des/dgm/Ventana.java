package com.liceolapaz.des.dgm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.Driver;


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
	private static final String URL_BASE_DATOS = "jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid";
	
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
		
		try {
			DriverManager.registerDriver(new Driver());
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error al registrar el driver", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private Connection crearConexion(String url) throws SQLException {
		return DriverManager.getConnection(url, this.usuario, this.password);
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
				cargarDatos();
						
			}
		});
		menuOpciones.add(cargarDatos);
				
		JMenuItem limpiarDatos = new JMenuItem("Limpiar Datos");
		limpiarDatos.setMnemonic(KeyEvent.VK_L);
		limpiarDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
		limpiarDatos.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				limpiar();
						
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
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void limpiar() {
	
		Component[] componentes = panelCampos.getComponents();
		for (int i = 0; i < componentes.length; i++) {
			try {
				JTextField textField = (JTextField) componentes[i];
				textField.setText("");
			} catch(ClassCastException ex) {}
			
		txtObservaciones.setText("");	
			
		}
		
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void cargarDatos() {
		
		Connection conexion = null;
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, FecMod, FecBaja, Motivo, Obs FROM actual WHERE Cod = ?");
			String codigo = JOptionPane.showInputDialog(this, "Introduzca el código:", "Cargar datos", JOptionPane.INFORMATION_MESSAGE);
			if (codigo == null || codigo.equals("")) {
				conexion.close();
				return;
			}
			try {
				
				ps.setString(1, codigo);
				ResultSet rs = ps.executeQuery();
				if (rs.first()) {
					txtCodigo.setText("" + codigo);
					txtDescripcion.setText(rs.getString(1));
					txtTipo.setText(rs.getString(2));
					txtMarca.setText(rs.getString(3));
					txtModelo.setText(rs.getString(4));
					
					if (rs.getObject(5) == null) {
						txtNumeroSerie.setText("");
					} else {
						txtNumeroSerie.setText(rs.getString(5));
					}
					
					if (rs.getObject(6) == null) {
						txtResponsable.setText("");
					} else {
						txtResponsable.setText(rs.getString(6));
					}
					
					if (rs.getObject(7) == null) {
						txtLocal.setText("");
					} else {
						txtLocal.setText(rs.getString(7));
					}
														
					txtFechaAlta.setText(rs.getString(8));
					
					if (rs.getObject(9) == null) {
						txtFechaModificacion.setText("");
					} else {
						txtFechaModificacion.setText(rs.getString(9));
					}
					
					if (rs.getObject(10) == null) {
						txtFechaBaja.setText("");
					} else {
						txtFechaBaja.setText(rs.getString(10));
					}
					
					if (rs.getObject(11) == null) {
						txtMotivoBaja.setText("");
					} else {
						txtMotivoBaja.setText(rs.getString(11));
					}
					
					if (rs.getObject(12) == null) {
						txtObservaciones.setText("");
					} else {
						txtObservaciones.setText(rs.getString(12));
					}
					
				} else {
					JOptionPane.showMessageDialog(this, "No existe ningún objeto con Código " + codigo +".", "Error", JOptionPane.ERROR_MESSAGE);
					conexion.close();
					return;
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El código del objeto tiene que ser un código válido", "Error", JOptionPane.ERROR_MESSAGE);
				conexion.close();
				return;
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}	
			} catch (SQLException e1) {}
		}
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
				grabarDatos();
				
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
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	protected void grabarDatos() {
	
		String codigo = txtCodigo.getText();
		if (codigo.equals("")) {
			
			Connection conexion = null;
			
			try {
				conexion = crearConexion(URL_BASE_DATOS);
				PreparedStatement ps = conexion.prepareStatement("INSERT INTO actual (Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, Obs) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				if (!rellenarPS(conexion, ps)) {
					return;
				}
				int filas1 = ps.executeUpdate();
				if (filas1 != 0) {
					JOptionPane.showMessageDialog(this, "Se han creado los datos", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				
				
				PreparedStatement ps2 = conexion.prepareStatement("SELECT Cod, FecAlta FROM actual WHERE NumSerie = ?");
				String numSerie = txtNumeroSerie.getText();
				ps2.setString(1, numSerie);
				ResultSet rs = ps2.executeQuery();
				PreparedStatement ps3 = conexion.prepareStatement("INSERT INTO historico (Cod, Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, Obs) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				rellenarPS2(ps3, rs);
				int filas2 = ps3.executeUpdate();
				if (filas2 != 0) {
					JOptionPane.showMessageDialog(this, "Se han creado los datos en el histórico", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
		
				conexion.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error al insertar un cliente", JOptionPane.ERROR_MESSAGE);
				try {
					if (conexion != null) {
						conexion.close();
					}
				} catch (SQLException e1) {}
			}
			
		}
		
	
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void rellenarPS2(PreparedStatement ps, ResultSet rs) {
	
		try {
			if (rs.first()) {
				// Rellenar campo código
				try {
					ps.setString(1, rs.getString(1));
				} catch (SQLException e) {}
				// Rellenar campo descripcion
				String descripcion = txtDescripcion.getText();
				try {
					ps.setString(2, descripcion);
				} catch (SQLException e) {}
				// Rellenar campo tipo
				String tipo = txtTipo.getText();
				try {
					ps.setString(3, tipo);
				} catch (SQLException e) {}
				// Rellenar campo marca
				String marca = txtMarca.getText();
				try {
					ps.setString(4, marca);
				} catch (SQLException e) {}
				// Rellenar campo modelo
				String modelo = txtModelo.getText();
				try {
					ps.setString(5, modelo);
				} catch (SQLException e) {}
				// Rellenar campo numero serie
				String numSerie = txtNumeroSerie.getText();
				try {
					ps.setString(6, numSerie);
				} catch (SQLException e) {}
				// Rellenar campo responsable
				String responsable = txtResponsable.getText();
				try {
					ps.setString(7, responsable);
				} catch (SQLException e) {}
				// Rellenar campo local
				String local = txtLocal.getText();
				try {
					ps.setString(8, local);
				} catch (SQLException e) {}
				// Rellenar campo fecha alta
				try {
					ps.setString(9, rs.getString(2));
				} catch (SQLException e) {}
				// Rellenar campo observaciones
				String observaciones = txtObservaciones.getText();
				try {
					ps.setString(10, observaciones);
				} catch (SQLException e) {}
				}
		} catch (SQLException e) {}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private boolean rellenarPS(Connection conexion, PreparedStatement ps) {
		
		// Rellenar el campo descripcion
		String descripcion = txtDescripcion.getText();
		if (descripcion.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para la descripción", "Campo obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
			
			return false;
		}
		try {
			ps.setString(1, descripcion);
		} catch (SQLException e) {}
		//Rellenar el campo tipo
		String tipo = txtTipo.getText();
		if (tipo.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para el tipo", "Campo obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
			
			return false;
		}
		try {
			ps.setString(2, tipo);
		} catch (SQLException e) {}
		// Rellenar el campo marca
		String marca = txtMarca.getText();
		if (marca.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para la marca", "Campo Obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
			
			return false;
		}
		try {
			ps.setString(3, marca);
		} catch (SQLException e) {}
		// Rellenar el campo modelo
		String modelo = txtModelo.getText();
		if (marca.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para el modelo", "Campo Obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
			
			return false;
		}
		try {
			ps.setString(4, modelo);
		} catch (SQLException e) {}
		// Rellenar el campo numero de serie
		String numSerie = txtNumeroSerie.getText();
		if (numSerie.equals("")) {
			try {
				ps.setObject(5, null);
			} catch (SQLException e) {}
		} else {
			try {
				ps.setString(5, numSerie);
			} catch (SQLException e) {
				return false;
			}
		}
		// Rellenar el campo responsable
		String responsable = txtResponsable.getText();
		if (responsable.equals("")) {
			try {
				ps.setObject(6, null);
			} catch (SQLException e) {}
		} else {
			try {
				ps.setString(6, responsable);
			} catch (SQLException e) {
				return false;
			}
		}
		// Rellenar el campo local
		String local = txtLocal.getText();
		if (local.equals("")) {
			try {
				ps.setObject(7, null);
			} catch (SQLException e) {}
		} else {
			try {
				ps.setString(7, local);
			} catch (SQLException e) {
				return false;
			}
		}
		// Rellenar el campo observaciones
		String observaciones = txtObservaciones.getText();
		if (observaciones.equals("")) {
			try {
				ps.setObject(8, null);
			} catch (SQLException e) {}
		} else {
			try {
				ps.setString(8, observaciones);
			} catch (SQLException e) {
				return false;
			}
		}
		
		return true;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

		
	private void crearCampos() {
			
		panelCampos = new JPanel();
		panelCampos.setLayout(new GridLayout(13, 2, 20, 10));
		panelCampos.setBorder(new EmptyBorder(20,20,20,20));
		
		JLabel lbCodigo = new JLabel("Código");
		panelCampos.add(lbCodigo);
		txtCodigo = new JTextField();
		txtCodigo.setEditable(false);
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
		txtFechaAlta.setEditable(false);
		panelCampos.add(txtFechaAlta);
		
		JLabel lbFechaModificacion = new JLabel("Fecha de Modificación");
		panelCampos.add(lbFechaModificacion);
		txtFechaModificacion = new  JTextField();
		txtFechaModificacion.setEditable(false);
		panelCampos.add(txtFechaModificacion);
		
		JLabel lbFechaBaja = new JLabel("Fecha de Baja");
		panelCampos.add(lbFechaBaja);
		txtFechaBaja = new  JTextField();
		txtFechaBaja.setEditable(false);
		panelCampos.add(txtFechaBaja);
		
		JLabel lbMotivoBaja = new JLabel("Motivo de baja");
		panelCampos.add(lbMotivoBaja);
		txtMotivoBaja = new  JTextField();
		txtMotivoBaja.setEditable(false);
		panelCampos.add(txtMotivoBaja);
		
		JLabel lbObservaciones = new JLabel("Observaciones");
		panelCampos.add(lbObservaciones);
		txtObservaciones = new JTextArea();
		panelCampos.add(txtObservaciones);
		
		add(panelCampos, BorderLayout.CENTER);
			
		}
		
	

}
