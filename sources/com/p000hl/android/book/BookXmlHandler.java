package com.p000hl.android.book;

import com.p000hl.android.book.entity.Book;
import com.p000hl.android.book.entity.BookMarkEntity;
import com.p000hl.android.book.entity.ButtonEntity;
import com.p000hl.android.book.entity.SectionEntity;
import com.p000hl.android.book.entity.SnapshotEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* renamed from: com.hl.android.book.BookXmlHandler */
public class BookXmlHandler implements ContentHandler {
    private boolean IsBookInfo = true;
    private boolean IsButton = false;
    private boolean IsSection = false;
    private boolean IsSnapshot = false;
    private boolean Ispages = false;
    private Book book;
    ButtonEntity button = null;
    BookMarkEntity mark = null;
    SectionEntity section = null;
    SnapshotEntity snaps = null;
    private String tagName = null;
    private String val = "";

    BookXmlHandler(Book book2) {
        this.book = book2;
    }

    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
        this.val += new String(arg0, arg1, arg2);
    }

    public void endDocument() throws SAXException {
    }

    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
        this.tagName = arg1;
        this.val = this.val.replace(" ", "");
        if (this.IsBookInfo) {
            if (this.tagName.equals("ID")) {
                this.book.getBookInfo().setId(this.val);
            } else if (this.tagName.equals("Name")) {
                this.book.getBookInfo().setName(this.val);
            } else if (this.tagName.equals("BackgroundMusicId")) {
                this.book.getBookInfo().setBackgroundMusicId(this.val);
            } else if (this.tagName.equals("Description")) {
                this.book.getBookInfo().setDescription(this.val);
            } else if (this.tagName.equals("BookType")) {
                this.book.getBookInfo().setBookType(this.val);
            } else if (this.tagName.equals("DeviceType")) {
                this.book.getBookInfo().setDeviceType(this.val);
            } else if (this.tagName.equals("BookIconId")) {
                this.book.getBookInfo().setBookIconId(this.val);
            } else if (this.tagName.equals("ADType")) {
                this.book.getBookInfo().adType = Integer.parseInt(this.val);
            } else if (this.tagName.equals("ADPosition")) {
                this.book.getBookInfo().position = this.val;
            } else if (this.tagName.equals("BookFlipType")) {
                this.book.getBookInfo().bookFlipType = this.val;
            } else if (this.tagName.equals("BookNavType")) {
                this.book.getBookInfo().setBookNavType(this.val);
            } else if (this.tagName.equals("HomePageID")) {
                this.book.getBookInfo().homePageID = this.val;
            } else if (this.tagName.equals("BookWidth")) {
                this.book.getBookInfo().bookWidth = Integer.parseInt(this.val);
            } else if (this.tagName.equals("BookHeight")) {
                this.book.getBookInfo().bookHeight = Integer.parseInt(this.val);
            } else if (this.tagName.equals("ActivePush")) {
                this.book.ActivePush = Boolean.parseBoolean(this.val);
            } else if (this.tagName.equals("PushID")) {
                this.book.PushID = this.val;
            } else if (this.tagName.equals("StartPageTime")) {
                if (!this.val.equals("")) {
                    this.book.getBookInfo().setStartPageTime(Double.valueOf(this.val).doubleValue());
                } else {
                    this.book.getBookInfo().setStartPageTime(0.0d);
                }
            } else if (this.tagName.equals("AndroidAdapterType")) {
                if ("KEEPSIZE_TRACTION".equals(this.val)) {
                    HLSetting.FitScreen = true;
                    BookSetting.FITSCREEN_TENSILE = false;
                } else if ("KEEPSIZE_SCALE".equals(this.val)) {
                    HLSetting.FitScreen = false;
                    BookSetting.FITSCREEN_TENSILE = false;
                } else if ("CHANGESIZE_TRACTION".equals(this.val)) {
                    HLSetting.FitScreen = true;
                    BookSetting.FITSCREEN_TENSILE = true;
                } else {
                    HLSetting.FitScreen = true;
                    BookSetting.FITSCREEN_TENSILE = true;
                }
            } else if (this.tagName.equals("IsLoadNavigation")) {
                if ("true".equals(this.val)) {
                    BookSetting.IS_NO_NAVIGATION = false;
                } else {
                    BookSetting.IS_NO_NAVIGATION = true;
                }
            }
            if (this.tagName.equals("IsFree")) {
                this.book.getBookInfo().isFree = Boolean.parseBoolean(this.val);
            }
        }
        if (this.Ispages && this.tagName.equals("ID")) {
            this.book.getPages().add(this.val);
        }
        if (this.IsSnapshot) {
            if (this.tagName.equals("SnapshotId")) {
                this.snaps.setId(this.val);
            } else if (this.tagName.equals("PageId")) {
                this.snaps.setPageID(this.val);
            } else if (this.tagName.equals("SnapshotWidth")) {
                this.snaps.setWidth(this.val);
            } else if (this.tagName.equals("SnapshotHeight")) {
                this.snaps.setHeight(this.val);
            }
        }
        if (this.tagName.equals("StartPageID")) {
            this.book.setStartPageID(this.val);
        }
        if (this.IsSection) {
            if (this.tagName.equals("ID")) {
                this.section.setID(this.val);
                this.section.bookID = this.book.getBookInfo().getId();
            } else if (this.tagName.equals("Name")) {
                this.section.setName(this.val);
            }
            if (this.tagName.equals("Page") && this.tagName.equals("Page")) {
                this.section.getPages().add(this.val);
            }
        }
        if (this.IsButton) {
            if (this.tagName.equals("X")) {
                this.button.setX(Float.valueOf(this.val).floatValue());
            } else if (this.tagName.equals("Y")) {
                this.button.setY(Float.valueOf(this.val).floatValue());
            } else if (this.tagName.equals("Width")) {
                this.button.setWidth(Float.valueOf(this.val).intValue());
            } else if (this.tagName.equals("Height")) {
                this.button.setHeight(Float.valueOf(this.val).intValue());
            } else if (this.tagName.equals("Type")) {
                this.button.setType(this.val);
            } else if (this.tagName.equals("isVisible")) {
                this.button.setVisible(Boolean.valueOf(this.val).booleanValue());
            } else if (this.tagName.equals("Source")) {
                this.button.setSource(this.val);
            } else if (this.tagName.equals("SelectedSource")) {
                this.button.setSelectedSource(this.val);
            }
        }
        if (HLSetting.IsHaveBookMark) {
            if (this.tagName.equals("IsShowBookMark")) {
                this.mark.setIsShowBookMark(this.val);
                if (this.val.equals("true")) {
                    HLSetting.IsShowBookMark = true;
                } else {
                    HLSetting.IsShowBookMark = false;
                }
            }
            if (this.tagName.equals("IsShowBookMarkLabel")) {
                if (this.val.equals("true")) {
                    HLSetting.IsShowBookMarkLabel = true;
                } else {
                    HLSetting.IsShowBookMarkLabel = false;
                }
                this.mark.setIsShowBookMarkLabel(this.val);
            }
            if (this.tagName.equals("BookMarkLablePositon")) {
                this.mark.setBookMarkLablePositon(this.val);
                HLSetting.BookMarkLablePositon = this.val;
            }
            if (this.tagName.equals("BookMarkLabelHorGap")) {
                this.mark.setBookMarkLabelHorGap(this.val);
                HLSetting.BookMarkLabelHorGap = Integer.valueOf(this.val).intValue();
            }
            if (this.tagName.equals("BookMarkLabelVerGap")) {
                this.mark.setBookMarkLabelVerGap(this.val);
                HLSetting.BookMarkLabelVerGap = Integer.valueOf(this.val).intValue();
            }
            if (this.tagName.equals("BookMarkLabelText")) {
                this.mark.setBookMarkLabelText(this.val);
                HLSetting.BookMarkLabelText = this.val;
            }
        }
        if (this.tagName.equals("Section")) {
            this.book.getSections().add(this.section);
        }
        if (this.tagName.equals("Snapshot")) {
            this.snaps.bookID = this.book.getBookInfo().getId();
            this.book.getSnapshots().add(this.snaps);
        }
        if (this.tagName.equals("Button")) {
            this.book.getButtons().add(this.button);
        }
        this.val = "";
    }

    public void endPrefixMapping(String arg0) throws SAXException {
    }

    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
    }

    public void processingInstruction(String arg0, String arg1) throws SAXException {
    }

    public void setDocumentLocator(Locator arg0) {
    }

    public void skippedEntity(String arg0) throws SAXException {
    }

    public void startDocument() throws SAXException {
    }

    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
        this.tagName = arg1;
        if (this.tagName.equals("Snapshots")) {
            this.IsBookInfo = false;
            this.IsSnapshot = true;
            this.IsButton = false;
        }
        if (this.tagName.equals("Pages")) {
            this.IsSnapshot = false;
            this.Ispages = true;
            this.IsButton = false;
        }
        if (this.tagName.equals("Sections")) {
            this.Ispages = false;
            this.IsSection = true;
            this.IsButton = false;
        }
        if (this.tagName.equals("Section")) {
            this.section = new SectionEntity();
        }
        if (this.tagName.equals("Snapshot")) {
            this.snaps = new SnapshotEntity();
        }
        if (this.tagName.equals("Buttons")) {
            this.Ispages = false;
            this.IsSection = false;
            this.IsButton = true;
        }
        if (this.tagName.equals("Button")) {
            this.button = new ButtonEntity();
        }
        if (this.tagName.equals("BookMark")) {
            this.mark = new BookMarkEntity();
            HLSetting.IsHaveBookMark = true;
        }
    }

    public void startPrefixMapping(String arg0, String arg1) throws SAXException {
    }
}
