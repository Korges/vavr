package com.korges.vavr;

import io.vavr.Tuple2;
import io.vavr.collection.CharSeq;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import org.junit.Test;

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
}
