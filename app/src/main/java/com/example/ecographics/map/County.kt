package com.example.ecographics.map

import android.graphics.Color
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import kotlin.math.roundToInt

class County(
    name:String,
    type:String,
    vectorTemp:VectorMasterView) {

    var name = name
    var colors:Array<String> = emptyArray()
    var type = type
    var vectorTemp = vectorTemp
    var tempkonstant = 5
    var regnkonstant = 10

    var tempMap:HashMap<Int,Float> = HashMap<Int,Float>()
    var regnMap:HashMap<Int,Float> = HashMap<Int,Float>()

    fun changeColor(currentYear:Int,datatype:String,
                    colorsArray:Array<String> = arrayOf("#00FFFF","#00F0FF",
        "#00D6FF","#00B7FF", "#24A7FD", "#4694E9", "#6F7DCF", "#876DBF",
        "#9566B4", "#B653A0", "#DF3A86", "#EC3A86", "#DE2B55","#F6171E",
        "#E01700","#C61700","#000000")){
        colors = colorsArray

        var colorNumber:Float? = tempMap[currentYear]
        if(colorNumber==null){
            colorNumber = 11f
        }
        var colorNumberInt = 0

        when(datatype){
            "temp" -> {
                colorNumberInt = colorNumber.roundToInt()+tempkonstant
            }
            "regn" -> {
                colorNumberInt = colorNumber.roundToInt()+regnkonstant
            }
        }

        var actualColor = Color.parseColor(colors[colorNumberInt])

        when(type){
            "single" -> vectorTemp.getPathModelByName(name).fillColor = actualColor
            "group" -> vectorTemp.getGroupModelByName(name).pathModels
                .forEach { it.fillColor=actualColor }
            "test" -> println("success")
        }
    }

    fun changeColorRain(currentYear: Int, colorsArray:Array<String> = arrayOf("#00FFFF","#00F0FF",
        "#00D6FF","#00B7FF", "#24A7FD", "#4694E9", "#6F7DCF", "#876DBF",
        "#9566B4", "#B653A0", "#DF3A86", "#EC3A86", "#DE2B55","#F6171E",
        "#E01700","#C61700","#000000")){

        colors = colorsArray

        var colorNumber:Float? = regnMap[currentYear]

        if(colorNumber==null){
            colorNumber = 320f
        }

        var colorNumberInt = (colorNumber / 200).roundToInt()

        if(colorNumberInt > 16){
            colorNumberInt = 15
        }

        var actualColor = Color.parseColor(colors[colorNumberInt])

        when(type){
            "single" -> vectorTemp.getPathModelByName(name).fillColor = actualColor
            "group" -> vectorTemp.getGroupModelByName(name).pathModels
                .forEach { it.fillColor=actualColor }
            "test" -> println("success")
        }
    }

    fun addTemp(year:Int,temp:Float){
        tempMap.put(year,temp)
    }

    fun addRegn(year:Int, regnmengde:Float){
        regnMap.put(year,regnmengde)
    }

}