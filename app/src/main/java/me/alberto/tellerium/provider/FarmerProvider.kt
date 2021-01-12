package me.alberto.tellerium.provider

import android.content.*
import android.content.UriMatcher.NO_MATCH
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import me.alberto.tellerium.util.FARM_DATABASE
import me.alberto.tellerium.util.TABLE_NAME

class FarmerProvider : ContentProvider() {
    companion object {
        const val DB_VERSION = 1
        private const val AUTHORITY = "me.alberto.tellerium.FarmerProvider"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY//$TABLE_NAME")
        const val ID = "id"
        const val IMAGE_URL = "imageUrl"
        const val NAME = "name"
        const val GENDER = "gender"
        const val ADDRESS = "address"
        const val DOB = "dob"
        const val FARMS = "farms"
        const val FARMERS_CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.me.alberto.tellerium.farmer_item"
        const val FARMERS_ID_CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.me.alberto.tellerium.farmer_item"
    }

    private val FARMERS = 1
    private val FARMERS_ID = 2
    private val uriMatcher = UriMatcher(NO_MATCH)
    private var farmDatabaseHelper: FarmerDbHelper? = null

    init {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, FARMERS)
        uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", FARMERS_ID)
    }

    override fun onCreate(): Boolean {
        farmDatabaseHelper = FarmerDbHelper(context!!, FARM_DATABASE, DB_VERSION)
        return farmDatabaseHelper != null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val queryBuilder = SQLiteQueryBuilder().apply { tables = TABLE_NAME }
        when (uriMatcher.match(uri)) {
            FARMERS_ID -> queryBuilder.appendWhere("$ID=${uri.lastPathSegment}")
            FARMERS -> { /*  Don't do nothing */ }
            else -> throw java.lang.IllegalArgumentException("Unknown URI - $uri")
        }

        val cursor = queryBuilder.query(
            farmDatabaseHelper?.readableDatabase,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            FARMERS -> FARMERS_CONTENT_TYPE
            FARMERS_ID -> FARMERS_ID_CONTENT_ITEM_TYPE
            else -> throw IllegalArgumentException("Unknown Uri: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val uriType = uriMatcher.match(uri)
        val db = farmDatabaseHelper?.writableDatabase
        val id = when (uriType) {
            FARMERS -> db?.insert(TABLE_NAME, null, values)
            else -> throw IllegalArgumentException("Unknown Url: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return Uri.parse("$TABLE_NAME/$id")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val uriType = uriMatcher.match(uri)
        val db = farmDatabaseHelper?.writableDatabase
        val rowsDeleted = when (uriType) {
            FARMERS -> db?.delete(TABLE_NAME, selection, selectionArgs)
            FARMERS_ID -> {
                val id = uri.lastPathSegment
                if (TextUtils.isEmpty(selection)) {
                    db?.delete(TABLE_NAME, "$ID=$id", null)
                } else {
                    db?.delete(TABLE_NAME, "$ID=$id AND $selection", selectionArgs)
                }
            }
            else -> throw IllegalArgumentException("Unknown Uri: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return rowsDeleted ?: 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val uriType = uriMatcher.match(uri)
        val db = farmDatabaseHelper?.writableDatabase
        val rowsUpdated = when (uriType) {
            FARMERS -> db?.update(TABLE_NAME, values, selection, selectionArgs)
            FARMERS_ID -> {
                val id = uri.lastPathSegment
                if (TextUtils.isEmpty(selection)) {
                    db?.update(TABLE_NAME, values, "$ID=$id", null)
                } else {
                    db?.update(TABLE_NAME, values, "$ID=$id AND $selection", selectionArgs)
                }
            }
            else -> throw  IllegalArgumentException("Unknown Uri: $uri")
        }

        context?.contentResolver?.notifyChange(uri, null)
        return rowsUpdated ?: 0
    }
}