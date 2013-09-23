package com.myqsc.mobile2.uti;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class AwesomeFontHelper {
	private static Typeface font = null;
	public static void setFontFace(TextView v, Context context){
		if (font == null)
			font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
		v.setTypeface(font);
	}
	
}
