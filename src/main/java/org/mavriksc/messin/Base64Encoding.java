package org.mavriksc.messin;

import java.nio.charset.Charset;
import java.util.Base64;

public class Base64Encoding {
    public static void main(String[] args){

        String src = "Sean Carlisle";
        String encoded =  new String(Base64.getEncoder().encode(src.getBytes()), Charset.defaultCharset());
        System.out.println(encoded);
        System.out.println(new String(Base64.getDecoder().decode(encoded),Charset.defaultCharset()));
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik1qSTRRVFEwT1VRNU9VSXlSVEV6TlRBd05UVXpSVVExTlVOR05FVkVORGRDTlRnM016VXdRZyJ9";//.eyJodHRwczovL2JpdG9vbXRyYWRlci5uZXQvYXV0aG9yaXphdGlvbiI6eyJncm91cHMiOlsiQ29uc3VtZXJzIl0sInJvbGVzIjpbIlVzZXIiXX0sImlzcyI6Imh0dHBzOi8vYml0em9vbS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NWNhNTE5NzZjYzMzZjUxMTBhYWNkYmM0IiwiYXVkIjpbImh0dHBzOi8vYml0em9vbS5hdXRoMC5jb20vYXBpL3YyLyIsImh0dHBzOi8vYml0em9vbS5hdXRoMC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNTU5MzIzNDI1LCJleHAiOjE1NTk0MDk4MjUsImF6cCI6IlliRGFSelRVQkFtZEFrSExqdjZ0bEI3U05xSTF1RlNtIiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCBhZGRyZXNzIHBob25lIHJlYWQ6Y3VycmVudF91c2VyIHVwZGF0ZTpjdXJyZW50X3VzZXJfbWV0YWRhdGEgZGVsZXRlOmN1cnJlbnRfdXNlcl9tZXRhZGF0YSBjcmVhdGU6Y3VycmVudF91c2VyX21ldGFkYXRhIGNyZWF0ZTpjdXJyZW50X3VzZXJfZGV2aWNlX2NyZWRlbnRpYWxzIGRlbGV0ZTpjdXJyZW50X3VzZXJfZGV2aWNlX2NyZWRlbnRpYWxzIHVwZGF0ZTpjdXJyZW50X3VzZXJfaWRlbnRpdGllcyIsImd0eSI6InBhc3N3b3JkIn0.St7097L1ZAlBWcAPrie-8CGV2F3Fr8uNYpSDVKSPVPF4zBZrmm62_UAj7Ssux8AjUy0LhjiF3kLpNph2L7yrpUREw6TyGJwQasfdVtM5VzRYUcy-fOGyRSqPQorbzxJQZzs2pyDJm-2hMQ0McJ37ubKIWrHFD5McMedN6THK7g5TExX47XCRPcOuCEWm3bf3zdWF2LEGhCw_c-lcZDwlb4ePkO721XjSWtrXEBvxc8scFNaHDt7VOnrSze4XK_LO8eE8bHRq6qUrWf1csYucK--aHazBsvfdl-6QDRk-tOBM-LdXJMT7H8Ih6trxVmZofQjr2dQ4j_3DTVoU3eLdog";
        String decoded =new String( Base64.getDecoder().decode(token),Charset.defaultCharset());
        System.out.println(decoded);

    }
}
