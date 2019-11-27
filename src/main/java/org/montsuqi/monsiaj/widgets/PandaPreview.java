/*      PANDA -- a simple transaction monitor

 Copyright (C) 1998-1999 Ogochan.
 2000-2003 Ogochan & JMA (Japan Medical Association).
 2002-2006 OZAWA Sakuro.

 This module is part of PANDA.

 PANDA is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY.  No author or distributor accepts responsibility
 to anyone for the consequences of using it or for whether it serves
 any particular purpose or works at all, unless he says so in writing.
 Refer to the GNU General Public License for full details.

 Everyone is granted permission to copy, modify and redistribute
 PANDA, but only under the conditions described in the GNU General
 Public License.  A copy of this license is supposed to have been given
 to you along with PANDA so you can know your rights and
 responsibilities.  It should be in a file named COPYING.  Among other
 things, the copyright notice and this notice must be preserved on all
 copies.
 */
package org.montsuqi.monsiaj.widgets;

import org.montsuqi.monsiaj.util.Messages;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.montsuqi.monsiaj.util.ExtensionFileFilter;
import org.montsuqi.monsiaj.util.PDFPrint;

/**
 * <p>
 * Preview pane with control buttons and display of current scale.</p>
 */
public class PandaPreview extends JPanel {

    static final Logger logger = LogManager.getLogger(PandaPreview.class);
    private final Preferences prefs = Preferences.userNodeForPackage(this.getClass());
    private static final float SCALE_FACTOR = 1.2f;
    private static final float SCALE_FIT_PAGE = -1.0f;
    private static final float SCALE_FIT_PAGE_WIDTH = -2.0f;
    private static final String SCALE_FIT_PAGE_WIDTH_STR = Float.toString(SCALE_FIT_PAGE_WIDTH);
    private static final String[] SCALE_STRING = {
        Messages.getString("PandaPreview.fitPage"),
        Messages.getString("PandaPreview.fitPageWidth"),
        "50%",
        "75%",
        "100%",
        "125%",
        "150%",
        "175%",
        "200%",
        "300%"
    };
    private static final float[] SCALE_VALUE = {
        SCALE_FIT_PAGE,
        SCALE_FIT_PAGE_WIDTH,
        0.5f,
        0.75f,
        1.0f,
        1.125f,
        1.5f,
        1.74f,
        2.0f,
        3.0f
    };
    private final JToolBar toolbar;
    private NumberEntry pageEntry;
    private final JLabel pageLabel;
    private JComboBox<String> combo;
    private final JScrollPane scroll;
    private float zoom;
    private String fileName;
    private final PDFPanel panel;
    private final Action nextAction;
    private final Action prevAction;
    private final Action saveAction;
    private final Action printAction;
    private final Action zoomInAction;
    private final Action zoomOutAction;
    private final Action fitPageAction;
    private final Action fitPageWidthAction;

