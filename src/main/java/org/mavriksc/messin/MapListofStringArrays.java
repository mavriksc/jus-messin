package org.mavriksc.messin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapListofStringArrays {
    public static void main(String[] args) throws JsonProcessingException {
        List<String[]> data = new ArrayList<>();
        String[] headers = {"Age","Name","Sex"};
        String[] row1 = {"23","John","Male"};
        String[] row2 = {"19","Sam","Female"};
        String[] row3 = {"18","Alex","Male"};
        data.add(headers);
        data.add(row1);
        data.add(row2);
        data.add(row3);
        List<String> head = Arrays.asList(data.get(0));
        List<Map<String,String>> output = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            String[] element = data.get(i);
            Map<String,String> person = new HashMap<>();
            for (int j = 0; j < element.length; j++) {
                person.put(head.get(j),element[j]);
            }
            output.add(person);
        }
        XmlMapper mapper = new XmlMapper();
        String answer  = mapper.writeValueAsString(output);
        System.out.println(answer);
    }
}
