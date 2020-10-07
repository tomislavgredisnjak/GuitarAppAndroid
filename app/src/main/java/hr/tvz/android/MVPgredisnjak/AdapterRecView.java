package hr.tvz.android.MVPgredisnjak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecView extends RecyclerView.Adapter<AdapterRecView.ViewHolder> {
    private List<Guitar> mDataset;
    private OnGuitarListener mOnGuitarListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextView;
        OnGuitarListener onGuitarListener;
        public ViewHolder(TextView v, OnGuitarListener onGuitarListener){
            super(v);
            mTextView = v;
            this.onGuitarListener = onGuitarListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGuitarListener.onGuitarClick(getAdapterPosition());
        }
    }


    public AdapterRecView(List<Guitar> myDataset, OnGuitarListener onGuitarListener) {
        mDataset = myDataset;
        this.mOnGuitarListener = onGuitarListener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_view, parent, false);

        return new ViewHolder((TextView)v, mOnGuitarListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mTextView.setText(mDataset.get(position).getName().replaceAll("_"," "));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnGuitarListener{
        void onGuitarClick(int position);
    }

}
