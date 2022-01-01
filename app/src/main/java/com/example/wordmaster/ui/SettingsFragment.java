package com.example.wordmaster.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wordmaster.R;
import com.example.wordmaster.business.LearningWordBus;
import com.example.wordmaster.business.UserBus;
import com.example.wordmaster.business.UserInfoBus;
import com.example.wordmaster.model.LearningWord;
import com.example.wordmaster.model.UserInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private MaterialButton btn_logout;
    private TextView settings_user_id;
    private UserBus userBus;
    private UserInfoBus userInfoBus;
    private TextInputEditText review_word_num;
    private TextInputEditText new_word_num;
    private LearningWordBus learningWordBus;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiate();
        setSettings_user_id();
        setBtn_logout();
        setLearningGoal();
    }

    private void initiate(){
        btn_logout=getActivity().findViewById(R.id.btn_logout);
        settings_user_id=getActivity().findViewById(R.id.settings_user_id);
        userBus=new UserBus(getContext());
        review_word_num=getActivity().findViewById(R.id.review_word_num);
        new_word_num=getActivity().findViewById(R.id.new_word_num);
        userInfoBus=new UserInfoBus(getContext());
        learningWordBus=new LearningWordBus(getContext());
    }

    private void setSettings_user_id(){
        settings_user_id.setText(userBus.getActiveUser().getUserID());
    }

    private void setBtn_logout(){
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBus.updateActiveUser(null);
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLearningGoal(){
        UserInfo userInfo;
        userInfo=userInfoBus.getUserInfo(userBus.getActiveUser().getUserID());
        review_word_num.setText(String.valueOf(userInfo.getReviewWordNum()) );
        new_word_num.setText(String.valueOf(userInfo.getNewWordNum()) );
        review_word_num.setOnFocusChangeListener(new GoalChange(userInfo));
        new_word_num.setOnFocusChangeListener(new GoalChange(userInfo));
    }

    class GoalChange implements View.OnFocusChangeListener{
        private UserInfo userInfo;

        public GoalChange(UserInfo userInfo){
            this.userInfo=userInfo;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                TextInputEditText textInputEditText;
                int num;

                textInputEditText=(TextInputEditText)v;
                num= Integer.parseInt(textInputEditText.getText().toString());
                switch (v.getId()){
                    case R.id.review_word_num:
                        learningWordBus.updateLearningGoal(userInfo.getUserID(),num,
                                userInfoBus.getUserInfo(userBus.getActiveUser().getUserID()).getNewWordNum());
                        break;
                    case R.id.new_word_num:
                        learningWordBus.updateLearningGoal(userInfo.getUserID(),
                                userInfoBus.getUserInfo(userBus.getActiveUser().getUserID()).getReviewWordNum(),num);
                        break;
                }
                List<LearningWord> learningWordList;
                List<String> wordList;

                wordList=new ArrayList<>();
                learningWordList=learningWordBus.getLearningWordByUser(userInfo.getUserID());
                for (int i = 0; i < learningWordList.size(); i++) {
                    wordList.add(learningWordList.get(i).getWord());
                }
                HomeFragment.wordCardAdapter.setWordList(wordList);
            }
        }
    }
}