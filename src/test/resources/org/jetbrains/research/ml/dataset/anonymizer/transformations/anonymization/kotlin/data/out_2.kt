fun main() {
    val main_v1 = arrayListOf(1, 2, 3, 4, 5, 6)
    main_v1.map { it -> it + 1}.foldRight(0, { main_v1_l1_p1, main_v1_l1_p2 -> main_v1_l1_p1 + main_v1_l1_p2 } )
}
