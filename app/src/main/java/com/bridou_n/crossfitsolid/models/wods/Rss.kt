package com.bridou_n.crossfitsolid.models.wods

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by bridou_n on 01/08/2017.
 */

@Root(name = "rss", strict = false)
class Rss {
    @set:Element(name = "channel")
    @get:Element(name = "channel")
    var channel: Channel? = null
}