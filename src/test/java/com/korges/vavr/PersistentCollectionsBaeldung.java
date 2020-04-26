package com.korges.vavr;

import io.vavr.Tuple2;
import io.vavr.collection.Array;
import io.vavr.collection.CharSeq;
import io.vavr.collection.HashSet;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import io.vavr.collection.SortedSet;
import io.vavr.collection.Stream;
import io.vavr.collection.TreeSet;
import org.junit.Test;


import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Collections API in Vavr
 * https://www.baeldung.com/vavr-collections
 *
 * Persistent Collections
 *
 * A persistent collection when modified produces a new version of the collection while preserving the current version.
 * Maintaining multiple versions of the same collection might lead to inefficient CPU and memory usage.
 * However, the Vavr collection library overcomes this by sharing data structure across different versions of a collection.
 */
public class PersistentCollectionsBaeldung {

    // Vavr - Sequences
    // The Seq interface represents sequential data structures.
    // It is the parent interface for List, Stream, Queue, Array, Vector, and CharSeq.

    /**
     * List
     *
     * Persistent Lists are formed recursively from a head and a tail:
     * Head – the first element
     * Tail – a list containing remaining elements (that list is also formed from a head and a tail)
     * There are static factory methods in the List API that can be used for creating a List.
     * We can use the static of() method to create an instance of List from one or more objects.
     */
    @Test
    public void vavr_list() {
        List<String> list = List.of(
                "Java", "PHP", "Jquery", "JavaScript", "JShell", "JAVA");

        List<String> list1 = list.drop(2);
        assertFalse(list1.contains("Java") && list1.contains("PHP"));

        List<String> list2 = list.dropRight(2);
        assertFalse(list2.contains("JAVA") && list2.contains("JShell"));

        List<String> list3 = list.dropUntil(s -> s.contains("Shell"));
        assertEquals(list3.size(), 2);

        List<String> list4 = list.dropWhile(s -> s.length() > 0);
        assertTrue(list4.isEmpty());

        List<String> list5 = list.take(1);
        assertEquals(list5.single(), "Java");

        List<String> list6 = list.takeRight(1);
        assertEquals(list6.single(), "JAVA");

        List<String> list7 = list.takeUntil(s -> s.length() > 6);
        assertEquals(list7.size(), 3);
    }

    /**
     * Very interestingly, there's also the intersperse() that inserts an element in between every element of a list.
     */
    @Test
    public void vavr_list2() {
        String words = List.of("Boys", "Girls")
                .intersperse("and")
                .reduce((s1, s2) -> s1.concat( " " + s2 ))
                .trim();
        assertEquals(words, "Boys and Girls");
    }

    /**
     * The grouped(int n) divides a List into groups of n elements each.
     * The groupdBy() accepts a Function that contains the logic for dividing the list
     * and returns a Map with two entries – true and false.
     *
     * The true key maps to a List of elements that satisfy the condition specified in the Function;
     * the false key maps to a List of elements that do not.
     */
    @Test
    public void vavr_list3() {
        List<String> list = List.of("Java", "PHP", "Jquery", "JavaScript", "JShell", "JAVA");
        Iterator<List<String>> iterator = list.grouped(2);
        assertEquals(iterator.head().size(), 2);

        Map<Boolean, List<String>> map = list.groupBy(e -> e.startsWith("J"));
        assertEquals(map.size(), 2);
        assertEquals(map.get(false).get().size(), 1);
        assertEquals(map.get(true).get().size(), 5);
    }

    /**
     * We can also interact with a List using stack semantics – last-in-first-out (LIFO) retrieval of elements.
     * To this extent, there are API methods for manipulating a stack such as peek(), pop() and push():
     */
    @Test
    public void vavr_list4() {
        List<Integer> intList = List.empty();

        List<Integer> intList1 = intList.pushAll(List.rangeClosed(5,10));

        assertTrue(intList.isEmpty());
        assertEquals(intList1.peek(), Integer.valueOf(10));

        List<Integer> intList2 = intList1.pop();
        assertEquals(intList2.size(), (intList1.size() - 1) );
    }

    /**
     * Queue
     *
     * A Queue internally consists of two linked lists, a front List, and a rear List.
     * The front List contains the elements that are dequeued, and the rear List contains the elements that are enqueued.
     * This allows enqueue and dequeue operations to perform in O(1).
     * When the front List runs out of elements, front and rear List's are swapped, and the rear List is reversed.
     */
    @Test
    public void vavr_queue() {
        Queue<Integer> queue = Queue.of(1, 2, 3);
        Queue<Integer> secondQueue = queue.enqueueAll(List.of(4,5));

        assertEquals(3, queue.size());
        assertEquals(5, secondQueue.size());

        Tuple2<Integer, Queue<Integer>> result = secondQueue.dequeue();
        assertEquals(Integer.valueOf(1), result._1);

        Tuple2<Integer, Queue<Integer>> result2 = result._2.dequeue();
        assertEquals(Integer.valueOf(2), result2._1);

        Queue<Integer> tailQueue = result._2;
        assertFalse(tailQueue.contains(secondQueue.get(0)));
    }

