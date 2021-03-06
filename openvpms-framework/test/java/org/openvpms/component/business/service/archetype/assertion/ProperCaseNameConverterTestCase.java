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
 *  Copyright 2009 (C) OpenVPMS Ltd. All Rights Reserved.
 *
 *  $Id$
 */
package org.openvpms.component.business.service.archetype.assertion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

/**
 * Tests the {@link ProperCaseNameConverter} class.
 *
 * @author <a href="mailto:support@openvpms.org">OpenVPMS Team</a>
 * @version $LastChangedDate: 2006-05-02 05:16:31Z $
 */
public class ProperCaseNameConverterTestCase {

    /**
     * The proper case converter.
     */
    private ProperCaseNameConverter converter;


    /**
     * Tests case conversion of simple names.
     */
    @Test
    public void testCaseConversion() {
        check("Phillip K Dick", "phillip k dick");
        check("J K Rowling", "J K rowLing");
    }

    /**
     * Tests names that contain <em>&amp;</em> are correctly spaced.
     */
    @Test
    public void testSpace() {
        check("Pippi & Reuben & Judah", "pippi & reuben & judah");
        check("Pippi & Reuben & Judah", "pippi &reuben &judah");
        check("Pippi & Reuben & Judah", "pippi&reuben&judah");
    }

    /**
     * Tests names that contain <em>(</em> have a space before them.
     */
    @Test
    public void testSpaceBefore() {
        check("Fido (Deceased)", "fido(deceased)");
    }

    /**
     * Tests names that contain <em>. </em> have a space after them.
     */
    @Test
    public void testSpaceAfter() {
        check("J. K. Rowling", "j.k.rowling");
    }

    /**
     * Tests names that start with <em>Mac</em>, <em>Mc</em>, <em>d'</em> and the various quote characters.
     */
    @Test
    public void testStartsWith() {
        check("Old MacDonald", "old macdonald");
        check("Joseph R McCarthy", "joseph r mccarthy");
        check("Charles d'Artagnan", "charles d'artagnan");
        check("'Old MacDonald'", "'old macdonald'");
        check("\"Old MacDonald\"", "\"old macdonald\"");
        check("`Old MacDonald`", "`old macdonald`");
    }

    /**
     * Tests names that contain <em>-</em>, <em>.</em>, <em>'</em> and <em>&amp;</em>.
     */
    @Test
    public void testContains() {
        check("Mary-Anne Fahey", "mary-anne fahey");
        check("Grace O'Malley", "grace o'malley");
        check("J. K. Rowling", "j.k. rowling");
        check("Werner von Braun", "werner von braun");
    }

    /**
     * Tests names that end with <em>'s</em>.
     */
    @Test
    public void testEndsWith() {
        check("'s", "'s");
        check("Test's", "test's");
        check("Test's Case", "test's case");
        check("Test's Case's", "test's case's");
        check("Test-Hyphen's Case", "test-hyphen's case");
    }

    /**
     * Tests names that contains words that are exceptions to the other rules.
     */
    @Test
    public void testExceptions() {
        check("La Trobe", "la trobe");
        check("Macquarie Bank", "macquarie bank");
        check("Apple Macintosh", "apple macintosh");
        check("Leonardo di Caprio", "leonardo di caprio");
        check("Leonardo da Vinci", "leonardo da vinci");
        check("Creme de la Creme", "creme de la creme");
    }

    /**
     * Verifies that leading and trailing spaces are removed, and that multiple spaces are replaced with a single space.
     */
    @Test
    public void testCollapseSpaces() {
        check("Old MacDonald", " old   macdonald ");
        check("Old MacDonald", "\nold   macdonald\n");
    }

    /**
     * Verifies that proper casing occurs for names containing new lines.
     */
    @Test
    public void testNewLines() {
        check("Old\nMacDonald", "old\nmacdonald");
    }

    /**
     * Sets up the fixture.
     */
    @Before
    public void setUp() {
        ProperCaseRules rules = new LocaleProperCaseRules(new Locale("")) {
            int version;

            public String[] getExceptions() {
                return new String[]{"von", "van", "de", "la", "da", "di", "Macintosh", "Macquarie", "La Trobe"};
            }

            public int getVersion() {
                return ++version;
            }
        };
        converter = new ProperCaseNameConverter(rules);
    }

    /**
     * Checks name conversion.
     *
     * @param expected the expected result
     * @param name     the name to convert
     */
    private void check(String expected, String name) {
        assertEquals(expected, converter.convert(name));
    }

}