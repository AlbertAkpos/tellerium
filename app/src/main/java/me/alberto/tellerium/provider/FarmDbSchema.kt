package me.alberto.tellerium.provider

import android.content.ContentResolver
import android.provider.BaseColumns
import me.alberto.tellerium.util.TABLE_NAME

object FarmDbSchema {
    const val COL_ID = "id"
    const val IMAGE_URL = "imageUrl"
    const val NAME = "name"
    const val GENDER = "gender"
    const val ADDRESS = "address"
    const val DOB = "dob"
    const val FARMS = "farms"
    private const val AUTHORITY = "me.alberto.tellerium"

    const val CONTENT_URI = "content://$AUTHORITY//$TABLE_NAME"

    const val CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.me.alberto.tellerium.farmer_item"
    const val CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.me.alberto.tellerium.farmer_item"


}