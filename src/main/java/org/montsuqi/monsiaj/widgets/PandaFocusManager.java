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

import org.montsuqi.monsiaj.monsia.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

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

		// pns ショートカット
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			// バルス
			if (e.getModifiers() == InputEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_Q) {
				int opt = JOptionPane.showConfirmDialog(null,
					"強制終了しますか？",
					"強制終了確認",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
				);
				if (opt == JOptionPane.YES_OPTION) { System.exit(1); }
			}

			// Window に応じた処理
			if (!(e.getSource() instanceof NumberEntry)) {
				switch (w.getName()) {
					case "K03": // K03 請求確認
						if (e.getModifiers() == InputEvent.CTRL_MASK) {
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
						}
						break;

					case "C02": // C02 病名登録
						if (e.getModifiers() == InputEvent.CTRL_MASK
							&& e.getKeyCode() == KeyEvent.VK_0) {
							// CTRL-0　：　疾患区分をクリア
							List<JComboBox<?>> combos = componentPicker(w,
								"C02.fixed6.MANSEIFLGCOMBO");  // 疾患区分
							combos.get(0).setSelectedIndex(0);
						}
						break;

					case "K02": // K02 診療行為入力
						if (e.getModifiers() == InputEvent.CTRL_MASK
							&& (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_F12)) {
							// CTRL-ENTER　：　中途終了1番を選択・展開
							List<JButton> k02buttons = componentPicker(w, "K02.fixed2.B12CS"); // 中途表示
							k02buttons.get(0).doClick();

							// K10 で走らせるマクロ: row 0 を選択して F12 で確定する
							Runnable r = () -> {
								// protect time to wait for k10 activation
								try {
									Thread.sleep(100);
								} catch (InterruptedException ex) {
								}

								java.awt.Window win = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
								if (win.getName().equals("K10")) {
									List<JComponent> components = componentPicker(win,
										"K10.fixed1.scrolledwindow1.LIST", // 表
										"K10.fixed1.B12"); // 確定ボタン

									PandaCList k10list = (PandaCList) components.get(0);
									JButton k10button = (JButton) components.get(1);

									k10list.singleSelection(0);
									k10list.fireChangeEvent(null);
									k10button.doClick();
								}
							};
							new Thread(r).start();

						} else if (e.getModifiers() == InputEvent.CTRL_MASK
							&& e.getKeyCode() == KeyEvent.VK_B) {
							// CTRL-B
							List<JButton> k02buttons = componentPicker(w, "K02.fixed2.B07S"); // 病名登録
							k02buttons.get(0).doClick();
						}
						break;

					case "XC01": // プレビュー
						switch (e.getKeyCode()) {
							case KeyEvent.VK_UP:
								List<JButton> xc01button = componentPicker(w, "XC01.fixed32.B05");
								xc01button.get(0).doClick();
								break;

							case KeyEvent.VK_DOWN:
								xc01button = componentPicker(w, "XC01.fixed32.B08");
								xc01button.get(0).doClick();
								break;

							case KeyEvent.VK_LEFT:
								xc01button = componentPicker(w, "XC01.fixed32.B06");
								xc01button.get(0).doClick();
								break;

							case KeyEvent.VK_RIGHT:
								xc01button = componentPicker(w, "XC01.fixed32.B07");
								xc01button.get(0).doClick();
								break;
						}
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

		if (w instanceof JFrame) { scan(((JFrame)w).getRootPane(), items, name); }
		if (w instanceof JDialog) { scan(((JDialog)w).getRootPane(), items, name); }
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
