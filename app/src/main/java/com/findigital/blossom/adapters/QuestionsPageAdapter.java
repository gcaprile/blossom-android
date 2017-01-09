package com.findigital.blossom.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.fragments.SurveyResultFragmentActivity;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.SurveyCareerResult;
import com.findigital.blossom.models.SurveyQuestion;
import com.findigital.blossom.models.SurveyResponse;
import com.findigital.blossom.models.SurveyResult;
import com.raweng.built.BuiltObject;
import com.raweng.okhttp.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class QuestionsPageAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    List<SurveyQuestion> questionsList;

    public QuestionsPageAdapter(Context context, List<SurveyQuestion> questionsList) {
        this.context = context;
        this.questionsList = questionsList;
    }

    @Override
    public int getCount() {
        return questionsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        TextView txtQuestionTitle;
        final Integer page = position;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.questionspager_item, container,
                false);

        txtQuestionTitle = (TextView) itemView.findViewById(R.id.txtQuestionTitle);
        txtQuestionTitle.setText(questionsList.get(position).getQuestion());

        // Get all responses from this question
        final DbHelper dbHelper = new DbHelper(context);
        final List<SurveyResponse> data = dbHelper.getAllResponses(questionsList.get(position).getID(), "");

        final ListView lvResponses = (ListView) itemView.findViewById(R.id.lvSurveyResponses);
        ResponsesListAdapter listAdapter = new ResponsesListAdapter(context, data);
        lvResponses.setAdapter(listAdapter);

        lvResponses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewPager viewPager = (ViewPager) container;
                viewPager.setCurrentItem(page + 1);

                //SurveyResult surveyResult = new SurveyResult();

                //String careers = data.get(position).getCareers();

                /*surveyResult.setResponse(data.get(position).getID());
                surveyResult.setQuestion(data.get(position).getQuestion());
                dbHelper.addResult(surveyResult);*/

                SurveyCareerResult careerResult = new SurveyCareerResult();
                careerResult.setCareer(data.get(position).getQuestion());
                careerResult.setPoints(data.get(position).getPoints());
                dbHelper.addCareerResult(careerResult);

                if ((page + 1) == questionsList.size()) {
                    // Reached the last question. Calculate results.
                    /*ArrayList<SurveyCareerResult> results = new ArrayList<SurveyCareerResult>();

                    for (SurveyResult result: dbHelper.getAllResults()) {
                        for (SurveyResponse response: dbHelper.getAllResponses("", result.getResponse())) {
                            // Cast list of careers to an array so it can be iterable
                            List<String> careersList = Arrays.asList(response.getCareers().split("\\s*,\\s*"));
                            // Iterate list of careers
                            for (String career: careersList) {
                                SurveyCareerResult careerResult = new SurveyCareerResult();
                                careerResult.setCareer(career);
                                careerResult.setPoints(response.getPoints());
                                //results.add(careerResult);
                                dbHelper.addCareerResult(careerResult);
                            }
                        }
                    }*/

                    /*List<SurveyCareerResult> data = dbHelper.getTopCareers();
                    for (SurveyCareerResult s: data) {
                        System.out.println(s.getCareer() + ": " + s.getPoints());
                    }*/

                    List<SurveyCareerResult> data = dbHelper.getTopCareers();
                    String totalPoints = "0";

                    for (SurveyCareerResult s: data) {
                        System.out.println(s.getCareer() + ": " + s.getPoints());
                        totalPoints = s.getPoints();
                    }

                    Intent intent = new Intent(context, SurveyResultFragmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("totalPoints", totalPoints);
                    context.startActivity(intent);
                }


                for (int i = 0; i < lvResponses.getChildCount(); i++) {
                    if(position == i ){
                        lvResponses.getChildAt(i).setBackgroundColor(Color.WHITE);
                        TextView option = (TextView) lvResponses.getChildAt(i);
                        option.setTextColor(Color.parseColor("#3B73B4"));
                    }else{
                        lvResponses.getChildAt(i).setBackgroundColor(ContextCompat.getColor(context, R.color.responseOption));
                        TextView option = (TextView) lvResponses.getChildAt(i);
                        option.setTextColor(Color.WHITE);
                    }
                }
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
