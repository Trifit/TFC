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
import android.widget.TextView;
import android.widget.Toast;

public class Controlador_assig extends Activity {

	BaseDatosHelper miBBDDHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlador_assig);

		crearBBDD();
		miBBDDHelper.abrirBaseDatos();

		pintar();

		miBBDDHelper.close();
	}

	public void crearBBDD() {
		miBBDDHelper = new BaseDatosHelper(this);
		try {
			miBBDDHelper.crearDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
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
	public void onRestart() {
		super.onRestart();
		setContentView(R.layout.controlador_assig);

		crearBBDD();
		miBBDDHelper.abrirBaseDatos();

		pintar();

		miBBDDHelper.close();
	}

	public void pintar() { // pintem la pantalla
		TextView Assig = (TextView) findViewById(R.id.assig);
		TextView Coord = (TextView) findViewById(R.id.Coord);
		TextView AssigProfe1 = (TextView) findViewById(R.id.Profe1);
		TextView AssigProfe2 = (TextView) findViewById(R.id.Profe2);
		TextView AssigProfe3 = (TextView) findViewById(R.id.Profe3);
		TextView Credits = (TextView) findViewById(R.id.Credits);
		TextView Competencies = (TextView) findViewById(R.id.Competencies);
		TextView NomMateria = (TextView) findViewById(R.id.NomMateria);
		Button home = (Button) this.findViewById(R.id.boton_home);

		Assig.setText(miBBDDHelper.GetFitxaAssig().GetAssignatura());
		Coord.setText(miBBDDHelper.GetFitxaAssig().GetCoord());
		if (miBBDDHelper.GetFitxaAssig().GetAssigProfe1() != null) {
			AssigProfe1.setText(miBBDDHelper.GetFitxaAssig().GetAssigProfe1());
			AssigProfe1.setVisibility(TextView.VISIBLE);
		}
		if (miBBDDHelper.GetFitxaAssig().GetAssigProfe2() != null) {
			AssigProfe2.setText(miBBDDHelper.GetFitxaAssig().GetAssigProfe2());
			AssigProfe2.setVisibility(TextView.VISIBLE);
		}
		if (miBBDDHelper.GetFitxaAssig().GetAssigProfe3() != null) {
			AssigProfe3.setText(miBBDDHelper.GetFitxaAssig().GetAssigProfe3());
			AssigProfe3.setVisibility(TextView.VISIBLE);
		}
		Credits.setText("" + miBBDDHelper.GetFitxaAssig().GetNCredits());
		Competencies.setText(miBBDDHelper.GetFitxaAssig().GetCompetencies());
		NomMateria.setText(miBBDDHelper.GetFitxaAssig().GetNomMateria());

		home.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 0);
			}

		});
	}

	@Override
	// classe per millorar la cualitat del degradat de fons
	public void onAttachedToWindow() {
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