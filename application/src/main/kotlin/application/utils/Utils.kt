package application.utils

fun <T : Any> updateFields(
    existing: T,
    updates: T,
    updateableFields: Set<String>,
): T {
    val existingClass = existing::class
    val updateClass = updates::class

    require(existingClass == updateClass) { "Both objects must be of the same type" }

    val constructor = existingClass.constructors.first()
    val args =
        constructor.parameters.associateWith { parameter ->
            val name = parameter.name
            if (name in updateableFields) {
                val property = updateClass.members.first { it.name == name }
                property.call(updates)
            } else {
                val property = existingClass.members.first { it.name == name }
                property.call(existing)
            }
        }

    return constructor.callBy(args)
}
