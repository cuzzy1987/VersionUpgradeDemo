package com.me.versionupdatedemo.utils;

import android.text.Editable;

/**
 * Created by cs on 2019/4/2.
 */
public class StringUtils {
	public static void deleteCode(Editable word,boolean deleteLetter,Boolean deleteNumber){
		if (word.length()>0){
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				if (((c>=0x41 && c<=0x5A) && deleteLetter)||(deleteLetter&&(c>=0x61)&&(c<=0x7A))||(deleteNumber&&(c>=0x31&&c<=0x39))){
					// TODO: 2019/3/28  nothing
					word.delete(i,i+1);
				}
			}
		}
	}
}
