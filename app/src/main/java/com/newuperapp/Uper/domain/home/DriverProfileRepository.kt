package com.newuperapp.Uper.domain.home

import kotlinx.coroutines.flow.Flow

interface DriverProfileRepository {
    fun observeProfile(): Flow<DriverProfile>
}

/**
 * بالتطبيق الحقيقي هاي بتتوصل بسوكيت أو polling للباك-إند. الـ Fake impl
 * (بملف data/home) لغرض المعاينة والتطوير بس، بدّليها بتنفيذ حقيقي قبل الإنتاج.
 */
interface RideRequestRepository {
    /** بترجع طلب رحلة جديد كل ما توصل واحد، أو null لإخفاء البطاقة الحالية. */
    fun observeIncomingRequests(): Flow<RideRequest?>
}