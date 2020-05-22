package com.liceolapaz.des.dgm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
		// Constructor de la ventana
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
	
	// Creamos la conexión
	private Connection crearConexion(String url) throws SQLException {
		return DriverManager.getConnection(url, this.usuario, this.password);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void crearMenu() {
		// Método para crear la barra de menú
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
				actuales();
				
			}
		});
		menuInformes.add(actuales);
		
		JMenuItem actualesBajas = new JMenuItem("Actuales + Bajas");
		actualesBajas.setMnemonic(KeyEvent.VK_B);
		actualesBajas.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
		actualesBajas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actualesBajas();
				
			}
		});
		menuInformes.add(actualesBajas);
		
		JMenuItem historico = new JMenuItem("Histórico");
		historico.setMnemonic(KeyEvent.VK_I);
		historico.setAccelerator(KeyStroke.getKeyStroke("ctrl I"));
		historico.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				historico();
				
			}
		});
		menuInformes.add(historico);
		
		JMenuItem responsable = new JMenuItem("Responsable/Aula");
		responsable.setMnemonic(KeyEvent.VK_R);
		responsable.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		responsable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				responsableAula();
				
			}
		});
		menuInformes.add(responsable);
			
		menuBar.add(menuInformes);
		
				
		setJMenuBar(menuBar);
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void responsableAula() {
		// Método que muestra la tabla resultado de una búsqueda introducida por teclado
		String resultado = JOptionPane.showInputDialog(this, "Introduzca un responsable o un aula: ", "Responsable/Aula", JOptionPane.INFORMATION_MESSAGE);
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM actual WHERE Resp LIKE '%" + resultado + "%' OR Local LIKE '" + resultado + "'");
			ResultSet rs = ps.executeQuery();
			Informe informe = new Informe(rs, "Responsable/Aula");
			informe.setVisible(true);
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al mostrar los datos", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {} 
		}
	
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void historico() {
		// Método que muestra la tabla histórico
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM historico");
			ResultSet rs = ps.executeQuery();
			Informe informe = new Informe(rs, "Histórico");
			informe.setVisible(true);
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al mostrar los datos", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {} 
		}
	
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void actualesBajas() {
		// Método que muestra la tabla actual al completo
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM actual");
			ResultSet rs = ps.executeQuery();
			Informe informe = new Informe(rs, "Actuales + Bajas");
			informe.setVisible(true);
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al mostrar los datos", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {} 
		}
	
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void actuales() {
		// Método que muestra la tabla actual filtrando por los elementos que no han sido dados de baja
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM actual WHERE FecBaja IS NULL");
			ResultSet rs = ps.executeQuery();
			Informe informe = new Informe(rs, "Actuales");
			informe.setVisible(true);
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al mostrar los datos", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {} 
		}
	
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void limpiar() {
		// Método que limpia los campos de la ventana, dejándolos vacíos
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
		// Método que carga los datos de un objeto con un código determinado introducido por teclado
		Connection conexion = null;
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			// Preparamos la orden
			PreparedStatement ps = conexion.prepareStatement("SELECT Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, FecMod, FecBaja, Motivo, Obs FROM actual WHERE Cod = ?");
			String codigo = JOptionPane.showInputDialog(this, "Introduzca el código:", "Cargar datos", JOptionPane.INFORMATION_MESSAGE);
			if (codigo == null || codigo.equals("")) {
				conexion.close();
				return;
			}
			try {
				
				ps.setString(1, codigo);
				ResultSet rs = ps.executeQuery();
				// Vamos introduciendo en los campos correspondientes la información obtenida
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
					// Si no existe el código introducido
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
		// Método que crea los dos botones de la parte inferior de la ventana
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
				darBaja();
				
			}
		});
		panelBotones.add(bDarBaja);
		add(panelBotones, BorderLayout.SOUTH);
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void darBaja() {
		// Método que da de baja un objeto
		String codigo = txtCodigo.getText();
		
		if (codigo.equals("")) {
			// Si no hay ningún objeto seleccionado no se puede dar de baja
			JOptionPane.showMessageDialog(this, "No existe ningún objeto al que dar de baja", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			Connection conexion = null;
			
			try {
				conexion = crearConexion(URL_BASE_DATOS);
				// Preparamos la actualización de la tabla actual
				PreparedStatement ps = conexion.prepareStatement("UPDATE actual SET FecAlta = ?, FecMod = ?, FecBaja = ?, Motivo = ? WHERE Cod = ?");
				// Obtenemos en una variable de tipo Timestamp la fecha y hora actuales del equipo
				java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
				// Obtenemos el motivo de la baja mediante un popup
				String motivoBaja = JOptionPane.showInputDialog(this, "Introduzca el motivo de la baja:", "Dar de baja", JOptionPane.QUESTION_MESSAGE);
				// Rellenamos los campos del ps
				ps.setString(1, txtFechaAlta.getText());
				String fecMod = txtFechaModificacion.getText();
				if (fecMod.equals("")) {
					try {
						ps.setObject(2, null);
					} catch (SQLException e) {}
				} else {
					try {
						ps.setString(2, fecMod);
					} catch (SQLException e) {}
				}
				ps.setTimestamp(3, timestamp);
				ps.setString(4, motivoBaja);
				ps.setString(5, txtCodigo.getText());
				
				int filas1 = ps.executeUpdate();
				if (filas1 != 0) {
					JOptionPane.showMessageDialog(this, "La baja se ha cursado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				// Preparamos el insert en la tabla historico 
				PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO historico (Cod, Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, FecMod, FecBaja, Motivo, Obs) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				// Rellenamos el ps2
				ps2.setString(1, txtCodigo.getText());
				ps2.setString(2, txtDescripcion.getText());
				ps2.setString(3, txtTipo.getText());
				ps2.setString(4, txtMarca.getText());
				ps2.setString(5, txtModelo.getText());
				
				String numSerie = txtNumeroSerie.getText();
				if (numSerie.equals("")) {
					try {
						ps2.setObject(6, null);
					} catch (SQLException e) {}
				} else {
					try {
						ps2.setString(6, numSerie);
					} catch (SQLException e) {}
				}
				
				String resp = txtResponsable.getText();
				if (resp.equals("")) {
					try {
						ps2.setObject(7, null);
					} catch (SQLException e) {}
				} else {
					try {
						ps2.setString(7, resp);
					} catch (SQLException e) {}
				}
				
				String local = txtLocal.getText();
				if (local.equals("")) {
					try {
						ps2.setObject(8, null);
					} catch (SQLException e) {}
				} else {
					try {
						ps2.setString(8, local);
					} catch (SQLException e) {}
				}
				// La fecAlta no puede ser nula, así que no hace falta hacer comprobación
				ps2.setString(9, txtFechaAlta.getText());
				// La variable fecMod ya está creada así que la reutilizamos
				if (fecMod.equals("")) {
					try {
						ps2.setObject(10, null);
					} catch (SQLException e) {}
				} else {
					try {
						ps2.setString(10, fecMod);
					} catch (SQLException e) {}
				}
				// Fecha de baja
				ps2.setTimestamp(11, timestamp);
				// Motivo de la baja
				ps2.setString(12, motivoBaja);
				
				String obs = txtObservaciones.getText();
				if (obs.equals("")) {
					try {
						ps2.setObject(13, null);
					} catch (SQLException e) {}
				} else {
					try {
						ps2.setString(13, obs);
					} catch (SQLException e) {}
				}
				// Ejecutamos la orden
				int filas2 = ps2.executeUpdate();
				if (filas2 != 0) {
					JOptionPane.showMessageDialog(this, "La baja se ha insertado en el histórico", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				// Llamamos al método de refrescar datos para actualizar la información de la ventana
				refrescarDatos();
				conexion.close();
				
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this,  e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				try {
					if (conexion != null) {
						conexion.close();
					}
				} catch (SQLException e1) {}
			}
			
		}
		
	
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	protected void grabarDatos() {
		// Método que da de alta o acualiza un objeto de la base de datos
		
		// Obtenemos el texto del campo codigo
		String codigo = txtCodigo.getText();
		// Si no hay código significa que hay que crear un nuevo objeto
		if (codigo.equals("")) {
			
			Connection conexion = null;
			
			try {
				conexion = crearConexion(URL_BASE_DATOS);
				// Preparamos la orden
				PreparedStatement ps = conexion.prepareStatement("INSERT INTO actual (Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, Obs) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				// Llamamos al método rellenar para completar el ps
				if (!rellenarPS(conexion, ps)) {
					return;
				}
				// Ejecutamos la orden y mandamos un mensaje de confirmación
				int filas1 = ps.executeUpdate();
				if (filas1 != 0) {
					JOptionPane.showMessageDialog(this, "Se han creado los datos", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				
				// Obtenemos los datos necesarios para hacer un insert en la tabla historico
				PreparedStatement ps2 = conexion.prepareStatement("SELECT Cod, FecAlta FROM actual WHERE Des = ? AND Tipo = ? AND Marca = ? AND Modelo = ?");
				ps2.setString(1, txtDescripcion.getText());
				ps2.setString(2, txtTipo.getText());
				ps2.setString(3, txtMarca.getText());
				ps2.setString(4, txtModelo.getText());
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
			
		} else {
			// Si existe un código en el campo txtCodigo actualizamos la información
			Connection conexion = null;
			
			try {
				conexion = crearConexion(URL_BASE_DATOS);
				PreparedStatement ps = conexion.prepareStatement("UPDATE actual SET Des = ?, Tipo = ?, Marca = ?, Modelo = ?, NumSerie = ?, Resp = ?, Local = ?, Obs = ?, FecAlta = ?, FecMod = ? WHERE Cod = ?");
				if (!rellenarPS(conexion, ps)) {
					return;
				}
				// Obtenemos en una variable de tipo Timestamp la fecha y hora actuales del equipo
				java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
				// Rellenamos los campos restantes del ps
				ps.setString(9, txtFechaAlta.getText());
				ps.setTimestamp(10, timestamp);
				ps.setString(11, txtCodigo.getText());
				int filas1 = ps.executeUpdate();
				if (filas1 != 0) {
					JOptionPane.showMessageDialog(this, "Se han actualizado los datos", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				// Obtenemos la información necesaria para hacer un insert en la tabla histórico
				PreparedStatement ps2 = conexion.prepareStatement("SELECT Cod, FecAlta FROM actual WHERE NumSerie = ?");
				String numSerie = txtNumeroSerie.getText();
				ps2.setString(1, numSerie);
				ResultSet rs = ps2.executeQuery();
				PreparedStatement ps3 = conexion.prepareStatement("INSERT INTO historico (Cod, Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, Obs, FecMod) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				rellenarPS2(ps3, rs);
				
				ps3.setTimestamp(11, timestamp);
				
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
		
		refrescarDatos();
	
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void refrescarDatos() {
		// Método para refrescar los datos de los campos tras hacer el insert o el update correspondiente
		Connection conexion = null;
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, FecMod, FecBaja, Motivo, Obs, Cod FROM actual WHERE Des = ? AND Tipo = ? AND Marca = ? AND Modelo = ?");
			
			try {
				ps.setString(1, txtDescripcion.getText());
				ps.setString(2, txtTipo.getText());
				ps.setString(3, txtMarca.getText());
				ps.setString(4, txtModelo.getText());

				ResultSet rs = ps.executeQuery();
				if (rs.first()) {
					txtCodigo.setText("" + rs.getString(13));
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
					JOptionPane.showMessageDialog(this, "No existe ningún objeto con los valores introducidos ", "Error", JOptionPane.ERROR_MESSAGE);
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void rellenarPS2(PreparedStatement ps, ResultSet rs) {
		// Método variante de rellenarPS en el que también introducimos en el ps el código del objeto
		// Al ejecutarse después del rellenarPS original, se entiende que todos los campos obligatorios estan cubiertos y no hace falta realizar su comprobación
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
				// Rellenar campo fecha
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
		// Método para rellenar un ps dado
		
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
		if (modelo.equals("")) {
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
		// Método que crea los campos y sus correspondientes JLabel que aparecen en la ventana principal
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
		// Creacion del JComboBox y adición los items.
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
