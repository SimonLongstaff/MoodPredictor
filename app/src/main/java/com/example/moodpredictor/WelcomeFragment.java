package com.example.moodpredictor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class WelcomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.welcome, container, false);
        final MainActivity activity = (MainActivity) getActivity();

        final EditText name = view.findViewById(R.id.wel_name_enter);
        Button enter = view.findViewById(R.id.wel_button);

        /**
         * Enters the new users into the database and moves to dashboard
         */
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User(name.getText().toString());

                if (user.getName().equals("")) {
                    new AlertDialog.Builder(activity)
                            .setTitle("Please Enter a name")
                            .setIcon(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
                            .setNegativeButton("Ok", null).show();
                } else {
                    activity.database.insertUser(user);
                    int newID = activity.database.getuID(user.getName());
                    activity.setLoggedInUser(newID);
                    activity.database.updateLoggedIn(newID);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                }
            }
        });

        return view;
    }


}
