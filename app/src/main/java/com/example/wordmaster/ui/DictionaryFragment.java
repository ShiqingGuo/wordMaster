package com.example.wordmaster.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.wordmaster.R;
import com.example.wordmaster.business.FrequentWordBus;
import com.example.wordmaster.database.Database;
import com.example.wordmaster.model.FrequentWord;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DictionaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DictionaryFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private RecyclerView rec_word_list;
    private FrequentWordBus frequentWordBus;
    private SearchView search_bar;
    WordListAdapter wordListAdapter;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DictionaryFragment.
     */

    public static DictionaryFragment newInstance(String param1, String param2) {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiate();
        setRec_word_list();
        setSearch_bar();
    }

    private void initiate(){
        rec_word_list=getView().findViewById(R.id.rec_word_list);
        frequentWordBus=new FrequentWordBus(getContext());
        search_bar=getActivity().findViewById(R.id.search_bar);
        wordListAdapter=new WordListAdapter(getActivity());
    }

    private List<String> getWordBatch(String lastWord){
        List<String> wordList;
        List<FrequentWord> frequentWordList;
        frequentWordList=frequentWordBus.getNextBatchFrequentWords(lastWord);
        wordList=new ArrayList<>();
        for (int i = 0; i < frequentWordList.size(); i++) {
            wordList.add(frequentWordList.get(i).getWord());
        }
        return wordList;
    }

    private void setRec_word_list(){
        rec_word_list.setAdapter(wordListAdapter);
        wordListAdapter.setWordList(getWordBatch(null));
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        rec_word_list.setLayoutManager(layoutManager);
        rec_word_list.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                wordListAdapter.addWords(getWordBatch(wordListAdapter.getLastWord()));
            }
        });
    }

    private void setSearch_bar(){
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<String> wordList;
                List<FrequentWord> frequentWordList;
                wordList=new ArrayList<>();

                frequentWordList=frequentWordBus.implicitSearch(newText);
                for (int i = 0; i < frequentWordList.size(); i++) {
                    wordList.add(frequentWordList.get(i).getWord());
                }
                wordListAdapter.setWordList(wordList);
                return true;
            }
        });
        search_bar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    wordListAdapter.setWordList(getWordBatch(null));
                }
            }
        });
    }
}