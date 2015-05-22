package com.dvd.clase;

public class Info {
	//Assignatures
	private String Assignatura = "";
	private int NAssignatura = 0;
	private String Coordinador = "";
	private String AssigProfe1 = "";
	private String AssigProfe2 = "";
	private String AssigProfe3 = "";
	
	//Profes
	private String NomProfe = "";
	private String Foto = "";
	private String Correu = "0";
	private int Tel = 0;
	private String Web = "No te web personal.";
	private String Despatx = "";
	private int NCredits= 0;
	private String Competencies ="";
	private String NomMateria="";
		
	//Classes
	private int NumClasse = 0;
	private int ColorClase = 0;
	private int HoraInici = 0;
	private int MinInici = 0;
	private int HoraFinal = 0;
	private int MinFinal = 0;
	private String Dia = "";
	
	/*Per la assignatura futura de Controlador_classe.java*/

	private int NAssignatura2 = 0;
	private int HoraInici2 = 0;
	private int MinInici2 = 0;
	private int HoraFinal2 = 0;
	private int MinFinal2 = 0;

	public String GetAssignatura() {
		return Assignatura;
	}

	public int GetNAssignatura() {
		return NAssignatura;
	}

	public String GetCoord() {
		return Coordinador;
	}
	
	public String GetAssigProfe1() {
		return AssigProfe1;
	}
	
	public String GetAssigProfe2() {
		return AssigProfe2;
	}
	public String GetAssigProfe3() {
		return AssigProfe3;
	}
	
	public int GetNCredits(){
		return NCredits;
	}
	
	public String GetCompetencies(){
		return Competencies;
	}
	
	public String GetNomMateria(){
		return NomMateria;
	}
	public String GetNomProfe() {
		return NomProfe;
	}

	public int GetColor() {
		return ColorClase;
	}

	public int GetNumClase() {
		return NumClasse;
	}

	public int GetHoraInici() {
		return HoraInici;
	}

	public int GetMinInici() {
		return MinInici;
	}

	public int GetHoraFi() {
		return HoraFinal;
	}

	public int GetMinFi() {
		return MinFinal;
	}

	public String GetDia() {
		return Dia;
	}

	public String GetFoto() {
		return Foto;
	}

	public String GetCorreu() {
		return Correu;
	}

	public int GetTel() {
		return Tel;
	}

	public String GetWeb() {
		return Web;
	}

	public String GetDespatx() {
		return Despatx;
	}

	public int GetNAssignatura2() {
		return NAssignatura2;
	}
	public int GetHoraInici2() {
		return HoraInici2;
	}

	public int GetMinInici2() {
		return MinInici2;
	}

	public int GetHoraFi2() {
		return HoraFinal2;
	}

	public int GetMinFi2() {
		return MinFinal2;
	}



	/**********************************************************/

	public void SetAssignatura(String assignatura) {
		Assignatura = assignatura;
	}

	public void SetNAssignatura(int nAssignatura) {
		NAssignatura = nAssignatura;
	}
	
	public void SetCoordinador(String coord) {
		Coordinador = coord;
	}
	
	public void SetAssigProfe1(String assigProfe1) {
		AssigProfe1 = assigProfe1;
	}
	
	public void SetAssigProfe2(String assigProfe2) {
		AssigProfe2 = assigProfe2;
	}
	
	public void SetAssigProfe3(String assigProfe3) {
		AssigProfe3 = assigProfe3;
	}
	
	public void SetNomProfe(String nomProfe) {
		NomProfe = nomProfe;
	}

	public void SetColor(int colorClase) {
		ColorClase = colorClase;
	}

	public void SetNumClase(int numClasse) {
		NumClasse = numClasse;
	}

	public void SetHoraInici(int horaInici) {
		HoraInici = horaInici;
	}

	public void SetMinInici(int minInici) {
		MinInici = minInici;
	}

	public void SetHoraFi(int horaFinal) {
		HoraFinal = horaFinal;
	}

	public void SetMinFi(int minFinal) {
		MinFinal = minFinal;
	}

	public void SetDia(String dia) {
		Dia = dia;
	}

	public void SetFoto(String foto) {
		Foto = foto;
	}

	public void SetCorreu(String correu) {
		Correu = correu;
	}

	public void SetWeb(String web) {
		Web = web;
	}

	public void SetTel(int tel) {
		Tel = tel;
	}

	public void SetDespatx(String despatx) {
		Despatx = despatx;
	}
	
	public void SetNCredits(int nCredits){
		NCredits=nCredits;
	}
	
	public void SetCompetencies (String competencies){
		Competencies=competencies;
	}
	
	public void SetNomMateria (String nomMateria){
		NomMateria = nomMateria;
	}
	public void SetNAssignatura2(int nAssignatura2) {
		NAssignatura2 = nAssignatura2;
	}
	public void SetHoraInici2(int horaInici2) {
		HoraInici2 = horaInici2;
	}

	public void SetMinInici2(int minInici2) {
		MinInici2 = minInici2;
	}

	public void SetHoraFi2(int horaFinal2) {
		HoraFinal2 = horaFinal2;
	}

	public void SetMinFi2(int minFinal2) {
		MinFinal2 = minFinal2;
	}


}