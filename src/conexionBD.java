import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class conexionBD {

	private static final String USER = "DAW_PNDC22_23_XIAL";
	private static final String PWD = "AX123";
//	private static final String URL = "jdbc:oracle:thin:@192.168.3.26:1521:xe";
	private static final String URL = "jdbc:oracle:thin:@oracle.ilerna.com:1521:xe";

	private static final Connection con = conectarBD();

	// Se conecta a la Base de datos
	public static Connection conectarBD() {
		Connection con = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(URL, USER, PWD);
			System.out.println("Conectado a la base de datos");
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (SQLException e) {
			System.out.println(e);
		}

		return con;

	}

	// Comprueba si el usuario existe
	public static boolean comprobarUsuario(String nombre) {
		int count = 99;
		String sql = "SELECT COUNT(*) FROM JUGADOR WHERE nombre =  '" + nombre + "'";

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}

		} catch (SQLException e) {
			System.out.println(e);
		}

		if (count == 0) {
			return true;
		} else {
			return false;
		}

	}

	// Retorna el numero de ID del jugador que le pasamos por parametro
	public static int selectIdJugador(String nombre) {
		int idJugador = 0;
		String sql = "SELECT ID_JUGADOR FROM JUGADOR WHERE nombre =  '" + nombre + "'";

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				idJugador = rs.getInt("ID_JUGADOR");
			}

		} catch (SQLException e) {
			System.out.println(e);
		}

		return idJugador;

	}

	// Guardar un nuevo usuario
	public static void guardarUsuario(String nombre) {
		// Sentencia
		String sql = "BEGIN addJugador('" + nombre + "'); END;";

		try {
			Statement st = con.createStatement();
			// Ejecuta la sentencia
			st.execute(sql);
		} catch (SQLException e) {
			System.out.println(e);
		}

	}

	// Guarda la partida terminada
	public static void guardarPartidaAcabada(int idUsuario, int rondas, String resultado) {

		String sql = "BEGIN addPartida(" + idUsuario + ", " + rondas + ", '" + resultado + "'); END;";

		try {
			Statement st = con.createStatement();
			st.execute(sql);
		} catch (SQLException e) {
			System.out.println(e);
		}

	}

	// Carga el ranking
	public static void cargarRanking() {

		String sql = "SELECT J.nombre, COUNT(p.resultado) AS VICTORIAS FROM JUGADOR J, PARTIDA P WHERE J.ID_JUGADOR= P.JUGADOR AND P.RESULTADO = 'V' GROUP BY J.nombre ORDER BY COUNT(P.RESULTADO) DESC";
		ArrayList<ArrayList> ranking = new ArrayList<ArrayList>();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			for (int i = 0; rs.next(); i++) {
				ArrayList<String> jugador = new ArrayList<String>();

				jugador.add(rs.getString("NOMBRE"));
				jugador.add(Integer.toString(rs.getInt("VICTORIAS")));

				ranking.add(jugador);
			}

		} catch (SQLException e) {
			System.out.println(e);
		}

	}

	// VA LENTO (TRIGGER) SHHHH
	public static void guardarPartida(int idJugador, ArrayList<ArrayList> vacunas, ArrayList<ArrayList> ciudadesBrotes,
			int brotes, int rondas) {

		String sqlVacunas = darFormatoVacunas(vacunas);
		String ciudadesAlfa = darFormatoCiudades(ciudadesBrotes, 0);
		String ciudadesBeta = darFormatoCiudades(ciudadesBrotes, 1);
		String ciudadesGama = darFormatoCiudades(ciudadesBrotes, 2);
		String ciudadesDelta = darFormatoCiudades(ciudadesBrotes, 3);

		String sql = "INSERT INTO P_GUARDADAS VALUES (" + idJugador + ", vacunas(" + sqlVacunas + "), ciudadesAlfa("
				+ ciudadesAlfa + "), ciudadesBeta(" + ciudadesBeta + "), ciudadesGama(" + ciudadesGama
				+ "), ciudadesDelta(" + ciudadesDelta + "), " + brotes + ", " + rondas + ")";

		try {
			Statement st = con.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e);
		}

		System.out.println("COMPLETADO");

	}

	/// COMPLETAR
	public static void cargarPartida(int user) {
		int jugador = 99;
		String sql = "SELECT JUGADOR FROM P_GUARDADAS";

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				jugador = rs.getInt("JUGADOR");
			}

			System.out.println(jugador);

		} catch (SQLException e) {
			System.out.println(e);
		}

	}

	public static String darFormatoVacunas(ArrayList<ArrayList> vacunas) {

		String[] vacuna = new String[vacunas.size()];
		for (int i = 0; i < vacuna.length; i++) {
			vacuna[i] = (String) vacunas.get(i).get(1);
		}

		return Arrays.toString(vacuna).substring(1, Arrays.toString(vacuna).length() - 1);
	}

	public static String darFormatoCiudades(ArrayList<ArrayList> ciudadesBrotes, int enfermedad) {
		String[] ciudades = new String[12];
		int x = 0;

		for (ArrayList ciudad : ciudadesBrotes) {
			int enf = Integer.parseInt((String) ciudad.get(2));
			if (enf == enfermedad) {
				ciudades[x] = (String) ciudad.get(1);
				x++;
			}
		}

		return Arrays.toString(ciudades).substring(1, Arrays.toString(ciudades).length() - 1);
	}

}
