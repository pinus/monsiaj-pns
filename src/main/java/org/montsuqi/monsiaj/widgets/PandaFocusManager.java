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

import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.montsuqi.monsiaj.monsia.Interface;

/** <p>A focus manager which delegates actions to Interface object first.</p>
 *
 * <p>It ignores key events if the window of the component is not active(is busy).</p>
 */
public class PandaFocusManager extends DefaultKeyboardFocusManager {

        // pns
        private static final boolean VERBOSE = false;

	public void processKeyEvent(Component focusedComponent, KeyEvent e) {
		java.awt.Window w = SwingUtilities.windowForComponent(focusedComponent);
		// Busy windows should not accept key events.
		if (w instanceof Window && ! ((Window)w).isActive()) {
			return;
		}

                //pns 特別ショートカット
                if (e.getModifiers() == InputEvent.CTRL_MASK) {
                    //pns K03（請求確認）で特別ショートカット処理
                    if ("K03".equals(w.getName())) {

                        // ComboBox を探す
                        List<JComboBox<?>> combos = componentPicker(w,
                                "K03.fixed3.HAKFLGCOMBO",       // 請求書兼領収書
                                "K03.fixed3.MEIPRTFLG_COMB",    // 診療費明細書
                                "K03.fixed3.SYOHOPRTFLGCOMBO"); // 処方箋

                        switch (e.getKeyCode()) {
                            // CTRL-0　：　領収書，明細書，処方箋発行なし
                            case KeyEvent.VK_0:
                                combos.get(0).setSelectedIndex(1);
                                combos.get(1).setSelectedIndex(1);
                                combos.get(2).setSelectedIndex(1);
                                break;
                            // CTRL-1　：　領収書，明細書だけ発行あり
                            case KeyEvent.VK_1:
                                combos.get(0).setSelectedIndex(2);
                                combos.get(1).setSelectedIndex(2);
                                combos.get(2).setSelectedIndex(1);
                                break;
                            // CTRL-2　：　処方だけ発行あり
                            case KeyEvent.VK_2:
                                combos.get(0).setSelectedIndex(1);
                                combos.get(1).setSelectedIndex(1);
                                combos.get(2).setSelectedIndex(2);
                                break;
                            // CTRL-3　：　全部発行あり
                            case KeyEvent.VK_3:
                                combos.get(0).setSelectedIndex(2);
                                combos.get(1).setSelectedIndex(2);
                                combos.get(2).setSelectedIndex(2);
                                break;
                        }

                    //pns C02 病名登録　
                    } else if ("C02".equals(w.getName())) {
                        List<JComboBox<?>> combos = componentPicker(w,
                                "C02.fixed6.MANSEIFLGCOMBO");  // 疾患区分

                        switch (e.getKeyCode()) {
                            // CTRL-0　：　疾患区分をクリア
                            case KeyEvent.VK_0:
                                combos.get(0).setSelectedIndex(0);
                                break;
                        }
                    }
                }

		// if the event is handled by the Interface, do nothing further.
		if (Interface.handleAccels(e)) {
			e.consume();
		} else {
			super.processKeyEvent(focusedComponent, e);
		}
	}

        /**
         * pns 指定された名前の component を返す.
         * @param <T>
         * @param name
         * @return
         */
        private <T> List<T> componentPicker(java.awt.Window w, String... name) {
            if (VERBOSE) { System.out.printf("Window Name : %s", w.getName()); }

            List<T> items = new ArrayList<>(name.length);
            for (int i=0; i<name.length; i++) { items.add(null); }

            scan(((JFrame)w).getRootPane(), items, name);
            return items;
        }

        /**
         * pns 再帰スキャン.
         * @param <T>
         * @param comp
         * @param items
         * @param name
         */
        private <T> void scan(Component comp, List<T> items, String[] name) {
            if (comp instanceof JComponent) {
                JComponent jc = (JComponent) comp;

                if (VERBOSE) {
                    String title = "";
                    if (jc instanceof JButton) {
                        title = ((JButton)jc).getText();
                    } else if (jc instanceof JLabel) {
                        title = ((JLabel)jc).getText();
                    } else if (jc instanceof JComboBox) {
                        JComboBox cb = (JComboBox) jc;
                        if (cb.getItemCount() != 0) {
                            title = (String)cb.getItemAt(cb.getItemCount()-1);
                        }
                    }
                    System.out.printf("%s [%s] %s\n", jc.getName(), jc.getClass().getName(), title);
                }

                for (Component c : jc.getComponents()) {
                    for (int i=0; i<name.length; i++) {
                        if (name[i].equals(c.getName())) { items.set(i, (T) c); }
                    }
                    scan(c, items, name);
                }
            }
        }
}
