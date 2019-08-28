package com.p000hl.android.book;

import android.util.Log;
import com.p000hl.android.book.entity.ContainerEntity;
import com.p000hl.android.book.entity.PageEntity;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* renamed from: com.hl.android.book.PageFactory */
public class PageFactory {
    public static PageEntity getPage(InputStream xmlStream) {
        PageEntity page = new PageEntity();
        try {
            setPage(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlStream).getDocumentElement(), page);
        } catch (Exception e) {
            Log.e("hl", "解析page错误", e);
        }
        return page;
    }

    private static void setPage(Element pageE, PageEntity page) {
        for (Node node = pageE.getFirstChild(); node != null; node = node.getNextSibling()) {
            String tagName = node.getNodeName();
            if (!setProperFromDom(page, node) && tagName.equals("Containers")) {
                for (Node containerNode = node.getFirstChild(); containerNode != null; containerNode = containerNode.getNextSibling()) {
                    page.getContainers().add(getContainer(containerNode));
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004b, code lost:
        if (r2.getType() != java.lang.String.class) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004d, code lost:
        r2.set(r11, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x007b, code lost:
        if (r2.getType() != java.lang.Boolean.TYPE) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x007d, code lost:
        r2.setBoolean(r11, java.lang.Boolean.parseBoolean(r6));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x008b, code lost:
        if (r2.getType() != java.lang.Float.TYPE) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x008d, code lost:
        r2.setFloat(r11, java.lang.Float.parseFloat(r6));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0031, code lost:
        r2.setAccessible(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x003b, code lost:
        if (r2.getType() != java.lang.Integer.TYPE) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r2.setInt(r11, java.lang.Integer.parseInt(r6));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean setProperFromDom(java.lang.Object r11, org.w3c.dom.Node r12) {
        /*
            r10 = 0
            java.lang.String r5 = r12.getNodeName()
            java.lang.String r6 = r12.getTextContent()
            java.lang.Class r7 = r11.getClass()     // Catch:{ Exception -> 0x0051 }
            java.lang.reflect.Field[] r0 = r7.getDeclaredFields()     // Catch:{ Exception -> 0x0051 }
            int r4 = r0.length     // Catch:{ Exception -> 0x0051 }
            r3 = 0
        L_0x0013:
            if (r3 >= r4) goto L_0x0044
            r2 = r0[r3]     // Catch:{ Exception -> 0x0051 }
            java.lang.String r7 = r2.getName()     // Catch:{ Exception -> 0x0051 }
            java.util.Locale r8 = java.util.Locale.getDefault()     // Catch:{ Exception -> 0x0051 }
            java.lang.String r7 = r7.toLowerCase(r8)     // Catch:{ Exception -> 0x0051 }
            java.util.Locale r8 = java.util.Locale.getDefault()     // Catch:{ Exception -> 0x0051 }
            java.lang.String r8 = r5.toLowerCase(r8)     // Catch:{ Exception -> 0x0051 }
            boolean r7 = r7.equals(r8)     // Catch:{ Exception -> 0x0051 }
            if (r7 == 0) goto L_0x0095
            r7 = 1
            r2.setAccessible(r7)     // Catch:{ Exception -> 0x0051 }
            java.lang.Class r7 = r2.getType()     // Catch:{ Exception -> 0x0051 }
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x0051 }
            if (r7 != r8) goto L_0x0045
            int r7 = java.lang.Integer.parseInt(r6)     // Catch:{ Exception -> 0x0051 }
            r2.setInt(r11, r7)     // Catch:{ Exception -> 0x0051 }
        L_0x0044:
            return r10
        L_0x0045:
            java.lang.Class r7 = r2.getType()     // Catch:{ Exception -> 0x0051 }
            java.lang.Class<java.lang.String> r8 = java.lang.String.class
            if (r7 != r8) goto L_0x0075
            r2.set(r11, r6)     // Catch:{ Exception -> 0x0051 }
            goto L_0x0044
        L_0x0051:
            r1 = move-exception
            java.lang.String r7 = "hl"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "解析设置属性出现问题，解析属性的name和value分别是"
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r8 = r8.append(r5)
            java.lang.String r9 = ","
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r8 = r8.append(r6)
            java.lang.String r8 = r8.toString()
            android.util.Log.e(r7, r8, r1)
            goto L_0x0044
        L_0x0075:
            java.lang.Class r7 = r2.getType()     // Catch:{ Exception -> 0x0051 }
            java.lang.Class r8 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x0051 }
            if (r7 != r8) goto L_0x0085
            boolean r7 = java.lang.Boolean.parseBoolean(r6)     // Catch:{ Exception -> 0x0051 }
            r2.setBoolean(r11, r7)     // Catch:{ Exception -> 0x0051 }
            goto L_0x0044
        L_0x0085:
            java.lang.Class r7 = r2.getType()     // Catch:{ Exception -> 0x0051 }
            java.lang.Class r8 = java.lang.Float.TYPE     // Catch:{ Exception -> 0x0051 }
            if (r7 != r8) goto L_0x0044
            float r7 = java.lang.Float.parseFloat(r6)     // Catch:{ Exception -> 0x0051 }
            r2.setFloat(r11, r7)     // Catch:{ Exception -> 0x0051 }
            goto L_0x0044
        L_0x0095:
            int r3 = r3 + 1
            goto L_0x0013
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.book.PageFactory.setProperFromDom(java.lang.Object, org.w3c.dom.Node):boolean");
    }

    private static ContainerEntity getContainer(Node nodeContainer) {
        ContainerEntity container = new ContainerEntity();
        for (Node node = nodeContainer.getFirstChild(); node != null; node = node.getNextSibling()) {
            String nodeName = node.getNodeName();
            if (!setProperFromDom(nodeContainer, node)) {
            }
        }
        return container;
    }
}
