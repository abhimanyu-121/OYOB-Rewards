package com.oyob.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oyob.controller.R;


/**
 * Created by Narender Kumar on 2/26/2018.
 * For Prominent, Faridabad (India)
 * narender.kumar.nishad@gmail.com
 */

public class FragmentAATandC extends FragmentBase {

    public static FragmentAATandC newInstance() {
        Bundle args = new Bundle();
        FragmentAATandC fragment = new FragmentAATandC();
        fragment.setArguments(args);
        return fragment;
    }

    TandC tandC;

    public interface TandC {
        public void onTermAndCondition();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container _context has implemented
        // the callback interface. If not, it throws an exception
        try {
            tandC = (TandC) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement LogoutUser");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aa_activate_tnc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
