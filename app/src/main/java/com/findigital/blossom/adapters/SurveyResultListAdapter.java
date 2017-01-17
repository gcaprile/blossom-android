package com.findigital.blossom.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.fragments.CareerDetailFragment;
import com.findigital.blossom.fragments.LoginFragmentActivity;
import com.findigital.blossom.fragments.MyCareerFragmentActivity;
import com.findigital.blossom.fragments.SurveyFragmentActivity;
import com.findigital.blossom.helpers.API;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.Career;
import com.findigital.blossom.models.MyCareer;
import com.findigital.blossom.models.SurveyResponse;
import com.findigital.blossom.models.User;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;

import java.util.ArrayList;

/**
 * Created by 14-AB109LA on 9/1/2017.
 */

public class SurveyResultListAdapter extends ArrayAdapter {

    Context context;
    ArrayList<Career> careers;
    DbHelper dbHelper;
    FragmentActivity parentActivity;

    public SurveyResultListAdapter(Context context, FragmentActivity parentActivity, ArrayList<Career> careers) {
        super(context, android.R.layout.simple_list_item_1, careers);
        this.context = context;
        this.careers = careers;
        this.parentActivity = parentActivity;

        dbHelper = new DbHelper(context);
    }

    private class ViewHolder {
        TextView careerName;
        TextView careerIntro;
        Button btnCareerInfo;
        Button btnAddCareer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Career career = careers.get(position);

        if (convertView == null) {
            holder = new SurveyResultListAdapter.ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.survey_result_list_item, null);
            holder.careerName = (TextView) convertView.findViewById(R.id.txtCareerName);
            holder.careerIntro = (TextView) convertView.findViewById(R.id.txtCareerIntro);
            holder.btnCareerInfo = (Button) convertView.findViewById(R.id.btnCareerMoreInfo);
            holder.btnAddCareer = (Button) convertView.findViewById(R.id.btnAddCareer);
            convertView.setTag(holder);
        } else {
            holder = (SurveyResultListAdapter.ViewHolder) convertView.getTag();
        }

        holder.careerName.setText(career.getCareerName());
        holder.careerIntro.setText(career.getCareerIntro());

        holder.btnCareerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CareerDetailFragment.class);
                intent.putExtra("careerId", career.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.btnAddCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmation(
                        career.getId(),
                        career.getCareerName(),
                        career.getCareerColor(),
                        context);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return careers.size();
    }

    @Override
    public SurveyResponse getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void showConfirmation(final String careerId, final String careerName, final String careerColor, final Context context) {
        new AlertDialog.Builder(parentActivity)
                .setTitle(context.getString(R.string.my_careers))
                .setMessage("You've just added " + careerName + " as a desired career. Create an account to track your progress.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyCareer myCareer = new MyCareer(careerId, careerName, careerColor);
                        dbHelper.deleteMyCareer();
                        dbHelper.addMyCareer(myCareer);

                        User user = dbHelper.getUser();

                        System.out.println(dbHelper.getUser());

                        // Verify if user is logged in
                        if (user.getId() != null) {
                            // User is logged in, just update career path
                            updateUserCareerPath(user.getId(), context);
                        } else {
                            // User not logged in, proceed to login/signup view
                            Intent intent = new Intent(context, LoginFragmentActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("uiStyle", 2);
                            context.startActivity(intent);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing
                    }
                }).show();
    }

    private void updateUserCareerPath(String userUid, final Context context) {
        try {
            BuiltApplication builtApplication = Built.application(context, API.API_KEY);
            final BuiltUser userObject  =  builtApplication.user(userUid);

            MyCareer myCareer = dbHelper.getMyCareer();

            if (myCareer != null) {
                userObject.set("selected_career", myCareer.getId());
            }

            userObject.updateUserInfo(new BuiltResultCallBack() {

                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                    if (error == null) {
                        // user has logged in successfully
                        Intent intent = new Intent(context, MyCareerFragmentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        System.out.println(error);
                        Toast.makeText(context,
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                        // login failed
                        // refer to the 'error' object for more details
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
