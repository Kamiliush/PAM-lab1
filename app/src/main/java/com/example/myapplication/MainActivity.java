package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnButtonClickListener {


    private int[] frames;
    private boolean hidden;

    private int[] sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            frames = new int[]{R.id.frame1, R.id.frame2, R.id.frame3, R.id.frame4};
            /**/
            Log.i("--","~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Log.i("Ramka1 ", Integer.toString(frames[0]));
            Log.i("Ramka2 ", Integer.toString(frames[1]));
            Log.i("Ramka3 ", Integer.toString(frames[2]));
            Log.i("Ramka4 ", Integer.toString(frames[3]));
            hidden = false;

            sequence = new int[]{0,1,2,3};

            Fragment[] fragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for(int i=0; i<4; i++){
                transaction.add(frames[i], fragments[i]);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else {
            frames = savedInstanceState.getIntArray("FRAMES");
            hidden = savedInstanceState.getBoolean("HIDDEN");

            sequence = savedInstanceState.getIntArray("SEQUENCE");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray("FRAMES", frames);
        outState.putBoolean("HIDDEN", hidden);

        outState.putIntArray("SEQUENCE", sequence);
    }

    @Override
    public void onButtonClickShuffle() {
        /*
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(frames[0], frames[1], frames[2], frames[3]));
        Collections.shuffle(list);
        for(int i=0; i<4; i++) frames[i] = list.get(i).intValue();
        */
        List<Integer> s = new ArrayList<>(Arrays.asList(sequence[0],sequence[1], sequence[2], sequence[3]));
        Collections.shuffle(s);
        for(int i=0; i<4; i++) sequence[i] = s.get(i);

        newFragments();
    }

    @Override
    public void onButtonClickClockwise() {
        int t = frames[0];
        frames[0] = frames[1];
        frames[1] = frames[2];
        frames[2] = frames[3];
        frames[3] = t;

        newFragments();
    }

    @Override
    public void onButtonClickHide() {
        if(hidden) return;

        FragmentManager fragmentManager = getSupportFragmentManager();

        for(Fragment f: fragmentManager.getFragments()){
            if (f instanceof  Fragment1) continue;

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.hide(f);

            transaction.addToBackStack(null);
            transaction.commit();
        }
        hidden = true;
    }

    @Override
    public void onButtonClickRestore() {

        if(!hidden) return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        for(Fragment f: fragmentManager.getFragments()){
            if(f instanceof  Fragment1) continue;
            transaction.show(f);
        }


        transaction.addToBackStack(null);
        transaction.commit();

        hidden = false;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof Fragment1){
            ((Fragment1)fragment).setOnButtonClickListener (this);
        }
    }

    private void newFragments()
    {
        Fragment[] newFragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};

        Log.i("--","---------------");
        Log.i("sekwencja1 ", Integer.toString(sequence[0]));
        Log.i("sekwencja2 ", Integer.toString(sequence[0]));
        Log.i("sekwencja3 ", Integer.toString(sequence[0]));
        Log.i("sekwencja4 ", Integer.toString(sequence[0]));

        Fragment[] inSequence = new Fragment[] {newFragments[sequence[0]], newFragments[sequence[1]], newFragments[sequence[2]], newFragments[sequence[3]]};
        newFragments = inSequence;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for(int i=0; i<4; i++){
            transaction.replace(frames[i], newFragments[i]);
            if(hidden && !(newFragments[i] instanceof  Fragment1)) transaction.hide(newFragments[i]);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}