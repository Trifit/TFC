package com.dvd.clase;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

public class main extends Activity {
	public Info classe = new Info();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button classes = (Button) this.findViewById(R.id.buscar_classe);
		classes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				carregarActivitat(Llista_classes.class);
			}

		});

		Button assignatura = (Button) this
				.findViewById(R.id.buscar_assignatura);
		assignatura.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				carregarActivitat(Llista_assig.class);

			}

		});

		Button profe = (Button) this.findViewById(R.id.buscar_profe);
		profe.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				carregarActivitat(Llista_profes.class);

			}

		});

		Button scanner = (Button) this.findViewById(R.id.scan_barcode);
		scanner.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				scan();
			}

		});
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

	public boolean onCreateOptionsMenu(Menu menu) { // creem el menu inferior

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.menu, menu);

		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) { // donem funcionalitat
		// als botons del
		// menu

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
				R.string.Sobre_missatge).setIcon(R.drawable.icono_sobre).setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();
	}
	private void carregarActivitat(Class<?> activitat) {
		Intent intent = new Intent(this, activitat);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, 0);
	}

	private void scan() {
		ProgressDialog pd = ProgressDialog.show(main.this, "", "Siusplau, espera mentres es carrega...", true,
                false);

        Thread thread = new Thread();
        thread.start();		
		
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (list.size() > 0) { // l'aplicació esta instalada?
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 1);
			pd.dismiss();
			
		} else { // no?
			pd.dismiss();
			Toast
					.makeText(
							main.this,
							"L'aplicació 'QR Droid' ha de estar instalada, per usar aquesta funcionalitat",

							Toast.LENGTH_LONG).show();
			
		}
	}

	@Override
	public void onAttachedToWindow() { // classe per millorar la qualitat del
		// degradat de fons
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}
