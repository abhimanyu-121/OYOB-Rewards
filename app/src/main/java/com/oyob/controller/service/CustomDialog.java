package com.oyob.controller.service;

import android.annotation.SuppressLint;



@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class CustomDialog {
	/*private static CustomDialog dialogOB;
	private AlertsListener alertsListener;
	AlertDialog alertDialog = null;
	
	public static CustomDialog getInstance() {
		if (dialogOB == null) {
			dialogOB = new CustomDialog();
		}

		return dialogOB;
	}

	public void showCustomDialog(final AlertsListener alertsListener, String alertTitle, String alertMessage, final String firstButton, final String secondButton, final int id) {

		this.alertsListener = alertsListener;
		Context context;
		context = (Context) alertsListener;
		// alerts creation
		LayoutInflater liYes = LayoutInflater.from(context);
		View callAddressView = liYes.inflate(R.layout.dialog_for_allinone_alerts, null);
		AlertDialog.Builder adbrok = new AlertDialog.Builder(context);
		adbrok.setCancelable(false);
		adbrok.setView(callAddressView);
		alertDialog = adbrok.create();
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationByHari;
		alertDialog.show();

		// alerts action
		TextView alertTitleTV = (TextView) alertDialog
				.findViewById(R.id.alertYNTitleTVID);
		alertTitleTV.setTypeface(Utility.font_bold);
		alertTitleTV.setText("" + alertTitle);

		TextView alertMessageTV = (TextView) alertDialog
				.findViewById(R.id.alertYNMessageTVID);
		alertMessageTV.setTypeface(Utility.font_reg);
		alertMessageTV.setText("" + alertMessage);

		Button yesBtn = (Button) alertDialog.findViewById(R.id.alertYESBtnID);
		yesBtn.setTypeface(Utility.font_bold);

		Button noBtn = (Button) alertDialog.findViewById(R.id.alertNOBtnID);
		noBtn.setTypeface(Utility.font_bold);

		if (secondButton.equals("EMPTY")) {
			yesBtn.setText("" + firstButton);
			noBtn.setVisibility(View.GONE);
		} else {
			yesBtn.setText("" + firstButton);
			noBtn.setText(secondButton);
		}

		yesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertsListener.onDialogPressed(id, firstButton);
				alertDialog.dismiss();
			}
		});
		noBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertsListener.onDialogPressed(id, secondButton);
				alertDialog.dismiss();
			}
		});

	}
*/}
