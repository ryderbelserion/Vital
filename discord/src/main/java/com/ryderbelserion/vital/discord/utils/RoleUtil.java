package com.ryderbelserion.vital.discord.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities related to roles.
 *
 * @version 0.0.3
 * @since 0.0.1
 */
public class RoleUtil {

    /**
     * Utilities related to roles.
     *
     * @since 0.0.1
     */
    public RoleUtil() {
        throw new AssertionError();
    }

    /**
     * Get the highest {@link Role} with a {@link java.awt.Color}.
     *
     * @param member the {@link Member}
     * @return {@link Role}
     * @since 0.0.1
     */
    public static Role getHighestRoleWithColor(@NotNull final Member member) {
        if (member.getRoles().isEmpty()) return null;

        for (final Role role : member.getRoles()) {
            if (role.getColor() == null) continue;

            return role;
        }

        return null;
    }

    /**
     * Get the highest {@link Role} from a {@link Member}.
     *
     * @param member the {@link Member}
     * @return {@link Role}
     * @since 0.0.1
     */
    public static Role getHighestRole(@NotNull final Member member) {
        if (member.getRoles().isEmpty()) {
            return null;
        }

        return member.getRoles().getFirst();
    }

    /**
     * Get a name from {@link Role}.
     *
     * @param role the {@link Role}
     * @return the name of the {@link Role}
     * @since 0.0.1
     */
    public static String getRoleName(@NotNull final Role role) {
        return role.getName();
    }
}