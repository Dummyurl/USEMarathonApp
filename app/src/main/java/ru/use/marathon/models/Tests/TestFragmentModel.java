package ru.use.marathon.models.Tests;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilyas on 28-Aug-18.
 */

public class TestFragmentModel {

    private Map<Integer,Boolean> fragmentData;

    public TestFragmentModel() {
        fragmentData = new HashMap<>();
    }

    public  void updateAnswer(int fragmentId, boolean isAnswered) {
        fragmentData.put(fragmentId,isAnswered);
    }

    public  void setAnswer(int fragmentId, boolean isAnswered){
        fragmentData.put(fragmentId,isAnswered);
    }

    public  boolean isAnswered(int fragmentId) {
        return fragmentData.get(fragmentId);
    }
    public  int answeredSize(){
        return fragmentData.size();
    }


}
