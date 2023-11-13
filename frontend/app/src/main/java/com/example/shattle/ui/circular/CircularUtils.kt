package com.example.shattle.ui.circular;

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CircularUtils {

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        // 프로젝트의 Drawable 리소스를 BitmapDescriptor 로 변환
        // (GoogleMap Marker 아이콘으로 쓰기 위함)

        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // Drawable 크기 설정
        vectorDrawable?.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )

        // Bitmap 생성
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        // Canvas 사용해서 Drawable 내용을 Bitmap 에 그림
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)

        // 해당 BitmapDescriptor 반환
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun bearingBetweenLocations(start: LatLng, end: LatLng): Float {
        // 시작점과 끝점으로 bearing 반환
        // (google Map route 위 화살표 회전용)

        val lat1 = Math.toRadians(start.latitude)
        val long1 = Math.toRadians(start.longitude)
        val lat2 = Math.toRadians(end.latitude)
        val long2 = Math.toRadians(end.longitude)

        val dLon = long2 - long1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

        val bearing = atan2(y, x)

        return Math.toDegrees(bearing).toFloat()
    }

    data class ArrowDirection(val position: LatLng, val direction: LatLng)

    val arrowData = listOf(
        // 정문
        ArrowDirection(LatLng(37.465526, 126.948502), LatLng(37.465319, 126.948548)),
        ArrowDirection(LatLng(37.464867, 126.948649), LatLng(37.464776, 126.948672)),
        ArrowDirection(LatLng(37.464252, 126.948782), LatLng(37.464131, 126.948807)),
        ArrowDirection(LatLng(37.463608, 126.948909), LatLng(37.463476, 126.948934)),
        // 법대
        ArrowDirection(LatLng(37.462742, 126.949081), LatLng(37.462616, 126.949107)),
        ArrowDirection(LatLng(37.462114, 126.949222), LatLng(37.461981, 126.949251)),
        ArrowDirection(LatLng(37.461522, 126.949337), LatLng(37.461413, 126.949328)),
        ArrowDirection(LatLng(37.460854, 126.94917), LatLng(37.460731, 126.949132)),
        // 자연대
        ArrowDirection(LatLng(37.460286, 126.948973), LatLng(37.460174, 126.948929)),
        ArrowDirection(LatLng(37.459569, 126.948709), LatLng(37.459469, 126.948689)),
        ArrowDirection(LatLng(37.458954, 126.948795), LatLng(37.458758, 126.948844)),
        ArrowDirection(LatLng(37.458201, 126.948984), LatLng(37.458041, 126.949027)),
        ArrowDirection(LatLng(37.457490, 126.949164), LatLng(37.457329, 126.949204)),
        // 농생대
        ArrowDirection(LatLng(37.456770, 126.949344), LatLng(37.456646, 126.949373)),
        ArrowDirection(LatLng(37.456135, 126.949515), LatLng(37.456032, 126.949545)),
        ArrowDirection(LatLng(37.455460, 126.949716), LatLng(37.455288, 126.949768)),
        // 공대입구
        ArrowDirection(LatLng(37.454737, 126.949938), LatLng(37.454572, 126.949990)),
        ArrowDirection(LatLng(37.454059, 126.950151), LatLng(37.453935, 126.950187)),
        // 신소재
        ArrowDirection(LatLng(37.453234, 126.950280), LatLng(37.453092, 126.950298)),
        ArrowDirection(LatLng(37.452603, 126.950286), LatLng(37.452512, 126.950247)),
        ArrowDirection(LatLng(37.451901, 126.949955), LatLng(37.451819, 126.949928)),
        ArrowDirection(LatLng(37.451142, 126.949809), LatLng(37.451010, 126.949788)),
        ArrowDirection(LatLng(37.450296, 126.949811), LatLng(37.450207, 126.949818)),
        ArrowDirection(LatLng(37.449434, 126.949749), LatLng(37.449331, 126.949719)),
        ArrowDirection(LatLng(37.448522, 126.949357), LatLng(37.448435, 126.949316)),
        ArrowDirection(LatLng(37.447641, 126.949196), LatLng(37.447580, 126.949236)),
        ArrowDirection(LatLng(37.447180, 126.949916), LatLng(37.447157, 126.950028)),
        ArrowDirection(LatLng(37.447047, 126.950859), LatLng(37.447040, 126.951001)),
        ArrowDirection(LatLng(37.447312, 126.951735), LatLng(37.447398, 126.951810)),
        ArrowDirection(LatLng(37.447857, 126.952043), LatLng(37.448003, 126.952039)),
        ArrowDirection(LatLng(37.448521, 126.952021), LatLng(37.448633, 126.952019)),
        // 제2공학관
        ArrowDirection(LatLng(37.449071, 126.952005), LatLng(37.449165, 126.952001)),
        ArrowDirection(LatLng(37.449769, 126.951983), LatLng(37.449909, 126.951979)),
        ArrowDirection(LatLng(37.450536, 126.952044), LatLng(37.450620, 126.952105)),
        ArrowDirection(LatLng(37.451252, 126.952437), LatLng(37.451324, 126.952471)),
        // 301동
        ArrowDirection(LatLng(37.451807, 126.952695), LatLng(37.451920, 126.952747)),
        ArrowDirection(LatLng(37.452569, 126.953046), LatLng(37.452660, 126.953089)),
        ArrowDirection(LatLng(37.453137, 126.953309), LatLng(37.453397, 126.953430)),
        ArrowDirection(LatLng(37.453740, 126.953590), LatLng(37.453818, 126.953647)),
        // 유전공학연구소
        ArrowDirection(LatLng(37.454249, 126.953942), LatLng(37.454337, 126.954003)),
        ArrowDirection(LatLng(37.454855, 126.954354), LatLng(37.454965, 126.954429)),
        ArrowDirection(LatLng(37.455424, 126.954763), LatLng(37.455544, 126.954853)),
        ArrowDirection(LatLng(37.455903, 126.955113), LatLng(37.455976, 126.955174)),
        // 교수회관
        ArrowDirection(LatLng(37.456330, 126.955470), LatLng(37.456425, 126.955548)),
        ArrowDirection(LatLng(37.456955, 126.955989), LatLng(37.457107, 126.956121)),
        ArrowDirection(LatLng(37.457564, 126.956309), LatLng(37.457673, 126.956336)),
        ArrowDirection(LatLng(37.458250, 126.956476), LatLng(37.458340, 126.956519)),
        ArrowDirection(LatLng(37.458769, 126.956756), LatLng(37.458840, 126.956806)),
        ArrowDirection(LatLng(37.459581, 126.957160), LatLng(37.459662, 126.957145)),
        ArrowDirection(LatLng(37.460289, 126.956854), LatLng(37.460347, 126.956823)),
        // 긱삼
        ArrowDirection(LatLng(37.461401, 126.955716), LatLng(37.461423, 126.955555)),
        ArrowDirection(LatLng(37.461886, 126.954537), LatLng(37.461999, 126.954546)),
        ArrowDirection(LatLng(37.462813, 126.954624), LatLng(37.462926, 126.954635)),
        ArrowDirection(LatLng(37.463747, 126.955000), LatLng(37.463853, 126.955063)),
        // 국제대학원
        ArrowDirection(LatLng(37.464468, 126.955217), LatLng(37.464575, 126.955211)),
        ArrowDirection(LatLng(37.465143, 126.955178), LatLng(37.465241, 126.955170)),
        ArrowDirection(LatLng(37.465750, 126.955011), LatLng(37.465788, 126.954960)),
        // 수의대
        ArrowDirection(LatLng(37.466198, 126.953895), LatLng(37.466172, 126.953716)),
        ArrowDirection(LatLng(37.466100, 126.953200), LatLng(37.466072, 126.953068)),
        // 경영대
        ArrowDirection(LatLng(37.465978, 126.952164), LatLng(37.465976, 126.951998)),
        ArrowDirection(LatLng(37.465962, 126.951414), LatLng(37.465958, 126.951266)),
        ArrowDirection(LatLng(37.465915, 126.950530), LatLng(37.465879, 126.950312)),
        ArrowDirection(LatLng(37.465745, 126.949650), LatLng(37.465715, 126.949510)),
        //ArrowDirection(LatLng(), LatLng()),
    )

    data class BusStop(val location: LatLng, val title: String, val snippet: String)

    val busStops = listOf(
        BusStop(LatLng(37.465783, 126.948402), "정문", ""),
        BusStop(LatLng(37.463030, 126.949023), "법대, 사회대입구", ""),
        BusStop(LatLng(37.460456, 126.949038), "자연대, 행정관입구", ""),
        BusStop(LatLng(37.457013, 126.949282), "농생대", ""),
        BusStop(LatLng(37.455034, 126.949846), "38동, 공대입구", ""),
        BusStop(LatLng(37.453581, 126.950239), "신소재", ""),
        BusStop(LatLng(37.448886, 126.952013), "제2공학관", ""),
        BusStop(LatLng(37.451623, 126.952611), "301동, 유회진학술정보관", ""),
        BusStop(LatLng(37.454127, 126.953861), "유전공학연구소", ""),
        BusStop(LatLng(37.456163, 126.955330), "교수회관입구", ""),
        BusStop(LatLng(37.461003, 126.956469), "관악사삼거리", ""),
        BusStop(LatLng(37.464251, 126.955216), "국제대학원", ""),
        BusStop(LatLng(37.466041, 126.954568), "수의대, 보건대학원", ""),
        BusStop(LatLng(37.465978, 126.952121), "경영대, 행정대학원", ""),
    )

    val roadCoordinates = listOf(
        LatLng(37.466229, 126.948175),
        LatLng(37.465851, 126.948367),
        LatLng(37.465783, 126.948402), //정문 앞
        LatLng(37.465733, 126.948429),
        LatLng(37.465527, 126.948501),
        LatLng(37.463030, 126.949023), //법대, 사회대입구 앞
        LatLng(37.461699, 126.949317),
        LatLng(37.461699, 126.949317),
        LatLng(37.461372, 126.949323),
        LatLng(37.460591, 126.949091),
        LatLng(37.460456, 126.949038), //자연대, 행정관입구 앞
        LatLng(37.459682, 126.948740),
        LatLng(37.459505, 126.948696),
        LatLng(37.459226, 126.948729),
        LatLng(37.458318, 126.948956),
        LatLng(37.457013, 126.949282), //농생대 앞
        LatLng(37.456586, 126.949388),
        LatLng(37.456412, 126.949431),
        LatLng(37.456161, 126.949508),
        LatLng(37.455907, 126.949583),
        LatLng(37.455667, 126.949655),
        LatLng(37.455420, 126.949729),
        LatLng(37.455124, 126.949816),
        LatLng(37.455034, 126.949846), //38동, 공대입구 앞
        LatLng(37.454777, 126.949924),
        LatLng(37.454529, 126.950001),
        LatLng(37.454262, 126.950087),
        LatLng(37.454084, 126.950144),
        LatLng(37.453877, 126.950206),
        LatLng(37.453581, 126.950239), //신소재 앞
        LatLng(37.453368, 126.950264),
        LatLng(37.453153, 126.950291),
        LatLng(37.452902, 126.950319),
        LatLng(37.452797, 126.950331),
        LatLng(37.452656, 126.950311),
        LatLng(37.452508, 126.950246),
        LatLng(37.452314, 126.950139),
        LatLng(37.452082, 126.950010),
        LatLng(37.451759, 126.949908),
        LatLng(37.451605, 126.949884),
        LatLng(37.451381, 126.949850),
        LatLng(37.451196, 126.949818),
        LatLng(37.450948, 126.949781),
        LatLng(37.450813, 126.949757),
        LatLng(37.450518, 126.949790),
        LatLng(37.450226, 126.949818),
        LatLng(37.449998, 126.949818),
        LatLng(37.449764, 126.949816),
        LatLng(37.449598, 126.949792),
        LatLng(37.449333, 126.949721), // (파워플랜트정류장)
        LatLng(37.449143, 126.949645),
        LatLng(37.448912, 126.949537),
        LatLng(37.448709, 126.949445),
        LatLng(37.448455, 126.949325),
        LatLng(37.448229, 126.949220),
        LatLng(37.448114, 126.949166),
        LatLng(37.448008, 126.949145),
        LatLng(37.447870, 126.949142),
        LatLng(37.447733, 126.949159),
        LatLng(37.447613, 126.949208),
        LatLng(37.447473, 126.949324),
        LatLng(37.447364, 126.949459),
        LatLng(37.447263, 126.949664),
        LatLng(37.447190, 126.949882), // (건환공)
        LatLng(37.447168, 126.949965),
        LatLng(37.447119, 126.950254),
        LatLng(37.447068, 126.950572),
        LatLng(37.447047, 126.950797),
        LatLng(37.447043, 126.951004),
        LatLng(37.447066, 126.951210),
        LatLng(37.447112, 126.951375),
        LatLng(37.447184, 126.951532),
        LatLng(37.447310, 126.951737),
        LatLng(37.447527, 126.951921),
        LatLng(37.447673, 126.952001),
        LatLng(37.447847, 126.952043),
        LatLng(37.448070, 126.952037),
        LatLng(37.448328, 126.952030),
        LatLng(37.448611, 126.952021),
        LatLng(37.448886, 126.952013), //제2공학관 앞
        LatLng(37.449424, 126.951992),
        LatLng(37.449915, 126.951981),
        LatLng(37.450329, 126.951968),
        LatLng(37.450436, 126.951985),
        LatLng(37.450620, 126.952102),
        LatLng(37.450773, 126.952214),
        LatLng(37.451028, 126.952338),
        LatLng(37.451623, 126.952611), //301동, 유회진학술정보관 앞
        LatLng(37.452085, 126.952823),
        LatLng(37.452579, 126.953050),
        LatLng(37.453121, 126.953302),
        LatLng(37.453490, 126.953471),
        LatLng(37.453709, 126.953573),
        LatLng(37.454127, 126.953861), //유전공학연구소 앞
        LatLng(37.454569, 126.954162),
        LatLng(37.455012, 126.954455),
        LatLng(37.455363, 126.954716),
        LatLng(37.455588, 126.954883),
        LatLng(37.455787, 126.955030),
        LatLng(37.455940, 126.955140),
        LatLng(37.456163, 126.955330), //교수회관입구 앞
        LatLng(37.456385, 126.955517),
        LatLng(37.456606, 126.955697),
        LatLng(37.456946, 126.955986),
        LatLng(37.457111, 126.956120),
        LatLng(37.457293, 126.956224),
        LatLng(37.457498, 126.956294),
        LatLng(37.457723, 126.956348),
        LatLng(37.457945, 126.956403),
        LatLng(37.458242, 126.956475),
        LatLng(37.458413, 126.956552),
        LatLng(37.458585, 126.956625),
        LatLng(37.458754, 126.956745),
        LatLng(37.458971, 126.956896),
        LatLng(37.459166, 126.957037),
        LatLng(37.459393, 126.957138),
        LatLng(37.459576, 126.957163),
        LatLng(37.459760, 126.957126),
        LatLng(37.460021, 126.956991),
        LatLng(37.460234, 126.956883),
        LatLng(37.460290, 126.956853),
        LatLng(37.460553, 126.956717),
        LatLng(37.460785, 126.956595),
        LatLng(37.461003, 126.956469), //기숙사삼거리 앞
        LatLng(37.461085, 126.956397),
        LatLng(37.461178, 126.956310),
        LatLng(37.461266, 126.956157),
        LatLng(37.461320, 126.956030),
        LatLng(37.461387, 126.955821),
        LatLng(37.461440, 126.955430),
        LatLng(37.461479, 126.955146),
        LatLng(37.461513, 126.954956),
        LatLng(37.461561, 126.954789),
        LatLng(37.461618, 126.954673),
        LatLng(37.461672, 126.954606),
        LatLng(37.461777, 126.954545),
        LatLng(37.461899, 126.954537),
        LatLng(37.462116, 126.954555),
        LatLng(37.462385, 126.954583),
        LatLng(37.462610, 126.954604),
        LatLng(37.462858, 126.954628),
        LatLng(37.463045, 126.954647),
        LatLng(37.463218, 126.954704),
        LatLng(37.463444, 126.954829),
        LatLng(37.463784, 126.955018),
        LatLng(37.464043, 126.955165),
        LatLng(37.464125, 126.955195),
        LatLng(37.464251, 126.955216), //국제대학원 앞
        LatLng(37.464417, 126.955221),
        LatLng(37.464660, 126.955206),
        LatLng(37.465002, 126.955185),
        LatLng(37.465293, 126.955168),
        LatLng(37.465495, 126.955146),
        LatLng(37.465627, 126.955100),
        LatLng(37.465744, 126.955016),
        LatLng(37.465870, 126.954864),
        LatLng(37.465934, 126.954751),
        LatLng(37.466041, 126.954568), //수의대, 보건대학원 앞
        LatLng(37.466099, 126.954469),
        LatLng(37.466173, 126.954275),
        LatLng(37.466206, 126.954107),
        LatLng(37.466199, 126.953899),
        LatLng(37.466102, 126.953207),
        LatLng(37.466029, 126.952834),
        LatLng(37.465985, 126.952483),
        LatLng(37.465978, 126.952121), //경영대, 행정대학원 앞
        LatLng(37.465972, 126.951728),
        LatLng(37.465959, 126.951407),
        LatLng(37.465949, 126.950842),
        LatLng(37.465901, 126.950432),
        LatLng(37.465689, 126.949390),
        LatLng(37.465526, 126.948499),
    )

}