    public PandaPreview() {
        super();
        panel = new PDFPanel();

        setLayout(new BorderLayout());

        nextAction = new NextAction();
        prevAction = new PrevAction();

        pageEntry = new NumberEntry();
        pageEntry.setFormat("------");
        pageEntry.setMinimumSize(new Dimension(55, 1));
        pageEntry.setMaximumSize(new Dimension(55, 40));
        pageEntry.addActionListener((ActionEvent e) -> {
            int pagenum;
            try {
                pagenum = Integer.parseInt(pageEntry.getText());
            } catch (NumberFormatException ex) {
                pagenum = panel.getPageNum();
            }
            if (1 <= pagenum && pagenum <= panel.getNumPages()) {
                panel.setPage(pagenum - 1);
            } else {
                pageEntry.setValue(panel.getPageNum() + 1);
            }
        });
        pageLabel = new JLabel("/");
        pageLabel.setMinimumSize(new Dimension(55, 1));
        pageLabel.setMaximumSize(new Dimension(55, 40));
        saveAction = new SaveAction();
        printAction = new PrintAction();
        zoomInAction = new ZoomInAction();
        zoomOutAction = new ZoomOutAction();
        fitPageAction = new FitPageAction();
        fitPageWidthAction = new FitPageWidthAction();

        combo = new JComboBox<>(SCALE_STRING);
        combo.addActionListener((ActionEvent anEvent) -> {
            zoom = SCALE_VALUE[combo.getSelectedIndex()];
            setScale();
        });
        final Dimension preferredSize = combo.getPreferredSize();
        combo.setMaximumSize(preferredSize);

        toolbar = new JToolBar();
        toolbar.setFloatable(false);

        toolbar.add(prevAction);
        toolbar.add(nextAction);
        toolbar.addSeparator();

        toolbar.add(pageEntry);
        toolbar.add(pageLabel);
        toolbar.addSeparator();

        toolbar.add(combo);
        toolbar.addSeparator();

        toolbar.add(saveAction);
        toolbar.add(printAction);

        add(toolbar, BorderLayout.NORTH);

        fileName = null;
        scroll = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(32);
        HandScrollListener hsl = new HandScrollListener();
        scroll.getViewport().addMouseMotionListener(hsl);
        scroll.getViewport().addMouseListener(hsl);
        add(scroll, BorderLayout.CENTER);

        zoom = Float.parseFloat(prefs.get("zoom", SCALE_FIT_PAGE_WIDTH_STR));
        this.updateCombo();

        ActionMap actionMap = getActionMap();
        actionMap.put("prev", prevAction);
        actionMap.put("next", nextAction);
        actionMap.put("save", saveAction);
        actionMap.put("print", printAction);
        actionMap.put("fitPage", fitPageAction);
        actionMap.put("fitPageWidth", fitPageWidthAction);
        actionMap.put("zoomOut", zoomOutAction);
        actionMap.put("zoomIn", zoomInAction);

        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ctrl shift P"), "prev");
        inputMap.put(KeyStroke.getKeyStroke("ctrl shift N"), "next");
        inputMap.put(KeyStroke.getKeyStroke("ctrl S"), "save");
        inputMap.put(KeyStroke.getKeyStroke("ctrl P"), "print");
        inputMap.put(KeyStroke.getKeyStroke("ctrl G"), "fitPage");
        inputMap.put(KeyStroke.getKeyStroke("shift F5"), "fitPage");
        inputMap.put(KeyStroke.getKeyStroke("ctrl F"), "fitPageWidth");
        inputMap.put(KeyStroke.getKeyStroke("shift F6"), "fitPageWidth");
        inputMap.put(KeyStroke.getKeyStroke("ctrl MINUS"), "zoomOut");
        inputMap.put(KeyStroke.getKeyStroke("shift F7"), "zoomOut");
        inputMap.put(KeyStroke.getKeyStroke("shift ctrl SEMICOLON"), "zoomIn");
        inputMap.put(KeyStroke.getKeyStroke("shift F8"), "zoomIn");
    }

    public void load(String fileName) {
        try {
            this.fileName = fileName;
            panel.clear();
            panel.setVisible(true);
            panel.load(fileName);
            pageLabel.setText("/" + panel.getNumPages());
            pageEntry.setValue(1);
            this.setScale();
        } catch (Exception ex) {
            logger.catching(Level.WARN, ex);
        }
    }

    public void clear() {
        fileName = null;
        panel.clear();
        panel.setVisible(false);
    }

    private void updateCombo() {
        int index = 1;
        ActionListener[] listeners = combo.getActionListeners();
        for (ActionListener listener : listeners) {
            combo.removeActionListener(listener);
        }
        if (zoom == SCALE_FIT_PAGE) {
            index = 0;
        } else if (zoom == SCALE_FIT_PAGE_WIDTH) {
            index = 1;
        } else {
            for (int i = 2; i < SCALE_VALUE.length; i++) {
                index = i;
                if (zoom <= SCALE_VALUE[i]) {
                    break;
                }
            }
        }
        combo.setSelectedIndex(index);
        for (ActionListener listener : listeners) {
            combo.addActionListener(listener);
        }
    }

