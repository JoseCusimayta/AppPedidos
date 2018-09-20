package yiwo.apppedidos.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragConfiguracion extends Fragment {


    public FragConfiguracion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_configuracion, container, false);
    }

}
