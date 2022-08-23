package com.ananas.shrink.utility;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ananas.shrink.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    
    public static LinearLayout genLinkItem(Context context, String _short_link, String _long_link, View.OnClickListener _onClickListener){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") LinearLayout linearLayoutTemplate = (LinearLayout) inflater.inflate(R.layout.template_scrollview_item, null);
        
        ((TextView) linearLayoutTemplate.findViewById(R.id.short_link)).setText(_short_link);
        ((TextView) linearLayoutTemplate.findViewById(R.id.long_link)).setText(_long_link);
        linearLayoutTemplate.findViewById(R.id.btnLinkInformation).setOnClickListener(_onClickListener);
        
        return linearLayoutTemplate;
    }
    
    public static boolean isItLink(String link){
        Pattern pattern = Pattern.compile("^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$");
        Matcher matcher = pattern.matcher(link);
        return matcher.find();
    }

    public static boolean isDeviceOnline(Context ctx) {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }
    
    public static Dialog linkInformationDialog(Context ctx, String id, String _long_link, String _short_link){
        Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.template_link_information_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView longLink = dialog.findViewById(R.id.dLongLink);
        longLink.setText(_long_link);
        TextView shortLink = dialog.findViewById(R.id.dShortLink);
        shortLink.setText(_short_link);
        dialog.findViewById(R.id.dbtnOk).setOnClickListener(view -> dialog.dismiss());
        dialog.findViewById(R.id.dbtnCopyLongLink).setOnClickListener(view -> {
            ClipData clpd = ClipData.newPlainText("label", longLink.getText().toString());
            ((ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(clpd);
            Toast.makeText(ctx, "Long link copied.", Toast.LENGTH_SHORT).show();
        });
        dialog.findViewById(R.id.dbtnCopyShortLink).setOnClickListener(view -> {
            ClipData clpd = ClipData.newPlainText("label", shortLink.getText().toString());
            ((ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(clpd);
            Toast.makeText(ctx, "Short link copied.", Toast.LENGTH_SHORT).show();
        });
        return dialog;
    }

    public static Dialog waitDialog(Context ctx){
        Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.template_wait_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static void openUrl(Context ctx, String Url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        ctx.startActivity(browserIntent);
    }
    
}
