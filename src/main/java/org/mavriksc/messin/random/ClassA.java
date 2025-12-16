package org.mavriksc.messin.random;

public class ClassA {
    private int myNum1;
    private int myNum2;

    public static void main(String args[])
    {
        ClassA n = new ClassA(1 , 2);
        int a = 0;
        a = n.methodA(a);
        System.out.println("a =" + a);


    }

    public ClassA(int x, int y)
    {
        myNum1 = x;
        myNum2 = y;
    }

    public int methodA(int y)
    {
        return myNum1+y;

    }

    public int getMyNum1()
    {
        return myNum1;

    }

    public int getMyNum2()
    {
        return myNum2;
    }
}
