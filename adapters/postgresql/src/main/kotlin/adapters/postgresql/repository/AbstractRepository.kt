package adapters.postgresql.repository

import domain.entity.Entity
import domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl

abstract class AbstractRepository<
    Domain : Entity<Id>,
    Id : Comparable<Id>,
    PersistenceModel,
    Record : UpdatableRecord<Record>,
    DAO : DAOImpl<Record, PersistenceModel, Id>,
    >(
    protected val dao: DAO,
) : Repository<Domain, Id> {
    abstract fun PersistenceModel.toDomain(): Domain

    abstract fun Domain.toPersistenceModel(): PersistenceModel

    override suspend fun findById(id: Id) =
        withContext(Dispatchers.IO) {
            dao.findById(id)?.toDomain()
        }

    override suspend fun create(entity: Domain): Domain =
        withContext(Dispatchers.IO) {
            val newRecord = entity.toPersistenceModel()
            dao.insert(newRecord)
            newRecord.toDomain()
        }

    override suspend fun update(entity: Domain): Domain =
        withContext(Dispatchers.IO) {
            dao.update(entity.toPersistenceModel())

            entity
        }
}
