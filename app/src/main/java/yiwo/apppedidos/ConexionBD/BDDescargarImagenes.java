package yiwo.apppedidos.ConexionBD;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.Data.BDArticulos;

public class BDDescargarImagenes {

    private String TAG = "BDDescargarImagenes";

    public Boolean GuardarMenu() {

        String ip;

        if (ConfiguracionEmpresa.isLAN)
            ip = ConfiguracionEmpresa.IP_LAN;
        else
            ip = ConfiguracionEmpresa.IP_Publica;


        String DOWNLOAD_URL = "http://" + ip + ":" + ConfiguracionEmpresa.PuertoImagenes + "/" + ConfiguracionEmpresa.IP_LAN + "/" + ConfiguracionEmpresa.CarpetaImagenes + "/";
        Log.d(TAG, "Conectandose a :" + DOWNLOAD_URL);
//        if (LAN)
//            DOWNLOAD_URL = "http://192.168.1.111:8080/192.168.1.111/Imagenes/";//IP LAN ERP SOLUTIONS
//        else
//            DOWNLOAD_URL = "http://148.102.21.175:8080/192.168.1.111/Imagenes/";//IP Publica ERP SOLUTIONS

        GuardarMenu_Articulos(DOWNLOAD_URL);
        GuardarMenu_Familia(DOWNLOAD_URL);
        GuardarMenu_SubFamilia(DOWNLOAD_URL);
        GuardarMenu_Conceptos(DOWNLOAD_URL);

        BDArticulos bdArticulos = new BDArticulos();
        ArrayList<List<String>> arrayList = bdArticulos.getListFull();
        for (int i = 0; i < arrayList.size(); i++) {
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
            Log.d(TAG, "Descargando desde: " + url);
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
            Log.d(TAG, "Descargando desde: " + url);

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

            Log.d(TAG, "Descargando desde: " + url);
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
                Log.d(TAG, "Descargando desde: " + url);
            }
        } catch (Exception e) {
            Log.d(TAG, "GuardarMenu_Conceptos - " + e.getMessage());
        }
    }

    private void GuardarArticulos(String DOWNLOAD_URL, String Codigo_Articulo) {
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
                Log.d(TAG, "Descargando desde: " + url);
            }

        } catch (Exception e) {
            Log.d(TAG, "GuardarArticulos -" + e.getMessage());
        }

    }


    public Boolean EsNecesarioActualizar() {
        try {

            String ip="";

            if (ConfiguracionEmpresa.isLAN && ConfiguracionEmpresa.isLANAviable)
                ip = ConfiguracionEmpresa.IP_LAN;
            else if (!ConfiguracionEmpresa.isLAN && ConfiguracionEmpresa.isPublicaAviable)
                ip = ConfiguracionEmpresa.IP_Publica;
            else
                return false;

            String DOWNLOAD_URL = "http://" + ip + ":" + ConfiguracionEmpresa.PuertoImagenes + "/" + ConfiguracionEmpresa.IP_LAN + "/" + ConfiguracionEmpresa.CarpetaImagenes + "/";
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
            Log.d(TAG, "DescargarImagenEnLinea" + e.getMessage());
        }
        return null;
    }

    public Bitmap getImageFromDirectory(String Imagen_nombre) {
        Log.d(TAG, "Descargando Imagen: ");

        Bitmap bitmap;
        try {
            Log.d(TAG, "Descargando Imagen: " + Imagen_nombre);
            File imgFile;
            Imagen_nombre = CodigosGenerales.Codigo_Empresa + "_" + Imagen_nombre;
            Log.d(TAG, "Descargando Imagen: " + Imagen_nombre);
            imgFile = new File(CodigosGenerales.myDirectorio, Imagen_nombre);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(imgFile));
        } catch (FileNotFoundException e) {
            return null;
        }
        return bitmap;
    }
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }
}