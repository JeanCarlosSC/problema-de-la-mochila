import java.util.ArrayList;

public class Algoritmo {
    static int[][] informacion; //datos procesados
    static ArrayList<Integer> items; //path o ruta de la cual sale cada valor
    static int[][] datosClass; //datos sin procesar
    private static int nClass; //cantidad de articulos
    private static int wClass; //peso maximo

    /**
     * Metodo solucion al problema de la mochila en el caso 0-1 con programacion dinamica
     * @param n numero de elementos
     * @param w peso maximo
     * @param datos matriz con datos ingresados por el usuario
     */
    public static void calcular(int n, int w, int[][] datos) {
        nClass = n;
        wClass = w;
        datosClass = datos;

        for (int i=1; i<=n; i++) {
            for (int j=0; j<=w; j++) {
                if (datos[i-1][0] > j)
                    informacion[i][j] = informacion[i-1][j];
                else
                    informacion[i][j] = Math.max(informacion[i-1][j], informacion[i-1][j-datos[i-1][0]] + datos[i-1][1]);
            }
        }
    }

    public static void hallarItems() {
        items = new ArrayList<>();
        int i=nClass;
        int k=wClass;

        while(i>0 && k>0) {
            if(informacion[i][k] != informacion[i-1][k]){
                items.add(i);
                i--;
                k-=datosClass[i][0];
            }else{
                i--;
            }
        }
    }

    public static void hallarItems(int n1, int k1) {
        items = new ArrayList<>();
        int i=n1;
        int k=k1;

        while(i>0 && k>0) {
            if(informacion[i][k] != informacion[i-1][k]){
                items.add(i);
                i--;
                k-=datosClass[i][0];
            }else{
                i--;
            }
        }
    }

}
