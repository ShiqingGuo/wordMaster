package com.example.wordmaster.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wordmaster.R;
import com.example.wordmaster.business.DictionaryBus;

public class DictionaryDefinition extends AppCompatActivity {
    private String word;
    private View dictionary_definition_container;
    private TextView word_card_word;
    private TextView word_card_definition;
    private DictionaryBus dictionaryBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_definition);

        initiate();
        setContent();
    }

    private void initiate(){
        Intent intent=getIntent();
        word=intent.getStringExtra("word");
        dictionary_definition_container=findViewById(R.id.dictionary_definition_container);
        word_card_word=dictionary_definition_container.findViewById(R.id.word_card_word);
        word_card_definition=dictionary_definition_container.findViewById(R.id.word_card_definition);
        dictionaryBus=new DictionaryBus(this);
    }

    private void setContent(){
        word_card_word.setText(word);
        String definition= dictionaryBus.getDefinitionByWord(word);
        word_card_definition.setText(definition);
    }
}