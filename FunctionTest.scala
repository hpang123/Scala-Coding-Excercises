

object FunctionTest {
  def main(args: Array[String]): Unit = {

    /* function literal 
     * it is instantiated into object called function values
     * It extends one of the FunctionN traits, such as Funtion0, Function1 to Funtion22,
     * depend on the number of arguments, chosen by compiler
     */
    val add = (x:Int, y:Int) => x + y
    add(1,2) //it will call the apply method of the function
    
    //It will Function2
    val areaOfRectangle: (Int, Int) => Int = (width:Int, height:Int)=> width*height
    val areaOfRectangle1: (Int, Int) => Int = (width, height)=> width*height
    
    /* define a function by implementing an appropriate Function Trait and define
     * its required apply method: 2 parameters Int, Int, and return Int
     */
    val areaOfRectangle2: (Int, Int) => Int = new Function2[Int, Int, Int]{
       def apply(width: Int, height: Int): Int ={
         width*height
       }
    }
    
    areaOfRectangle.apply(5,3)
    println(areaOfRectangle1(5,3))
    println(areaOfRectangle2(5,3))
    
    /*Function as Parameter
     * 
     */
    def operation(f: (Int, Int) => Int) {
      println(f(4,4))
    }
    
    operation(add)
    
    val subtract = (x: Int, y:Int) => {x-y}
    val muptiply = (x: Int, y:Int) => {x*y}
    operation(subtract)
    operation(muptiply)
    
    /* return function */
    def getOperation() = add
    val addOperation = getOperation()
    println(addOperation(1,3))
    
    /* Closure: function whose return value depends on the value of one or more variables
     * declared outside this function
     * 
     */
    var y = 3
    val multiplier = (x:Int) => x*y
    println(multiplier(3))
    
    /* Curried Function
     * 
     */
    def Add(x: Int)(y:Int) = x +y
    def Add1(x: Int) = (y:Int) => x+y //same as above
    println(Add1(2)(2))
    val addTwo = Add1(2) // return (y:Int) => 2+y
    println(addTwo(2))
    
  }
}