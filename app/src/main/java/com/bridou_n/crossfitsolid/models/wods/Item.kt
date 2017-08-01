package com.bridou_n.crossfitsolid.models.wods

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

/**
 * Created by bridou_n on 01/08/2017.
 */

@Root(name = "item", strict = false)
class Item {
    @set:Element(name = "title")
    @get:Element(name = "title")
    var title: String? = null

    @set:Element(name = "link")
    @get:Element(name = "link")
    var link: String? = null

    @set:Element(name = "pubDate")
    @get:Element(name = "pubDate")
    var pubDate: String? = null

    @Namespace(prefix = "dc")
    @set:Element(name = "creator", data = true)
    @get:Element(name = "creator", data = true)
    var creator: String? = null

    @set:Element(name = "category")
    @get:Element(name = "category")
    var category: String? = null

    @set:Element(name = "description", data = true)
    @get:Element(name = "description", data = true)
    var description: String? = null
}