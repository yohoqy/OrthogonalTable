package com.example.orthogonaltable;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import static android.content.Context.CLIPBOARD_SERVICE;

    public class CopyButtonLibrary {

        private ClipboardManager myClipboard;
        private ClipData myClip;
        private Context context;
        private TextView textView;

        public CopyButtonLibrary(Context context, TextView textView) {
            this.context = context;
            this.textView = textView;
        }

        public void init() {
            myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            String text;
            text = textView.getText().toString();

            myClip = ClipData.newPlainText("text", text);
            myClipboard.setPrimaryClip(myClip);

            Toast.makeText(context,  "已复制", Toast.LENGTH_SHORT).show();
        }

    }

