package com.example.wordmaster.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordmaster.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WordCardAdapter extends RecyclerView.Adapter<WordCardAdapter.ViewHolder> {
    private Context context;
    private List<String> wordList;

    public WordCardAdapter(Context context) {
        this.context=context;
        wordList=new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.word_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.word_card_word.setText(wordList.get(position));
    }

    public void setWordList(List<String> wordList){
        this.wordList=wordList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView word_card_word;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            word_card_word=itemView.findViewById(R.id.word_card_word);
        }
    }
}
