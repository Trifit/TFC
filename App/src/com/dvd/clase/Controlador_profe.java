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
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Controlador_profe extends Activity {

	BaseDatosHelper miBBDDHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlador_profe);

		crearBBDD();
		miBBDDHelper.abrirBaseDatos();
		pintar(miBBDDHelper.GetFitxaProfe());
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
	protected void onResume() {

		super.onResume();

		crearBBDD();
		miBBDDHelper.abrirBaseDatos();

		Info GetFitxa = miBBDDHelper.GetFitxaProfe();
		pintar(GetFitxa);
		miBBDDHelper.close();
	}

	@Override
	protected void onRestart() {

		super.onRestart();

		crearBBDD();
		miBBDDHelper.abrirBaseDatos();

		Info GetFitxa = miBBDDHelper.GetFitxaProfe();
		pintar(GetFitxa);
		miBBDDHelper.close();
	}

	@Override
	protected void onPause() {

		super.onPause();

		crearBBDD();
		miBBDDHelper.abrirBaseDatos();

		Info GetFitxa = miBBDDHelper.GetFitxaProfe();
		pintar(GetFitxa);
		miBBDDHelper.close();
	}

	public void pintar(final Info fitxa) {
		TextView TNom = (TextView) findViewById(R.id.NomProfe);
		TextView TCorreu = (TextView) findViewById(R.id.correu);
		TextView TTel = (TextView) findViewById(R.id.tel);
		TextView TWeb = (TextView) findViewById(R.id.web);
		TextView TDespatx = (TextView) findViewById(R.id.despatx);
		TextView TAssignatura = (TextView) findViewById(R.id.assignatura);
		Button BCorreu = (Button) findViewById(R.id.boton_correu);
		Button BTel = (Button) findViewById(R.id.boton_trucar);
		Button BWeb = (Button) findViewById(R.id.boton_internet);
		Button home = (Button) this.findViewById(R.id.boton_home);

		TNom.setText(fitxa.GetNomProfe());

		if (fitxa.GetCorreu() == null)
			TCorreu.setText("No te correu.");
		else {
			TCorreu.setText(fitxa.GetCorreu());
			BCorreu.setVisibility(Button.VISIBLE);
			TCorreu.setPadding(0, 0, 0, 5);
		}

		if (fitxa.GetTel() == 0) {
			TTel.setText("No te telefon.");

		} else {
			TTel.setText("" + fitxa.GetTel());
			TTel.setPadding(0, 0, 0, 5);
			BTel.setVisibility(Button.VISIBLE);
		}

		if (fitxa.GetWeb() == null)
			TWeb.setText("No te web personal.");
		else {
			TWeb.setText(fitxa.GetWeb());
			TWeb.setPadding(0, 0, 0, 5);
			BWeb.setVisibility(Button.VISIBLE);
		}

		TDespatx.setText(fitxa.GetDespatx());
		
		TAssignatura.setText(fitxa.GetAssignatura());

		BCorreu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				final Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, fitxa.GetCorreu());
				startActivity(Intent.createChooser(emailIntent,
						"Siusplau, escull aplicació per enviar un correu..."));
			}
		});
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		BTel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				builder.setIcon(R.drawable.icon).setMessage(
						"Estas apunt de trucar al professor, estas segur?")
						.setPositiveButton("Si",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Intent phoneIntent = new Intent(
												Intent.ACTION_CALL);
										phoneIntent.setData(Uri.parse("tel:"
												+ fitxa.GetTel()));
										startActivity(phoneIntent);
									}
								}).setNegativeButton("No", null).show();

			}
		});

		BWeb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				final Intent webIntent = new Intent(Intent.ACTION_VIEW);
				webIntent.setData(Uri.parse(fitxa.GetWeb()));
				startActivity(webIntent);
			}
		});

		home.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 0);
			}

		});

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