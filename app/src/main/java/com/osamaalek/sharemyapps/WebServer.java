package com.osamaalek.sharemyapps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
    private final Context context;
    private List<ResolveInfo> apps;


    public WebServer(int port, Context context) {
        super(port);
        this.context = context;
        initAppsList();
        try {
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {

        if (session.getUri().equals("/")) {
            String msg = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<title>Share your apps</title>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Share your apps</h1>\n" +
                    "<h5>Please choose an app to download</div>\n" +
                    "<ol>\n" +
                    createListInHtml() +
                    "</ol>\n" +
                    "</body>\n" +
                    "</html>";
            return newFixedLengthResponse(msg);
        } else if (session.getUri().equals("/download") && session.getParms().get("app") != null) {
            int appId;
            try {
                appId = Integer.parseInt(Objects.requireNonNull(session.getParms().get("app")));
            } catch (NumberFormatException e) {
                return newFixedLengthResponse(context.getString(R.string.msg_some_thing_wrong));
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(apps.get(appId).activityInfo.applicationInfo.publicSourceDir);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException ignored) {
            }
            return newChunkedResponse(Response.Status.OK, "application/vnd.android.package-archive", fis);

        } else return newFixedLengthResponse(context.getString(R.string.msg_some_thing_wrong));
    }

    private void initAppsList() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        apps = context.getPackageManager().queryIntentActivities(mainIntent, PackageManager.MATCH_ALL);
    }

    private String createListInHtml() {
        StringBuilder htmlList = new StringBuilder();
        for (int i = 0; i < apps.size(); i++) {
            htmlList.append("<li><a href=\"")
                    .append("/download?app=")
                    .append(i)
                    .append("\">")
                    .append(apps.get(i).loadLabel(context.getPackageManager()))
                    .append("</a></li>");
        }
        return String.valueOf(htmlList);
    }
}
