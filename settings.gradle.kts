rootProject.name = "blue"
include("event")
include("collections")
include("storage")
include("sql")
include(":storage:storage-sql")
findProject(":storage:storage-sql")?.name ?: "storage-sql"
include("raw-data")
