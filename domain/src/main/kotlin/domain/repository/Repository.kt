package domain.repository

interface Repository<Entity : Any, Id : Any> {
    suspend fun findById(id: Id): Entity?

    suspend fun create(entity: Entity): Entity

    suspend fun update(entity: Entity): Entity
}