    private float getRealZoom() {
        float z;

        if (this.zoom == SCALE_FIT_PAGE) {
            float h = (float) panel.getPageHeight();
            if (h > 0) {
                if (scroll.isShowing()) {
                    z = scroll.getSize().height * 1.0f / h;
                } else {
                    z = this.getSize().height * 1.0f / h;
                }
            } else {
                z = 1.0f;
            }
        } else if (this.zoom == SCALE_FIT_PAGE_WIDTH) {
            float w = (float) panel.getPageWidth();
            if (w > 0) {
                if (scroll.isShowing()) {
                    z = scroll.getSize().width * 1.0f / w;
                } else {

                    z = this.getSize().width * 1.0f / w;
                }
            } else {
                z = 1.0f;
            }
        } else {
            z = this.zoom;
        }
        if (z > SCALE_VALUE[SCALE_VALUE.length - 1]
                || z <= 0.02) {
            z = 1.0f;
        }
        return z;
    }

    private void setScale() {
        prefs.put("zoom", Float.toString(zoom));
        panel.setScale(getRealZoom());
    }

    class PDFPanel extends JPanel {

        private PDDocument document;
        private PDFRenderer renderer;
        private BufferedImage image;
        private float scale;
        private int pageNum;
        private int pageWidth;
        private int pageHeight;

        public PDFPanel() {
            document = null;
            renderer = null;
            image = null;
            scale = 1f;
            pageNum = 0;
            pageWidth = 0;
            pageHeight = 0;
        }

        public void load(String file) {
            try {
                document = PDDocument.load(new File(file));
                renderer = new PDFRenderer(document);
                setPage(0);
            } catch (IOException ex) {
                document = null;
                renderer = null;
                logger.debug(ex, ex);
            }
        }

