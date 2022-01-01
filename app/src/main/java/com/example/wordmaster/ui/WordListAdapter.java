package com.example.wordmaster.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordmaster.R;
import com.example.wordmaster.business.FrequentWordBus;
import com.example.wordmaster.model.FrequentWord;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {
    private Context context;
    private List<String> wordList;
    private FrequentWordBus frequentWordBus;

    public WordListAdapter(Context context) {
        this.context=context;
        wordList=new ArrayList<>();
        frequentWordBus=new FrequentWordBus(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.word_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if (position%2==0){
            holder.word_list_item_parent.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_white));
        }else {
            holder.word_list_item_parent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        holder.word_list_item_word.setText(wordList.get(position));
        holder.setOnClickListener(position);
    }

    public void setWordList(List<String> wordList){
        this.wordList=wordList;
        notifyDataSetChanged();
    }

    public void addWords(List<String> newWordList){
        int prevSize=this.wordList.size();
        this.wordList.addAll(newWordList);
        notifyItemRangeChanged(prevSize,newWordList.size());
    }


    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public String getLastWord(){
        return wordList.get(wordList.size()-1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout word_list_item_parent;
        private TextView word_list_item_word;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            word_list_item_parent=itemView.findViewById(R.id.word_list_item_parent);
            word_list_item_word=itemView.findViewById(R.id.word_list_item_word);
        }

        public void setOnClickListener(int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String word= wordList.get(position);
                    Intent intent=new Intent(context,DictionaryDefinition.class);
                    intent.putExtra("word",word);
                    context.startActivity(intent);
                }
            });
        }
    }


}
