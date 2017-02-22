package ca.nismit.simplerssreader.rss;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlParser {
    private static final String TAG = XmlParser.class.getSimpleName();

    private static final int TAG_TITLE = 1;
    private static final int TAG_SUMMARY = 2;
    private static final int TAG_CONTENT = 3;
    private static final int TAG_LINK = 4;
    private static final int TAG_THUMBNAIL = 5;
    private static final int TAG_PUBLISHED = 6;
    private static final String ns = null;
    private static String feedType = null;
    //private static final String feed[] = {"title", "summary", "content", "thumbnail", "published"};
    //private static final String rss[] = {"title", "description", "content", "thumbnail", "pubDate"};

    public List<RssItem> parse(String data) throws XmlPullParserException, IOException, ParseException {
        try {
            final XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(data));
            return readFeed(parser);
        } finally {

        }
    }

    private void feedType(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        parser.next();
        feedType = parser.getName();
        Log.d(TAG, "Feed Type: "+ feedType);
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        List items = new ArrayList();

        feedType(parser);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (feedType.equals("feed") && name.equals("entry")) {
                items.add(readEntry(parser));
            }else if (feedType.equals("rss") && name.equals("channel")) {
                // Nothing to do
            }else if(feedType.equals("rss") && name.equals("item")) {
                items.add(readItem(parser));
            }else {
                skip(parser);
            }
        }

        return items;
    }

    private RssItem readEntry(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String title = null;
        String summary = null;
        String content = null;
        String link = null;
        String thumbnail = null;
        String date = null;
        long published = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if(name.equals("title")) {
                title = readTag(parser, "title", TAG_TITLE);
            }else if(name.equals("summary")) {
                summary = readTag(parser, "summary", TAG_SUMMARY);
                //Log.d(TAG, "SUMMARY " + summary);
            }else if(name.equals("content")) {
                content = readTag(parser, "content", TAG_CONTENT);
                //Log.d(TAG, "CONTENT " + content);
//            }else if(name.equals("link")) {
//                link = readTag(parser, "link", TAG_LINK);
            }else if(name.equals("thumbnail")) {
                thumbnail = readTag(parser, "thumbnail", TAG_THUMBNAIL);
            }else if(name.equals("published")) {
                date = readTag(parser, "published", TAG_PUBLISHED);
                Date d = dateConverter(date);
                //Log.d(TAG, "DATE " + date);
                //Log.d(TAG, "LONG TIME " + date.getTime());
                published = d.getTime();
            }else {
                skip(parser);
            }
        }

        if(content == null && summary != null) {
            content = summary;
        } else if(content != null && summary == null) {
            summary = content;
        }

        if(thumbnail == null && content != null) {
            thumbnail = getImgSrc(content);
        }

        return new RssItem(title, summary, content, link, thumbnail, date, published);
    }

    private RssItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String summary = null;
        String content = null;
        String link = null;
        String thumbnail = null;
        String date = null;
        long published = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if(name.equals("title")) {
                title = readTag(parser, "title", TAG_TITLE);
            }else if(name.equals("description")) {
                summary = readTag(parser, "description", TAG_SUMMARY);
            }else if(name.equals("content")) {
                content = readTag(parser, "description", TAG_CONTENT);
//            }else if(name.equals("link")) {
//                link = readTag(parser, "link", TAG_LINK);
            }else if(name.equals("thumbnail")) {
                thumbnail = readTag(parser, "thumbnail", TAG_THUMBNAIL);
            }else if(name.equals("pubDate")) {
                date = readTag(parser, "pubDate", TAG_PUBLISHED);
                Date d = dateConverter(date);
                //Log.d(TAG, "DATE " + date);
                //Log.d(TAG, "LONG TIME " + date.getTime());
                published = d.getTime();
            }else {
                skip(parser);
            }
        }

        if(content == null && summary != null) {
            content = summary;
        } else if(content != null && summary == null) {
            summary = content;
        }

        if(thumbnail == null && content != null) {
            thumbnail = getImgSrc(content);
        }

        return new RssItem(title, summary, content, link, thumbnail, date, published);
    }

    private String readTag(XmlPullParser parser, String tagName, int tagType) throws XmlPullParserException, IOException {
        switch (tagType) {
            case TAG_TITLE:
                return readBasicTag(parser, tagName);
            case TAG_SUMMARY:
                return readBasicTag(parser, tagName);
            case TAG_CONTENT:
                return readBasicTag(parser, tagName);
            case TAG_LINK:
                return readLink(parser);
            case TAG_THUMBNAIL:
                return readBasicTag(parser, tagName);
            case TAG_PUBLISHED:
                return readBasicTag(parser, tagName);
            default:
                throw new IllegalArgumentException("Unknown tag type:" + tagType);
        }
    }

    private String readBasicTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = null;
        if(parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
        String link = null;
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String type = parser.getAttributeValue(null, "rel");
        if(type.equals("alternate")) {
            link = parser.getAttributeValue(null, "href");
        }

        while (true) {
            if(parser.nextTag() == XmlPullParser.END_TAG) {
                break;
            }
        }
        return link;
    }

    @Nullable
    private String getImgSrc(String str) {
        Pattern sImageUrlPattern = Pattern.compile("<\\s*img\\s?[^>]*src\\s*=\\s*([\"'])(.*?)\\1");
        Matcher m = sImageUrlPattern.matcher(str);
        if(m.find()){
            return m.group(2);
        } else {
            return null;
        }
    }

    @Nullable
    private Date dateConverter(String str) {
        String[] patternList = new String[] {"yyyyy-MM-dd'T'HH:mm:ss.SSSZZZZ", "yyyyy-MM-dd'T'HH:mm:ssZZZZ", "EEE, d MMM yyyy HH:mm:ss Z"};
        Date date = null;

        //2017-02-16T11:59:00.001-08:00
        //2017-02-16T13:05:00+09:00
        //Thu, 16 Feb 2017 10:27:43 +0000

        for(String pattern : patternList) {
            try {
                DateFormat df = new SimpleDateFormat(pattern);
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                date = df.parse(str);
                break;
            } catch (ParseException e) {
                // Nothing to do
            }
        }

        if(date == null) {
            return null;
        } else {
            return date;
        }
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