        public void setScale(float z) {
            scale = z;
            try {
                image = renderer.renderImage(pageNum, scale);
            } catch (IOException ex) {
                logger.debug(ex, ex);
            }
            setPreferredSize(new Dimension((int) (pageWidth * scale), (int) (pageHeight * scale)));
            revalidate();
            repaint();
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPage(int n) {
            try {
                if (document == null) {
                    return;
                }
                if (n < 0 || n >= document.getNumberOfPages()) {
                    n = 0;
                }
                pageNum = n;
                pageWidth = 0;
                pageHeight = 0;

                image = renderer.renderImage(pageNum, 1f);
                pageWidth = image.getWidth();
                pageHeight = image.getHeight();
                setPreferredSize(new Dimension((int) (pageWidth * scale), (int) (pageHeight * scale)));
                image = renderer.renderImage(pageNum, scale);
                revalidate();
                repaint();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(PandaPreview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }

        public int getPageWidth() {
            if (document != null) {
                return pageWidth;
            } else {
                return 0;
            }
        }

        public int getPageHeight() {
            if (document != null) {
                return pageHeight;
            } else {
                return 0;
            }
        }

        public int getNumPages() {
            if (document != null) {
                return document.getNumberOfPages();
            } else {
                return 0;
            }
        }

        public void clear() {
            document = null;
            renderer = null;
            image = null;
            pageNum = 0;
            this.revalidate();
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (document == null || renderer == null) {
                g2.setBackground(Color.WHITE);
                g2.clearRect(0, 0, getWidth(), getHeight());
                return;
            }

            g2.setBackground(Color.GRAY);
            g2.clearRect(0, 0, getWidth(), getHeight());
            if (image != null) {
                int cx = (int) ((this.getWidth() / 2.0) - (image.getWidth(this) / 2.0));
                int cy = (int) ((this.getHeight() / 2.0) - (image.getHeight(this) / 2.0));
                g2.drawImage(image, cx, cy, this);
            }
        }
    }

    class HandScrollListener extends MouseInputAdapter {

        private final Cursor defCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        private final Cursor hndCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        private final Point pp = new Point();

        @Override
        public void mouseDragged(final MouseEvent e) {
            JViewport vport = scroll.getViewport();
            Point cp = e.getPoint();
            Point vp = vport.getViewPosition();
            vp.translate(pp.x - cp.x, pp.y - cp.y);
            panel.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
            pp.setLocation(cp);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            panel.setCursor(hndCursor);
            pp.setLocation(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            panel.setCursor(defCursor);
            panel.repaint();
        }
    }

    private final class NextAction extends AbstractAction {

        NextAction() {
            URL iconURL = getClass().getResource("/images/next.png");
            if (iconURL != null) {
                putValue(Action.SMALL_ICON, new ImageIcon(iconURL));
            }
            putValue(Action.NAME, Messages.getString("PandaPreview.next"));
            putValue(Action.SHORT_DESCRIPTION, Messages.getString("PandaPreview.next_short_description"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int numPages = panel.getNumPages();
            int pageNum = panel.getPageNum();
            if (pageNum < (numPages - 1)) {
                panel.setPage(pageNum + 1);
                setScale();
                pageEntry.setValue(pageNum + 2);
            }
        }
    }

    private final class PrevAction extends AbstractAction {

        PrevAction() {
            URL iconURL = getClass().getResource("/images/prev.png");
            if (iconURL != null) {
                putValue(Action.SMALL_ICON, new ImageIcon(iconURL));
            }
            putValue(Action.NAME, Messages.getString("PandaPreview.prev"));
            putValue(Action.SHORT_DESCRIPTION, Messages.getString("PandaPreview.prev_short_description"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int pageNum = panel.getPageNum();
            if (pageNum > 0) {
                panel.setPage(pageNum - 1);
                setScale();
                pageEntry.setValue(pageNum);
            }
        }
    }

    private final class SaveAction extends AbstractAction {

        SaveAction() {
            URL iconURL = getClass().getResource("/images/save.png");
            if (iconURL != null) {
                putValue(Action.SMALL_ICON, new ImageIcon(iconURL));
            }
            putValue(Action.NAME, Messages.getString("PandaPreview.save"));
            putValue(Action.SHORT_DESCRIPTION, Messages.getString("PandaPreview.save_short_description"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new ExtensionFileFilter(".pdf", "PDF (*.pdf)"));
            int returnVal = fc.showSaveDialog(PandaPreview.this.getRootPane());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    byte[] b;
                    File file;
                    String outFileName = fc.getSelectedFile().getPath();
                    if (!outFileName.endsWith(".pdf") && !outFileName.endsWith(".PDF")) {
                        outFileName += ".pdf";
                        file = new File(outFileName);
                    } else {
                        file = fc.getSelectedFile();
                    }
                    FileOutputStream out;
                    try (FileInputStream in = new FileInputStream(fileName)) {
                        out = new FileOutputStream(file);
                        b = new byte[in.available()];
                        while (in.read(b) > 0) {
                            out.write(b);
                        }
                    }
                    out.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    private final class PrintAction extends AbstractAction {

        PrintAction() {
            URL iconURL = getClass().getResource("/images/print.png");
            if (iconURL != null) {
                putValue(Action.SMALL_ICON, new ImageIcon(iconURL));
            }
            putValue(Action.NAME, Messages.getString("PandaPreview.print"));
            putValue(Action.SHORT_DESCRIPTION, Messages.getString("PandaPreview.print_short_description"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileName != null) {
                PDFPrint.print(new File(fileName));
            }
        }
    }

    private final class ZoomInAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            zoom = getRealZoom() * SCALE_FACTOR;
            if (zoom > SCALE_VALUE[SCALE_VALUE.length - 1]) {
                zoom = SCALE_VALUE[SCALE_VALUE.length - 1];
            }
            updateCombo();
            setScale();
        }
    }

    private final class ZoomOutAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            zoom = getRealZoom() / SCALE_FACTOR;
            if (zoom < 0.02f) {
                zoom = 0.02f;
            }
            updateCombo();
            setScale();
        }
    }

    private final class FitPageAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            zoom = SCALE_FIT_PAGE;
            updateCombo();
            setScale();
        }
    }

    private final class FitPageWidthAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            zoom = SCALE_FIT_PAGE_WIDTH;
            updateCombo();
            setScale();
        }
    }

    public static void main(String[] args) throws IOException {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        PandaPreview preview = new PandaPreview();
        f.add(preview);
        f.pack();
        f.setSize(1024, 800);
        f.validate();
        f.setVisible(true);
        preview.load(args[0]);
    }
}
