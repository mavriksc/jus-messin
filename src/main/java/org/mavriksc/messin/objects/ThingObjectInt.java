package org.mavriksc.messin.objects;

public class ThingObjectInt {
    private  int num;
    public ThingObjectInt(int i) {
        num=i;
    }
    public int getNum() {
        return num;
    }

    @Override
    public String toString() {
        return ""+ num + ' ';
    }
}
