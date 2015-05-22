package com.dvd.clase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase que facilita el acceso a una Base de Datos SQLite creando la Base de
 * datos a partir de un fichero en la carpeta Assets
 **/
public class BaseDatosHelper extends SQLiteOpenHelper {

	public static Info tClase = new Info();
	public static Info tProfe = new Info();
	public static Info tAssig = new Info();
	/*
	 * La carpeta per defecte on Android espera trobar la Base de Dades de la
	 * aplicació
	 */
	private static final String DB_PATH = "/data/data/com.dvd.clase/databases/";
	private static final String DB_NAME = "Classes.db";

	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private final int numClase = tClase.GetNumClase();
	private final String nomProfe = tProfe.GetNomProfe();

	// taula de la base de dades:
	private static final String TABLE_CLASSES = "Classes";
	private static final String TABLE_HORARIS = "Horaris";
	private static final String TABLE_PROFES = "Profes";
	private static final String TABLE_ASSIGNATURAS = "Assignaturas";
	// Columnes de les diferentes taules:
	private static final String TABLE_KEY_NUM_CLASE = "NumClasse";
	private static final String TABLE_KEY_COLOR = "Color";
	private static final String TABLE_KEY_PROFE = "Nom";
	private static final String TABLE_KEY_NPROFE = "NProfe";
	private static final String TABLE_KEY_CORREU = "Correu";
	private static final String TABLE_KEY_TEL = "Tel";
	private static final String TABLE_KEY_WEB = "Web";
	private static final String TABLE_KEY_DESPATX = "Despatx";
	private static final String TABLE_KEY_ASSIGNATURA = "Assignatura";
	private static final String TABLE_KEY_NASSIGNATURA = "NAssignatura";
	private static final String TABLE_KEY_COORDINADOR = "Coordinador";
	private static final String TABLE_KEY_NCREDITS = "NCredits";
	private static final String TABLE_KEY_COMPETENCIES = "Competencies";
	private static final String TABLE_KEY_NOM_MATERIA = "NomMateria";
	private static final String TABLE_KEY_PROFE1 = "Profe1";
	private static final String TABLE_KEY_PROFE2 = "Profe2";
	private static final String TABLE_KEY_PROFE3 = "Profe3";
	private static final String TABLE_KEY_HORA_INICI = "HInici";
	private static final String TABLE_KEY_MIN_INICI = "MInici";
	private static final String TABLE_KEY_DIA = "Dia";
	private static final String TABLE_KEY_FOTO = "Foto";

	/**
	 * Constructor
	 * 
	 * Guarda una referencia al contexto para acceder a la carpeta assets de la
	 * aplicación y a los recursos
	 * 
	 * @param contexto
	 **/
	public BaseDatosHelper(Context contexto) {
		super(contexto, DB_NAME, null, 1);
		this.myContext = contexto;
	}

	/** Comencem les consultes a la base de dades... **/

	public Info GetFitxaAssig() {
		Info setFitxaAssig = new Info();
		Cursor c = myDataBase.rawQuery("SELECT " + TABLE_KEY_ASSIGNATURA + ", "
				+ TABLE_KEY_COORDINADOR + ", " + TABLE_KEY_PROFE1 + ", "
				+ TABLE_KEY_PROFE2 + ", " + TABLE_KEY_PROFE3 + ", "
				+ TABLE_KEY_NCREDITS + ", " + TABLE_KEY_COMPETENCIES + ", "
				+ TABLE_KEY_NOM_MATERIA + " FROM " + TABLE_ASSIGNATURAS
				+ " WHERE " + TABLE_KEY_ASSIGNATURA + "='"
				+ tClase.GetAssignatura() + "'", null);
		c.moveToFirst();
		if (c.moveToFirst() == true) {
			setFitxaAssig.SetAssignatura(c.getString(0));
			setFitxaAssig.SetCoordinador(GetProfeNom(c.getInt(1)));
			setFitxaAssig.SetAssigProfe1(GetProfeNom(c.getInt(2)));
			setFitxaAssig.SetAssigProfe2(GetProfeNom(c.getInt(3)));
			setFitxaAssig.SetAssigProfe3(GetProfeNom(c.getInt(4)));
			setFitxaAssig.SetNCredits(c.getInt(5));
			setFitxaAssig.SetCompetencies(c.getString(6));
			setFitxaAssig.SetNomMateria(c.getString(7));
			c.close();
		}
		return setFitxaAssig;
	}

