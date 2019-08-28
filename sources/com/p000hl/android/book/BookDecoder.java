package com.p000hl.android.book;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.p000hl.android.book.entity.Book;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.core.utils.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* renamed from: com.hl.android.book.BookDecoder */
public class BookDecoder {
    public static BookDecoder bookDecoder;
    private HashMap<String, BookIndex> itemMap = new HashMap<>();

    public static BookDecoder getInstance() {
        if (bookDecoder == null) {
            bookDecoder = new BookDecoder();
        }
        return bookDecoder;
    }

    public Book decode(Activity activity, String xmlName) {
        return getInstance().decode(FileUtils.getInstance().getFileInputStream(activity, xmlName));
    }

    public Book decode(InputStream xmlStream) {
        if (xmlStream == null) {
            return null;
        }
        Book book = new Book();
        try {
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setContentHandler(new BookXmlHandler(book));
            reader.parse(new InputSource(xmlStream));
            try {
                return book;
            } catch (Exception e) {
                e.printStackTrace();
                return book;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        } finally {
            try {
                xmlStream.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    private PageEntity decodePageEntity(InputStream xmlStream) {
        if (xmlStream == null) {
            return null;
        }
        PageEntity page = new PageEntity();
        try {
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setContentHandler(new PageXmlHandler(page));
            reader.parse(new InputSource(xmlStream));
            if (xmlStream == null) {
                return page;
            }
            try {
                xmlStream.close();
                return page;
            } catch (IOException e) {
                e.printStackTrace();
                return page;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (xmlStream == null) {
                return page;
            }
            try {
                xmlStream.close();
                return page;
            } catch (IOException e2) {
                e2.printStackTrace();
                return page;
            }
        } finally {
            if (xmlStream != null) {
                try {
                    xmlStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public PageEntity decodePageEntity(Context context, String fileID) {
        BookIndex bi = (BookIndex) this.itemMap.get(fileID);
        if (bi == null) {
            return null;
        }
        FileUtils.getInstance();
        InputStream pageIS = FileUtils.readBookPage(BookSetting.fileName, bi.getStart(), bi.getEnd());
        if (pageIS == null) {
            BookSetting.fileName = "book.dat";
            pageIS = FileUtils.readBookPage(BookSetting.fileName, bi.getStart(), bi.getEnd());
        }
        if (pageIS != null) {
            return decodePageEntity(pageIS);
        }
        Log.i("hl", "解析index字符串 返回为空");
        return null;
    }

    public void initBookItemList() {
        String indexStr = FileUtils.readBookIndex("book.dat");
        if (StringUtils.isEmpty(indexStr)) {
            Log.i("hl", "解析index字符串 返回为空");
        } else {
            parseXmlForBookIndex(new StringReader(indexStr));
        }
    }

    public static int fromArray(byte[] payload) {
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getInt();
    }

    private void parseXmlForBookIndex(StringReader inStream) {
        try {
            NodeList items = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(inStream)).getDocumentElement().getElementsByTagName("xmlIndex");
            for (int i = 0; i < items.getLength(); i++) {
                BookIndex bookIndex = new BookIndex();
                NodeList childsNodes = ((Element) items.item(i)).getChildNodes();
                for (int j = 0; j < childsNodes.getLength(); j++) {
                    Node node = childsNodes.item(j);
                    if (node.getNodeType() == 1) {
                        Element childNode = (Element) node;
                        if ("id".equals(childNode.getNodeName())) {
                            bookIndex.setItemID(childNode.getFirstChild().getNodeValue());
                        } else if ("startnumber".equals(childNode.getNodeName())) {
                            bookIndex.setStart(Integer.valueOf(childNode.getFirstChild().getNodeValue()).intValue());
                        } else {
                            if ("endnumber".equals(childNode.getNodeName())) {
                                bookIndex.setEnd(Integer.valueOf(childNode.getFirstChild().getNodeValue()).intValue());
                            }
                        }
                    }
                }
                this.itemMap.put(bookIndex.getItemID(), bookIndex);
            }
        } catch (Exception e) {
            Log.e("hl", "初始化bookindex 信息出错，出错原因字符内容不正确", e);
        } finally {
            inStream.close();
        }
    }
}
