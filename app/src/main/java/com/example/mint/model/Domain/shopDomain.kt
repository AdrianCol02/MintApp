package com.example.mint.model.Domain

class shopDomain {
    private var title: String? = null
    private var tipoTienda: String? = null
    private var picPath: String? = null

    constructor(title: String?, tipoTienda: String?, picPath: String?) {
        this.title = title
        this.tipoTienda = tipoTienda
        this.picPath = picPath
    }
}