	public Info GetFitxaProfe() {
		Info setFitxaProfe = new Info();
		Cursor c = myDataBase.rawQuery("SELECT " + TABLE_PROFES + "."
				+ TABLE_KEY_PROFE + ", " + TABLE_PROFES + "."
				+ TABLE_KEY_CORREU + ", " + TABLE_PROFES + "." + TABLE_KEY_TEL
				+ ", " + TABLE_PROFES + "." + TABLE_KEY_WEB + ", "
				+ TABLE_PROFES + "." + TABLE_KEY_DESPATX + ", " + TABLE_PROFES
				+ "." + TABLE_KEY_FOTO + ", " + TABLE_ASSIGNATURAS + "."
				+ TABLE_KEY_ASSIGNATURA + " FROM " + TABLE_PROFES + ", "
				+ TABLE_ASSIGNATURAS + " WHERE " + TABLE_PROFES + "."
				+ TABLE_KEY_NASSIGNATURA + "=" + TABLE_ASSIGNATURAS + "."
				+ TABLE_KEY_NASSIGNATURA + " AND " + TABLE_PROFES + "."
				+ TABLE_KEY_PROFE + "='" + nomProfe + "'", null);

		/*
		 * Cursor c = myDataBase.rawQuery(
		 * "SELECT Profes.Nom, Profes.Correu, Profes.Tel, Profes.Web, Profes.Despatx, Profes.Foto, Assignaturas.Assignatura FROM Profes, Assignaturas WHERE Profes.NAssignatura=Assignaturas.NAssignatura AND Profes.Nom='"
		 * + nomProfe + "'", null);
		 */

		c.moveToFirst();
		if (c.moveToFirst() == true) {
			setFitxaProfe.SetNomProfe(c.getString(0));
			setFitxaProfe.SetCorreu(c.getString(1));
			setFitxaProfe.SetTel(c.getInt(2));
			setFitxaProfe.SetWeb(c.getString(3));
			setFitxaProfe.SetDespatx(c.getString(4));
			setFitxaProfe.SetFoto(c.getString(5));
			setFitxaProfe.SetAssignatura(c.getString(6));

			c.close();
		}
		return setFitxaProfe;
	}

