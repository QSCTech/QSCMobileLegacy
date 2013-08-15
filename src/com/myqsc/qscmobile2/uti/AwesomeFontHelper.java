package com.myqsc.qscmobile2.uti;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class AwesomeFontHelper {
	private static Typeface font;
	public static void setFontFace(TextView v, Context context){
		font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
		v.setTypeface(font);
	}
	
}