    @Test
    public void vavr_queue2() {
        Queue<Integer> queue = Queue.of(1, 2, 3);

        Queue<Queue<Integer>> queue1 = queue.combinations(2);
        assertEquals(queue1.get(2).toCharSeq(), CharSeq.of("23"));
    }

    /**
     * Stream
     *
     * Implementation of a lazy linked list and is quite different from java.util.stream.
     * Unlike java.util.stream, the Vavr Stream stores data and is lazily evaluating next elements.
     *
     * Vavr Stream is immutable and may be Empty or Cons. A Cons consists of a head element and a lazy computed
     * tail Stream. Unlike a List, for a Stream, only the head element is kept in memory. The tail elements are computed on demand.
     */
    @Test
    public void vavr_stream() {
        Stream<Integer> intStream = Stream.iterate(0, i -> i + 1)
                .take(10);

        assertEquals(10, intStream.size());
        assertEquals(Integer.valueOf(9), intStream.last());

        long evenSum = intStream.filter(i -> i % 2 == 0)
                .sum()
                .longValue();

        assertEquals(20, evenSum);
    }

    @Test
    public void vavr_stream2() {
        Stream<Integer> s1 = Stream.tabulate(10, i -> i + 1);
        assertEquals(10, s1.size());
        assertEquals(s1.get(2).intValue(), 3);
    }

    @Test
    public void vavr_stream3() {
        Stream<Integer> s = Stream.of(2,1,3,4);

        Stream<Tuple2<Integer, Integer>> s2 = s.zip(List.of(7,8,9));
        Tuple2<Integer, Integer> t1 = s2.get(0);

        assertEquals(t1._1().intValue(), 2);
        assertEquals(t1._2().intValue(), 7);
    }

    /**
     * Array
     *
     * Immutable, indexed, sequence that allows efficient random access. It is backed by a Java array of objects.
     * Essentially, it is a Traversable wrapper for an array of objects of type T.
     */
    @Test
    public void vavr_array() {
        Array<Integer> arr = Array.of(1, 3, 5);

        Array<Integer> arr2 = Array.range(1, 10);
        assertEquals(9, arr2.size());

        Array<Integer> arr3 = Array.rangeBy(1, 10, 2);
        assertEquals(5, arr3.size());
    }

    @Test
    public void vavr_array2() {
        Array<Integer> intArray = Array.of(1, 2, 3);
        Array<Integer> newArray = intArray.removeAt(1);

        assertEquals(2, newArray.size());
        assertEquals(3, intArray.size());
        assertEquals(1, newArray.get(0).intValue());
        assertEquals(3, newArray.get(1).intValue());

        Array<Integer> array2 = intArray.replace(1, 5);
        assertEquals(array2.get(0).intValue(), 5);

        Array<Integer> array3 = intArray.update(2, 99);
        assertEquals(array3.get(2).intValue(), 99);
    }


    // Vavr - Set
    // The unique feature of the Set data structure is that it doesn't allow duplicate values.

    /**
     * HashSet
     *
     * Basic Implementation of Set
     */
    @Test
    public void vavr_hashSet() {
        HashSet<Integer> set0 = HashSet.rangeClosed(1,5);
        HashSet<Integer> set1 = HashSet.rangeClosed(3, 6);

        assertEquals(set0.union(set1), HashSet.rangeClosed(1,6));
        assertEquals(set0.diff(set1), HashSet.rangeClosed(1,2));
        assertEquals(set0.intersect(set1), HashSet.rangeClosed(3,5));
    }

    @Test
    public void vavr_hashSet2() {
        HashSet<String> set = HashSet.of("Red", "Green", "Blue");
        HashSet<String> newSet = set.add("Yellow");
        HashSet<String> newestSet = newSet.add("Yellow");

        assertEquals(3, set.size());
        assertEquals(4, newSet.size());
        assertEquals(4, newestSet.size());
        assertTrue(newestSet.contains("Yellow"));
    }

    /**
     * TreeSet
     *
     * An immutable TreeSet is an implementation of the SortedSet interface.
     * It stores a Set of sorted elements and is implemented using binary search trees
     * All its operations run in O(log n) time.
     * By default, elements of a TreeSet are sorted in their natural order.
     */
    @Test
    public void vavr_treeSet() {
        SortedSet<String> set = TreeSet.of("Red", "Green", "Blue");
        assertEquals("Blue", set.head());

        SortedSet<Integer> intSet = TreeSet.of(1,2,3);
        assertEquals(2, intSet.average().get().intValue());
    }

    @Test
    public void vavr_treeSet2() {
        SortedSet<String> reversedSet
                = TreeSet.of(Comparator.reverseOrder(), "Green", "Red", "Blue");
        assertEquals("Red", reversedSet.head());

        String str = reversedSet.mkString(" and ");
        assertEquals("Red and Green and Blue", str);
    }
}
