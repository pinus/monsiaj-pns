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

	public void processKeyEvent(Component focusedComponent, KeyEvent e) {
		java.awt.Window w = SwingUtilities.windowForComponent(focusedComponent);
		// Busy windows should not accept key events.
		if (w instanceof Window && ! ((Window)w).isActive()) {
			return;
		}

                //pns component label を調べる
                showComponentLabels(w);

                //pns K03（請求確認）で特別ショートカット処理
                if ("K03".equals(w.getName())) {
                    if (e.getModifiers() == InputEvent.CTRL_MASK) {
                        // ComboBox を探す
                        // combo[0] = 請求書兼領収書　combo[1] = 診療費明細書　combo[2] = 処方箋
                        JComboBox[] combo = new JComboBox[3];
                        searchComboBox(((JFrame)w).getRootPane(), combo);

                        switch (e.getKeyCode()) {
                            // CTRL-0　：　領収書，明細書，処方箋発行なし
                            case KeyEvent.VK_0:
                                combo[0].setSelectedIndex(1);
                                combo[1].setSelectedIndex(1);
                                combo[2].setSelectedIndex(1);
                                break;
                            // CTRL-1　：　発行あり
                            case KeyEvent.VK_1:
                                combo[0].setSelectedIndex(2);
                                combo[1].setSelectedIndex(2);
                                combo[2].setSelectedIndex(2);
                                break;
                            // CTRL-2　：　処方だけ発行あり
                            case KeyEvent.VK_2:
                                combo[0].setSelectedIndex(1);
                                combo[1].setSelectedIndex(1);
                                combo[2].setSelectedIndex(2);
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

        //pns 請求書兼領収書，診療費明細書，処方箋のコンボボックスを探す
        // combo[0] = K03.fixed3.HAKFLGCOMBO　請求書兼領収書
        // combo[1] = K03.fixed3.MEIPRTFLG_COMB　診療費明細書
        // combo[2] = K03.fixed3.SYOHOPRTFLGCOMBO　処方箋
        private void searchComboBox(Component comp, JComboBox[] combo) {
            if (comp instanceof JComponent) {
                JComponent jc = (JComponent) comp;
                for (Component c : jc.getComponents()) {
                    if (c instanceof JComboBox) {
                        if ("K03.fixed3.HAKFLGCOMBO".equals(c.getName())) { combo[0] = (JComboBox) c; }
                        else if ("K03.fixed3.MEIPRTFLG_COMB".equals(c.getName())) { combo[1] = (JComboBox) c; }
                        else if ("K03.fixed3.SYOHOPRTFLGCOMBO".equals(c.getName())) { combo[2] = (JComboBox) c; }
                    }
                    searchComboBox(c, combo);
                }
            }
        }

        private List<JComponent> componentPicker(Class clazz, String... name) {
            List<JComponent> items = new ArrayList<>();

            return items;
        }


        /**
         * pns component の名前を調べる.
         * @param w
         */
        private void showComponentLabels(java.awt.Window w) {
            System.out.printf("========= Window Name: %s =========\n", w.getName());
            scan(((JFrame)w).getRootPane());
        }

        private void scan(Component c) {
            if (c instanceof JComponent) {
                String title = "";
                if (c instanceof JButton) {
                    title = ((JButton)c).getText();
                } else if (c instanceof JLabel) {
                    title = ((JLabel)c).getText();
                } else if (c instanceof JComboBox) {
                    JComboBox cb = (JComboBox) c;
                    if (cb.getItemCount() != 0) {
                        title = (String)cb.getItemAt(cb.getItemCount()-1);
                    }
                }
                System.out.printf("%s [%s] %s\n", c.getName(), c.getClass().getName(), title);

                for (Component comp : ((JComponent)c).getComponents()) {
                    scan(comp);
                }
            }
        }
}
