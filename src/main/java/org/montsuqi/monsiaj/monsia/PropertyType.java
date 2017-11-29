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

package org.montsuqi.monsiaj.monsia;

/** <p>A set of constants which indicates the destination of current pending property hash.</p>
 */
class PropertyType {

	private PropertyType() {
		// inhibit instantiation
	}

	/** <p> A constant which indicates that current property hash is for nothing.</p>
	 */
	static final PropertyType NONE = new PropertyType();
	
	/** <p> A constant which indicates that current property hash is to be set to widget info.</p>
	 */
	static final PropertyType WIDGET = new PropertyType();
	
	/** <p> A constant which indicates that current property hash is to be set to atk.</p>
	 */
	static final PropertyType ATK = new PropertyType();
	
	/** <p> A constant which indicates that current property hash is to be set to child info.</p>
	 */
	static final PropertyType CHILD = new PropertyType();
}
