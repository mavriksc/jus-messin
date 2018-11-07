package main;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONTest {


    public static void main(String[] args){
        JSONObject company = new JSONObject();
        JSONObject user = new JSONObject();
        JSONObject thing = new JSONObject();
        String userJson = "";
        try {
            company.put("name", "this company has an ' in the name and things");
            company.put("id", 1256855L);
            company.put("active", new Boolean(true));
            company.put("label", "this company has an ' in the name and things");
            user.put("id", 1337L);
            user.put("tx", "America/Chicago");
            user.put("company", company);
            thing.put("user", user);
            userJson = thing.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(userJson);
        String subd = userJson.replaceAll("'","\\\\'");
        System.out.println(subd);
    }
}
