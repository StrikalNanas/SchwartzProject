package com.example.data.model

import com.example.domain.model.UserKey

fun UserKey.userKeyMapToData(): UserKeyApi {
    return UserKeyApi(
        userKeyApi = userKey
    )
}