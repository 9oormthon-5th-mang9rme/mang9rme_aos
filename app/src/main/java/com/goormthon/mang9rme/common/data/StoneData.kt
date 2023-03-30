package com.goormthon.mang9rme.common.data

/**
 * 홈 화면에서 사용하는 돌 데이터 클래스
 *
 * @param imageUrl        돌 이미지 url
 * @param stoneName     돌 닉네임
 * @param stoneType     돌 종류
 * @param dateTime          기록한 시각
 * @param address       기록한 위치의 주소
 * @param lat           기록한 위치의 위도
 * @param lng           기록한 위치의 경도
 * @param level         돌 레벨
 * @param rarity        돌 희귀도
 * @param attack        공격력
 * @param defense       방어력
 * @param magicDefense  마법저항력
 */
data class StoneData(
    val stoneId : Int,
    val imageUrl: String,
    val stoneName : String,
    val stoneType: String,
    val dateTime: String,
    val address: String,
    val lat: String,
    val lng: String,
    val level: String,
    val rarity: String,
    val attack: Int,
    val defense: Int,
    val magicDefense: Int
) : java.io.Serializable {

    override fun toString(): String {
        return "${TAG}{" +
                "imgUrl=$imageUrl, " +
                "stoneType=$stoneType, " +
                "time=$dateTime, " +
                "address=$address, " +
                "lat=$lat, " +
                "lng=$lng, " +
                "level=$level, " +
                "rarity=$rarity}"
    }

    companion object {
        private const val TAG: String = "StoneData"
    }
}