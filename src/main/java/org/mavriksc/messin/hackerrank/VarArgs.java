package org.mavriksc.messin.hackerrank;

public class VarArgs {
    public static void main(String[] args) {

    }
}
class Add{
    Add(){

    }
    public void add(int... vals){
        int sum=0;
        StringBuilder sb = new StringBuilder();
        for (int val : vals) {
            sum += val;
            sb.append(val).append("+");
        }
        sb.deleteCharAt(sb.lastIndexOf("+")).append("=").append(sum);
        System.out.println(sum);
    }
}
