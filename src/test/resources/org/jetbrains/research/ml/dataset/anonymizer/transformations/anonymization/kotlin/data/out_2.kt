fun main() {
    val main_v1 = arrayListOf(1, 2, 3, 4, 5, 6)
    main_v1.map { it + 1 }.foldRight(0, { main_l2_f1_p1, main_l2_f1_p2 -> main_l2_f1_p1 + main_l2_f1_p2 } )
}
