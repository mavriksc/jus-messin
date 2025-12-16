package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Streamys {
    public static void main(String[] args) {

        List<Object> employeesList = new ArrayList<>();
        List<String> neededEmployees = new ArrayList<>();
        List<Object> sortedEmployeesList = new ArrayList<>();
        List<Object> newList = neededEmployees.stream()
                .map(eid-> employeesList.stream().filter(employee -> employee.equals(eid)).findAny().get())
                .collect(Collectors.toList());
    }

    void things(){
        List<Object> docs = new ArrayList<>();


    }
}
