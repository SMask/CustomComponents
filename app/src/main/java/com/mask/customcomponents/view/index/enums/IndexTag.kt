package com.mask.customcomponents.view.index.enums

/**
 * 索引 Tag
 *
 * Create by lishilin on 2025-05-21
 */
enum class IndexTag(val value: String) {

    TOP("↑"), // 返回顶部
    OTHER("#"), // 其他

    A("A"), B("B"), C("C"), D("D"), E("E"), F("F"), G("G"),
    H("H"), I("I"), J("J"), K("K"), L("L"), M("M"), N("N"),
    O("O"), P("P"), Q("Q"), R("R"), S("S"), T("T"),
    U("U"), V("V"), W("W"), X("X"), Y("Y"), Z("Z");

    companion object {
        val ARR = arrayOf(
            TOP,
            A, B, C, D, E, F, G,
            H, I, J, K, L, M, N,
            O, P, Q, R, S, T,
            U, V, W, X, Y, Z,
            OTHER
        )
    }
}