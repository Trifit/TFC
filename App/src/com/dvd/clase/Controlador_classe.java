package com.dvd.clase;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Controlador_classe extends Activity {

	BaseDatosHelper miBBDDHelper;
	private int NumClasse = 100;
	private int Hora = 10;
	private int Min = 0;
	private int Dia = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlador_classe);

		crearBBDD();
		miBBDDHelper.abrirBaseDatos();
		String Dia_text = miBBDDHelper.dia_actual(Dia);
		Info GetClasse = miBBDDHelper.GetFitxaClasse(NumClasse, Hora, Min, Dia);

		int Nassig = GetClasse.GetNAssignatura();

		Info GetAssignatura = miBBDDHelper.GetAssignatura(Nassig);
		Info GetProfe = miBBDDHelper.GetProfe(Nassig);
		Info Getcolor = miBBDDHelper.GetColor(NumClasse);

		// Cridem la classe GetNClasse amb els valors de sortida de getclasse
		// per obtenir la seguent clase
		Info GetNClasse = miBBDDHelper.GetNClasse(GetClasse.GetNumClase(),
				GetClasse.GetHoraFi(), GetClasse.GetMinFi(), Dia);
		Info GetNAssignatura = miBBDDHelper.GetAssignatura(GetNClasse
				.GetNAssignatura2());
		Info GetNProfe = miBBDDHelper.GetProfe(GetNClasse.GetNAssignatura2());

		TextView TAssigAra = (TextView) findViewById(R.id.assignatura_actual);
		TextView THoraAra = (TextView) findViewById(R.id.hora_actual);
		TextView TProfeAra = (TextView) findViewById(R.id.Profe_actual);
		TextView TAssigSeg = (TextView) findViewById(R.id.assignatura_futura);
		TextView TProfeSeg = (TextView) findViewById(R.id.Profe_futura);
		TextView THoraSeg = (TextView) findViewById(R.id.hora_futura);
		String profeactual=GetProfe.GetNomProfe();
		if (profeactual=="")
		{
			TAssigAra.setText("No hi ha cap assignatura ara mateix");
			THoraAra.setText("");
			TProfeAra.setText("");
	
			TAssigSeg.setText("");
			THoraSeg
					.setText("");
			TProfeSeg.setText("");
		}
		else
		{
			TAssigAra.setText(GetAssignatura.GetAssignatura());
			THoraAra.setText("(" + Dia_text + ": " + GetClasse.GetHoraInici() + ":"
					+ GetClasse.GetMinInici() + "0 - " + GetClasse.GetHoraFi()
					+ ":" + GetClasse.GetMinFi() + "0)");
			TProfeAra.setText(GetProfe.GetNomProfe());
	
			TAssigSeg.setText(GetNAssignatura.GetAssignatura());
			THoraSeg
					.setText("(" + Dia_text + ": " + GetNClasse.GetHoraInici2()
							+ ":" + GetNClasse.GetMinInici2() + "0 - "
							+ GetNClasse.GetHoraFi2() + ":"
							+ GetNClasse.GetMinFi2() + "0)");
			TProfeSeg.setText(GetNProfe.GetNomProfe());
		}
		CanviarColor(GetClasse.GetNumClase(), Getcolor.GetColor());
		miBBDDHelper.close();
		Button home = (Button) this.findViewById(R.id.boton_home);
		home.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 0);

			}

		});
	}

	public void crearBBDD() {
		miBBDDHelper = new BaseDatosHelper(this);
		try {
			miBBDDHelper.crearDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
	}

	public void CanviarColor(int classe, int color) {
		// Classe para pintar el numero de classe y la letra del color adecuado

		FrameLayout mScreen = (FrameLayout) findViewById(R.id.ClasseLayout);
		TextView TClasse = (TextView) findViewById(R.id.Classe);

		if (color == 1) {
			mScreen.setBackgroundColor(getResources().getColor(R.color.Blau));
			TClasse.setTextColor(getResources().getColor(R.color.Blau_Clar));
			TClasse.setText("" + classe + "B");
		}

		if (color == 2) {
			mScreen.setBackgroundColor(getResources().getColor(R.color.Groc));
			TClasse.setTextColor(getResources().getColor(R.color.Groc_Clar));
			TClasse.setText("" + classe + "G");
		}

		if (color == 3) {
			mScreen
					.setBackgroundColor(getResources()
							.getColor(R.color.Vermell));
			TClasse.setTextColor(getResources().getColor(R.color.Vermell_Clar));
			TClasse.setText("" + classe + "V");
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.menu, menu);

		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.opcio_menu1:
			carregarActivitat(Llista_classes.class);
			break;
		case R.id.opcio_menu2:
			carregarActivitat(Llista_assig.class);
			break;
		case R.id.opcio_menu3:
			carregarActivitat(Llista_profes.class);
			break;
		case R.id.opcio_menu4:
			scan();
			break;
		case R.id.opcio_menu5:
			openOptionsDialog();
			break;
		}
		return true;
	}

	private void openOptionsDialog() {
		new AlertDialog.Builder(this).setTitle(R.string.Sobre).setMessage(
				R.string.Sobre_missatge).setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();
	}

	@Override
	public void onAttachedToWindow() { // classe per millorar la cualitat del
										// degradat de fons
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	private void carregarActivitat(Class<?> activitat) {
		Intent intent = new Intent(this, activitat);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, 0);
	}

	private void scan() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (list.size() > 0) { // l'aplicació esta instalada?

			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 1);
		} else { // no?
			Toast
					.makeText(
							this,
							"L'aplicació 'QR Droid' ha de estar instalada, per usar aquesta funcionalitat",

							Toast.LENGTH_LONG).show();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

			BaseDatosHelper.tClase.SetNumClase(Integer.parseInt(data
					.getStringExtra("SCAN_RESULT")));

			carregarActivitat(Controlador_classe.class);

		}

	}

	
}