package ledare.com.br.myfood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ledare.com.br.myfood.R;
import ledare.com.br.myfood.adapter.PratoAdapter;
import ledare.com.br.myfood.model.Prato;
import ledare.com.br.myfood.util.PratoOnClickListener;

public class PratoFragment extends Fragment {

    protected RecyclerView recyclerView;
    private List<Prato> pratoList;

    public static PratoFragment newInstance() {
        PratoFragment fragment = new PratoFragment();
        return fragment;
    }

    public PratoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pratoList = new ArrayList<>();
        for (int i=1; i<20 ; i++){
            Prato prato = new Prato();
            prato.id = String.valueOf(i);
            prato.nome = "Arroz com carne assada: " + i;
            prato.foto = "http://barrancas.com.br/wp-content/uploads/2016/07/bar1.jpg";
            pratoList.add(prato);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_pratos, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerPrato);

        if(getResources().getBoolean(R.bool.portrait)){
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(new PratoAdapter(getContext(), pratoList, onClickPrato()));
    }

    //Tratamento do evento do clique
    private PratoOnClickListener onClickPrato(){
        return new PratoOnClickListener() {
            @Override
            public void onClickPrato(View view, int position) {
                Prato prato = pratoList.get(position);
                Toast.makeText(getActivity(), "Prato: " + prato.nome, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
