fun main() {
    val l = arrayListOf(1, 2, 3, 4, 5, 6)
    l.map { it -> it + 1}.foldRight(0, { c, a -> c + a } )
}
