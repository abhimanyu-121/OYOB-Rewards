package com.oyob.controller.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;

import com.darwindeveloper.wcviewpager.WCCardPagerIndicator;
import com.oyob.controller.R;
import com.oyob.controller.fragment.CardFragment;


/**
 * Created by Ramasamy on 9/6/2017.
 */

public class CardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.card_toolbar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;

        final WCCardPagerIndicator wcViewPagerIndicator = (WCCardPagerIndicator) findViewById(R.id.wcview_pager_card);
        //creamos un nuevo adpater
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        wcViewPagerIndicator.setAdapter(viewPagerAdapter);//aplicamos el adapter

        //obtenemos el viewpager y capturamos sus cambios en tiempo de ejecucuion
        wcViewPagerIndicator.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //NOTA: las posiciones del viewpager inician en 0
                //cambiamos el indicador a la posicion del viewpager
                wcViewPagerIndicator.setSelectedindicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ActivityDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {

            //usaremos la misma clase para todos los fragment solo cambiaremos el texto de cada fragment
            Fragment fragment = new CardFragment();
            Bundle args = new Bundle();//para pasar datos al fragment
            fragment.setArguments(args);//le pasamos los datos(numero de pagina) al fragment

            return fragment;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return 2;//nuemro de paginas para nuestro wcviewpager
        }
    }
}
