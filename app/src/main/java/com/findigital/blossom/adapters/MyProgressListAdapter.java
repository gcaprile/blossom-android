package com.findigital.blossom.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.fragments.MyCareerFragmentActivity;
import com.findigital.blossom.fragments.MyProgressFragmentActivity;
import com.findigital.blossom.helpers.API;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.MyCareer;
import com.findigital.blossom.models.SurveyResponse;
import com.findigital.blossom.models.User;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;
import com.raweng.twitter4j.internal.org.json.JSONArray;
import com.raweng.twitter4j.internal.org.json.JSONException;
import com.raweng.twitter4j.internal.org.json.JSONObject;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by 14-AB109LA on 9/1/2017.
 */

public class MyProgressListAdapter extends ArrayAdapter {

    Context context;
    List<BuiltObject> tasks;
    List<BuiltObject> userResponses;
    DbHelper dbHelper;
    MyProgressFragmentActivity parentActivity;

    public MyProgressListAdapter(Context context,
                                 MyProgressFragmentActivity parentActivity,
                                 List<BuiltObject> tasks,
                                 List<BuiltObject> userResponses) {
        super(context, android.R.layout.simple_list_item_1, tasks);
        this.context = context;
        this.tasks = tasks;
        this.parentActivity = parentActivity;
        this.userResponses = userResponses;

        dbHelper = new DbHelper(context);

    }

    private class ViewHolder {
        TextView txtTaskTitle;
        TextView txtTaskDescription;
        TextView txtTaskQuestion;
        LinearLayout llTaskLinks;
        EditText editTaskResponse;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final BuiltObject task = tasks.get(position);

        if (convertView == null) {
            holder = new MyProgressListAdapter.ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.tasks_list_item, null);
            holder.txtTaskTitle = (TextView) convertView.findViewById(R.id.txtTaskTitle);
            holder.txtTaskDescription = (TextView) convertView.findViewById(R.id.txtTaskDescription);
            holder.txtTaskQuestion = (TextView) convertView.findViewById(R.id.txtTaskQuestion);
            holder.llTaskLinks = (LinearLayout) convertView.findViewById(R.id.llTaskLinks);
            holder.editTaskResponse = (EditText) convertView.findViewById(R.id.editTaskResponse);
            convertView.setTag(holder);
        } else {
            holder = (MyProgressListAdapter.ViewHolder) convertView.getTag();
        }

        holder.txtTaskTitle.setText(task.get("task_name").toString());

        if (task.get("description") != null) {
            holder.txtTaskDescription.setVisibility(View.VISIBLE);
            holder.txtTaskDescription.setText(task.get("description").toString());
        } else if (!task.get("task_bullets").toString().isEmpty()) {
            String taskBullets = "";
            try {
                JSONArray jsonArray = new JSONArray(task.get("task_bullets").toString());
                String[] array = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    array[i] = jsonArray.getString(i);
                }
                for (int i = 0; i < array.length; i++) {
                    taskBullets += "-" + array[i] + "\r\n";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.txtTaskDescription.setVisibility(View.VISIBLE);
            holder.txtTaskDescription.setText(taskBullets);
        } else {
            holder.txtTaskDescription.setVisibility(GONE);
        }

        try {
            JSONArray jsonUrlArray = new JSONArray(task.get("task_url").toString());
            for (int i = 0; i < jsonUrlArray.length(); i++) {
                final JSONObject jsonObject = new JSONObject(jsonUrlArray.getString(i));
                TextView txtTaskLink = new TextView(context);
                txtTaskLink.setText(jsonObject.get("title").toString());
                txtTaskLink.setTextColor(Color.WHITE);
                txtTaskLink.setPaintFlags(txtTaskLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 10);
                txtTaskLink.setLayoutParams(params);

                final String url = jsonObject.get("href").toString();

                txtTaskLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse(url));
                        context.startActivity(intent);
                    }
                });

                holder.llTaskLinks.addView(txtTaskLink);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.editTaskResponse.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (v.getText().length() == 0) {
                        for (int i=0; i < userResponses.size(); i ++) {
                            BuiltObject response = userResponses.get(i);
                            if (response.get("task").toString().equals(task.getUid())) {
                                userResponses.remove(i);
                            }
                        }
                        updateUserResponses();
                    } else {
                        for (int i=0; i < userResponses.size(); i ++) {
                            BuiltObject response = userResponses.get(i);
                            if (response.get("task").toString().equals(task.getUid())) {
                                userResponses.remove(i);
                            }
                        }
                        addTaskResponse(v.getText().toString(), task.getUid());
                    }
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        String taskResponse = "";

        for (int i=0; i < userResponses.size(); i ++) {
            BuiltObject response = userResponses.get(i);
            if (response.get("task").toString().equals(task.getUid())) {
                taskResponse = response.get("user_response").toString();
            }
        }

        holder.editTaskResponse.setText(taskResponse);

        if (task.get("user_task_question") != null) {
            holder.txtTaskQuestion.setText(task.get("user_task_question").toString());
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public SurveyResponse getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void addTaskResponse(final String response, String taskId) {
        try {
            BuiltApplication builtApplication = Built.application(context, API.API_KEY);
            final BuiltObject taskObject = builtApplication.classWithUid("task_response").object();

            taskObject.set("task", taskId);
            taskObject.set("user_response", response);

            taskObject.save(new BuiltResultCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                    if (error == null) {
                        userResponses.add(taskObject);
                        updateUserResponses();
                    } else {
                        System.out.println(error);
                        Toast.makeText(context,
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserResponses() {
        try {
            User user = dbHelper.getUser();

            final String[] responses = new String[userResponses.size()];
            for (int i=0; i < userResponses.size(); i ++) {
                BuiltObject response = userResponses.get(i);
                responses[i] = response.getUid();
            }

            System.out.println(responses.length);

            BuiltApplication builtApplication = Built.application(context, API.API_KEY);
            final BuiltUser userObject  =  builtApplication.user(user.getId());

            if (responses.length > 0) {
                userObject.set("completed_responses", responses);
            } else {
                userObject.set("completed_responses", "");
            }

            userObject.updateUserInfo(new BuiltResultCallBack() {

                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                    if (error == null) {
                        parentActivity.updateProgressStatus(tasks.size(), responses.length);
                    } else {
                        Log.e("UPDATE USER RESPONSES", error.getErrorMessage());
                        Toast.makeText(context,
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                        // refer to the 'error' object for more details
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
