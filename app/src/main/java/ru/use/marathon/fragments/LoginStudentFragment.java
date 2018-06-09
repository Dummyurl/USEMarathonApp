package ru.use.marathon.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.use.marathon.R;

/**
 * Created by ilyas on 09-Jun-18.
 */

public class LoginStudentFragment extends Fragment {

    public LoginStudentFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login_student, container, false);
    }
}
