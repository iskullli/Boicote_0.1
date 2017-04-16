package com.example.perei.boicote.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.example.perei.boicote.R;
import com.example.perei.boicote.fragments.BoicoteFragment;
import com.example.perei.boicote.fragments.MeusBoicotesFragment;
import com.example.perei.boicote.fragments.NewsFragment;
import com.example.perei.boicote.fragments.RankingFragment;

/**
 * Created by perei on 21/03/2017.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

 private Context context;
 //private String[] abas = new String[] {"BOICOTES", "RANKING", "MEUS BOICOTES", "NEWS"};
private int[] icones = new int[]{R.drawable.ic_action_home,R.drawable.ic_ranking, R.drawable.ic_person, R.drawable.ic_action_news};
    private int tamanhoIcone;

    public TabsAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.context = c;
        double escala = this.context.getResources().getDisplayMetrics().density;
        tamanhoIcone = (int) (36 * escala);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch(position){
            case 0 :
            fragment = new BoicoteFragment();
                break;
            case 1 :
                fragment = new RankingFragment();
                break;
            case 2 :
                fragment = new MeusBoicotesFragment();
                break;
            case 3 :
                fragment = new NewsFragment();
                break;
        }
return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Drawable drawable = ContextCompat.getDrawable(this.context, icones[position]);
        drawable.setBounds(0,0,tamanhoIcone,tamanhoIcone);
//permite colocar uma imagem dentro de um texto
        ImageSpan imageSpan = new ImageSpan(drawable);
        //classe utilizada para retornar CharSequence
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public int getCount() {

        return icones.length;
    }
}