package com.example.myintermediate

import com.example.myintermediate.data.remote.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse() : List<ListStoryItem> {
        val items : MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "name $i",
                description = "description $i",
                createdAt = "createdAt $i",
                photoUrl = "photoUrl $i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}