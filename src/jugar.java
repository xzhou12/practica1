import java.util.ArrayList;
import java.util.Scanner;

public class jugar {

	public static void Main() {

		Scanner s = new Scanner(System.in);

		int rondas = 0;
		boolean derrota, victoria;
		derrota = victoria = false;

		// Array con las ciudades
		String[][] ciudades = IA.leerCiudades();

		int[] numBrotes = { 0 };

		// Array con las ciudades y su nivel de brote
		ArrayList<ArrayList> nivelBroteCiudades = brotes.inicializarNivelBrote(ciudades);
		ArrayList<ArrayList> vacunasCura = vacunas.inicializarVacunas();

		conexionBD.guardarPartida(1, vacunasCura, nivelBroteCiudades, 4, 5);

		while (derrota == false && victoria == false) {
			// Turno jugador
//			accion.Main(nivelBroteCiudades, vacunasCura);
			// Turno IA

			ArrayList<String> ciudadesAfectadas = IA.infectarCiudadesRondas(nivelBroteCiudades);
//			System.out.println("CIUDADES AFECTADAS: " + ciudadesAfectadas);

			// Comprobaciones
			IA.comprobarBroteNivel4(nivelBroteCiudades, numBrotes);
			victoria = IA.comprobarVictoria(nivelBroteCiudades);
			derrota = IA.comprobarDerrota(nivelBroteCiudades, numBrotes);

			derrota = true;

			rondas++;

		}

	}

}
