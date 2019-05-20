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

import javax.swing.DefaultListSelectionModel;

class PandaCListSelectionModel extends DefaultListSelectionModel {

	private boolean notifySelectionChange;

	public PandaCListSelectionModel() {
		super();
		notifySelectionChange = true;
	}

	public void setNotifySelectionChange(boolean notifySelectionChange) {
		this.notifySelectionChange = notifySelectionChange;
	}

	protected void fireValueChanged(int firstIndex, int lastIndex, boolean isAdjusting) {
		if (notifySelectionChange) {
			super.fireValueChanged(firstIndex, lastIndex, isAdjusting);
		}
	}

	public boolean isNotifySelectionChange() {
		return notifySelectionChange;
	}
}
