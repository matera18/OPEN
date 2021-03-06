/*
 *  Version: 1.0
 *
 *  The contents of this file are subject to the OpenVPMS License Version
 *  1.0 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.openvpms.org/license/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  Copyright 2005 (C) OpenVPMS Ltd. All Rights Reserved.
 *
 *  $Id: BigDecimalOperationEqual.java 3770 2010-06-29 12:36:53Z tanderson $
 */


package org.openvpms.component.system.common.jxpath;

// java core
import java.math.BigDecimal;

// commons-jxpath
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Support for big decimal equals,
 * 
 * @author   <a href="mailto:support@openvpms.org">OpenVPMS Team</a>
 * @version  $LastChangedDate: 2010-06-29 09:36:53 -0300 (Tue, 29 Jun 2010) $
 */
public class BigDecimalOperationEqual extends CoreOperationEqual {

    /**
     * Constructs a <tt>BigDecimalOperationEqual</tt>.
     * 
     * @param arg1 the left hand side of the expression
     * @param arg2 the right hand side of the expression
     */
    public BigDecimalOperationEqual(Expression arg1, Expression arg2) {
        super(arg1, arg2);
    }

    /* (non-Javadoc)
     * @see org.apache.commons.jxpath.ri.compiler.CoreOperationCompare#equal(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean equal(Object l, Object r) {
        if ((l instanceof BigDecimal) && (r instanceof BigDecimal)) {
            return ((BigDecimal) l).compareTo((BigDecimal) r) == 0;
        } else {
            return super.equal(l, r);
        }
    }

}
