package ledare.com.br.myfood.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import ledare.com.br.myfood.R;
import ledare.com.br.myfood.model.Prato;
import ledare.com.br.myfood.util.PratoOnClickListener;

public class PratoAdapter extends RecyclerView.Adapter<PratoAdapter.PratosViewHolder>{

    private final Context context;
    private final List<Prato> pratoList;
    private PratoOnClickListener pratoOnClickListener;

    public PratoAdapter(Context context, List<Prato> pratoList, PratoOnClickListener pratoOnClickListener){
        this.context = context;
        this.pratoList = pratoList;
        this.pratoOnClickListener = pratoOnClickListener;
    }

    @Override
    public PratosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_prato, viewGroup, false);
        PratosViewHolder holder = new PratosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PratosViewHolder holder, final int position) {
        Prato prato = pratoList.get(position);
        holder.txtCardNome.setText(prato.nome);
        holder.progressPrato.setVisibility(View.VISIBLE);
        //Faz o download da foto
        Glide.with(context)
                .load(prato.foto)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressPrato.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressPrato.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imgCardFoto);

        //Item selecionado
        if(pratoOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pratoOnClickListener.onClickPrato(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.pratoList != null ? this.pratoList.size() : 0;
    }

    public static class PratosViewHolder extends RecyclerView.ViewHolder{

        TextView txtCardNome;
        ImageView imgCardFoto;
        ProgressBar progressPrato;
        CardView cardView;

        public PratosViewHolder(View itemView) {
            super(itemView);
            txtCardNome = (TextView) itemView.findViewById(R.id.txtCardNome);
            imgCardFoto = (ImageView) itemView.findViewById(R.id.imgCardFoto);
            progressPrato = (ProgressBar) itemView.findViewById(R.id.progressPrato);
            cardView = (CardView) itemView.findViewById(R.id.cardPrato);
        }
    }
}
