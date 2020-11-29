fun main() {
    val l = arrayListOf(1, 2, 3, 4, 5, 6)
    l.map { it + 1}.foldRight(0, { c, a -> c + a } )
}
