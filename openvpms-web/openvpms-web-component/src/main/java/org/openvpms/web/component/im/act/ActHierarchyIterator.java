/*
 * Version: 1.0
 *
 * The contents of this file are subject to the OpenVPMS License Version
 * 1.0 (the 'License'); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.openvpms.org/license/
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Copyright 2015 (C) OpenVPMS Ltd. All Rights Reserved.
 */

package org.openvpms.web.component.im.act;

import org.apache.commons.collections.Predicate;
import org.openvpms.component.business.domain.im.act.Act;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * This class enables supports iteration over the first level of an act hierarchy, optionally filtering child acts.
 * e.g, given a hierarchy of:
 * <ul>
 * <li>event1</li>
 * <ul><li>note1</li><li>problem1</li><li>weight1</li></ul>
 * <li>event2</li>
 * <ul><li>note2</li><li>problem2</li></ul>
 * </ul>
 * and filtering out all child acts bar <em>act.patientNote</em> and
 * <em>act.patientClinicalProblem</em>, the following acts would be returned:
 * {@code event1, note1, problem1, event2, note2, problem2}.
 * <br/>
 * Note that the child acts are ordered on increasing start time.
 *
 * @author Tim Anderson
 */
public class ActHierarchyIterator<T extends Act> implements Iterable<T> {

    /**
     * The top level acts.
     */
    private Iterable<T> acts;

    /**
     * The predicate to filter relationships.
     */
    private ActHierarchyFilter<T> filter;

    /**
     * The maximum depth to iterate to. Use {@code -1} to not limit the depth.
     */
    private int maxDepth;


    /**
     * Constructs an {@link ActHierarchyIterator}.
     *
     * @param acts the collection of acts
     */
    public ActHierarchyIterator(Iterable<T> acts) {
        this(acts, (Predicate) null, -1);
    }

    /**
     * Constructs an {@link ActHierarchyIterator}.
     *
     * @param acts       the collection of acts
     * @param shortNames the child short names to include
     */
    public ActHierarchyIterator(Iterable<T> acts, String[] shortNames) {
        this(acts, shortNames, true, -1);
    }

    /**
     * Constructs an {@link ActHierarchyIterator}.
     *
     * @param acts       the collection of acts
     * @param shortNames the child short names to include
     * @param maxDepth   the maximum depth to iterate to, or {@code -1} to have unlimited depth
     */
    public ActHierarchyIterator(Iterable<T> acts, String[] shortNames, int maxDepth) {
        this(acts, shortNames, true, maxDepth);
    }

    /**
     * Constructs an {@link ActHierarchyIterator}.
     *
     * @param acts       the collection of acts
     * @param shortNames the child short names to include/exclude
     * @param include    if {@code true} include the acts, otherwise exclude them
     * @param maxDepth   the maximum depth to iterate to, or {@code -1} to have unlimited depth
     */
    public ActHierarchyIterator(Iterable<T> acts, String[] shortNames, boolean include, int maxDepth) {
        this(acts, new ActHierarchyFilter<T>(shortNames, include), maxDepth);
    }

    /**
     * Constructs an {@link ActHierarchyIterator}.
     *
     * @param acts      the collection of acts
     * @param predicate the predicate to select act relationships. If {@code null}, indicates to select all child acts
     * @param maxDepth  the maximum depth to iterate to, or {@code -1} to have unlimited depth
     */
    public ActHierarchyIterator(Iterable<T> acts, Predicate predicate, int maxDepth) {
        this(acts, new ActHierarchyFilter<T>(predicate), maxDepth);
    }

    /**
     * Constructs an {@link ActHierarchyIterator}.
     *
     * @param acts     the collection of acts
     * @param filter   the hierarchy filter
     * @param maxDepth the maximum depth to iterate to, or {@code -1} to have unlimited depth
     */
    public ActHierarchyIterator(Iterable<T> acts, ActHierarchyFilter<T> filter, int maxDepth) {
        this.acts = acts;
        this.filter = filter;
        this.maxDepth = maxDepth;
    }

    /**
     * Returns an iterator over the acts.
     *
     * @return a new iterator
     */
    public Iterator<T> iterator() {
        return new ActIterator(maxDepth);
    }

    /**
     * Returns the filter.
     *
     * @return the filter
     */
    protected ActHierarchyFilter<T> getFilter() {
        return filter;
    }

    /**
     * Flattens the tree of child acts beneath the specified root.
     * <p/>
     * Child acts are filtered using the {@link #filter}, and recursively processed up to depth maxDepth.
     * The result is an in-order traversal of the tree.
     *
     * @param root the root element
     * @return the flattened tree
     */
    protected List<T> flattenTree(T root) {
        ActHierarchyLister<T> flattener = new ActHierarchyLister<T>();
        return flattener.list(root, filter, maxDepth);
    }

    private class ActIterator implements Iterator<T> {

        /**
         * Iterator over the parent acts.
         */
        private final Iterator<T> parent;

        /**
         * Iterator over the child acts.
         */
        private Iterator<T> child;

        /**
         * The current act.
         */
        private T current;

        /**
         * The maximum depth in the hierarchy to descend to.
         */
        private final int maxDepth;


        /**
         * Constructs an {@link ActIterator}.
         *
         * @param maxDepth the maximum depth in the hierarchy to descend to, or {@code -1} if there is no limit
         */
        public ActIterator(int maxDepth) {
            this.maxDepth = maxDepth;
            parent = acts.iterator();
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            if (current == null) {
                advance();
            }
            return (current != null);
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration.
         * @throws NoSuchElementException iteration has no more elements.
         */
        public T next() {
            if (current == null) {
                if (!advance()) {
                    throw new NoSuchElementException();
                }
            }
            T result = current;
            current = null;
            return result;
        }

        /**
         * Not supported.
         *
         * @throws UnsupportedOperationException if invoked
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Attempts to advance to the next top level act.
         *
         * @return {@code true} if the advance was successful.
         */
        private boolean advance() {
            current = null;
            while (child == null || !child.hasNext()) {
                if (parent.hasNext()) {
                    T root = parent.next();
                    if (maxDepth == -1 || maxDepth > 1) {
                        child = flattenTree(root).iterator();
                    } else {
                        child = Arrays.asList(root).iterator();
                    }
                } else {
                    return false;
                }
            }
            current = child.next();
            return true;
        }

    }

}
