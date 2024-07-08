/*
 * This file is generated by jOOQ.
 */
package adapters.postgresql.generated.tables


import adapters.postgresql.generated.Public
import adapters.postgresql.generated.indexes.USER_PROFILE_EMAIL_UNIQUE
import adapters.postgresql.generated.keys.USER_PROFILE_EMAIL_KEY
import adapters.postgresql.generated.keys.USER_PROFILE_PKEY
import adapters.postgresql.generated.tables.records.UserProfileRecord

import java.time.LocalDate
import java.time.OffsetDateTime

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Index
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Schema
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UserProfile(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, UserProfileRecord>?,
    parentPath: InverseForeignKey<out Record, UserProfileRecord>?,
    aliased: Table<UserProfileRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<UserProfileRecord>(
    alias,
    Public.PUBLIC,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>public.user_profile</code>
         */
        val USER_PROFILE: UserProfile = UserProfile()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<UserProfileRecord> = UserProfileRecord::class.java

    /**
     * The column <code>public.user_profile.id</code>.
     */
    val ID: TableField<UserProfileRecord, String?> = createField(DSL.name("id"), SQLDataType.VARCHAR(16).nullable(false), this, "")

    /**
     * The column <code>public.user_profile.email</code>.
     */
    val EMAIL: TableField<UserProfileRecord, String?> = createField(DSL.name("email"), SQLDataType.VARCHAR(64).nullable(false), this, "")

    /**
     * The column <code>public.user_profile.password</code>.
     */
    val PASSWORD: TableField<UserProfileRecord, String?> = createField(DSL.name("password"), SQLDataType.VARCHAR(256).nullable(false), this, "")

    /**
     * The column <code>public.user_profile.first_name</code>.
     */
    val FIRST_NAME: TableField<UserProfileRecord, String?> = createField(DSL.name("first_name"), SQLDataType.VARCHAR(16), this, "")

    /**
     * The column <code>public.user_profile.birthday_date</code>.
     */
    val BIRTHDAY_DATE: TableField<UserProfileRecord, LocalDate?> = createField(DSL.name("birthday_date"), SQLDataType.LOCALDATE, this, "")

    /**
     * The column <code>public.user_profile.create_time</code>.
     */
    val CREATE_TIME: TableField<UserProfileRecord, OffsetDateTime?> = createField(DSL.name("create_time"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "")

    /**
     * The column <code>public.user_profile.update_time</code>.
     */
    val UPDATE_TIME: TableField<UserProfileRecord, OffsetDateTime?> = createField(DSL.name("update_time"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<UserProfileRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<UserProfileRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<UserProfileRecord>?, where: Condition): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.user_profile</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.user_profile</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.user_profile</code> table reference
     */
    constructor(): this(DSL.name("user_profile"), null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIndexes(): List<Index> = listOf(USER_PROFILE_EMAIL_UNIQUE)
    override fun getPrimaryKey(): UniqueKey<UserProfileRecord> = USER_PROFILE_PKEY
    override fun getUniqueKeys(): List<UniqueKey<UserProfileRecord>> = listOf(USER_PROFILE_EMAIL_KEY)
    override fun `as`(alias: String): UserProfile = UserProfile(DSL.name(alias), this)
    override fun `as`(alias: Name): UserProfile = UserProfile(alias, this)
    override fun `as`(alias: Table<*>): UserProfile = UserProfile(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): UserProfile = UserProfile(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): UserProfile = UserProfile(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): UserProfile = UserProfile(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition): UserProfile = UserProfile(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): UserProfile = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition): UserProfile = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>): UserProfile = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): UserProfile = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): UserProfile = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): UserProfile = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): UserProfile = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): UserProfile = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): UserProfile = where(DSL.notExists(select))
}