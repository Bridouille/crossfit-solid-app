package com.bridou_n.crossfitsolid.models.wods

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Commit
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bridou_n on 01/08/2017.
 */

@Root(name = "item", strict = false)
open class Item : RealmObject() {
    @set:Element(name = "title")
    @get:Element(name = "title")
    var title: String? = null

    @set:Element(name = "link")
    @get:Element(name = "link")
    var link: String? = null

    @Ignore
    @set:Element(name = "pubDate")
    @get:Element(name = "pubDate")
    var xmlDate: String? = null

    @PrimaryKey
    var pubDate: Long? = null

    @Commit
    private fun sanitizeData() {
        // Date parsing
        if (xmlDate != null) {
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)

            try {
                val date = sdf.parse(xmlDate)
                pubDate = date.time
            } catch (e: ParseException) { // If there is an error with the date format we receive..
                pubDate = Date().time
            }
        }

        // Trim the wod title to remove the word "password"
        title = title?.replace("Lösenordsskyddad:", "")?.trim()

        // Removes the last link (to the full article)
        content = content?.substringBeforeLast("<p>")

        // Unwrap the content of the last "<p>this one</p>" so we don't have an extra line break
        val lastP = content?.substringAfterLast("<p>")?.replace("</p>", "")
        content = content?.substringBeforeLast("<p>") + lastP

        // Removes the " characters
        content = content?.replace("&#8221;", "")
    }

    @Namespace(prefix = "dc")
    @set:Element(name = "creator", data = true)
    @get:Element(name = "creator", data = true)
    var creator: String? = null

    @set:Element(name = "category")
    @get:Element(name = "category")
    var category: String? = null

    @Namespace(prefix = "content")
    @set:Element(name = "encoded", data = true)
    @get:Element(name = "encoded", data = true)
    var content: String? = null
}