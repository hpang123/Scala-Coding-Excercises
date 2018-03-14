/*
 * collection.immutable package is automatically add to the current namespace
 * but collection.mutable need to import
 */
import scala.collection.mutable

object CollectionTest {
  def main(argv: Array[String]) {
    
    /*
     * Sequences (Seq->Iterable->Traversable)
     * stored in specific order
     * IndexedSeq and LinearSeq
     * 
     */
    
    val x = Seq(1,2,3) //By default, Seq creates a List
    println(x) //List(1, 2, 3)
    
    println(x(0))
    val x1 = IndexedSeq(1,2,3) //by default it creates a Vector
    //x1(1) = 4 //not allowed to update
    println(x1) //Vector(1, 2, 3)
    
    /*
     * Set (Set->Iterable->Traversable)
     * Unique elements
     * SortedSet, BitSet
     * 
     */
    val set = Set(1,2,3,3) //by default, creates an immutable Set
    println(set) //Set(1, 2, 3)
    
    /* 
     * Map (Map->Iterable->Traversable)
     * key/value pair
     * 
     * SortedMap
     */
    val map = Map(1->"a", 2->"b", 3->"c")
    
    /*
     * Immutable Sequence
     * IndexedSeq(Vector, Range)
     * LinearSeq(List, Stack, Queue, Stream)
     */
    val imX = scala.collection.immutable.IndexedSeq(1,2,3)
    println(imX) //Vector(1, 2, 3)
    
    val imX1 = scala.collection.immutable.LinearSeq(1,2,3)
    println(imX1) //List(1, 2, 3)
    
    val imX2 = scala.collection.immutable.Seq(1,2,3)
    println(imX2) //List(1, 2, 3)
    println(imX1==imX2) //true
    println(imX1==x) //true
    
    /*
     * Immutable Set
     * (HashSet, SortedSet<-TreeSet, BitSet, ListSet)
     */
    val imSet = collection.immutable.Set(1,2,3)
    
    val imSet1 = collection.immutable.SortedSet(2,3,1)
    println(imSet1) //TreeSet(1,2,3)
    
    /*
     * Immutable Map
     * (HashMap, SortedMap<-TreeMap, ListMap)
     *
     */
    val imMap = collection.immutable.SortedMap(1->"a", 3->"c", 2->"b")
    //imMap(1) ="d" //not allowed to update
    
    println(imMap) //Map(1 -> a, 2 -> b, 3 -> c)
    
    /*
     * Mutable Seq (IndexedSeq, Buffer(ArrayBuffer, ListBuffer), LinearSeq)
     */
    val buffer = collection.mutable.Buffer(1,2,3)
    println(buffer) //ArrayBuffer(1, 2, 3)
    buffer(1) =5
    println(buffer)
    
    val mSeq = mutable.Seq(1,2,3)
    mSeq(1) = 6
    println(mSeq) //ArrayBuffer(1, 6, 3)
    
    val mSeq1 = mutable.LinearSeq(2,1,3)
    println(mSeq1) //MutableList(2, 1, 3)
    
    val mSeq2 = mutable.IndexedSeq(1,2,3) //ArrayBuffer
    mSeq2(1) = 6
    println(mSeq2==mSeq) //true
    
    val mSet = mutable.Set(1,2,3)
    val mSet1 = mutable.SortedSet(3,1,2)
    mSet1.add(10)
    println(mSet1) //TreeSet(1, 2, 3, 10)
    
    val mMap = mutable.Map(1->"a", 2->"b", 3->"c")
    mMap(2) = "x"
    println(mMap) //Map(2 -> x, 1 -> a, 3 -> c)
    println("get 2 from map " + (mMap get 2))
    
    //Vector created by IndexedSeq
    val vector = IndexedSeq(1,2,3)
    println(vector(0))
    val b = vector ++ Vector(4,5)
    println(b)
    
    val c = b.updated(0, "x")
    println(c) //Vector(x, 2, 3, 4, 5), but b is not change
    val d = b.take(3)
    println(d) //Vector(1, 2, 3)
    val  b1 = b.filter(_>2) //Vector(3, 4, 5)
    println(b1)
    
    val b2 = b :+ 11 //Vector(1, 2, 3, 4, 5, 11)
    println(b2)
    b2.foreach {println}
   
    /*
     * Stream is computed lazily
     *
     */
    val stream = 1 #:: 2 #:: 3 #:: Stream.empty //stream use #:: instead of ::
    println(stream) //Stream(1, ?) ? means the end
    val stream1 = (1 to 1000000000).toStream
    println(stream1.head)
    println(stream1.tail) //Stream(2, ?)
    
    /* Mutable Collection */
    val m = imMap.toBuffer //convert immutable map to mutable map
    println(m) //ArrayBuffer((1,a), (2,b), (3,c))
    m += (4 -> "d")
    val newMap = m.toMap //back to immutable Map
    
    val ints = mutable.Queue[Int]()
    ints += 1
    ints += (2,3)
    ints.enqueue(4)
    println(ints.dequeue) //1
    println(ints) //Queue(2, 3, 4)
    ints.foreach { println }
    
    val stack = mutable.Stack[Int](1,2,3)
    stack.push(4)
    println(stack) //Stack(4, 1, 2, 3)
    stack.push(4,5,6)
    println(stack) //Stack(6, 5, 4, 4, 1, 2, 3)
    println(stack.pop) // 6
    
    
  }
}