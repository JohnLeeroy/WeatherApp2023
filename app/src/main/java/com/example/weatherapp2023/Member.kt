package com.example.weatherapp2023

data class Member(
    val id: Long,
    val discordId: Long,
    val discordUsername: String,
    val discordDisplayName: String,
    val cbName: String,
    val group: String
)

//data class CBHero()


enum class HouseRole { Liege, Seneschal, Treasurer, Member }

interface HouseResponsibility { }

sealed class WarRole()

data class House(val id: Long, val name: String, val liege: Member, val members: List<Member>)

data class Stack(val id: Long, val name: String, val lead: Member, val members: List<Member>)

data class Squad(val id: Long, val name: String, val lead: Member, val members: List<Member>)