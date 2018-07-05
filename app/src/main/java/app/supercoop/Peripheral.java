package app.supercoop;

import android.util.Log;

public class Peripheral {
    public int ControlState;

    public Peripheral() {
        ControlState = 0;
    }

    public int togglePeripheral() {
        if(ControlState == 1) {
            ControlState = 0;
        }
        else
            ControlState = 1;

        return ControlState;
    }

    public void setPeripheral(int x) {
        ControlState = x;
    }

    public String getPeripheral () {
        if (ControlState == 1)
            return "ON";
        else
            return "OFF";
    }
}
