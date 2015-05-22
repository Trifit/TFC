package com.dvd.clase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Llista_classes extends ListActivity {
	BaseDatosHelper miBBDDHelper;

	private class ClasseAdapter extends ArrayAdapter<Info> {
		ArrayList<Info> items;

		public ClasseAdapter(Context context, int textViewResourceId,
				ArrayList<Info> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.llista_item, null);

			}

			Info classe = items.get(position);
			if (classe != null) {

				TextView tnClasse = (TextView) v.findViewById(R.id.item);

				if (tnClasse != null) {
					if (classe.GetColor() == 1) {
						tnClasse.setText("" + classe.GetNumClase() + "B");
					}

					if (classe.GetColor() == 2) {
						tnClasse.setText("" + classe.GetNumClase() + "G");
					}

					if (classe.GetColor() == 3) {
						tnClasse.setText("" + classe.GetNumClase() + "V");
					}
				}
			}
			return v;
		}
	}

	public void crearBBDD() {
		miBBDDHelper = new BaseDatosHelper(this);
		try {
			miBBDDHelper.crearDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.llista_classes);

		crearBBDD();

		ArrayList<Info> classes = getItems();
		setListAdapter(new ClasseAdapter(this, R.layout.llista_item, classes));

		ListView lv = getListView();
		lv.setClickable(true);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {

				TextView tv = (TextView) view.findViewById(R.id.item);
				String numClasse = "" + tv.getText(); // obtenemos el texto del
				// item
				numClasse = numClasse.substring(0, numClasse.length() - 1); // quitamos
				// el
				// ultimo
				// caracter

				/*
				 * Passem el valor numClasse (el numero de classe) a la
				 * activitat controlador_classe per poder accedir desde l'altre
				 * classe
				 */
				BaseDatosHelper.tClase.SetNumClase(Integer.parseInt(numClasse));

				Intent intent = new Intent(Llista_classes.this,
						Controlador_classe.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Llista_classes.this.startActivity(intent);

			}
		});

		Button home = (Button) this.findViewById(R.id.boton_home);
		home.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 0);

			}
		});
	}

	// llenamos un array con los valores obtenidos de la base de datos
	public ArrayList<Info> getItems() {
		miBBDDHelper.abrirBaseDatos();
		ArrayList<Info> listaClasses = miBBDDHelper.GetLlistaClasses();
		miBBDDHelper.close();
		return listaClasses;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		menu.findItem(R.id.opcio_menu1).setVisible(false);
		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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

	
	private void carregarActivitat(Class<?> activitat) {
		Intent intent = new Intent(this, activitat);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, 0);
	}
}
