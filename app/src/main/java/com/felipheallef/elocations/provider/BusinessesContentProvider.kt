package com.felipheallef.elocations.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.media.UnsupportedSchemeException
import android.net.Uri
import com.felipheallef.elocations.Application

class BusinessesContentProvider : ContentProvider() {

    private lateinit var mUriMatcher: UriMatcher

    override fun onCreate(): Boolean {
        // TODO: Implement this to initialize your content provider on startup.
        mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTHORITY, "business", BUSINESS)
        mUriMatcher.addURI(AUTHORITY, "business/#", BUSINESS_BY_ID)

//        if (context != null) {
//
//        }

        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        if (mUriMatcher.match(uri) == BUSINESS_BY_ID) {
            val id = uri.lastPathSegment!!.toInt()
            val deleted = Application.database?.businessDao()?.deleteById(id)
            context?.contentResolver?.notifyChange(uri, null)
            return deleted as Int
        } else {
            throw UnsupportedSchemeException("The Uri provided is invalid.")
        }

    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        return when(mUriMatcher.match(uri)) {
            BUSINESS -> null
            BUSINESS_BY_ID -> null
            else -> null
        }

    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    companion object {
        const val AUTHORITY = "com.felipheallef.elocations.provider"
        val BASE_URI: Uri = Uri.parse("content://$AUTHORITY")
        val URI_BUSINESS: Uri = Uri.withAppendedPath(BASE_URI, "business")

        const val BUSINESS = 1
        const val BUSINESS_BY_ID = 2
    }
}