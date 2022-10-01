package com.example.composeapp.domain.use_case

import java.util.Calendar

class GetPassedTimeUseCase {
    operator fun invoke(timeStamp:Long) : String {
        // get current time in millis
        val currentMillis = Calendar.getInstance().timeInMillis
        // find passed time in millis
        val passedTime = currentMillis - timeStamp
        // time periods in millis
        val minute = 1000*60L
        val hour = 60*minute
        val day = 24*hour
        val month = 30*day
        val year = 12*month

        val period:Long

        if(passedTime/year != 0L){
            period = passedTime/year
            return "$period year${if(period>1) "s" else ""} ago"
        }else if(passedTime/month != 0L){
            period = passedTime/month
            return "$period month${if(period>1) "s" else ""} ago"
        }else if(passedTime/day != 0L){
            period = passedTime/day
            return "$period day${if(period>1) "s" else ""} ago"
        }else if(passedTime/hour != 0L){
            period = passedTime/hour
            return "$period hour${if(period>1) "s" else ""} ago"
        }else{
            period = passedTime/minute
            return "$period minute${if(period>1) "s" else ""} ago"
        }
    }
}