	public Info GetFitxaClasse(int NumClasse, int hora, int min, int dia) {

		Info setClasse = new Info();

		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_HORARIS
				+ " WHERE " + TABLE_KEY_NUM_CLASE + "=" + numClase + " AND "
				+ TABLE_KEY_HORA_INICI + "=" + hora + " AND "
				+ TABLE_KEY_MIN_INICI + "=" + min + " AND " + TABLE_KEY_DIA
				+ "='" + dia_actual(dia) + "'", null);
		c.moveToFirst();
		if (c.moveToFirst() == true) {
			setClasse.SetNumClase(c.getInt(0));
			setClasse.SetNAssignatura(c.getInt(1));
			setClasse.SetHoraInici(c.getInt(2));
			setClasse.SetMinInici(c.getInt(3));
			setClasse.SetHoraFi(c.getInt(4));
			setClasse.SetMinFi(c.getInt(5));
			setClasse.SetDia(c.getString(6));
			c.close();
		}
		return setClasse;
	}

	public ArrayList<Info> GetLlistaClasses() {
		ArrayList<Info> llistaClasses = new ArrayList<Info>();

		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_CLASSES
				+ " ORDER BY " + TABLE_KEY_NUM_CLASE, null);

		// Iteramos a traves de los registros del cursor
		c.moveToFirst();

		while (c.isAfterLast() == false) {
			Info classe = new Info();

			classe.SetNumClase(c.getInt(0));
			classe.SetColor(c.getInt(1));

			llistaClasses.add(classe);

			c.moveToNext();
		}
		c.close();

		return llistaClasses;
	}

	public ArrayList<Info> GetLlistaAssignaturas() {
		ArrayList<Info> llistaAssignaturas = new ArrayList<Info>();

		Cursor c = myDataBase.rawQuery("SELECT " + TABLE_KEY_ASSIGNATURA
				+ " FROM " + TABLE_ASSIGNATURAS + " ORDER by "
				+ TABLE_KEY_ASSIGNATURA  , null);
		// Iteramos a traves de los registros del cursor
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			Info classe = new Info();

			classe.SetAssignatura(c.getString(0));

			llistaAssignaturas.add(classe);

			c.moveToNext();
		}

		c.close();

		return llistaAssignaturas;
	}

	public ArrayList<Info> GetLlistaProfes() {
		ArrayList<Info> llistaProfes = new ArrayList<Info>();

		Cursor c = myDataBase.rawQuery("SELECT " + TABLE_KEY_PROFE + ","
				+ TABLE_KEY_FOTO + " FROM " + TABLE_PROFES + " ORDER BY "
				+ TABLE_KEY_PROFE, null);
		// Iteramos a traves de los registros del cursor
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			Info classe = new Info();

			classe.SetNomProfe(c.getString(0));
			classe.SetFoto(c.getString(1));

			llistaProfes.add(classe);
			c.moveToNext();
		}
		c.close();
		return llistaProfes;
	}

	public Info GetNClasse(int NumClasse, int hora, int min, int dia) {
		// Funcio per trobar la classe que ve despres de la actual
		Info setClasse = new Info();

		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_HORARIS
				+ " WHERE " + TABLE_KEY_NUM_CLASE + "=" + numClase + " AND "
				+ TABLE_KEY_HORA_INICI + "=" + hora + " AND "
				+ TABLE_KEY_MIN_INICI + "=" + min + " AND " + TABLE_KEY_DIA
				+ "='" + dia_actual(dia) + "'", null);
		c.moveToFirst();
		if (c.moveToFirst() == true) {
			setClasse.SetNAssignatura2(c.getInt(1));
			setClasse.SetHoraInici2(c.getInt(2));
			setClasse.SetMinInici2(c.getInt(3));
			setClasse.SetHoraFi2(c.getInt(4));
			setClasse.SetMinFi2(c.getInt(5));

			c.close();
		}
		return setClasse;
	}

	public Info GetAssignatura(int NAssig) {
		Info setAssignatura = new Info();
		Cursor c = myDataBase.rawQuery("SELECT " + TABLE_KEY_ASSIGNATURA
				+ " FROM " + TABLE_ASSIGNATURAS + " WHERE "
				+ TABLE_KEY_NASSIGNATURA + "=" + NAssig, null);
		c.moveToFirst();
		if (c.moveToFirst() == true) {
			setAssignatura.SetAssignatura(c.getString(0));
			c.close();
		}
		return setAssignatura;

	}

	public Info GetProfe(int NAssig) {
		Info setProfe = new Info();
		Cursor c = myDataBase.rawQuery("SELECT " + TABLE_KEY_PROFE + ","
				+ TABLE_KEY_FOTO + " FROM " + TABLE_PROFES + " WHERE "
				+ TABLE_KEY_NASSIGNATURA + "=" + NAssig, null);
		c.moveToFirst();
		if (c.moveToFirst() == true) {
			setProfe.SetNomProfe(c.getString(0));
			setProfe.SetFoto(c.getString(1));
			c.close();
		}
		return setProfe;
	}

	public String GetProfeNom(int NumProfe) {
		String NomProfe = "";
		// Info setProfe = new Info();
		Cursor c = myDataBase.rawQuery("SELECT " + TABLE_KEY_PROFE + " FROM "
				+ TABLE_PROFES + " WHERE " + TABLE_KEY_NPROFE + "=" + NumProfe,
				null);
		c.moveToFirst();
		if (c.moveToFirst() == true) {
			// setProfe.SetNomProfe(c.getString(0));
			// setProfe.SetFoto(c.getString(1));
			NomProfe = c.getString(0);
			c.close();
		}
		return NomProfe;
	}

	public Info GetColor(int numClasse) {
		Info setColor = new Info();
		Cursor c = myDataBase.rawQuery("SELECT " + TABLE_KEY_COLOR + " FROM "
				+ TABLE_CLASSES + " WHERE " + TABLE_KEY_NUM_CLASE + "="
				+ numClase, null);
		c.moveToFirst();
		if (c.moveToFirst() == true) {
			setColor.SetColor(c.getInt(0));
			c.close();
		}
		return setColor;
	}

	/**
	 * Fins aquí totes les consultes
	 **/

	public void crearDataBase() throws IOException {

		boolean dbExist = comprobarBaseDatos();

		if (dbExist) {
			// Si ya existe no hacemos nada
		} else {
			// Si no existe, creamos una nueva Base de datos en la carpeta por
			// defecto de nuestra aplicación,
			// de esta forma el Sistema nos permitirá sobreescribirla con la que
			// tenemos en la carpeta Assets
			this.getReadableDatabase();
			try {
				copiarBaseDatos();
			} catch (IOException e) {
				throw new Error("Error al copiar la Base de Datos");
			}
		}
	}

	/**
	 * Comprobamos si la base de datos existe
	 * 
	 * @return true si existe, false en otro caso
	 **/
	private boolean comprobarBaseDatos() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

		} catch (SQLiteException e) {
			throw new Error("Imposible abrir base de datos");// No existe
		}
		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copia la base de datos desde la carpeta Assets sobre la base de datos
	 * vacía recién creada en la carpeta del sistema, desde donde es accesible
	 **/
	private void copiarBaseDatos() throws IOException {

		// Abrimos la BBDD de la carpeta Assets como un InputStream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Carpeta de destino
		String outFileName = DB_PATH + DB_NAME;

		// Abrimos la BBDD como OutputStream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// Transfiere los Bytes entre el Stream de entrada y el de Salida
		byte[] buffer = new byte[1024];
		int length;

		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Cerramos los ficheros abiertos
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Abre la base de datos
	 **/

	public void abrirBaseDatos() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	/**
	 * Cierra la base de datos
	 **/
	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// No usamos este método
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// No usamos este método
	}

	/*
	 * obtenemos la hora actual
	 */

	public int hora_actual() {
		Date tiempoActual=new Date();
		int hora=tiempoActual.getHours();
		return hora;
	}

	/*
	 * obtenemos los minutos actuales
	 */

	public int min_actual() {
		// Date tiempoActual=new Date();
		// int min=tiempoActual.getMinutes();
		int min = 0;
		return min;
	}

	public String dia_actual(int dia) {
		Date tiempoActual=new Date(); 
		dia = tiempoActual.getDay();
		String dia_Sem = "";
		
		switch (dia) {
		case 1: {
			dia_Sem = "Dilluns";
			break;
		}
		case 2: {
			dia_Sem = "Dimarts";
			break;
		}
		case 3: {
			dia_Sem = "Dimecres";
			break;
		}
		case 4: {
			dia_Sem = "Dijous";
			break;
		}
		case 5: {
			dia_Sem = "Divendres";
			break;
		}
		case 6: {
			dia_Sem = "Dissabte";
			break;
		}
		case 7: {
			dia_Sem = "Diumenge";
			break;
		}
		}

		return dia_Sem;
	}

}