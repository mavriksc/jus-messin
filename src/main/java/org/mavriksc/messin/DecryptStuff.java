/*
* @(#)DecryptStuff.java Oct 31, 2017
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * @author SCarlisle
 *
 */
public class DecryptStuff {
    private static final String KEY = "vzwencryptionkey";
    private static final String CIPHER_TEXT_BASE_64 = "iZPAOsG3m08iXj9h+ROgEhoBiHpSc59LahIycN39P5hrb9/lZVYQ416ktsXaB2LlnaIkJpjxeUwoRo6UpTnJqNALTOub61wl9NC/oplhcj5+TQU3pbCaAokFcUWciIKU8012LUAhnZvJdIasVxhh59wiLMg1qdlDTO9hiNWMnayj7z9amz/BiwmTcSXMj6pgZef7+0mgweRep+XwFiS7IvI+CcH+1egihO5dr4jye6HWclj9KHNd7mNEC8oUC/ly/uzZakIZjhGgnXYzD4ThTJL5jj8RilaffGE93vbsyfrQQxlNY6N35KUDaRgW+SR3rR++ogelrSAxUlddIw+3LZ2RT69ud2w0slkh+QoTnJa7gOIECW0d/ISUt0zpivPrWtn447L8FXdhQG4zWQx6krjqeWnVMsaAChd+IN3qChL47L1a2zr8MidrLHE7aziLrMvoJ3PYAKQlp8vvY8/uuw==";
    private static final String CIPHER_TEXT_BASE_64_2 = "g7BE+306Plsd791DJnbq7jYOuQ8kN9Y/qn0ipNI+EDEjgcADaw1yBsCC5ykhO96qoI8K4UDHV1mInudk2fFVBpDHAszpK+cjtY8oAe10zI72fudwcGpoHXhjyYexQDwFdCd/55OI9hfE1UGZoOE8VT55SNCksapAPcjifhJcql3VrTvWPrCjYdhfMacuYp+aAPvqMxOKmwfQrDmsRsDb+g7GXRI2KhizXVBuf82d1tURYtjRCiosTlTuvOH320GmSXzL5ejLWmEft9q0A7Uay8t3q+y2Gogc/++2HBwunXHVcolSUC3MYSlfzA9oTN3vl3UyBzGfsxNGJ57nx/DgjEkgX+JbhapRDGVQnBmSUfZMdxzeIQBjeZbVGXi1nkGyB0hgIufOmPv/50nS2OrLUiIWyuwQ9v7ImrJtcDIYgHw=";
    
    
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Key key = new SecretKeySpec(DecryptStuff.KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] cText = Base64.getDecoder().decode(CIPHER_TEXT_BASE_64_2.getBytes());
        byte[] textBytes = cipher.doFinal(cText);
        System.out.println("cipher text base64 "+ CIPHER_TEXT_BASE_64_2);
        System.out.println("cipher text "+ new String (cText));
        System.out.println("cipher text "+ new String (textBytes));
    }

}
