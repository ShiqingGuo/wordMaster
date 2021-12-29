package com.example.wordmaster.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wordmaster.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private RelativeLayout home_parent;
    private RecyclerView recycler_word_card;
    public static WordCardAdapter wordCardAdapter;
    private SnapHelper snapHelper;
    private RecyclerView.LayoutManager layoutManager;
    private MaterialButton familiar_button,notsure_button,unfamiliar_button;
    private boolean btnClicked;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiate();
        setRecycler_word_card();
        setBtns();
    }

    private void initiate(){
        home_parent=getView().findViewById(R.id.home_parent);
        recycler_word_card=getView().findViewById(R.id.recycler_word_card);
        wordCardAdapter=new WordCardAdapter(getContext());
        familiar_button=getView().findViewById(R.id.familiar_button);
        notsure_button=getView().findViewById(R.id.notsure_button);
        unfamiliar_button=getView().findViewById(R.id.unfamiliar_button);
        btnClicked=false;
    }

    private void setRecycler_word_card(){
        List<String> wordList=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            wordList.add("word"+i);
        }
        wordCardAdapter.setWordList(wordList);
        recycler_word_card.setAdapter(wordCardAdapter);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recycler_word_card.setLayoutManager(layoutManager);
        snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recycler_word_card);
        recycler_word_card.addOnScrollListener(new OnSnapPositionChanged());
    }

    private void setBtns(){
        familiar_button.setOnClickListener(new OnClickFamiliarBtn());
        notsure_button.setOnClickListener(new OnClickFamiliarBtn());
        unfamiliar_button.setOnClickListener(new OnClickFamiliarBtn());
    }

    private int getCurrPos(){
        View view=snapHelper.findSnapView(layoutManager);
        return layoutManager.getPosition(view);
    }

    class OnSnapPositionChanged extends RecyclerView.OnScrollListener {
        private int prevPos;

        @Override
        public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState==RecyclerView.SCROLL_STATE_DRAGGING ){
                prevPos=getCurrPos();
            }else if (newState==RecyclerView.SCROLL_STATE_IDLE){
                clearBtnBackground();
                btnClicked=false;
            }else if(newState==RecyclerView.SCROLL_STATE_SETTLING){
                if (prevPos+1==getCurrPos()&&!btnClicked){
                    setBtnBackground(familiar_button);
                }
            }
        }

    }

    private void clearBtnBackground(){
        notsure_button.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        familiar_button.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        unfamiliar_button.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
    }

    private void setBtnBackground(MaterialButton button){
        ColorStateList strokeColor = button.getStrokeColor();
        button.setBackgroundTintList(strokeColor);
    }

    class OnClickFamiliarBtn implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            btnClicked=true;
            MaterialButton button=(MaterialButton) v;
            clearBtnBackground();
            setBtnBackground(button);
            int pos=getCurrPos();
            recycler_word_card.smoothScrollToPosition(pos+1);
//            switch (button.getId()){
//                case R.id.familiar_button:
//                    notsure_button.setBackgroundColor(Color.TRANSPARENT);
//                    unfamiliar_button.setBackgroundColor(Color.TRANSPARENT);
//                    break;
//                case R.id.notsure_button:
//                    familiar_button.setBackgroundColor(Color.TRANSPARENT);
//                    unfamiliar_button.setBackgroundColor(Color.TRANSPARENT);
//                    break;
//                case R.id.unfamiliar_button:
//                    notsure_button.setBackgroundColor(Color.TRANSPARENT);
//                    familiar_button.setBackgroundColor(Color.TRANSPARENT);
//                    break;
//            }
        }
    }

}