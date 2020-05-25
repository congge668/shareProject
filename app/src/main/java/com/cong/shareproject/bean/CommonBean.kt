package com.cong.shareproject.bean

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList


data class User7DayBean(
    val user_7day: List<User7day>? = null
)

data class User7day(
    val amount: Float? = null,
    val display_date: String? = null
)

data class AreaDataBean(
    val areaData: List<AreaData>? = null
)

data class AreaData(
    val num: Int? = null,
    val source: String? = null
)

data class TwoDataBean(
    val studyDetail: StudyDetail? = null
)

data class StudyDetail(
    val memberCount: List<MemberCount>? = null,
    val studyHour: List<StudyHour>? = null
)

data class MemberCount(
    val day: String? = null,
    val number: Int = 0
)

data class StudyHour(
    val day: String? = null,
    val number: Float = 0f
)



