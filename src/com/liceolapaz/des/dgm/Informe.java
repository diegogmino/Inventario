package com.liceolapaz.des.dgm;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Informe extends JDialog {
	
	private ResultSet rs;
	
	public Informe(ResultSet rs, String titulo) {
		// Método constructor de la clase
		super();
		this.rs = rs;
		setSize(1300, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			setTitle(titulo);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al obtener los metadatos", JOptionPane.ERROR_MESSAGE);
		}
		setLayout(new BorderLayout());
		crearTabla();
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private void crearTabla() {
		
		Modelo modelo = new Modelo(rs);
		JTable tabla = new JTable(modelo);
		JScrollPane scrollPane = new JScrollPane(tabla);
		add(scrollPane, BorderLayout.CENTER);
		
	}

}
