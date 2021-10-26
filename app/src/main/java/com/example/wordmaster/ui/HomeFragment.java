package com.example.wordmaster.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wordmaster.R;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean tapped;
    private MaterialCardView curr_card,prev_card,next_card;
    private RelativeLayout home_parent;
    private View definition_page;


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
        setWord_card();
    }

    private void initiate(){
        
        home_parent=getView().findViewById(R.id.home_parent);
        LayoutInflater layoutInflater=getLayoutInflater();
        curr_card= (MaterialCardView) layoutInflater.inflate(R.layout.operation_instruction,home_parent,false);
        prev_card= (MaterialCardView) layoutInflater.inflate(R.layout.operation_instruction,home_parent,false);
        next_card= (MaterialCardView) layoutInflater.inflate(R.layout.operation_instruction,home_parent,false);
        home_parent.addView(curr_card);
        home_parent.addView(next_card);
        home_parent.addView(prev_card);
        curr_card.setId(R.id.curr_card);
        next_card.setId(R.id.next_card);
        prev_card.setId(R.id.prev_card);
        RelativeLayout.LayoutParams paramsNext = (RelativeLayout.LayoutParams) next_card.getLayoutParams();
        paramsNext.addRule(RelativeLayout.RIGHT_OF, curr_card.getId());
        next_card.setLayoutParams(paramsNext);
        RelativeLayout.LayoutParams paramsPrev = (RelativeLayout.LayoutParams) prev_card.getLayoutParams();
        paramsPrev.addRule(RelativeLayout.LEFT_OF, curr_card.getId());
        prev_card.setLayoutParams(paramsPrev);

        tapped=false;
        View view=layoutInflater.inflate(R.layout.word_definition,home_parent,false);
        definition_page=view.findViewById(R.id.word_definition_wrapper);
    }

    private void setWord_card(){
        curr_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tapped){
                    curr_card.removeAllViews();
                    curr_card.addView(definition_page);
                    tapped=true;
                }
            }
        });

        curr_card.setOnTouchListener(new View.OnTouchListener() {
            static final int MIN_DISTANCE = 150;
            float currXStart,currYStart,nextXStart,nextYStart;
            float eventXStart,eventYStart,eventXEnd;
            float translationX,translationY;

           @Override
           public boolean onTouch(View v, MotionEvent event) {

               switch(event.getAction())
               {
                   case MotionEvent.ACTION_DOWN:
                       currXStart=v.getX();
                       currYStart=v.getY();
                       nextXStart=next_card.getX();
                       nextYStart=next_card.getY();
                       eventXStart=event.getX();
                       eventYStart=event.getY();
                       break;
                   case MotionEvent.ACTION_MOVE:
                       translationX=event.getRawX()-eventXStart;
                       translationY=event.getRawY()-eventYStart;
                       curr_card.setX(currXStart+translationX);
                       next_card.setX(nextXStart+translationX);
                       break;
                   case MotionEvent.ACTION_UP:
                       eventXEnd = event.getX();
                       float eventDistance = eventXEnd - eventXStart;

                       if (Math.abs(eventDistance) > MIN_DISTANCE)
                       {
                           // Left to Right swipe action
                           if (eventDistance>0)
                           {
                           }
                           // Right to left swipe action
                           else
                           {
                           }

                       }

                       break;
               }
               return true;
           }
        });
    }
}