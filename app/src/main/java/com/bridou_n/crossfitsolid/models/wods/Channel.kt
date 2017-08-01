package com.bridou_n.crossfitsolid.models.wods

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by bridou_n on 01/08/2017.
 */

@Root(name = "channel", strict = false)
class Channel {
    @set:Element(name = "title")
    @get:Element(name = "title")
    var title: String? = null

    @set:Element(name = "description")
    @get:Element(name = "description")
    var description: String? = null

    @set:ElementList(name = "item", inline = true)
    @get:ElementList(name = "item", inline = true)
    var items: ArrayList<Item>? = null
}