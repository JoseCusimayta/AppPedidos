package yiwo.apppedidos.ConexionBD;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.Data.BDArticulos;

public class BDDescargarImagenes {

    private String TAG = "BDDescargarImagenes";
    private int Puerto = 8080;

    private RedDisponible redDisponible = new RedDisponible();

    private String ip_lan = "192.168.1.111", ip_publica = "148.102.21.175", Carpeta = "Imagenes";

    public Boolean GuardarMenu() {

        String ip;
        Log.d(TAG, "Conecntandose a: " + ip_lan + " - " + redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_lan, Puerto));
        Log.d(TAG, "Conecntandose a: " + ip_publica + " - " + redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_publica, Puerto));
        if (redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_lan, Puerto))
            ip = ip_lan;
        else if (redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_publica, Puerto))
            ip = ip_publica;
        else
            return false;

        String DOWNLOAD_URL = "http://" + ip + ":" + Puerto + "/" + ip_lan + "/" + Carpeta + "/";
        Log.d(TAG, "Conectandose a :" + DOWNLOAD_URL);
//        if (LAN)
//            DOWNLOAD_URL = "http://192.168.1.111:8080/192.168.1.111/Imagenes/";//IP LAN ERP SOLUTIONS
//        else
//            DOWNLOAD_URL = "http://148.102.21.175:8080/192.168.1.111/Imagenes/";//IP Publica ERP SOLUTIONS

        GuardarMenu_Articulos(DOWNLOAD_URL);
        GuardarMenu_Familia(DOWNLOAD_URL);
        GuardarMenu_SubFamilia(DOWNLOAD_URL);
        GuardarMenu_Conceptos(DOWNLOAD_URL);

        BDArticulos bdArticulos= new BDArticulos();
        ArrayList<List<String>> arrayList= bdArticulos.getList("");
        for(int i=0;i<arrayList.size();i++){
            GuardarArticulos(DOWNLOAD_URL, arrayList.get(i).get(0));
        }
        return true;
    }

    private void GuardarMenu_Articulos(String DOWNLOAD_URL) {
        try {
            String Nombre_Imagen = CodigosGenerales.Codigo_Empresa + "_articulos.jpg";

            URL url = new URL(DOWNLOAD_URL + Nombre_Imagen);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(input);

            //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            File imgFile = new File(CodigosGenerales.myDirectorio, Nombre_Imagen);

            FileOutputStream out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d(TAG,"Descargando desde: "+url);
        } catch (Exception e) {
            Log.d(TAG, "GuardarMenu_Articulos - " + e.getMessage());
        }
    }

    private void GuardarMenu_Familia(String DOWNLOAD_URL) {
        try {
            String Nombre_Imagen = CodigosGenerales.Codigo_Empresa + "_familia.jpg";

            URL url = new URL(DOWNLOAD_URL + Nombre_Imagen);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(input);

            //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            File imgFile = new File(CodigosGenerales.myDirectorio, Nombre_Imagen);

            FileOutputStream out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            Log.d(TAG,"Descargando desde: "+url);

        } catch (Exception e) {
            Log.d(TAG, "GuardarMenu_Familia - " + e.getMessage());
        }
    }

    private void GuardarMenu_SubFamilia(String DOWNLOAD_URL) {
        try {
            String Nombre_Imagen = CodigosGenerales.Codigo_Empresa + "_subfamilia.jpg";

            URL url = new URL(DOWNLOAD_URL + Nombre_Imagen);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(input);

            //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            File imgFile = new File(CodigosGenerales.myDirectorio, Nombre_Imagen);

            FileOutputStream out = new FileOutputStream(imgFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();

            Log.d(TAG,"Descargando desde: "+url);
        } catch (Exception e) {
            Log.d(TAG, "GuardarMenu_SubFamilia - " + e.getMessage());
        }
    }

    private void GuardarMenu_Conceptos(String DOWNLOAD_URL) {
        try {
            for (int i = 1; i <= 7; i++) {
                String Nombre_Imagen = CodigosGenerales.Codigo_Empresa + "_concepto" + i + ".jpg";

                URL url = new URL(DOWNLOAD_URL + Nombre_Imagen);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(input);

                //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
                File imgFile = new File(CodigosGenerales.myDirectorio, Nombre_Imagen);

                FileOutputStream out = new FileOutputStream(imgFile);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                out.flush();

                out.close();
                Log.d(TAG,"Descargando desde: "+url);
            }
        } catch (Exception e) {
            Log.d(TAG, "GuardarMenu_Conceptos - " + e.getMessage());
        }
    }

    public void GuardarArticulos(String DOWNLOAD_URL, String Codigo_Articulo) {

        try {
            for (int i = 1; i <= 4; i++) {
                String Nombre_Imagen = CodigosGenerales.Codigo_Empresa + "_" + Codigo_Articulo + "_" + i + ".jpg";
                URL url = new URL(DOWNLOAD_URL + Nombre_Imagen);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(input);

                //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
                File imgFile = new File(CodigosGenerales.myDirectorio, Nombre_Imagen);

                FileOutputStream out = new FileOutputStream(imgFile);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                out.flush();

                out.close();
                Log.d(TAG,"Descargando desde: "+url);
            }

        } catch (Exception e) {
            Log.d(TAG, "GuardarArticulos -" + e.getMessage());
        }

    }


    public Boolean EsNecesarioActualizar() {
        try {

            String ip;
            Log.d(TAG, "Conecntandose a: " + ip_lan + " - " + redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_lan, Puerto));
            Log.d(TAG, "Conecntandose a: " + ip_publica + " - " + redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_publica, Puerto));
            if (redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_lan, Puerto))
                ip = ip_lan;
            else if (redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_publica, Puerto))
                ip = ip_publica;
            else
                return false;

            String DOWNLOAD_URL = "https://" + ip + ":" + Puerto + "/" + ip_lan + "/" + Carpeta + "/";
            Log.d(TAG, "Conectandose a :" + DOWNLOAD_URL);

            URL url = new URL(DOWNLOAD_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);

            connection.connect();

            int length = connection.getContentLength();
            Log.d("DetectarVariacion", "Peso Local " + CodigosGenerales.myDirectorio.length());
            Log.d("DetectarVariacion", "Peso Online " + length);
            if (length > CodigosGenerales.myDirectorio.length())
                return true;
        } catch (Exception e) {
            Log.d(TAG, "EsNecesarioActualizar: " + e.getMessage());
        }
        return false;
    }


    protected Bitmap DescargarImagenEnLinea(String imgurl) {

        try {
            URL url = new URL(imgurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int length = connection.getContentLength();
            InputStream is = (InputStream) url.getContent();
            byte[] imageData = new byte[length];
            int buffersize = (int) Math.ceil(length / (double) 100);
            int downloaded = 0;
            int read;
            while (downloaded < length) {
                if (length < buffersize) {
                    read = is.read(imageData, downloaded, length);
                } else if ((length - downloaded) <= buffersize) {
                    read = is.read(imageData, downloaded, length
                            - downloaded);
                } else {
                    read = is.read(imageData, downloaded, buffersize);
                }
                downloaded += read;
                //  publishProgress((downloaded * 100) / length);
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, length);
            if (bitmap != null) {
                System.out.println("Bitmap created");
            } else {
                System.out.println("Bitmap not created");
            }
            is.close();
            return bitmap;

        } catch (Exception e) {
            Log.d(TAG,"DescargarImagenEnLinea"+e.getMessage());
        }
        return null;
    }

    public Bitmap getImageFromDirectory(String Imagen_nombre){

        File imgFile;
        Imagen_nombre=CodigosGenerales.Codigo_Empresa+"_"+ Imagen_nombre;
        imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre);
        Log.d(TAG,"Cargando Imagen... "+Imagen_nombre);
        try {
            return BitmapFactory.decodeStream(new FileInputStream(imgFile));
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}