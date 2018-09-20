package yiwo.apppedidos.Fragment;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.ConexionBD.BDDescargarImagenes;
import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.Data.BDArticulos;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LateralActualizar extends Fragment {
    BDDescargarImagenes bdDescargarImagenes= new BDDescargarImagenes();
    BDArticulos bdArticulos = new BDArticulos();
    Button b_actualizar, b_actualizar_lan, b_cancelar;
    ProgressBar pb_loading;
    ArrayList<List<String>> articulos;
    Integer sizeList = 0, progress = 0, descargados = 0;
    TextView tv_mensaje;

    BackGroundTask task;
    Boolean Online = true, descarga_exitosa = false;

    public LateralActualizar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_lateral_actualizar, container, false);
        tv_mensaje = view.findViewById(R.id.tv_mensaje);
        b_actualizar = view.findViewById(R.id.b_actualizar);
        b_actualizar_lan = view.findViewById(R.id.b_actualizar_lan);
        b_cancelar = view.findViewById(R.id.b_cancelar);
        pb_loading = view.findViewById(R.id.pb_loading);

        b_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                try {
                    BackGroundTask2 task2 = new BackGroundTask2();
                    task2.execute("");
//                    Online = true;
//                    task = new BackGroundTask();
//                    task.execute("");
                } catch (Exception e) {
                    Log.d("CreateView", "Task " + e.getMessage());
                }
            }
        });

        b_actualizar_lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Online = false;
                    task = new BackGroundTask();
                    task.execute("");
                } catch (Exception e) {
                    Log.d("CreateView", "Task " + e.getMessage());
                }
            }
        });

        b_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    task.cancel(true);
                    task.onPostExecute("");
                    pb_loading.setVisibility(View.GONE);
                    b_cancelar.setVisibility(View.GONE);
                    tv_mensaje.setVisibility(View.GONE);
                    b_actualizar.setVisibility(View.VISIBLE);
                    b_actualizar.setEnabled(true);
                    b_actualizar_lan.setEnabled(true);
                } catch (Exception e) {
                    Log.d("CreateView", "Task " + e.getMessage());
                }
            }
        });

        return view;
    }


    public class BackGroundTask extends AsyncTask<String, String, String> {
        int level = 0;

        @Override
        protected void onPreExecute() {
            pb_loading.setVisibility(View.VISIBLE);

            CodigosGenerales.CancelarTask = false;
            pb_loading.setProgress(0);
            b_actualizar.setEnabled(false);
            b_actualizar_lan.setEnabled(false);
            pb_loading.setVisibility(View.VISIBLE);
            b_cancelar.setVisibility(View.VISIBLE);
            tv_mensaje.setVisibility(View.VISIBLE);
            tv_mensaje.setText("Descargando...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (Online) {
                    //region Descargar Imagenes Online
                    articulos = bdArticulos.getList("");
                    sizeList = articulos.size();
                    progress = (sizeList * 4) + 9;
                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_articulos");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_familia");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_subfamilia");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_concepto1");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_concepto2");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_concepto3");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_concepto4");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_concepto5");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_concepto6");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_concepto7");
                    descargados++;

                    for (int i = 0; i < sizeList; i++) {
                        String Codigo = articulos.get(i).get(0);
                        GuardarImagenes(CodigosGenerales.DOWNLOAD_URL, CodigosGenerales.Codigo_Empresa + "_" + Codigo);
                        level = (int) Math.ceil((i / 1000.0) * 13);
//                        Log.d("DoInBackground", i + " Progreso: " + sizeList + " prueba: " + (i / sizeList) + " level " + level);
                        descargados++;
                        pb_loading.setProgress(level);
                        if (CodigosGenerales.CancelarTask)
                            cancel(true);
                        if (isCancelled())
                            break;
                    }

                    //endregion+

                } else {
                    //region Descargar Imagenes OffLine
                    articulos = bdArticulos.getListLan("");
                    sizeList = articulos.size();
                    progress = (sizeList * 4) + 9;
                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_articulos");
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_familia");
                    pb_loading.setProgress(1 / sizeList);
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_subfamilia");
                    pb_loading.setProgress(2 / sizeList);
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_concepto1");
                    pb_loading.setProgress(3 / sizeList);
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_concepto2");
                    pb_loading.setProgress(4 / sizeList);
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_concepto3");
                    pb_loading.setProgress(5 / sizeList);
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_concepto4");
                    pb_loading.setProgress(6 / sizeList);
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_concepto5");
                    pb_loading.setProgress(7 / sizeList);
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_concepto6");
                    pb_loading.setProgress(8 / sizeList);
                    descargados++;

                    GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_concepto7");
                    pb_loading.setProgress(9 / sizeList);
                    descargados++;

                    for (int i = 0; i < sizeList; i++) {
                        String Codigo = articulos.get(i).get(0);
                        GuardarImagenes(CodigosGenerales.DOWNLOAD_URL_Lan, CodigosGenerales.Codigo_Empresa + "_" + Codigo);
                        level = (int) Math.ceil((i / 1000.0) * 13);
//                        Log.d("DoInBackground", i + " Progreso: " + sizeList + " prueba: " + (i / sizeList) + " level " + level);
                        descargados++;
                        pb_loading.setProgress(level);
                        descarga_exitosa = true;
                        if (CodigosGenerales.CancelarTask)
                            cancel(true);
                        if (isCancelled())
                            break;
                    }

                }
                //endregion
            } catch (Exception e) {
                Log.d("FragList", "BackGroundTask: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (descarga_exitosa) {
                Toast.makeText(getContext(), "Actualización finalizada", Toast.LENGTH_SHORT).show();
                tv_mensaje.setText("Finzalizado.");
                pb_loading.setProgress(100);
            } else {
                Toast.makeText(getContext(), "Actualización fallida", Toast.LENGTH_SHORT).show();
                tv_mensaje.setText("Hubo un problema en la actualización.");
            }
            b_actualizar.setEnabled(true);
            b_actualizar_lan.setEnabled(true);

            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tv_mensaje.setText("Descargando " + level + "%...");
            pb_loading.setProgress(level);
            super.onProgressUpdate(values);
        }
    }


    private void SaveImageNormal(String _url, String Imagen_nombre) {
        HttpURLConnection connection;
        InputStream input;
        FileOutputStream out;

        Bitmap bitmap;
        File imgFile;
        URL url;

        try {
            //region Imagen 1
            url = new URL(CodigosGenerales.DOWNLOAD_URL + Imagen_nombre + ".jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + ".jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + ".jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);

            //endregion

        } catch (Exception e) {
//            Log.d("Progreso",pb_loading.getProgress()+" de "+sizeList+" de "+progress);
//            Log.d("SaveImage", e.getMessage());
        }
    }


    private void GuardarImagenes(String _url, String Imagen_nombre) {
        HttpURLConnection connection;
        InputStream input;
        FileOutputStream out;

        Bitmap bitmap;
        File imgFile;
        URL url;

        try {
            //region Imagen 1
            url = new URL(_url + Imagen_nombre + "_1.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_1.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);

            //endregion

            //region Imagen 2
            url = new URL(_url + Imagen_nombre + "_2.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_2.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_2.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);
            //endregion


            //region Imagen 3
            url = new URL(_url + Imagen_nombre + "_3.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_3.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_3.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);

            //endregion


            //region Imagen 4
            url = new URL(_url + Imagen_nombre + "_4.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_4.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_4.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);

            //endregion
        } catch (Exception e) {
//            Log.d("Progreso",pb_loading.getProgress()+" de "+sizeList+" de "+progress);
//            Log.d("SaveImage", e.getMessage());
        }
    }


    private void SaveImage(String Imagen_nombre) {
        HttpURLConnection connection;
        InputStream input;
        FileOutputStream out;

        Bitmap bitmap;
        File imgFile;
        URL url;

        try {
            //region Imagen 1
            url = new URL(CodigosGenerales.DOWNLOAD_URL + Imagen_nombre + "_1.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_1.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);

            //endregion

            //region Imagen 2
            url = new URL(CodigosGenerales.DOWNLOAD_URL + Imagen_nombre + "_2.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_2.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_2.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);
            //endregion


            //region Imagen 3
            url = new URL(CodigosGenerales.DOWNLOAD_URL + Imagen_nombre + "_3.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_3.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_3.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);

            //endregion


            //region Imagen 4
            url = new URL(CodigosGenerales.DOWNLOAD_URL + Imagen_nombre + "_4.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_4.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_4.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);

            //endregion
        } catch (Exception e) {
//            Log.d("Progreso",pb_loading.getProgress()+" de "+sizeList+" de "+progress);
//            Log.d("SaveImage", e.getMessage());
        }
    }


    private void SaveImageLan(String Imagen_nombre) {
        HttpURLConnection connection;
        InputStream input;
        FileOutputStream out;

        Bitmap bitmap;
        File imgFile;
        URL url;

        try {
            //region Imagen 1
            url = new URL(CodigosGenerales.DOWNLOAD_URL_Lan + Imagen_nombre + "_1.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d("ImagenDescargada", Imagen_nombre + "_2.jpg - " + pb_loading.getProgress() + " de " + sizeList + " de " + progress);

            //endregion

            //region Imagen 2
            url = new URL(CodigosGenerales.DOWNLOAD_URL_Lan + Imagen_nombre + "_2.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_2.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            //endregion


            //region Imagen 3
            url = new URL(CodigosGenerales.DOWNLOAD_URL_Lan + Imagen_nombre + "_3.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_3.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            //endregion


            //region Imagen 4
            url = new URL(CodigosGenerales.DOWNLOAD_URL_Lan + Imagen_nombre + "_4.jpg");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            CodigosGenerales.myDirectorio.mkdir();

            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre + "_4.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"

            if (imgFile.exists())
                imgFile.delete(); //Para reemplazar la imagen

            out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            //endregion
        } catch (Exception e) {
//            Log.d("Progreso",pb_loading.getProgress()+" de "+sizeList+" de "+progress);
//            Log.d("SaveImage", e.getMessage());
        }
    }












    public class BackGroundTask2 extends AsyncTask<String, String, String> {
        int level = 0;
Boolean exito;
BDDescargarImagenes bdDescargarImagenes= new BDDescargarImagenes();
        @Override
        protected void onPreExecute() {
            pb_loading.setVisibility(View.VISIBLE);

            CodigosGenerales.CancelarTask = false;
            pb_loading.setProgress(0);
            b_actualizar.setEnabled(false);
            b_actualizar_lan.setEnabled(false);
            pb_loading.setVisibility(View.VISIBLE);
            b_cancelar.setVisibility(View.VISIBLE);
            tv_mensaje.setVisibility(View.VISIBLE);
            tv_mensaje.setText("Descargando...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                exito=bdDescargarImagenes.GuardarMenu();
            } catch (Exception e) {
                Log.d("FragList", "BackGroundTask: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (exito) {
                Toast.makeText(getContext(), "Actualización finalizada", Toast.LENGTH_SHORT).show();
                tv_mensaje.setText("Finzalizado.");
                pb_loading.setProgress(100);
            } else {
                Toast.makeText(getContext(), "Actualización fallida", Toast.LENGTH_SHORT).show();
                tv_mensaje.setText("Hubo un problema en la actualización.");
                pb_loading.setProgress(0);
            }
            b_actualizar.setEnabled(true);
            b_actualizar_lan.setEnabled(true);

            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tv_mensaje.setText("Descargando " + level + "%...");
            pb_loading.setProgress(level);
            super.onProgressUpdate(values);
        }
    }


}