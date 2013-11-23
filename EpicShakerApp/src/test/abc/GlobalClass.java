package test.abc;

import android.app.Application;

public class GlobalClass extends Application {

    private int cupWeight=0;
    public String drinkName="";
    public String tags="";
    public String meterial="";
    

    public int getCupWeight() {
        return cupWeight;
    }

    public void setCupWeight(int data) {
        this.cupWeight = data;
    }
}