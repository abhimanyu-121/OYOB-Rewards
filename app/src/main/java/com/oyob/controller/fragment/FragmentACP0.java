package com.oyob.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.oyob.controller.R;


/**
 * Created by Narender Kumar on 2/24/2018.
 * Prominent Developer
 * narender.kumar.nishad@gmail.com
 */

public class FragmentACP0 extends FragmentBase implements View.OnClickListener {

    AppCompatEditText faaa0_acet_web_address, faaa0_acet_membership_number, faaa0_acet_password,
            faaa0_acet_new_password;
    AppCompatTextView faaa0_actv_next;

    public static FragmentACP0 newInstance() {
        Bundle args = new Bundle();
        FragmentACP0 fragment = new FragmentACP0();
        fragment.setArguments(args);
        return fragment;
    }

    CreatePINZero mCreatePINZero;

    public interface CreatePINZero {
        void zero(String webAddress, String membershipNumber, String password);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container _context has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCreatePINZero = (CreatePINZero) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement LogoutUser");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aa_activate0, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        faaa0_acet_web_address = view.findViewById(R.id.faaa0_acet_web_address);
        faaa0_acet_membership_number = view.findViewById(R.id.faaa0_acet_membership_number);
        faaa0_acet_password = view.findViewById(R.id.faaa0_acet_password);
        faaa0_acet_password.setVisibility(View.GONE);
        faaa0_acet_new_password = view.findViewById(R.id.faaa0_acet_new_password);
        faaa0_actv_next = view.findViewById(R.id.faaa0_actv_next);
        faaa0_actv_next.setOnClickListener(this);
    }

    private void checkDetails() {
        //String web_address = faaa0_acet_web_address.getText().toString().trim();
        String web_address = "oyob.myrewards.com.au";
        String membership_number = faaa0_acet_membership_number.getText().toString();
        String new_password = faaa0_acet_new_password.getText().toString().trim();

        if (URLUtil.isValidUrl(web_address)) {
            showToastLong(_context, getString(R.string.please_check_url));
            return;
        }

        if (membership_number.length() < 1) {
            showToastLong(_context, getString(R.string.membership_number_should_be_6));
            return;
        }

        if (new_password.length() < 1) {
            showToastLong(_context, "Password cannot be empty");
            return;
        }
        mCreatePINZero.zero(web_address, membership_number, new_password);
    }

    @Override
    public void onClick(View view) {
        checkDetails();
    }
}