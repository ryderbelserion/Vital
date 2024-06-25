package com.ryderbelserion.vital.discord.util

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

public object RoleUtil {

    public fun getRoleName(role: Role?): String {
        return role?.name ?: ""
    }

    public fun getHighestRole(member: Member): Role? {
        return if (member.roles.isEmpty()) null else member.roles[0]
    }

    public fun getHighestRoleWithColor(member: Member): Role? {
        for (role in member.roles) {
            if (role.color == null) continue

            return role
        }

        return null
    }
}