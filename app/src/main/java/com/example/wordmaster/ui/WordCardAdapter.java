package com.example.wordmaster.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordmaster.R;
import com.example.wordmaster.business.DictionaryBus;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WordCardAdapter extends RecyclerView.Adapter<WordCardAdapter.ViewHolder> {
    private Context context;
    private List<String> wordList;
    private DictionaryBus dictionaryBus;

    public WordCardAdapter(Context context) {
        this.context=context;
        wordList=new ArrayList<>();
        dictionaryBus=new DictionaryBus(context);
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
        holder.word_card_definition.setText(R.string.tap_instr);
        holder.setOnclickListener(position);
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
        private TextView word_card_definition;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            word_card_word=itemView.findViewById(R.id.word_card_word);
            word_card_definition=itemView.findViewById(R.id.word_card_definition);
        }

        public void setOnclickListener(int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String definition;
                    definition= dictionaryBus.getDefinitionByWord(wordList.get(position));
                    if (definition==null){
                        definition="sorry, no definition available.";
                    }
                    word_card_definition.setText(definition);
                }
            });
        }
    }

}
