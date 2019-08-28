package com.p000hl.android.view.component.moudle.bookshelf;

import android.os.Environment;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.core.utils.WebUtils;
import java.io.File;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* renamed from: com.hl.android.view.component.moudle.bookshelf.BookshelfXMLParseHandler */
public class BookshelfXMLParseHandler extends DefaultHandler {
    private ShelvesBook book;
    private List<ShelvesBook> books;
    private String val;

    public BookshelfXMLParseHandler(List<ShelvesBook> books2) {
        this.books = books2;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.val = null;
        if (localName.equalsIgnoreCase("BOOK")) {
            this.book = new ShelvesBook();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        this.val = new String(ch, start, length);
    }

    private void setBookDataPath(ShelvesBook book2) {
        String valueUrl = book2.mBookUrl;
        if (!StringUtils.isEmpty(valueUrl)) {
            book2.mLocalPath = parseURL2LocalData(valueUrl);
        }
    }

    private String parseURL2LocalData(String valueUrl) {
        String shelvesRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BookController.getInstance().hlActivity.getPackageName() + "/shelves/";
        int startIndex = valueUrl.indexOf("//") + 2;
        if (startIndex < 0) {
            startIndex = 0;
        }
        int endIndex = valueUrl.lastIndexOf(".");
        if (endIndex < 0) {
            endIndex = valueUrl.length();
        }
        String foldPath = (shelvesRootPath + valueUrl.substring(startIndex, endIndex)) + "/";
        File bookDir = new File(foldPath);
        if (!bookDir.exists()) {
            bookDir.mkdirs();
        }
        return foldPath;
    }

    private String parseURL2LocalCover(String valueUrl) {
        String shelvesRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BookController.getInstance().hlActivity.getPackageName() + "/shelves/";
        int startIndex = valueUrl.indexOf("//") + 2;
        if (startIndex < 0) {
            startIndex = 0;
        }
        int endIndex = valueUrl.lastIndexOf("/");
        if (endIndex < 0) {
            endIndex = valueUrl.length();
        }
        File bookDir = new File((shelvesRootPath + valueUrl.substring(startIndex, endIndex)) + "/");
        if (!bookDir.exists()) {
            bookDir.mkdirs();
        }
        return shelvesRootPath + valueUrl.substring(startIndex);
    }

    private void setBookCoverath(ShelvesBook book2) {
        String valueUrl = book2.mCoverUrl;
        if (!StringUtils.isEmpty(valueUrl)) {
            book2.mCoverPath = parseURL2LocalCover(valueUrl);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equalsIgnoreCase("BOOKID")) {
            this.book.mBookID = this.val;
        } else if (localName.equalsIgnoreCase("COVERURL")) {
            this.book.mCoverUrl = this.val;
        } else if (localName.equalsIgnoreCase("BOOKURL")) {
            this.book.mBookUrl = this.val;
        } else if (localName.equalsIgnoreCase("VERSION")) {
            this.book.version = this.val;
        } else if (localName.equalsIgnoreCase("BOOK")) {
            this.books.add(this.book);
            setBookDataPath(this.book);
            setBookCoverath(this.book);
            downloadBitmap(this.book);
        }
    }

    private void downloadBitmap(ShelvesBook book2) {
        if (!new File(book2.mCoverPath).exists() && WebUtils.isConnectingToInternet(BookController.getInstance().hlActivity)) {
            WebUtils.downLoadResource(book2.mCoverUrl, book2.mCoverPath);
        }
    }
}
