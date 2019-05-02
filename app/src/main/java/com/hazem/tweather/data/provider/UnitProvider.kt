package com.hazem.tweather.data.provider

import com.hazem.tweather.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}